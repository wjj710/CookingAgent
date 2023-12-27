package furhatos.app.cookingagent.flow

import furhatos.app.cookingagent.*
import furhatos.app.cookingagent.nlu.*
import furhatos.flow.kotlin.*


// Start of interaction
val Start = state(parent = Interaction) {
    onEntry {
        //furhat.ask("Hello, I'm your Cooking Agent. How can I help you?")
        furhat.say("Hello, I'm your Cooking Agent. I will first ask you some questions.")
        users.current.order.clear()
        goto(GettingInformation)
    }

//    onResponse<AskRecommendationIntent> {
//        users.current.order.adjoin(it.intent)
//        furhat.say("Ok, you want me to recommend a recipe")
//        goto(GettingInformation)
//    }
}

// Form-filling state that checks any missing slots and if so, goes to specific slot-filling states.
val GettingInformation = state {
    onEntry {
        val order = users.current.order
        when {
            order.mealTime == null -> goto(RequestMealTime)
            order.mealType == null -> goto(RequestMealType)
            order.peopleNumber == null -> goto(RequestPeopleNumber)
            order.peopleType == null -> goto(RequestPeopleType)
            order.cookingTime == null -> goto(RequestCookingTime)
            order.work == null -> goto(RequestWork)
            order.mood == null -> goto(RequestMood)
            else -> {
                furhat.say("Alright, I will recommend the fitting recipe to you.")
                goto(GivingRecommendation)
            }
        }
    }
}

val RequestMealTime : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("You want to eat breakfast, lunch or dinner?")
    }

    onResponse<TellMealTimeIntent> {
        furhat.say("Okay, ${it.intent.mealTime}")
        users.current.order.mealTime = it.intent.mealTime
        goto(GettingInformation)
    }
}

val RequestMealType : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("What kind of food do you like?")
    }

    onResponse<TellMealTypeIntent> {
        furhat.say("Okay, ${it.intent.mealType}")
        users.current.order.mealType = it.intent.mealType
        goto(GettingInformation)
    }
}

val RequestPeopleNumber : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("How many people will have the meal?")
    }

    onResponse<TellPeopleNumberIntent> {
        furhat.say("Okay, ${it.intent.peopleNumber}")
        users.current.order.peopleNumber = it.intent.peopleNumber
        goto(GettingInformation)
    }
}

val RequestPeopleType : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("Who are those people?")
    }

    onResponse<TellPeopleTypeIntent> {
        furhat.say("Okay, ${it.intent.peopleType}")
        users.current.order.peopleType = it.intent.peopleType
        goto(GettingInformation)
    }
}

val RequestCookingTime : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("How much time do you want to prepare the food?")
    }

    onResponse<TellCookingTimeIntent> {
        furhat.say("Okay, ${it.intent.cookingTime}")
        users.current.order.cookingTime = it.intent.cookingTime
        goto(GettingInformation)
    }
}

val RequestWork : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("How's your day? Are you tired?")
    }

    onResponse<TellWorkIntent> {
        furhat.say("Okay, ${it.intent.work}")
        users.current.order.work = it.intent.work
        goto(GettingInformation)
    }
}

val RequestMood : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("How do you feel today?")
    }

    onResponse<TellMoodIntent> {
        furhat.say("Okay, ${it.intent.mood}")
        users.current.order.mood = it.intent.mood
        goto(GettingInformation)
    }
}



