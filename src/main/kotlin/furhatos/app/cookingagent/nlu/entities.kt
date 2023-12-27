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
}

class MealType : EnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("French food", "Italian food", "American food", "Turkish food", "Chinese food", "Indian food")
    }

    // Method overridden to produce a spoken utterance of the place
    override fun toText(lang: Language): String {
        return generate(lang,"$value")
    }
}

class PeopleType : EnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("friends", "family", "lover")
    }

    // Method overridden to produce a spoken utterance of the place
    override fun toText(lang: Language): String {
        return generate(lang,"$value")
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
}

class Mood : EnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf("happy", "upset")
    }

    // Method overridden to produce a spoken utterance of the place
    override fun toText(lang: Language): String {
        return generate(lang,"$value")
    }
}
