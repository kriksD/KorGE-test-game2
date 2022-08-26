import com.soywiz.korge.*
import com.soywiz.korge.scene.*
import com.soywiz.korim.color.*
import com.soywiz.korinject.*
import com.soywiz.korma.geom.*
import kotlin.reflect.*

suspend fun main() = Korge(Korge.Config(module = MainModule))

object MainModule : Module() {
    override val bgcolor: RGBA = Colors["#0e5c60"]
    override val mainScene: KClass<out Scene> = MenuScene::class
    override val size = SizeInt(1000, 600)
    override val windowSize = SizeInt(1000, 600)

    override suspend fun AsyncInjector.configure() {
        mapPrototype { PlayScene(get()) }
        mapPrototype { MenuScene() }
    }
}
