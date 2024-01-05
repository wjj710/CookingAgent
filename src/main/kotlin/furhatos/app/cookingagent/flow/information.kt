package furhatos.app.cookingagent.flow

import furhatos.app.cookingagent.*
import furhatos.app.cookingagent.nlu.*
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

// Start of interaction
val Start = state(parent = Interaction) {
    onEntry {
        //furhat.ask("Hello, I'm your Cooking Agent. How can I help you?")
        furhat.say("Hello, I'm your Cooking Agent. I will first ask you some questions.")
        users.current.request.clear()
        goto(GettingInformation)
    }
}

// Form-filling state that checks any missing slots and if so, goes to specific slot-filling states.
val GettingInformation = state {
    onEntry {
        val request = users.current.request
        when {
            request.mealTime == null -> goto(RequestMealTime)
            request.mealType == null -> goto(RequestMealType)
            request.peopleNumber == null -> goto(RequestPeopleNumber)
            request.peopleType == null -> goto(RequestPeopleType)
            request.cookingTime == null -> goto(RequestCookingTime)
            request.work == null -> goto(RequestWork)
            request.mood == null -> goto(RequestMood)
            else -> {
                goto(ConfirmingInformation)
            }
        }
    }
}

val EditInformation: State = state {
    onEntry {
        furhat.ask("What would you like to change?")
    }

    onResponse<AskRecommendationIntent> {
        val oldRequest = users.current.request

        val changes = mutableListOf<String>()
        val int = it.intent
        with(users.current.request) {
            if (int.mealTime != null && oldRequest.mealTime.toString() != int.mealTime.toString()) changes.add("meal time to ${int.mealTime}")
            if (int.mealType != null && oldRequest.mealType.toString() != int.mealType.toString()) changes.add("meal type to ${int.mealType}")
            if (int.peopleNumber != null && oldRequest.peopleNumber.toString() != int.peopleNumber.toString()) changes.add("number of people to ${int.peopleNumber}")
            if (int.peopleType != null && oldRequest.peopleType.toString() != int.peopleType.toString()) changes.add("people type to ${int.peopleType}")
            if (int.cookingTime != null && oldRequest.cookingTime.toString() != int.cookingTime.toString()) changes.add("cooking time to ${int.cookingTime} minutes")
            if (int.work != null && oldRequest.work.toString() != int.work.toString()) changes.add("work status to ${int.work}")
            if (int.mood != null && oldRequest.mood.toString() != int.mood.toString()) changes.add("mood to ${int.mood}")
        }

        val message = if (changes.isEmpty()) {
            "You didn't make any changes."
        } else {
            "Okay, I've updated your request: " + changes.joinToString(", ") + "."
        }

        // Saying the response message
        furhat.say(message)

        oldRequest.adjoin(it.intent)
        goto(state = ConfirmingInformation)
    }
}

val ConfirmingInformation: State = state {
    onEntry {
        val request = users.current.request
        furhat.ask("You are looking for a recipe for ${request.mealTime}. It should be ${request.mealType}. The meal " +
                "needs to be for ${request.peopleNumber} people, who are ${request.peopleType}. You want it to take " +
                "${request.cookingTime} minutes to cook. You are ${request.work}, and are feeling ${request.mood}. " +
                "Would you like to change anything?")
    }

    onResponse<Yes>{
        goto(state = EditInformation)
    }

    onResponse<No>{
        furhat.say("Okay, I will now look for a fitting recipe...")
        goto(GivingRecommendation)
    }
}

val RequestMealTime : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("You want to eat breakfast, lunch or dinner?")
    }

    onResponse<TellMealTimeIntent> {
        furhat.say("Okay, ${it.intent.mealTime}")
        users.current.request.mealTime = it.intent.mealTime
        goto(GettingInformation)
    }
}

val RequestMealType : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("What kind of food do you like?")
    }

    onResponse<TellMealTypeIntent> {
        furhat.say("Okay, ${it.intent.mealType}")
        users.current.request.mealType = it.intent.mealType
        goto(GettingInformation)
    }

    onResponse<RequestMealTypeOptions> {
        raise(TellMealTypeOptions())
    }

    onEvent<TellMealTypeOptions> {
        furhat.say("Some examples are French food, Chinese food or Italian food.")
        reentry()
    }
}

val RequestPeopleNumber : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("How many people will have the meal?")
    }

    onResponse<TellPeopleNumberIntent> {
        furhat.say("Okay, ${it.intent.peopleNumber}")
        users.current.request.peopleNumber = it.intent.peopleNumber
        goto(GettingInformation)
    }
}

val RequestPeopleType : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("For what people will you cook the food?")
    }

    onResponse<TellPeopleTypeIntent> {
        furhat.say("Okay, ${it.intent.peopleType}")
        users.current.request.peopleType = it.intent.peopleType
        goto(GettingInformation)
    }

    onResponse<RequestPeopleTypeOptions> {
        raise(TellPeopleTypeOptions())
    }

    onEvent<TellPeopleTypeOptions> {
        furhat.say("Will you be cooking for yourself, your partner, friends, or family?")
        reentry()
    }
}

val RequestCookingTime : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("How much time do you want to spend cooking? Please answer in minutes.")
    }

    onResponse<TellCookingTimeIntent> {
        furhat.say("Okay, ${it.intent.cookingTime}")
        users.current.request.cookingTime = it.intent.cookingTime
        goto(GettingInformation)
    }
}

val RequestWork : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("How's your day? Are you tired or relaxed?")
    }

    onResponse<TellWorkIntent> {
        furhat.say("Okay, ${it.intent.work}")
        users.current.request.work = it.intent.work
        goto(GettingInformation)
    }
}

val RequestMood : State = state(parent = Interaction) {
    onEntry {
        furhat.ask("How do you feel today?")
    }

    onResponse<TellMoodIntent> {
        furhat.say("Okay, ${it.intent.mood}")
        users.current.request.mood = it.intent.mood
        goto(GettingInformation)
    }

    onResponse<RequestMoodOptions> {
        raise(TellMoodOptions())
    }

    onEvent<TellMoodOptions> {
        furhat.say("Are you happy or upset?")
        reentry()
    }
}



