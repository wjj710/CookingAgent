package furhatos.app.cookingagent.flow

import furhatos.app.cookingagent.*
import furhatos.app.cookingagent.nlu.*
import furhatos.flow.kotlin.*
import com.theokanning.openai.service.OpenAiService
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import furhatos.app.cookingagent.setting.SERVICE_KEY
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.annotation.JsonProperty
import com.sun.org.apache.xpath.internal.operations.Bool
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.nlu.common.Number
import java.io.InputStream
import java.io.OutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.File

val GivingRecommendation : State = state {
    onEntry {
        furhat.say("Let me think for a moment.")
        //read memory from local file
        if(File("local.csv").exists() && users.current.memdata.memory.size==0){
            val input = FileInputStream("local.csv")
            users.current.memdata.memory = readCsv(input)
        }
        //copy user order to current episode and initialize the episode
        val tem : CAMemory = CAMemory(0,"","",0,"",0,"","","",0,0,"")
        tem.index = users.current.memdata.memory.size+1
        tem.mealTime = users.current.order.mealTime.toString()
        tem.mealType = users.current.order.mealType.toString()
        tem.peopleType = users.current.order.peopleType.toString()
        tem.peopleNumber = users.current.order.peopleNumber?.value
        tem.cookingTime = users.current.order.cookingTime?.value
        tem.work = users.current.order.work.toString()
        tem.mood = users.current.order.mood.toString()
        users.current.memdata.episode = tem
        //find current episode in memory
        val ok : Boolean
        var diff : Int = -1
        var ind : Int = -1
        var firstTime : Boolean = true
        val iterator = users.current.memdata.memory.listIterator()
        while (iterator.hasNext()) iterator.next()
        while(iterator.hasPrevious()){
            val id = iterator.previousIndex()
            val record = iterator.previous()
            if(record.mealTime == tem.mealTime && record.mealType == tem.mealType && record.peopleType == tem.peopleType){
                //higher score indicates less similarity
                var score : Int = 0
                if(record.peopleNumber != tem.peopleNumber) score += 1
                if(record.cookingTime != tem.cookingTime) score += 1
                if(record.work != tem.work) score += 1
                if(record.mood != tem.mood) score += 1
                if(firstTime){
                    firstTime = false
                    diff = score
                    ind = id
                }else{
                    if(score < diff){
                        diff = score
                        ind = id
                    }
                }
            }
        }
        var feedback : String? = ""
        if(ind == -1){
            ok = false
            println("Didn't find any similar episode.")
        }else{
            val mem : CAMemory = users.current.memdata.memory[ind]
            if(mem.accept == 1){
                ok = true
                users.current.memdata.episode.recommendation = mem.recommendation
                println("Found a similar episode, the user accepted the recommendation last time.")
            }else{
                ok = false
                feedback = mem.feedback
                println("Found a similar episode, the user didn't accept the recommendation last time.")
            }
        }
        val robotResponse : String?
        if(!ok){
            val userString = users.current.order.toString() + feedback
            robotResponse = call {
                getChatCompletion(userString)
            } as String?
            users.current.memdata.episode.recommendation = robotResponse
        }else{
            robotResponse = users.current.memdata.episode.recommendation
        }
        if(robotResponse == null){
            furhat.say("Sorry, I didn't come up with any ideas.")
            goto(Idle)
        }else{
            furhat.say(robotResponse)
            goto(AskingUser)
        }
    }
}

val AskingUser: State = state(parent = Interaction) {
    onEntry {
        furhat.ask("Are you satisfied with my recommendation?")
    }
    onResponse<Yes> {
        furhat.say("I'm very glad that my recommendation is useful.")
        users.current.memdata.episode.accept = 1
        goto(GettingRating)
    }
    onResponse<No> {
        furhat.say("Sorry, my recommendation is not very useful.")
        users.current.memdata.episode.accept = 2
        goto(GettingFeedback)
    }
}

val GettingRating: State = state(parent = Interaction) {
    onEntry {
        val rating  = furhat.askFor<Number>("Please give me a rating, which is an integer from 1 to 10.")
        val r : Int = rating.toString().toInt()
        if(r < 1 || r > 10){
            furhat.say("The rating format is not between 1 and 10, try again.")
            reentry()
        }
        val correct = furhat.askYN("You gave me $r, is that correct?")
        if(correct){
            furhat.say("Okay, thank you, see you next time.")
            users.current.memdata.episode.rating = r
            users.current.memdata.memory.add(users.current.memdata.episode)
            val output = FileOutputStream("local.csv")
            output.apply { writeCsv(users.current.memdata.memory) }
            goto(Idle)
        }else{
            furhat.say("Okay, try again.")
            reentry()
        }
    }
}

val GettingFeedback: State = state(parent = Interaction) {
    onEntry {
        furhat.ask("Please give me some feedback so I can do better next time.")
    }
    onResponse {
        val correct = furhat.askYN("You said ${it.text}, is that correct?")
        if(correct){
            furhat.say("Okay, thank you, see you next time.")
            users.current.memdata.episode.feedback = it.text
            users.current.memdata.memory.add(users.current.memdata.episode)
            val output = FileOutputStream("local.csv")
            output.apply { writeCsv(users.current.memdata.memory) }
            goto(Idle)
        }else{
            furhat.say("Okay, try again.")
            reentry()
        }
    }
}

//call openai service
val service = OpenAiService(SERVICE_KEY)

fun getChatCompletion(userstring: String): String? {
    val instruction = "You are a personal cooking agent. You need to provide a human with fitting recipes according to his requirements. " +
            "The recommendation should be no more than 50 words, and it should only contain the name of recipe, description of ingredients and cooking method."
    val messages = mutableListOf(ChatMessage().apply { role = "system"; content = instruction })
    messages.add(ChatMessage().apply { role = "user"; content = userstring })
    val completionRequest = ChatCompletionRequest.builder()
        .messages(messages)
        .model("gpt-3.5-turbo")
        .build()
    try {
        val completion = service.createChatCompletion(completionRequest).choices.first().message.content
        return completion.trim()
    } catch (e: Exception) {
        println("Problem with connection to OpenAI")
    }
    return null
}

//data format in memory and log
data class CAMemory(
    @field:JsonProperty("Index") var index: Int,
    @field:JsonProperty("MealTime") var mealTime: String?,
    @field:JsonProperty("MealType") var mealType: String?,
    @field:JsonProperty("PeopleNumber") var peopleNumber: Int?,
    @field:JsonProperty("PeopleType") var peopleType: String?,
    @field:JsonProperty("CookingTime") var cookingTime: Int?,
    @field:JsonProperty("Work") var work: String?,
    @field:JsonProperty("Mood") var mood: String?,
    @field:JsonProperty("Recommendation") var recommendation: String?,
    @field:JsonProperty("Accept") var accept: Int,
    @field:JsonProperty("Rating") var rating: Int?,
    @field:JsonProperty("Feedback") var feedback: String?
) {
    constructor() : this(0, "", "", 0, ""
        , 0, "", "", "", 0, 0, "")
}

//csv operations
val csvMapper = CsvMapper().apply {
    enable(CsvParser.Feature.TRIM_SPACES)
    enable(CsvParser.Feature.SKIP_EMPTY_LINES)
}

val schema = CsvSchema.builder()
    .addNumberColumn("Index")
    .addColumn("MealTime")
    .addColumn("MealType")
    .addColumn("PeopleNumber")
    .addColumn("PeopleType")
    .addColumn("CookingTime")
    .addColumn("Work")
    .addColumn("Mood")
    .addColumn("Recommendation")
    .addColumn("Accept")
    .addColumn("Rating")
    .addColumn("Feedback")
    .build()

fun readCsv(inputStream: InputStream): MutableList<CAMemory> =
    csvMapper.readerFor(CAMemory::class.java)
        .with(schema.withSkipFirstDataRow(true))
        .readValues<CAMemory>(inputStream)
        .readAll()

fun OutputStream.writeCsv(memory: MutableList<CAMemory>) {
    csvMapper.writer().with(schema.withHeader()).writeValues(this).writeAll(memory)
}