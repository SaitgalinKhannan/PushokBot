package entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    @SerialName("base")
    val base: String,
    @SerialName("clouds")
    val clouds: Clouds,
    @SerialName("cod")
    val cod: Int,
    @SerialName("coord")
    val coord: Coord,
    @SerialName("dt")
    val dt: Long,
    @SerialName("id")
    val id: Int,
    @SerialName("main")
    val main: Main,
    @SerialName("name")
    val name: String,
    @SerialName("rain")
    var rain: Rain = Rain(0.0, 0.0),
    @SerialName("snow")
    var snow: Snow = Snow(0.0, 0.0),
    @SerialName("sys")
    val sys: Sys,
    @SerialName("timezone")
    val timezone: Int,
    @SerialName("visibility")
    val visibility: Int,
    @SerialName("weather")
    val weather: List<Weather>,
    @SerialName("wind")
    val wind: Wind
) {
    @Serializable
    data class Clouds(
        @SerialName("all")
        val all: Int
    )

    @Serializable
    data class Coord(
        @SerialName("lat")
        val lat: Double,
        @SerialName("lon")
        val lon: Double
    )

    @Serializable
    data class Main(
        @SerialName("feels_like")
        val feelsLike: Double,
        @SerialName("grnd_level")
        val grndLevel: Double,
        @SerialName("humidity")
        val humidity: Int,
        @SerialName("pressure")
        val pressure: Double,
        @SerialName("sea_level")
        val seaLevel: Double,
        @SerialName("temp")
        val temp: Double,
        @SerialName("temp_max")
        val tempMax: Double,
        @SerialName("temp_min")
        val tempMin: Double
    )

    @Serializable
    data class Rain(
        @SerialName("1h")
        val h1: Double,
        @SerialName("3h")
        val h3: Double
    )

    @Serializable
    data class Snow(
        @SerialName("1h")
        val h1: Double,
        @SerialName("3h")
        val h3: Double
    )

    @Serializable
    data class Sys(
        @SerialName("country")
        val country: String,
        @SerialName("id")
        val id: Int,
        @SerialName("sunrise")
        val sunrise: Long,
        @SerialName("sunset")
        val sunset: Long,
        @SerialName("type")
        val type: Int,
        @SerialName("message")
        var message: String = ""
    )

    @Serializable
    data class Weather(
        @SerialName("description")
        val description: String,
        @SerialName("icon")
        val icon: String,
        @SerialName("id")
        val id: Int,
        @SerialName("main")
        val main: String
    )

    @Serializable
    data class Wind(
        @SerialName("deg")
        val deg: Int,
        @SerialName("gust")
        val gust: Double,
        @SerialName("speed")
        val speed: Double
    )
}