package lclang.exceptions

class VariableNotFoundException(variable: String, line: Int, column: Int):
    Exception("Variable $variable not found at line $line, column $column")