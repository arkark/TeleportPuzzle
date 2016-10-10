package puzzle.stage.entity

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class Entity(indexX: Int, indexY: Int) {
    var indexX: Int = indexX
    var indexY: Int = indexY
    protected lateinit var sprite: Sprite

    fun setImage(sprite: Sprite) {
        this.sprite = sprite
    }

    abstract fun update()
    abstract fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float)

}