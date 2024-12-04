package day3

import day3.MulFunction.Normal.Do
import day3.MulFunction.Normal.Dont

object MulCompiler {
    var dosFeatureEnabled = false

    private var state = ProgramState()
    fun compile(char: Char) {
        val cState = state
        with(cState) {
            val function = mulFunction?.evaluate(char) ?: MulFunction(mulFunction, char)
            state = when (function?.isExecutable) {
                true -> when (function) {
                    is MulFunction.Mul -> copy(
                        products = if (!dosFeatureEnabled || mulEnabled) products + function()!! else products,
                        mulFunction = null
                    )

                    is Dont,
                    is Do -> copy(
                        mulEnabled = function is Do,
                        mulFunction = null
                    )
                }

                else -> state.copy(mulFunction = function)
            }
        }
    }

    fun runMul() = state.products.sum().also { state = ProgramState() }

    private data class ProgramState(
        val mulFunction: MulFunction<*>? = null,
        val products: List<Int> = emptyList(),
        val mulEnabled: Boolean = true
    )
}