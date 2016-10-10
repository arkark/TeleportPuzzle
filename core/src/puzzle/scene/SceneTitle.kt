package puzzle.scene

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.game.GameStage
import puzzle.stage.StageManager
import puzzle.util.FadeManager

class SceneTitle(val batch: SpriteBatch): SceneBase() {

    private val stageManager: StageManager = StageManager(GameStage.titleStage, 0)
    private val fadeManager: FadeManager = FadeManager()

    init {
        stageManager.invisualizeStageName()

        update[Phase.OPENING] = fn@ {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            if (frameCount == 0) {
                fadeManager.set(10, false)
            }

            batch.begin()
            stageManager.draw(batch)
            fadeManager.draw(batch)
            batch.end()

            fadeManager.step()

            if (fadeManager.done()) {
                phase = Phase.MIDDLE
            }

            return@fn null
        }

        update[Phase.MIDDLE] = fn@ {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            stageManager.update()

            batch.begin()
            stageManager.draw(batch)
            batch.end()

            if (stageManager.isFinished) {
                phase = Phase.END
            }

            return@fn null
        }

        update[Phase.END] = fn@ {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            if (frameCount == 0) fadeManager.set(10, true)

            batch.begin()
            stageManager.draw(batch)
            fadeManager.draw(batch)
            batch.end()

            fadeManager.step()

            if (fadeManager.done()) {
                return@fn SceneStageSelection(batch)
            }

            return@fn null
        }
    }

}