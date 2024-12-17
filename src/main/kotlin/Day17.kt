import java.io.File

fun main() {
    val (registers, progInput) = File("./src/main/resources/day17.txt")
        .readText()
        .split("\n\n")

    var (regA, regB, regC) = Regex("\\d+").findAll(registers).toList().map { it.value.toInt() }
    val program =  Regex("\\d+").findAll(progInput).toList().map { it.value.toInt() }

    println("A: $regA, B: $regB, C: $regC")
    println(program)

    var pointer = 0
    val result = mutableListOf<Int>()

    while (pointer >= 0 && pointer < program.size) {
        // Ensure we have at least 2 elements left to read
        if (pointer + 1 >= program.size) break

        val opCode = program[pointer]
        val literalOperand = program[pointer + 1]

        // Combo operand calculation based on description
        val comboOperand = when (literalOperand) {
            in 0..3 -> literalOperand
            4 -> regA
            5 -> regB
            6 -> regC
            else -> 0
        }

        when (opCode) {
            // adv: Divide A by 2^(combo operand)
            0 -> {
                regA = regA / (1 shl comboOperand)
            }
            // bxl: Bitwise XOR B with literal operand
            1 -> {
                regB = regB xor literalOperand
            }
            // bst: Set B to combo operand modulo 8
            2 -> {
                regB = comboOperand % 8
            }
            // jnz: Jump if A is not zero
            3 -> {
                if (regA != 0) {
                    pointer = literalOperand
                    continue
                }
            }
            // bxc: XOR B with C (ignore operand)
            4 -> {
                regB = regB xor regC
            }
            // out: Output combo operand modulo 8
            5 -> {
                result.add(comboOperand % 8)
            }
            // bdv: Divide A by 2^(combo operand), store in B
            6 -> {
                regB = regA / (1 shl comboOperand)
            }
            // cdv: Divide A by 2^(combo operand), store in C
            7 -> {
                regC = regA / (1 shl comboOperand)
            }
            else -> throw IllegalArgumentException("Unknown opcode: $opCode")
        }
        // Only increment pointer by 2 if not jumping
        pointer += 2
    }
    println("Final Registers - A: $regA, B: $regB, C: $regC")
    println("Output: ${result.joinToString(",")}")
}