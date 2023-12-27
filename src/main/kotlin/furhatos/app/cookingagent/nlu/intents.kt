package furhatos.app.cookingagent.nlu

import furhatos.nlu.TextGenerator
import furhatos.util.Language
import furhatos.nlu.*
import furhatos.nlu.common.Number
import furhatos.records.GenericRecord

open class AskRecommendationIntent : Intent(), TextGenerator {

    var mealTime: MealTime? = null
    var mealType: MealType? = null
    var peopleNumber: Number? = null
    var peopleType: PeopleType? = null
    var cookingTime: Number? = null
    var work: Work? = null
    var mood: Mood? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "please recommend a recipe",
                "I want you to recommend a recipe",
                "I want to eat @mealTime with my @peopleType, please recommend a recipe",
                "I would like to have @mealType, please recommend some food",
                "can you recommend a fitting recipe",
                "I want you to recommend a fitting recipe"
        )
    }

    override fun toText(lang : Language) : String =
        generate(lang, "[I want to have $mealType] [for $mealTime] [with my $peopleType.] [The number of people who will eat the meal is $peopleNumber] [and I want to cook for $cookingTime minutes.]" +
                "[I feel $work and $mood today.] Please recommend a fitting recipe to me.")

    override fun toString(): String {
        return toText()
    }

    override fun adjoin(record: GenericRecord<Any>?) {
        super.adjoin(record)
    }

    fun clear(){
        mealType = null
        mealTime = null
        peopleNumber = null
        peopleType = null
        cookingTime = null
        work = null
        mood = null
    }
}

class TellMealTimeIntent : Intent() {
    var mealTime : MealTime? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("@mealTime", "breakfast", "lunch", "dinner")
    }
}

class TellMealTypeIntent : Intent() {
    var mealType : MealType? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("@mealType", "I want @mealType", "I want to eat @mealType")
    }
}

class TellPeopleNumberIntent : Intent() {
    var peopleNumber : Number? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("@peopleNumber", "@peopleNumber people")
    }
}

class TellPeopleTypeIntent : Intent() {
    var peopleType : PeopleType? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("my @peopleType", "I will eat with my @peopleType")
    }
}

class TellCookingTimeIntent : Intent() {
    var cookingTime : Number? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("@cookingTime minutes")
    }
}

class TellWorkIntent : Intent() {
    var work : Work? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("I'm @work", "@work")
    }
}

class TellMoodIntent : Intent() {
    var mood : Mood? = null

    override fun getExamples(lang: Language): List<String> {
        return listOf("I'm @mood", "I feel @mood", "@mood")
    }
}



