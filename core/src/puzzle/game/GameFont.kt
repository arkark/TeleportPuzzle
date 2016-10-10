package puzzle.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont

object GameFont {
    lateinit var font: BitmapFont

    fun load() {
        font = BitmapFont(Gdx.files.internal("fonts/po.fnt"))
    }

    fun dispose() {
        font.dispose()
    }

}