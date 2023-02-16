package botBehaviorContext

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand

suspend fun BehaviourContext.edu() {
    onCommand("pe") {
        sendTextMessage(it.chat.id, "Ильдар @VoSnee, киттек физкультураға")
    }

    onCommand("tc") {
        sendTextMessage(it.chat.id, "Ильдар @VoSnee, киттек параға")
    }
}
