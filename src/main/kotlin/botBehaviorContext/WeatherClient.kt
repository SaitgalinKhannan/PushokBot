package botBehaviorContext

import dev.inmo.tgbotapi.extensions.api.chat.modify.pinChatMessage
import dev.inmo.tgbotapi.extensions.api.deleteMessage
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
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
            client.get("https://api.openweathermap.org/data/2.5/weather?lat=54.81&lon=56.06&lang=ru&units=metric&appid=$API")
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
        var oldMessageID: MessageId? = null

        onCommand("infinityWeather") {
            while (true) {
                oldMessageID?.let { mesId ->  deleteMessage(it.chat.id, mesId) }
                val message = cityWeather(weatherClient, it.chat.id).messageId
                pinChatMessage(it.chat.id, message)
                oldMessageID = message
                delay(3600000)
            }
        }
    }
}

suspend fun BehaviourContext.cityWeather(weatherClient: WeatherClient, chatId: IdChatIdentifier): ContentMessage<TextContent> {
    val weather = weatherClient.weather()
    val sunrise = kotlinx.datetime.Instant.fromEpochMilliseconds((weather.sys.sunrise + weather.timezone) * 1000).toLocalDateTime(TimeZone.UTC).time
    val sunset = kotlinx.datetime.Instant.fromEpochMilliseconds((weather.sys.sunset + weather.timezone) * 1000).toLocalDateTime(TimeZone.UTC).time

    return sendTextMessage(
        chatId,
        "Текущая погода в Уфе ☀️⛅️❄️\n" +
                "\uD83C\uDF21 Температура: ${weather.main.temp.roundToInt()} °C\n" +
                "\uD83E\uDEE6 По ощущению: ${weather.main.feelsLike.roundToInt()} °C\n" +
                "\uD83C\uDF2C Ветер: ${weather.wind.speed} м/с\n" +
                "\uD83C\uDF21 Давление: %.2f мм рт.ст.\n".format(weather.main.pressure * 0.75) +
                "\uD83D\uDC33 Влажность: ${weather.main.humidity} %\n" +
                "☁️ Облачность: ${weather.clouds.all} %\n" +
                "\uD83C\uDF1E Рассвет: $sunrise\n" +
                "\uD83C\uDF16 Закат: $sunset\n"
    )
}