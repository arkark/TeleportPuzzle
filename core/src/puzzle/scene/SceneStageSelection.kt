package puzzle.scene

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.stage.StageSelectManager
import puzzle.util.FadeManager

class SceneStageSelection(val batch: SpriteBatch): SceneBase() {

    private val stageSelectManager: StageSelectManager = StageSelectManager()
    private val fadeManager: FadeManager = FadeManager()

    init {
        update[Phase.OPENING] = fn@ {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            if (frameCount == 0) fadeManager.set(10, false)

            batch.begin()
            stageSelectManager.draw(batch)
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

            stageSelectManager.update()

            batch.begin()
            stageSelectManager.draw(batch)
            batch.end()

            if (stageSelectManager.isFinished) {
                phase = Phase.END
            }

            return@fn null
        }


        update[Phase.END] = fn@ {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            if (frameCount == 0) fadeManager.set(10, true)

            batch.begin()
            stageSelectManager.draw(batch)
            fadeManager.draw(batch)
            batch.end()

            fadeManager.step()

            if (fadeManager.done()) {
                return@fn stageSelectManager.getNextScene(batch)
            }

            return@fn null
        }
    }

}