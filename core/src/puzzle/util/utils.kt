package puzzle.util

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

operator fun Vector2.plus(rhs: Vector2): Vector2 = this.add(rhs)
operator fun Vector2.minus(rhs: Vector2): Vector2 = this.sub(rhs)
operator fun Vector2.plus(rhs: Float): Vector2 = this.add(rhs, rhs)
operator fun Vector2.minus(rhs: Float): Vector2 = this.sub(rhs, rhs)
operator fun Vector2.times(rhs: Float): Vector2 = Vector2(x*rhs, y*rhs)
operator fun Vector2.div(rhs: Float): Vector2 = Vector2(x/rhs, y/rhs)
operator fun Vector2.mod(rhs: Float): Vector2 = Vector2(x%rhs, y%rhs)
operator fun Float.plus(rhs: Vector2): Vector2 = Vector2(this+rhs.x, this+rhs.y)
operator fun Float.minus(rhs: Vector2): Vector2 = Vector2(this-rhs.x, this-rhs.y)
operator fun Float.times(rhs: Vector2): Vector2 = Vector2(this*rhs.x, this*rhs.y)
operator fun Float.div(rhs: Vector2): Vector2 = Vector2(this/rhs.x, this/rhs.y)
operator fun Float.mod(rhs: Vector2): Vector2 = Vector2(this%rhs.x, this%rhs.y)

// アルファブレンド
fun Batch.setBlendModeToBlend() = this.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
// 加算
fun Batch.setBlendModeToAdd() = this.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE)
// 減算
fun Batch.setBlendModeToSubtract() = this.setBlendFunction(GL20.GL_ZERO, GL20.GL_ONE_MINUS_SRC_ALPHA)
