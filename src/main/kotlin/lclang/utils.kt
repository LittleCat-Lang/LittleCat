package lclang

import lclang.exceptions.LCLangException
import lclang.lang.CharClass
import lclang.lang.StringClass
import lclang.libs.Library
import lclang.methods.LibraryMethod
import lclang.methods.Method
import lclang.types.BaseType
import lclang.types.Types
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun List<BaseType>.isAccept(array: List<BaseType>): Int {
    if(size!=array.size) return array.size-1
    for((i, type) in this.withIndex())
        if(!type.isAccept(array[i]))
            return i

    return -1
}

fun method(args: List<BaseType> = listOf(), returnType: BaseType = Types.VOID,
           run: (List<Any?>, LCFileVisitor) -> Any?): Method {
    return object: LibraryMethod(args, returnType){
        override fun callMethod(fileVisitor: LCFileVisitor, args: List<Any?>) = run(args, fileVisitor)
    }
}

fun runInput(file: LCFileVisitor, input: String) {
    val lexer = lclangLexer(CharStreams.fromString(input))
    val tokens = CommonTokenStream(lexer)
    val parser = lclangParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(ErrorListener(file.path))

    file.execute(parser.file())
}