import com.soywiz.korio.file.std.*
import kotlinx.coroutines.sync.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.native.concurrent.*

@ThreadLocal
object DataSaver {
    private val mutex: Mutex = Mutex()
    var playerData: PlayerData? = null

    suspend fun loadData(): PlayerData {
        return mutex.withLock {
            val file = rootLocalVfs["player.json"]

            if (file.exists()) {
                Json.decodeFromString(file.readString())
            } else PlayerData()
        }
    }

    suspend fun saveData(playerData: PlayerData) {
        mutex.withLock {
            rootLocalVfs["player.json"].writeString(Json.encodeToString(playerData))
        }
    }
}
