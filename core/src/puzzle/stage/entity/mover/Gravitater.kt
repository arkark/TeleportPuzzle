package puzzle.stage.entity.mover

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import puzzle.game.GameStage
import puzzle.util.*

class Gravitater(indexX: Int, indexY: Int, direction: GameStage.Direction): Mover(indexX, indexY) {

    val direction: GameStage.Direction = direction

    var isActive: Boolean = true
        get() = revivingEase.done
        private set
    fun enactivate() {
        isActive = false
        revivingEase.set(0f, 1f, revivingTime)
    }

    // 再びActiveになるまでにかかるフレーム数
    private val revivingTime: Int = 80
    private val revivingEase: EasingManager = Ease.Linear.InOut.build()

    private val backMover: Back = Back(this)
    init {
        sprite = Sprite(when(direction) {
            GameStage.Direction.UP    -> GameStage.Texture.gravitaterUp
            GameStage.Direction.DOWN  -> GameStage.Texture.gravitaterDown
            GameStage.Direction.LEFT  -> GameStage.Texture.gravitaterLeft
            GameStage.Direction.RIGHT -> GameStage.Texture.gravitaterRight
        })
    }

    override fun update() {
        super.update()
        backMover.update()
        revivingEase.step()
    }

    fun getCenter(): Vector2 {
        return Vector2(
                (indexX + 0.5f)* GameStage.Tile.W + diffX,
                (indexY + 0.5f)* GameStage.Tile.H + diffY
        )
    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        backMover.draw(batch, left, bottom, angle, scale, alpha)
        val a = (if (isActive) 1.0f else revivingEase.get()) * alpha
        super.draw(batch, left, bottom, angle, scale, a)
    }

    class Back(val parent: Gravitater): Mover(parent.indexX, parent.indexY) {

        init {
            sprite = Sprite(GameStage.Texture.gravitater)
        }

        override fun update() {
            indexX = parent.indexX
            indexY = parent.indexY
            diffX = parent.diffX
            diffY = parent.diffY
            super.update()
        }

        override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
            batch.setBlendModeToAdd()
            super.draw(batch, left, bottom, angle, scale, alpha)
            batch.setBlendModeToBlend()
        }


    }


}