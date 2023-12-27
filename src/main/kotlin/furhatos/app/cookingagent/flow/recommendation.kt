package furhatos.app.cookingagent.flow

import furhatos.app.cookingagent.*
import furhatos.app.cookingagent.nlu.*
import furhatos.flow.kotlin.*
import com.theokanning.openai.service.OpenAiService
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import furhatos.app.cookingagent.setting.SERVICE_KEY

val GivingRecommendation : State = state {
    onEntry{
        furhat.say("Let me think for a moment.")
        val userstring = users.current.order.toString()
        val robotResponse = call {
            getChatCompletion(userstring)
        } as String?
        furhat.say(robotResponse?:"Sorry, I didn't come up with any ideas.")
        goto(Idle)
    }
}

val service = OpenAiService(SERVICE_KEY)

fun getChatCompletion(userstring: String): String? {
    val instruction = "You are a personal cooking agent. You need to provide a human with fitting recipes according to his requirements. " +
            "The recommendation should be no more than 100 words, and it should contains the description of ingredients and cooking method."
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