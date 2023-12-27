package furhatos.app.cookingagent

import furhatos.app.cookingagent.flow.Idle
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill

class CookingagentSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
