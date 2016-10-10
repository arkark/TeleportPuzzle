package puzzle.stage.entity.mover

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.game.GameStage
import puzzle.stage.Stage
import puzzle.stage.entity.tile.Block

class DummyPlayer(val player: Player): Mover(player.indexX, player.indexY) {

    private lateinit var stage: Stage

    private val cross: Cross = Cross(this)

    init {
        sprite = Sprite(GameStage.Texture.player)
    }

    override fun update() {
        indexX = player.indexX
        indexY = player.indexY + GameStage.NUM_Y/2 // ずらす
        diffX  = player.diffX
        diffY  = player.diffY
        super.update()
        cross.update()
    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        val a = (if (isCollide()) 0.1f else 0.4f) * alpha
        sprite.setAlpha(a)
        super.draw(batch, left, bottom, angle, scale, a)
        if (isCollide()) {
            cross.draw(batch, left, bottom, angle, scale, alpha)
        }
    }

    fun isCollide(): Boolean {
        if (stage.tileList[indexY][indexX] is Block) return true
        if (diffX<0 && stage.tileList[indexY][indexX-1] is Block) return true
        if (diffX>0 && stage.tileList[indexY][indexX+1] is Block) return true
        if (diffY<0 && stage.tileList[indexY-1][indexX] is Block) return true
        if (diffY>0 && stage.tileList[indexY+1][indexX] is Block) return true
        if (diffX<0 && diffY<0 && stage.tileList[indexY-1][indexX-1] is Block) return true
        if (diffX<0 && diffY>0 && stage.tileList[indexY+1][indexX-1] is Block) return true
        if (diffX>0 && diffY<0 && stage.tileList[indexY-1][indexX+1] is Block) return true
        if (diffX>0 && diffY>0 && stage.tileList[indexY+1][indexX+1] is Block) return true
        return false
    }

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    class Cross(val parent: DummyPlayer) : Mover(parent.indexX, parent.indexY) {

        init {
            sprite = Sprite(GameStage.Texture.cross)
        }

        override fun update() {
            indexX = parent.indexX
            indexY = parent.indexY
            diffX  = parent.diffX
            diffY  = parent.diffY
            super.update()
        }

        override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
            val a = 0.6f*alpha
            super.draw(batch, left, bottom, angle, scale, a)
        }
    }

}