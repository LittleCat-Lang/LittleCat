package lclang.libs.std

import lclang.*
import lclang.exceptions.LCLangException
import lclang.lang.ArrayClass
import lclang.lang.CharClass
import lclang.lang.StringClass
import lclang.libs.Library
import lclang.libs.std.classes.*
import lclang.methods.Method
import lclang.types.CallableType
import lclang.types.Types
import java.util.regex.Pattern
import kotlin.system.exitProcess


class StdLibrary: Library("std") {

    init {
        globals["LC_VERSION"] = StringClass(Global.version).asValue()
        classes["array"] = ArrayClass()
        classes["string"] = StringClass()
        classes["char"] = CharClass()
        classes["Socket"] = SocketClass()
        classes["File"] = FileClass()

        globals["math"] = MathClass().asValue()
        globals["std"] = StdClass().asValue()

        globals["thread"] = method(listOf(CallableType(listOf(), Types.VOID)), Types.VOID) {
            val method = it[0] as Method
            Thread {
                method.call(this, listOf())
            }.start()
        }

        // DEPRECATED: NEED TRANSFER TO STREAMS.LCAT LIB
        globals["printError"] = method(listOf(Types.ANY), Types.VOID) {
            println("$ERROR_COLOR${it[0]}")
        }

        globals["assert"] = method(listOf(Types.BOOL)) { args ->
            if(args[0]==false) throw LCLangException("Assertion Error", "Value is false", this)
        }

        globals["exit"] = method(listOf(Types.INT), Types.VOID) { exitProcess(it[0] as Int) }
        globals["time"] = method(listOf(), Types.LONG) { System.currentTimeMillis() / 1000 }
        globals["millisTime"] = method(listOf(), Types.LONG) { System.currentTimeMillis() }
        globals["sleep"] = method(listOf(Types.LONG), Types.VOID) {
            Thread.sleep(it[0] as Long)
        }

        globals["regexMatch"] = method(listOf(Types.STRING, Types.STRING), Types.ARRAY) {

            val matcher = Pattern.compile(it[0].toString())
                .matcher(it[1].toString())

            if (!matcher.find()) return@method false

            val array = ArrayClass()
            array.add(StringClass(matcher.group(0)).asValue())
            for (i in 0 until matcher.groupCount()) {
                val group = matcher.group(i+1)
                array.add(if(group!=null)
                    StringClass(group).asValue()
                else Value(Types.UNDEFINED, null))
            }

            return@method array
        }
    }

    inner class StdClass : LCClass("std") {
        init {
            executor.variables["output"] = OutputClass(System.out).asValue()
            executor.variables["input"] = InputClass(System.`in`).asValue()
        }
    }
}