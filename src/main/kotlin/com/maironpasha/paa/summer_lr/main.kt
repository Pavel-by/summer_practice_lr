package com.maironpasha.paa.summer_lr

import java.text.ParseException

class Node(
    val name: String,
    var left: Node? = null,
    var right: Node? = null,
    var rawLength: Int = 0
) {
    fun compareTree(other: Node?): Boolean {
        if (other == null)
            return false

        if (name != other.name)
            return false

        if (left != other.left && left?.compareTree(other.left) != true)
            return false

        if (right != other.right && right?.compareTree(other.right) != true)
            return false

        return true
    }
}

class Parser {
    fun parse(s: String): Node {
        val node = parse(s, 0)!!

        if (node.rawLength != s.length)
            throw ParseException("Line parsed, but extra symbols found at the end of line.", node.rawLength)

        return node
    }

    private fun parse(s: String, startIndex: Int): Node? {
        if (startIndex >= s.length)
            throw ParseException("Index $startIndex more than string length: ${s.length}", startIndex)

        if (s[startIndex] == '^')
            return null

        if (s[startIndex] != '(')
            throw ParseException("${s[startIndex]} symbol received; '(' symbol expected", startIndex)

        var index = startIndex + 1

        val nameEndIndex = s.indexOfAny(
                charArrayOf('(', '^', ')'),
                index
        )

        if (nameEndIndex == index)
            throw ParseException("Name of node is empty.", index)

        index = nameEndIndex

        val node = Node(s.substring(startIndex + 1, index))

        if (s[index] in "(^") {
            node.left = parse(s, index)
            index += node.left?.rawLength ?: 1

            if (index >= s.length)
                throw ParseException("Invalid end on line", index)

            if (s[index] in "(^") {
                node.right = parse(s, index)
                index += node.right?.rawLength ?: 1

                if (index >= s.length)
                    throw ParseException("Invalid end on line", index)

                if (s[index] in "(^")
                    throw ParseException("3rd child found on position $index. No more than 2 children are possible.", index)
            }
        }

        if (s[index] != ')')
            throw ParseException("')' symbol expected on position $index, but ${s[index]} was found.", index)

        node.rawLength = index - startIndex + 1

        return node
    }

    companion object {
        val instance: Parser by lazy { Parser() }
    }
}

fun main() {
    try {
        println("You should enter trees as '(<name>[<com.maironpasha.paa.summer_lr.Node>[<com.maironpasha.paa.summer_lr.Node>]])', where <com.maironpasha.paa.summer_lr.Node> is line in format " +
                "'(<name>[<com.maironpasha.paa.summer_lr.Node>[<com.maironpasha.paa.summer_lr.Node>]])' or '^' symbol, <name> is name of the tree's node and <name> could be " +
                "empty. Expression '[<something>] is mean, that <something> string might be empty.")

        println("Example: (root_node(left_child_of_root)(right_child_of_root^(right_child_of_right_child_of_root)))")
        println()

        print("Enter first tree: ")
       val first = Parser.instance.parse(readLine()?.trim() ?: "")

        print("Enter second tree: ")
        val second = Parser.instance.parse(readLine()?.trim() ?: "")

        if (first.compareTree(second))
            println("Trees are equals.")
        else
            println("Trees are different.")
    } catch (e: ParseException) {
        println("Cannot parse input.")
        e.printStackTrace()
    } catch (e: Throwable) {
        println("Unknown error was thrown.")
        e.printStackTrace()
    }
}