package furhatos.app.cookingagent

import furhatos.app.cookingagent.flow.Init
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class CookingagentSkill : Skill() {
    override fun start() {
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
