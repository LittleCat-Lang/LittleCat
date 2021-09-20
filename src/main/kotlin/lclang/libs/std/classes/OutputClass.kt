package lclang.libs.std.classes

import lclang.LCClass
import lclang.LCFileVisitor
import lclang.ValueList
import lclang.method
import lclang.types.Types
import java.io.OutputStream
import java.io.PrintStream

const val OUTPUT_CLASSNAME = "Output"
class OutputClass(fileVisitor: LCFileVisitor): LCClass(OUTPUT_CLASSNAME, fileVisitor) {
    constructor(input: OutputStream, fileVisitor: LCFileVisitor) : this(fileVisitor) {
        val printer = PrintStream(input)

        globals["println"] = method(listOf(Types.ANY)) { list, _ -> printer.println(list[0]) }
        globals["print"] = method(listOf(Types.ANY)) { list, _ -> printer.print(list[0]) }
        globals["printf"] = method(listOf(Types.ANY, Types.ARRAY)) { list, _ -> printer.printf(list[0].toString(),
            * (list[1] as ValueList).list.map { it.get() }.toTypedArray())}
        globals["flush"] = method { _, _ -> printer.flush() }
        globals["close"] = method { _, _ -> printer.close() }
    }
}