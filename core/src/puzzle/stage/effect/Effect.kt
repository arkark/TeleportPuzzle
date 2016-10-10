package puzzle.stage.effect

import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class Effect() {

    abstract fun step()
    abstract fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle : Float, scale: Float, alpha: Float)
    abstract fun done(): Boolean

}