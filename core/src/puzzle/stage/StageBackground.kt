package puzzle.stage

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.stage.entity.mover.Player

class StageBackground(val player: Player) {

    private val spriteBackground = Sprite(GameStage.Texture.background)
    private val spriteBlock = Sprite(GameStage.Texture.block)

    fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle:Float, scale: Float, alpha: Float, stageRotating: Boolean) {

        val w = GameStage.Tile.W * scale
        val h = GameStage.Tile.H * scale
        val initX = left
        val initY = bottom
        spriteBackground.setSize(w, h)
        spriteBlock.setSize(w, h)

        val lx = if (stageRotating) -GameStage.NUM_X/2 else 0 - 1
        val rx = if (stageRotating) GameStage.NUM_X - 1 + GameStage.NUM_X/2 else GameStage.NUM_X - 1 + 1
        val ly = if (stageRotating) -GameStage.NUM_Y/2 else 0 - 1
        val ry = if (stageRotating) GameStage.NUM_Y - 1 + GameStage.NUM_Y/2 else GameStage.NUM_Y - 1 + 1

        for (i in lx..rx) for (j in ly..ry) {
            if (isSameArea(j)) {
                spriteBackground.setColor(1f, 1f, 1f, 1f)
            } else {
                spriteBackground.setColor(0.6f, 0.55f, 0.5f, 1f)
            }
//            (if (isSameArea(j)) 1f else 0.65f).let {
//                spriteBackground.setColor(it, it, it, 1f)
//            }

            spriteBackground.setAlpha(alpha)
            spriteBlock.setAlpha(alpha)

            val x = initX + i * w
            val y = initY + j * h

            // 画面外を描画しない
                val d = Vector2(x-GameSystem.WINDOW_W, y-GameSystem.WINDOW_H).len()
                if (d > Math.max(GameSystem.WINDOW_W, GameSystem.WINDOW_H)*Math.sqrt(2.0) + w) continue

            spriteBackground.setPosition(x, y)
            spriteBackground.setOrigin(GameSystem.WINDOW_W / 2f - spriteBackground.x, GameSystem.WINDOW_H / 2f - spriteBackground.y)
            spriteBackground.rotation = angle
            spriteBackground.draw(batch)
            spriteBackground.rotation = 0f

            if (i<0 || i>=GameStage.NUM_X || j<0 || j>=GameStage.NUM_Y) {
                spriteBlock.setPosition(x, y)
                spriteBlock.setOrigin(GameSystem.WINDOW_W / 2f - spriteBackground.x, GameSystem.WINDOW_H / 2f - spriteBackground.y)
                spriteBlock.rotation = angle
                spriteBlock.draw(batch)
                spriteBlock.rotation = 0f
            }
        }
    }

    private fun isSameArea(indexY: Int): Boolean {
        val flg1 = indexY < GameStage.NUM_Y/2
        val flg2 = player.indexY < GameStage.NUM_Y/2
        return flg1==flg2
    }

}