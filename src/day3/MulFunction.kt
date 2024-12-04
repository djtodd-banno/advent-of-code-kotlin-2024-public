package day3

import day3.MulFunction.Normal.Do
import day3.MulFunction.Normal.Dont
import toNullableInt

internal sealed class MulFunction<T> {
    abstract val name: String
    abstract val step: Int
    abstract operator fun invoke(): T?
    abstract infix fun evaluate(char: Char): MulFunction<T>?
    operator fun get(index: Int): Char = name[index]

    val lastStep: Int get() = name.lastIndex
    val isExecutable: Boolean get() = step == lastStep

    companion object {
        operator fun invoke(from: MulFunction<*>?, char: Char): MulFunction<*>? = (from as? Do)
            ?.let { Dont(from.step).evaluate(char) }
            ?: Mul().evaluate(char)
            ?: Do().evaluate(char)
    }

    internal data class Mul(
        override val step: Int = 0,
        private var _x: String = "",
        private var _y: String = "",
    ) : MulFunction<Int>() {
        override val name: String = "mul(X,Y)"
        override fun invoke(): Int? = y?.let { x?.times(it) }?.takeIf { step == lastStep }

        override fun evaluate(char: Char): Mul? = when {
            step == name.indexOf('X') -> evaluateX(char)
            step == name.indexOf('Y') -> evaluateY(char)

            char == name[step] -> if (step == lastStep) this else copy(step = step + 1)

            else -> null
        }

        private fun evaluateY(char: Char) = when {
            char.isDigit() && _y.length < 3 -> copy(_y = _y + char)
            char == name[step + 1] -> copy(step = lastStep)
            else -> null
        }

        private fun evaluateX(char: Char) = when {
            char.isDigit() && _x.length < 3 -> copy(_x = _x + char)
            char == name[step + 1] -> copy(step = step + 2)
            else -> null
        }

        private val x: Int?
            get() = _x.takeIf { it.isNotEmpty() }
                ?.toNullableInt()
        private val y: Int?
            get() = _y.takeIf { it.isNotEmpty() }
                ?.toNullableInt()
    }

    internal sealed class Normal<T> : MulFunction<T>() {
        abstract fun inc(): Normal<T>
        override fun evaluate(char: Char): Normal<T>? {
            return when (char) {
                name[step] -> if (step == lastStep) this else inc()
                else -> null
            }
        }

        data class Do(override val step: Int = 0) : Normal<Boolean>() {
            override val name: String = "do()"
            override fun inc() = copy(step = step + 1)
            override fun invoke(): Boolean? = true.takeIf { isExecutable }
        }

        data class Dont(override val step: Int = 0) : Normal<Boolean>() {
            override fun inc() = copy(step = step + 1)
            override val name: String = "don't()"
            override fun invoke(): Boolean? = false.takeIf { isExecutable }

        }
    }
}