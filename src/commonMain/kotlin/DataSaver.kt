import com.soywiz.korio.file.std.*
import kotlinx.coroutines.sync.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

object DataSaver {
    private val mutex: Mutex = Mutex()

    suspend fun loadData(): PlayerData {
        //return mutex.withLock {
            val file = rootLocalVfs["player.json"]

            return if (file.exists()) {
                Json.decodeFromString(file.readString())
            } else PlayerData()
        //}
    }

    suspend fun saveData(playerData: PlayerData) {
        //mutex.withLock {
            rootLocalVfs["player.json"].writeString(Json.encodeToString(playerData))
        //}
    }
}
