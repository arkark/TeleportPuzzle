package puzzle

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import puzzle.game.GameFont
import puzzle.game.GameSound
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.scene.SceneBase
import puzzle.scene.SceneTitle

class Puzzle : ApplicationAdapter() {
    private lateinit var logger: FPSLogger

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport

    private lateinit var scene: SceneBase
    private lateinit var nextScene: SceneBase

    internal lateinit var batch: SpriteBatch

    override fun create() {
        logger = FPSLogger()

        camera = OrthographicCamera(GameSystem.WINDOW_W.toFloat(), GameSystem.WINDOW_H.toFloat())
        camera.setToOrtho(false, GameSystem.WINDOW_W.toFloat(), GameSystem.WINDOW_H.toFloat())
        viewport = FitViewport(GameSystem.WINDOW_W.toFloat(), GameSystem.WINDOW_H.toFloat(), camera)

        Gdx.graphics.setDisplayMode(GameSystem.WINDOW_W, GameSystem.WINDOW_H, false)

        GameStage.Texture.load()
        GameFont.load()
        GameSound.init()

        batch = SpriteBatch()

        scene = SceneTitle(batch)
        nextScene = scene
    }

    override fun dispose() {
        GameStage.Texture.dispose()
        GameFont.dispose()
        GameSound.dispose()
        batch.dispose()
    }

    override fun render() {
        camera.update()
        batch.projectionMatrix = camera.combined

        if (!(scene === nextScene)) {
            scene = nextScene
        }
        val tempScene = scene.update()
        if (tempScene != null) nextScene = tempScene

        //logger.log()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

}
