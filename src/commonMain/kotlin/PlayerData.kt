import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    var name: String = "unknown",
    val levels: Map<Level, LevelData> = mapOf(
        Pair(Level.Level1, LevelData("Level 1", -1, false)),
        Pair(Level.Level2, LevelData("Level 2", -1, false)),
        Pair(Level.Level3, LevelData("Level 3", -1, false)),
    )
)
