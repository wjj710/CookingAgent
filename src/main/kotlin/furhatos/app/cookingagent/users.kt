package furhatos.app.cookingagent

import furhatos.app.cookingagent.nlu.AskRecommendationIntent
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.User
import furhatos.app.cookingagent.flow.CAMemory

// Associate an order to a user
val User.order by NullSafeUserDataDelegate { AskRecommendationIntent() }

class MemData(
    var memory : MutableList<CAMemory> = mutableListOf(),
    var episode : CAMemory = CAMemory(0,"","",0,"",0,"","","",0,0,"")
)

val User.memdata : MemData
    get() = data.getOrPut(MemData::class.qualifiedName, MemData())
