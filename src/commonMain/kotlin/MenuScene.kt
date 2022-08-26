import com.soywiz.klock.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*

class MenuScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        val playerData = DataSaver.loadData()

        val card2 = drawLevelCard(this, playerData.levels[Level.Level2] ?: LevelData())
            .centerOnStage()

        val card1 = drawLevelCard(this, playerData.levels[Level.Level1] ?: LevelData())
            .alignRightToLeftOf(card2, 16)
            .alignTopToTopOf(card2)

        val card3 = drawLevelCard(this, playerData.levels[Level.Level3] ?: LevelData())
            .alignLeftToRightOf(card2, 16)
            .alignTopToTopOf(card2)

        card1.onClick {
            sceneContainer.changeTo<PlayScene>(Level.Level1)
        }

        card2.onClick {
            sceneContainer.changeTo<PlayScene>(Level.Level2)
        }

        card3.onClick {
            sceneContainer.changeTo<PlayScene>(Level.Level3)
        }
    }

    private fun drawLevelCard(container: Container, levelData: LevelData): Container {
        return container.container {
            solidRect(300, 200, Colors.BLACK).alpha(0.5)

            val nameText = text(levelData.name, 50.0).position(10, 10)

            val timeText = text(
                "Best time: ${levelData.timeToString()}",
                30.0
            ).alignTopToBottomOf(nameText, 16).alignLeftToLeftOf(nameText)

            text(
                if (levelData.completed) "Completed" else "Is not completed",
                30.0,
                if (levelData.completed) Colors.GREEN else Colors.RED
            ).alignTopToBottomOf(timeText, 8).alignLeftToLeftOf(timeText)
        }
    }
}
