import com.soywiz.klock.*
import kotlinx.serialization.Serializable

@Serializable
data class LevelData(
    val name: String = "ERROR",
    var time: Int = -1,
    var completed: Boolean = false,
) {
    fun timeToString(): String {
        val timeC = Time(time.seconds)
        return if (time >= 0) timeC.format("mm:ss") else "--:--"
    }

    fun setNewTime(value: Int) {
        if ((value < time && time >= 0) || time < 0) time = value
    }
}
