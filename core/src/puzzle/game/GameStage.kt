package puzzle.game

import com.badlogic.gdx.Gdx

// ステージに関するパラメータ
object GameStage {

    object Tile {
        val W: Float = 24f
        val H: Float = 24f
    }

    val NUM_X: Int = 30
    val NUM_Y: Int = 30

    val LEFT: Float = (GameSystem.WINDOW_W - Tile.W * NUM_X)/2 // ステージの左端のx座標
    val BOTTOM : Float = (GameSystem.WINDOW_H - Tile.H * NUM_Y)/2 // ステージの上端のy座標

    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    val titleStage: Data = Data(
            "",
            arrayOf("stages/title.txt")
    )

    // ステージデータ
    val DataList: Array<Data> = arrayOf(
            Data(
                    "stage1",
                    arrayOf(
                            "stages/001/01.txt",
                            "stages/001/02.txt",
                            "stages/001/03.txt",
                            "stages/001/04.txt",
                            "stages/001/05.txt",
                            "stages/001/06.txt",
                            "stages/001/07.txt",
                            "stages/001/08.txt",
                            "stages/stageClear.txt"
                    )
            ),

            Data(
                    "stage2",
                    arrayOf(
                            "stages/002/01.txt",
                            "stages/002/02.txt",
                            "stages/002/03.txt",
                            "stages/002/04.txt",
                            "stages/002/05.txt",
                            "stages/002/06.txt",
                            "stages/002/07.txt",
                            "stages/002/08.txt",
                            "stages/stageClear.txt"
                    )
            ),


            Data(
                    "stage3",
                    arrayOf(
                            "stages/003/01.txt",
                            "stages/003/02.txt",
                            "stages/003/03.txt",
                            "stages/003/04.txt",
                            "stages/003/05.txt",
                            "stages/003/06.txt",
                            "stages/003/07.txt",
                            "stages/stageClear.txt"
                    )
            )

    )

    class Data(
            val name: String,
            val pathList: Array<String>
    ) {}

    object Texture {
        lateinit var block: com.badlogic.gdx.graphics.Texture
        lateinit var space: com.badlogic.gdx.graphics.Texture
        lateinit var player: com.badlogic.gdx.graphics.Texture
        lateinit var cross: com.badlogic.gdx.graphics.Texture
        lateinit var goalPoint: com.badlogic.gdx.graphics.Texture
        lateinit var goalPointBackground: com.badlogic.gdx.graphics.Texture
        lateinit var needle: com.badlogic.gdx.graphics.Texture
        lateinit var gravitater: com.badlogic.gdx.graphics.Texture
        lateinit var gravitaterUp: com.badlogic.gdx.graphics.Texture
        lateinit var gravitaterDown: com.badlogic.gdx.graphics.Texture
        lateinit var gravitaterLeft: com.badlogic.gdx.graphics.Texture
        lateinit var gravitaterRight: com.badlogic.gdx.graphics.Texture
        lateinit var background: com.badlogic.gdx.graphics.Texture

        lateinit var fade               : com.badlogic.gdx.graphics.Texture
        lateinit var stageSelector      : com.badlogic.gdx.graphics.Texture

        fun load() {
            block = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/block.gif"))
            space = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/space.gif"))
            player = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/player.gif"))
            cross = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/cross.gif"))
            goalPoint = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/goalpoint.gif"))
            goalPointBackground = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/goalpointBackground.gif"))
            needle = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/needle.gif"))
            gravitater = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/gravitater.gif"))
            gravitaterUp = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/gravitaterUp.gif"))
            gravitaterDown = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/gravitaterDown.gif"))
            gravitaterLeft = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/gravitaterLeft.gif"))
            gravitaterRight = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/gravitaterRight.gif"))
            background = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/background.gif"))
            fade = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/fade.gif"))
            stageSelector = com.badlogic.gdx.graphics.Texture(Gdx.files.internal("images/selector.gif"))
        }

        fun dispose() {
            block.dispose()
            space.dispose()
            player.dispose()
            cross.dispose()
            goalPoint.dispose()
            goalPointBackground.dispose()
            needle.dispose()
            gravitater.dispose()
            gravitaterUp.dispose()
            gravitaterDown.dispose()
            gravitaterLeft.dispose()
            gravitaterRight.dispose()
            background.dispose()
            fade.dispose()
            stageSelector.dispose()
        }
    }

}