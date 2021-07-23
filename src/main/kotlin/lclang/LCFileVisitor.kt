package lclang

import lclang.exceptions.LCLangException
import lclang.libs.Library
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

open class LCFileVisitor(
    val path: String
): LCBaseVisitor() {
    val classes = HashMap<String, LCClass>()
    val libraries = ArrayList<Library>()
    val globals = HashMap<String, Value?>()

    init {
        fileVisitor = this
    }

    override fun visitGlobal(ctx: lclangParser.GlobalContext?): Value? {
        if(ctx==null) return null
        globals[ctx.ID().text] = visitValue(ctx.value())

        return null
    }

    override fun visitComponent(ctx: lclangParser.ComponentContext?): Value? {
        val name = Type.from(ctx!!.type()).name + "\\"
        for(classExpr in ctx.classExpr())
            classes[classExpr.ID().text] = LCClass.from(name,this, classExpr);

        return null
    }

    override fun visitFile(ctx: lclangParser.FileContext?): Value? {
        if(ctx===null) return null

        for(classExpr in ctx.classExpr())
            classes[classExpr.ID().text] = LCClass.from("",this, classExpr)

        for(component in ctx.component())
            visitComponent(component)

        for(library in libraries){
            methods.putAll(library.methods)
            globals.putAll(library.globals)
        }

        for(usage in ctx.use()){
            val requiredFile = File(File(fileVisitor.path).parent, usage.STRING().text
                .substring(1).substringBeforeLast('"'))
            if(!requiredFile.exists())
                throw LCLangException("File", "file "+requiredFile.name+" not found",
                    usage.start.line, usage.stop.line, fileVisitor.path)
            else if(requiredFile.length()!=0L){
                val lexer = lclangLexer(CharStreams.fromString(read(requiredFile)))
                val tokens = CommonTokenStream(lexer)
                val parser = lclangParser(tokens)
                parser.removeErrorListeners()
                parser.addErrorListener(ErrorListener(requiredFile.path.toString()))

                val eval = LCFileVisitor(requiredFile.path.toString()).apply {
                    libraries.addAll(this@LCFileVisitor.libraries)
                    visitFile(parser.file())
                }

                val name = Type.from(usage.type()).name
                if(usage.useGlobal!=null){
                    if(eval.globals.containsKey(name))
                        globals[name] = eval.globals[name]
                    else throw LCLangException("File", "global "+requiredFile.name+" not found",
                        usage.start.line, usage.stop.line, fileVisitor.path)
                }else{
                    if(eval.classes.containsKey(name))
                        classes[name] = eval.classes[name]!!
                    else throw LCLangException("File", "class "+requiredFile.name+" not found",
                        usage.start.line, usage.stop.line, fileVisitor.path)
                }
            }
        }

        for(global in ctx.global())
            visitGlobal(global)

        for(method in ctx.method())
            visitMethod(method)

        for(stmt in ctx.stmt())
            visitStmt(stmt)

        return null
    }
}