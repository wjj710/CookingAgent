package furhatos.app.cookingagent.nlu

import furhatos.app.cookingagent.*
import furhatos.app.cookingagent.nlu.*
import furhatos.flow.kotlin.*
import furhatos.nlu.Intent
import furhatos.util.Language

class RequestMealTypeOptions : Intent()  {
    override fun getExamples(lang: Language): List<String> {
        return listOf("what meals are available", "what do you mean", "what options are there", "what kind of meals are there", "can you give me some examples")
    }
}

class RequestMoodOptions : Intent()  {
    override fun getExamples(lang: Language): List<String> {
        return listOf("what do you mean", "what moods can i choose from", "can you give me some examples", "what options are there")
    }
}

class RequestPeopleTypeOptions : Intent()  {
    override fun getExamples(lang: Language): List<String> {
        return listOf("can you give me some examples", "what do you mean", "what options are there")
    }
}
