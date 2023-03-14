package com.group.ardiagram.data

import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.License

class ARExpression(
    function: String,
    x: Float = 0.0f,
    y: Float = 0.0f
) {
    init {
        // Don't mind this, it's there just to disable red scary log warnings.
        License.iConfirmCommercialUse("Darth Vader")
    }

    private val xArg: Argument = Argument("x", x.toDouble())
    private val yArg: Argument = Argument("y", y.toDouble())
    private val expression: Expression = Expression(function, xArg, yArg)

    fun setX(value: Float): ARExpression {
        xArg.argumentValue = value.toDouble()
        return this
    }

    fun setY(value: Float): ARExpression {
        yArg.argumentValue = value.toDouble()
        return this
    }

    fun evaluate(): Float {
        return expression.calculate().toFloat()
    }
}