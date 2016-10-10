package puzzle.stage

import puzzle.game.GameStage
import puzzle.stage.entity.mover.*
import puzzle.stage.entity.tile.Block
import puzzle.stage.entity.tile.Space
import puzzle.stage.entity.tile.Tile
import java.io.File

object StageLoader {

    fun load(name: String, path: String): Stage {

        val stageFile = File(path).absoluteFile
        val tileIdList: Array<Array<Int>> = stageFile.readLines().filter { !it.isEmpty() }.map { str -> str.split(",").filter { !it.isEmpty() }.map { c -> c.filter { it!=' ' } }.map { c -> c.toInt() }.toTypedArray() }.toTypedArray().reversedArray()

        var tileList: Array<Array<out Tile>>
        var player: Player? = null
        var moverList: List<out Mover> = listOf()
        var goalNum: Int = 0

        tileList = Array(tileIdList.size) {y -> Array(tileIdList.first().size) { x ->
            val id = tileIdList[y][x]
            when(id) {
                0 -> Space(x, y)
                10 -> Block(x, y)
                20 -> {
                    moverList += Gravitater(x, y, GameStage.Direction.RIGHT)
                    Space(x, y)
                }
                21 -> {
                    moverList += Gravitater(x, y, GameStage.Direction.UP)
                    Space(x, y)
                }
                22 -> {
                    moverList += Gravitater(x, y, GameStage.Direction.LEFT)
                    Space(x, y)
                }
                23 -> {
                    moverList += Gravitater(x, y, GameStage.Direction.DOWN)
                    Space(x, y)
                }
                30 -> {
                    moverList += Needle(x, y)
                    Space(x, y)
                }
                -1 -> {
                    player = Player(x, y)
                    Space(x, y)
                }
                -2 -> {
                    moverList += GoalPoint(x, y)
                    goalNum++
                    Space(x, y)
                }
                else -> {
                    require(false, {"${id} は不正なステージデータです"})
                    Space(x, y)
                }
            }
        }}

        require(player != null, {"Stage dataにプレイヤー情報がありません"})
        require(goalNum > 0, {"ゴールがありません"})
        val stage = Stage(tileList, player!!, moverList.toTypedArray(), StageBackground(player!!), goalNum, name, path)
        player?.setStage(stage)
        moverList.forEach { if (it is GoalPoint) it.setStage(stage) }
        return stage
    }

}