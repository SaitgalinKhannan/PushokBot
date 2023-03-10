package botBehaviorContext

import dev.inmo.tgbotapi.extensions.api.chat.modify.pinChatMessage
import dev.inmo.tgbotapi.extensions.api.deleteMessage
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onPinnedMessage
import dev.inmo.tgbotapi.types.IdChatIdentifier
import dev.inmo.tgbotapi.types.MessageId
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import entity.Weather
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

class WeatherClient {
    private val client = HttpClient(CIO)

    companion object {
        const val API = "dba3189bcb15da5ea93c1530d0ef84fb"
    }

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun weather(): Weather = withContext(Dispatchers.IO) {
        val response =
            client.get("OpenWeathermapApi")
        return@withContext json.decodeFromString<Weather>(response.bodyAsText())
    }
}

suspend fun BehaviourContext.currentWeather() = withContext(Dispatchers.IO) {
    val weatherClient = WeatherClient()

    onCommand("weather") {
        cityWeather(weatherClient, it.chat.id)
    }
}

suspend fun BehaviourContext.weatherInfo() {
    CoroutineScope(Dispatchers.IO).launch {
        val weatherClient = WeatherClient()

        onCommand("infinityWeather") { commonMessage ->
            var oldMessageID: MessageId? = null

            while (isActive) {
                oldMessageID?.let { mesId -> deleteMessage(commonMessage.chat.id, mesId) }
                val message = cityWeather(weatherClient, commonMessage.chat.id).messageId
                pinChatMessage(commonMessage.chat.id, message, true)
                onPinnedMessage {

                }
                oldMessageID = message
                delay(10800000)
            }
        }
    }
}

suspend fun BehaviourContext.cityWeather(
    weatherClient: WeatherClient,
    chatId: IdChatIdentifier,
): ContentMessage<TextContent> {
    val weather = weatherClient.weather()
    val sunrise = kotlinx.datetime.Instant.fromEpochMilliseconds((weather.sys.sunrise + weather.timezone) * 1000)
        .toLocalDateTime(TimeZone.UTC).time
    val sunset = kotlinx.datetime.Instant.fromEpochMilliseconds((weather.sys.sunset + weather.timezone) * 1000)
        .toLocalDateTime(TimeZone.UTC).time

    return sendTextMessage(
        chatId,
        "?????????????? ???????????? ?? ?????? ??????????????????\n" +
                "\uD83C\uDF21 ??????????????????????: ${weather.main.temp.roundToInt()} ??C\n" +
                "\uD83E\uDEE6 ???? ????????????????: ${weather.main.feelsLike.roundToInt()} ??C\n" +
                "\uD83C\uDF2C ??????????: ${weather.wind.speed} ??/??\n" +
                "\uD83C\uDF21 ????????????????: %.2f ???? ????.????.\n".format(weather.main.pressure * 0.75) +
                "\uD83D\uDC33 ??????????????????: ${weather.main.humidity} %\n" +
                "?????? ????????????????????: ${weather.clouds.all} %\n" +
                "\uD83C\uDF1E ??????????????: $sunrise\n" +
                "\uD83C\uDF16 ??????????: $sunset\n"
    )
}
