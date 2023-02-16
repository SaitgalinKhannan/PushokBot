package botBehaviorContext

import dev.inmo.tgbotapi.extensions.api.forwardMessage
import dev.inmo.tgbotapi.extensions.api.send.media.sendPhoto
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContentMessage
import dev.inmo.tgbotapi.requests.abstracts.InputFile
import dev.inmo.tgbotapi.utils.buildEntities
import java.io.File

suspend fun BehaviourContext.ent() {
    onCommand("jojo") {
        sendTextMessage(it.chat.id, "Ильдар @leth4rgy, посмотри JoJo!")
    }

    onCommand("gigachad") {
        sendPhoto(it.chat.id, InputFile(File("/home/khannan/pushok/IldarSigma.jpg")), text = "Sigma")
    }
}

suspend fun BehaviourContext.mess() {
    onContentMessage {
        val toAnswer = buildEntities {
            +"${it.messageId}"
        }
        reply(it, toAnswer)
    }
}

suspend fun BehaviourContext.replyMessage() {
    onCommand("tyan") {
        forwardMessage(it.chat.id, it.chat.id, 1340)
    }

    onCommand("flex") {
        forwardMessage(it.chat.id, it.chat.id, 1368)
    }
}
