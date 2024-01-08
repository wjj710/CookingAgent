package furhatos.app.cookingagent.nlu

import furhatos.nlu.EnumEntity
import furhatos.util.Language

class MealTime : EnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("breakfast", "lunch", "dinner")
    }

    // Method overridden to produce a spoken utterance of the place
    override fun toText(lang: Language): String {
        return generate(lang,"$value")
    }

    override fun toString(): String {
        return toText()
    }
}

class MealType : EnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("French food", "Italian food", "American food", "Turkish food", "Chinese food", "Indian food", "Dutch food")
    }

    // Method overridden to produce a spoken utterance of the place
    override fun toText(lang: Language): String {
        return generate(lang,"$value")
    }

    override fun toString(): String {
        return toText()
    }
}

class PeopleType : EnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("myself", "friends", "family", "partner", "girlfriend", "boyfriend")
    }

    // Method overridden to produce a spoken utterance of the place
    override fun toText(lang: Language): String {
        return generate(lang,"$value")
    }

    override fun toString(): String {
        return toText()
    }
}

class Work : EnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("tired", "relaxed")
    }

    // Method overridden to produce a spoken utterance of the place
    override fun toText(lang: Language): String {
        return generate(lang,"$value")
    }

    override fun toString(): String {
        return toText()
    }
}

class Mood : EnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("happy", "upset", "sad", "angry", "excited", "depressed", "stressed")
    }

    // Method overridden to produce a spoken utterance of the place
    override fun toText(lang: Language): String {
        return generate(lang,"$value")
    }

    override fun toString(): String {
        return toText()
    }
}
