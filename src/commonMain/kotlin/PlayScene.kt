import com.soywiz.klock.*
import com.soywiz.klock.hr.*
import com.soywiz.korev.*
import com.soywiz.korge.box2d.*
import com.soywiz.korge.internal.*
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Circle
import com.soywiz.korge.view.camera.*
import com.soywiz.korge.view.position
import com.soywiz.korge.view.tween.*
import com.soywiz.korim.color.*
import com.soywiz.korio.async.*
import com.soywiz.korma.geom.*
import kotlinx.coroutines.*
import org.jbox2d.common.*
import org.jbox2d.dynamics.*

class PlayScene(private val level: Level) : Scene() {
    lateinit var playerCircle: Circle
    lateinit var playerData: PlayerData
    private var time = 0
    private var startTime = DateTime.now()

    @OptIn(KorgeUntested::class)
    override suspend fun SContainer.sceneMain() {
        playerData = DataSaver.loadData()

        val cameraContainer = cameraContainer(
            1000.0, 600.0, clip = true,
            block = {
                clampToBounds = true
            }
        ) {
            playerCircle = circle(fill = Colors.DARKBLUE).anchor(0.5, 0.5)
                .registerBodyWithFixture(
                    type = BodyType.DYNAMIC,
                )
            playerCircle.circle(radius = 5.0, fill = Colors.BLUE).position(1, 1)
                .circle(radius = 4.0, fill = Colors.BLUE).position(-7, -8)

            playerCircle.addFixedUpdater(60.timesPerSecond) {
                when {
                    input.keys[Key.RIGHT] -> {
                        body?.applyLinearImpulse(Vec2(0.02F, 0F), Vec2(-2.0F, 0F), true)
                    }
                    input.keys[Key.LEFT] -> {
                        body?.applyLinearImpulse(Vec2(-0.02F, 0F), Vec2(2.0F, 0F), true)
                    }
                    input.keys[Key.W] -> {
                        position(pos.x, pos.y - 10)
                    }
                    input.keys[Key.S] -> {
                        position(pos.x, pos.y + 10)
                    }
                    input.keys[Key.D] -> {
                        position(pos.x + 10, pos.y)
                    }
                    input.keys[Key.A] -> {
                        position(pos.x - 10, pos.y)
                    }
                    input.keys[Key.Q] -> {
                        position(pos.x - 0.00001, pos.y)
                    }
                    input.keys[Key.ENTER] -> {
                        launchImmediately { sceneContainer.changeTo<PlayScene>(level) }
                    }
                }

                if (playerCircle.pos.y >= 1000) {
                    launchImmediately { sceneContainer.changeTo<PlayScene>(level) }
                }
            }

            drawLevel(this)

            val spawn = getChildByName("spawn") ?: solidRect(40, 40, Colors["#631a66ff"]).position(140, 860)
            val finish = getChildByName("finish") ?: solidRect(40, 40, Colors["#366633ff"]).position(2820, 780)

            playerCircle.position(spawn.pos)

            finish.onCollision(filter = { it == playerCircle }) {
                playerData.levels[level]?.completed = true
                playerData.levels[level]?.setNewTime(time)

                launchImmediately {
                    DataSaver.saveData(playerData)
                    sceneContainer.changeTo<MenuScene>()
                }
            }
        }

        cameraContainer.follow(playerCircle, setImmediately = true)

        val levelData = playerData.levels[level]
        val nameText = text(levelData?.name ?: "", 40.0).position(10, 5)

        val bestTimeText = text(
            "Best time: ${levelData?.timeToString()}",
            30.0,
            Colors.GOLD
        ).alignTopToBottomOf(nameText, 4).alignLeftToLeftOf(nameText)

        val timeText = text(
            "00:00",
            30.0
        ).alignTopToBottomOf(bestTimeText, 2).alignLeftToLeftOf(bestTimeText)

        addUpdater {
            time = (DateTime.now() - startTime).seconds.toInt()
            val currentTime = Time(time.seconds)
            timeText.text = currentTime.format("mm:ss")
        }
    }

    private fun drawLevel(container: Container) {
        when (level) {
            Level.Level1 -> drawLevel1(container)
            Level.Level2 -> drawLevel2(container)
            Level.Level3 -> drawLevel3(container)
        }
    }

    private fun drawLevel1(container: Container) {
        container.solidRect(40, 40, Colors["#631a66ff"]).position(140, 860).name("spawn")
        container.solidRect(40, 40, Colors["#366633ff"]).position(2820, 780).name("finish")
        container.solidRect(100, 1000, Colors.BLACK).registerBodyWithFixture()
        container.solidRect(1240, 100, Colors.BLACK).position(0, 900).registerBodyWithFixture()
        container.solidRect(100, 1000, Colors.BLACK).position(2900, 0).registerBodyWithFixture()
        container.solidRect(100, 385, Colors.BLACK).position(760, 800).rotation(75.degrees).registerBodyWithFixture()
        container.solidRect(100, 480, Colors.BLACK).position(1240, 800).rotation(90.degrees).registerBodyWithFixture()
        container.solidRect(1420, 100, Colors.BLACK).position(1480, 900).registerBodyWithFixture()
        container.solidRect(100, 140, Colors.BLACK).position(2060, 820).rotation(45.degrees).registerBodyWithFixture()
        container.solidRect(940, 100, Colors.BLACK).position(2060, 820).registerBodyWithFixture()

        container.text("<  > - Move\nEnter - Restart", 60.0).position(180, 680)
    }

    private fun drawLevel2(container: Container) {
        container.solidRect(40, 40, Colors["#631a66ff"]).position(140, 860).name("spawn")
        container.solidRect(40, 40, Colors["#366633ff"]).position(3820, 860).name("finish")
        container.solidRect(100, 1000, Colors.BLACK).registerBodyWithFixture()
        container.solidRect(360, 100, Colors.BLACK).position(0, 900).registerBodyWithFixture()
        container.solidRect(100, 400, Colors.BLACK).position(740, 866).rotation(85.degrees).registerBodyWithFixture()
        container.solidRect(420, 100, Colors.BLACK).position(900, 900).registerBodyWithFixture()
        container.solidRect(160, 20, Colors.BLACK).position(1320, 980).registerBodyWithFixture()
        container.solidRect(320, 100, Colors.BLACK).position(1480, 900).registerBodyWithFixture()
        container.solidRect(100, 300, Colors.BLACK).position(1820, 800).rotation(70.degrees).registerBodyWithFixture()
        container.solidRect(240, 100, Colors.BLACK).position(1820, 800).registerBodyWithFixture()
        container.solidRect(190, 20, Colors.BLACK).position(1450, 800).registerBodyWithFixture()
        container.solidRect(242, 20, Colors.BLACK).position(1240, 700).rotation(25.degrees).registerBodyWithFixture()
        container.solidRect(180, 20, Colors.BLACK).position(1060, 700).registerBodyWithFixture()
        container.solidRect(240, 200, Colors.BLACK).position(2060, 700).registerBodyWithFixture()
        container.solidRect(280, 100, Colors.BLACK).position(2660, 800).registerBodyWithFixture()
        container.solidRect(100, 400, Colors.BLACK).position(3320, 905).rotation(105.degrees).registerBodyWithFixture()
        container.solidRect(2320, 100, Colors.BLACK).position(1780, 900).registerBodyWithFixture()
        container.solidRect(100, 1000, Colors.BLACK).position(4000, 0).registerBodyWithFixture()
        container.solidRect(560, 20, Colors.BLACK).position(1500, 700).registerBodyWithFixture()

        val button1 = container.solidRect(40, 10, Colors["#527985"]).position(2000, 790)
        val inter1 = container.solidRect(120, 10, Colors["#527985"]).position(1520, 805).registerBodyWithFixture()
        var inter1Moved = false
        button1.onCollision(filter = { it == playerCircle }) {
            if (!inter1Moved) {
                coroutineContext.launchUnscoped {
                    button1.moveTo(2000, 798, 0.5.seconds)
                    inter1.moveTo(1640, 805)
                }
            }

            inter1Moved = true
        }

        val button2 = container.solidRect(40, 10, Colors["#53856e"]).position(1070, 690)
        val inter2 = container.solidRect(210, 10, Colors["#53856e"]).position(1500, 705).registerBodyWithFixture()
        var inter2Moved = false
        button2.onCollision(filter = { it == playerCircle }) {
            if (!inter2Moved) {
                coroutineContext.launchUnscoped {
                    button2.moveTo(1070, 698, 0.5.seconds)
                    inter2.moveTo(1290, 705)
                }
            }

            inter2Moved = true
        }

        container.solidRect(75, 75, Colors["#763b10"]).position(1220, 820)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(75, 75, Colors["#844212"]).position(1170, 820)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(90, 90, Colors["#894412"]).position(2180, 620)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(90, 90, Colors["#572a0d"]).position(2080, 620)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(90, 90, Colors["#69340f"]).position(2140, 540)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(90, 90, Colors["#7c3e12"]).position(2100, 540)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(90, 90, Colors["#50270c"]).position(2170, 540)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(90, 90, Colors["#462209"]).position(2150, 540)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
    }

    private fun drawLevel3(container: Container) {
        container.solidRect(40, 40, Colors["#631a66ff"]).position(140, 860).name("spawn")
        container.solidRect(40, 40, Colors["#366633ff"]).position(3820, 860).name("finish")
        container.solidRect(100, 1000, Colors.BLACK).registerBodyWithFixture()
        container.solidRect(360, 100, Colors.BLACK).position(0, 900).registerBodyWithFixture()
        container.solidRect(360, 100, Colors.BLACK).position(1020, 900).registerBodyWithFixture()
        container.solidRect(100, 540, Colors.BLACK).position(1540, 460).registerBodyWithFixture()
        container.solidRect(860, 100, Colors.BLACK).position(1540, 900).registerBodyWithFixture()
        container.solidRect(580, 100, Colors.BLACK).position(1840, 900).rotation((-10).degrees).registerBodyWithFixture()
        container.solidRect(380, 200, Colors.BLACK).position(2400, 800).registerBodyWithFixture()
        container.solidRect(620, 20, Colors.BLACK).position(2780, 980).registerBodyWithFixture()
        container.solidRect(600, 100, Colors.BLACK).position(3400, 900).registerBodyWithFixture()
        container.solidRect(100, 1000, Colors.BLACK).position(3900, 0).registerBodyWithFixture()

        val platform1 = container.solidRect(260, 10, Colors["#343157"]).position(360, 905).registerBodyWithFixture()
        val platform2 = container.solidRect(160, 10, Colors["#343157"]).position(1380, 905).registerBodyWithFixture()

        coroutineContext.launchUnscoped {
            while (true) {
                platform1.moveTo(760, 905, 3.seconds)
                platform1.moveTo(360, 905, 3.seconds)
            }
        }

        coroutineContext.launchUnscoped {
            while (true) {
                platform2.moveTo(1380, 455, 3.seconds)
                platform2.moveTo(1380, 905, 3.seconds)
            }
        }

        container.solidRect(85, 85, Colors["#763b10"]).position(2510, 800)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(85, 85, Colors["#844212"]).position(2600, 800)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(85, 85, Colors["#894412"]).position(2700, 800)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(85, 85, Colors["#572a0d"]).position(2550, 700)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(85, 85, Colors["#69340f"]).position(2660, 700)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(85, 85, Colors["#7c3e12"]).position(2580, 600)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(85, 85, Colors["#7c3e12"]).position(2880, 500)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(85, 85, Colors["#7c3e12"]).position(2840, 400)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
        container.solidRect(85, 85, Colors["#7c3e12"]).position(2780, 300)
            .registerBodyWithFixture(type = BodyType.DYNAMIC, density = 0.05)
    }
}
