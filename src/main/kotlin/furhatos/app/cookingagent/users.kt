package furhatos.app.cookingagent

import furhatos.app.cookingagent.nlu.AskRecommendationIntent
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.User

// Associate an order to a user
val User.order by NullSafeUserDataDelegate { AskRecommendationIntent() }