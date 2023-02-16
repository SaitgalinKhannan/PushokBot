import botBehaviorContext.*
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

suspend fun main() {
    val botToken = "5093518853:AAFuGkXV4NSoTJOe6O02KwnuWWgFX0DMzBQ"

    val bot = telegramBot(botToken)

    bot.buildBehaviourWithLongPolling(CoroutineScope(Dispatchers.Default)) {
        println(getMe())
        ent()
        edu()
        replyMessage()
        weatherInfo()
        currentWeather()
    }.join()
}