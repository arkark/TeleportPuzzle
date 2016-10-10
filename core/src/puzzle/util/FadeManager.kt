package puzzle.util

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.game.GameStage
import puzzle.game.GameSystem

class FadeManager() {

    private val ease: EasingManager = Ease.Linear.InOut.build()
    private val sprite: Sprite = Sprite(GameStage.Texture.fade)

    fun set(duration: Int, isFadeout: Boolean) {
        ease.set(if (isFadeout) 0f else 1f , if (isFadeout) 1f else 0f, duration)
    }

    fun step() {
        ease.step()
    }

    fun draw(batch: SpriteBatch) {
        val w = GameSystem.WINDOW_W.toFloat()
        val h = GameSystem.WINDOW_H.toFloat()
        val x = 0f
        val y = 0f
        sprite.setSize(w, h)
        sprite.setPosition(x, y)
        sprite.setAlpha(ease.get())
        batch.setBlendModeToSubtract()
        sprite.draw(batch)
        batch.setBlendModeToBlend()
    }

    fun done() = ease.done
}