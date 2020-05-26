package calculator

import calculator.Expecting.OPERAND
import calculator.Expecting.OPERATOR
import calculator.Operator.Invalid
import calculator.Operator.Minus
import java.math.BigInteger
import kotlin.math.pow

fun main() {
    repl@ do {
        val input = readLine()!!.trim()
        when {
            input.isBlank() -> continue@repl
            input == "/exit" -> break@repl
            input == "/help" -> println("The program calculates the sum of numbers")
            input.startsWith("/") -> println("Unknown command")
            input.contains("=") -> tryAssignment(input)
            else -> tryEvaluation(input)
        }
    } while (true)
    println("Bye!")
}

private fun tryAssignment(input: String) {
    val assignment = input.split('=').map { it.trim() }
    if (assignment.size != 2 || !ASSIGNMENT.matches(assignment[1])) {
        println("Invalid assignment")
        return
    }
    if (!IDENTIFIER.matches(assignment[0])) {
        println("Invalid identifier")
        return
    }
    try {
        val (definition, expression) = assignment
        MEMORY[definition] = expression.toBigInteger()
    } catch (e: Exception) {
        println("Unknown variable")
    }
    return
}

private fun tryEvaluation(input: String) {
    try {
        when (val result = evaluate(input)) {
            is Evaluated -> println(result.value)
            is Error -> println(result.message)
        }
    } catch (e: Exception) {
        println("Invalid expression")
    }
}

val SYMBOLS = arrayOf(LeftParens.symbol, RightParens.symbol).plus(Operator.symbols.toTypedArray())
val TOKEN_REGEX = SYMBOLS.joinToString("|") { "\\$it" }.toRegex()
val MEMORY = mutableMapOf<String, BigInteger>()
val IDENTIFIER = "[a-zA-Z]+".toRegex()
val ASSIGNMENT = """[a-zA-Z]+|\d+""".toRegex()

enum class Expecting {
    OPERATOR,
    OPERAND
}

sealed class Result
open class Parsed : Result()
open class Evaluated(val value: BigInteger) : Result()
open class Error(val message: String) : Result()
open class UnknownVariable : Error("Unknown variable")
open class InvalidExpression : Error("Invalid expression")

sealed class Token
sealed class Operand(val value: BigInteger) : Token()
data class Number(val number: BigInteger) : Operand(number)
sealed class Parens(val symbol: Char) : Token()
object LeftParens : Parens('(')
object RightParens : Parens(')')
object InvalidToken : Token()

sealed class Operator(val symbol: Char, private val precedence: Int, val operation: BigInteger.(BigInteger) -> BigInteger, val repeatable: Boolean = false) : Token() {
    fun isGreaterThan(o: Operator) = precedence > o.precedence

    fun apply(o2: Operand, o1: Operand) = Number(operation(o1.value, o2.value))

    companion object {
        private val values = arrayOf(Pow, Times, Div, Plus, Minus)
        val symbols = values.map { it.symbol }.toCharArray()
        fun of(c: Char) = values.find { c == it.symbol } ?: Invalid
    }

    object Pow : Operator('^', 3, { this.toDouble().pow(it.toInt()).toBigDecimal().toBigInteger() })
    object Times : Operator('*', 2, { this * it })
    object Div : Operator('/', 2, { this / it })
    object Plus : Operator('+', 1, { this + it }, true)
    object Minus : Operator('-', 1, { this - it }, true)
    object Invalid : Operator(Char.MIN_VALUE, Int.MIN_VALUE, { it })
}

fun MutableList<Token>.peek() = if (this.isNotEmpty()) this.last() else InvalidToken
fun <T> MutableList<T>.enqueue(t: T) = this.add(t)
fun <T> MutableList<T>.dequeue() = this.removeAt(0)
fun <T> MutableList<T>.pop() = this.removeAt(this.lastIndex)

val QUEUE = mutableListOf<Token>()
fun enqueue(t: Token) = QUEUE.enqueue(t)
fun dequeue() = QUEUE.dequeue()

val STACK = mutableListOf<Token>()
fun push(t: Token) = STACK.add(t)
fun peek() = STACK.peek()
fun pop(): Token = STACK.pop()
fun popOperand() = pop() as Operand
fun isEmpty() = STACK.isEmpty()
fun isGreater(operator: Operator) = isEmpty() || operator.isGreaterThan(peek() as Operator)
fun popAndEnqueue() = enqueue(pop())
fun hasLeftParensOfTop() = !isEmpty() && peek() == LeftParens
fun handleOperand(operand: Operand) = enqueue(operand)
fun handleOperator(operator: Operator) {
    when {
        isEmpty() || hasLeftParensOfTop() -> push(operator)
        isGreater(operator) -> push(operator)
        else -> {
            do {
                popAndEnqueue()
            } while (!(hasLeftParensOfTop() || isGreater(operator)))
            push(operator)
        }
    }
}


private fun evaluate(input: String): Result {
    QUEUE.clear()
    STACK.clear()
    val result = parse(input)
    if (result is Error) return result

    do {
        when (val token = dequeue()) {
            is Operand -> push(token)
            is Operator -> push(token.apply(popOperand(), popOperand()))
        }
    } while (QUEUE.isNotEmpty())

    val value = popOperand().value

    return Evaluated(value)
}

fun parse(input: String): Result {
    var parens = 0
    val expression = input.replace("""\s+""".toRegex(), "")
    val operands = expression.split(TOKEN_REGEX)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toMutableList()
    var cursor = 0
    var expecting: Expecting = OPERAND
    var operandEvaluated = false
    var currentNumber: BigInteger
    var signOfNumber = 1
    while (cursor < expression.length) {
        when (val c = expression[cursor]) {
            '(' -> {
                parens++
                push(LeftParens)
            }
            ')' -> {
                parens--
                do {
                    popAndEnqueue()
                } while (!hasLeftParensOfTop())
                pop()
            }
            in Operator.symbols -> {
                val op = Operator.of(c)
                if (op is Invalid) return InvalidExpression()
                when (expecting) {
                    OPERATOR -> {
                        expecting = OPERAND
                        operandEvaluated = false
                        handleOperator(op)
                    }
                    OPERAND -> {
                        if (op.repeatable) {
                            if (op is Minus) {
                                signOfNumber *= -1
                            }
                        } else {
                            return InvalidExpression()
                        }
                    }
                }
            }
            in 'a'..'z', in 'A'..'Z', in '0'..'9' -> {
                if (expecting == OPERATOR) return InvalidExpression()
                currentNumber = signOfNumber.toBigInteger() * if (c.isDigit()) {
                    val digits = operands.removeAt(0)
                    cursor += digits.length - 1
                    digits.toBigInteger()
                } else {
                    val identifier = operands.removeAt(0)
                    if (!MEMORY.containsKey(identifier)) {
                        return UnknownVariable()
                    }
                    cursor += identifier.length - 1
                    MEMORY[identifier]!!
                }
                signOfNumber = 1
                expecting = OPERATOR
                operandEvaluated = true
                handleOperand(Number(currentNumber))
            }
            else -> return InvalidExpression()
        }
        cursor++
    }
    if (parens != 0 || expecting == OPERAND && !operandEvaluated) {
        return InvalidExpression()
    }
    while (STACK.isNotEmpty()) popAndEnqueue()
    return Parsed()
}
