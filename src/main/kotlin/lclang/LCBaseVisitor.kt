package lclang

import lclang.exceptions.LCLangException
import lclang.exceptions.MethodNotFoundException
import lclang.exceptions.TypeErrorException
import lclang.exceptions.VariableNotFoundException
import lclang.lang.ArrayClass
import lclang.lang.CharClass
import lclang.lang.StringClass
import lclang.methods.LambdaMethod
import lclang.methods.Method
import lclang.types.CallableType
import lclang.types.Type
import lclang.types.Types
import kotlin.math.pow

open class LCBaseVisitor(
    parent: LCBaseVisitor? = null, importVars: Boolean = false): lclangBaseVisitor<Value?>() {
    lateinit var fileVisitor: LCFileVisitor
    val globals = HashMap<String, Value>()
    val variables = HashMap<String, Value>()

    init {
        if(parent!=null) {
            if(importVars) variables.putAll(parent.variables)
            globals.putAll(parent.globals)
            fileVisitor = parent.fileVisitor
        }
    }

    override fun visitVariable(ctx: lclangParser.VariableContext): Value {
        val variableName = ctx.ID().text
        val value = variables[variableName] ?: globals[variableName] ?:
            Value(Types.UNDEFINED, {
                throw VariableNotFoundException(variableName,
                    Caller(fileVisitor, ctx.start.line, ctx.stop.line)
                )
            })
        value.set = { _, newValue ->
            variables[variableName] = newValue
        }

        return value
    }

    override fun visitBlock(ctx: lclangParser.BlockContext): Value? {
        for(stmt in ctx.stmt()) {
            val value = visitStmt(stmt)
            if (value!=null){
                if (value.isReturn || value.stop)
                    return value
                else value.get(Caller(fileVisitor, stmt.start.line, stmt.stop.line))
            }
        }

        return null
    }

    override fun visitContainer(ctx: lclangParser.ContainerContext): Value? {
        for(stmt in ctx.stmt()) {
            val value = visitStmt(stmt)
            if(value!=null){
                if(value.isReturn||value.stop) {
                    value.isReturn = false
                    return value
                }else value.get(Caller(fileVisitor, stmt.start.line, stmt.stop.line))
            }
        }

        return Value(Types.VOID, null)
    }

    override fun visitValue(ctx: lclangParser.ValueContext): Value {
        return when {
            ctx.STRING()!=null -> StringClass(ctx.STRING().text.substring(1)
                .substringBeforeLast('"'), fileVisitor).asValue()

            ctx.CHAR()!=null -> CharClass(ctx.CHAR().text.substring(1)
                .substringBeforeLast('\'')[0], fileVisitor).asValue()

            ctx.DOUBLE()!=null -> Value(Types.DOUBLE, ctx.DOUBLE().text.toDouble())
            ctx.INTEGER()!=null -> Value(Types.INT, ctx.INTEGER().text.toInt())

            ctx.LONG()!=null -> Value(Types.LONG, ctx.LONG().text
                .substringBeforeLast('L').toLong())

            ctx.HEX_LONG()!=null -> Value(Types.LONG, ctx.HEX_LONG().text
                .substring(1).toLong(radix=16))

            ctx.BOOL()!=null -> Value(Types.BOOL, ctx.BOOL().text.startsWith('t'))
            ctx.NULL()!=null -> Value(Types.UNDEFINED, null)

            else -> throw LCLangException("Syntax error", "Invalid value "+ctx.text,
                Caller(fileVisitor, ctx.start.line, ctx.stop.line))
        }
    }

    override fun visitLambda(ctx: lclangParser.LambdaContext): Value = LambdaMethod(ctx)
    override fun visitWhileStmt(ctx: lclangParser.WhileStmtContext): Value? {
        val condition = ctx.condition
        val stmt = ctx.stmt()
        val caller = Caller(fileVisitor, condition.start.line, condition.stop.line)

        while(true) {
            if(visitExpression(condition).get(caller)==false) break
            if(stmt!=null){
                val value = visitStmt(ctx.stmt())

                if(value?.isReturn==true)
                    return value
                else if(value?.stop==true)
                    break
            }
        }

        return null
    }

    override fun visitIfStmt(ctx: lclangParser.IfStmtContext): Value? {
        val cond = visitExpression(ctx.condition)
        if(cond.get(Caller(fileVisitor, ctx.condition.start.line, ctx.condition.stop.line))==true){
            return visitStmt(ctx.ifT)
        }

        return if(ctx.ifF!=null)
            visitStmt(ctx.ifF) else null
    }

    override fun visitStmt(ctx: lclangParser.StmtContext): Value? {
        return ctx.children[0].accept(this)
    }

    override fun visitArray(ctx: lclangParser.ArrayContext): Value? {
        return Value(Types.ARRAY, {
            val array = ArrayClass(fileVisitor)
            for(expression in ctx.expression()){
                array.add(visitExpression(expression))
            }

            return@Value array
        }, { caller, it ->
            val array = it.get(caller)
            if(array !is ArrayClass)
                throw TypeErrorException("Value is not array", caller)

            for((i, expression) in ctx.expression().withIndex()){
                visitExpression(expression).set(caller, array[i])
            }
        })
    }

    override fun visitReturnExpr(ctx: lclangParser.ReturnExprContext): Value? {
        val expression = ctx.expression()
        var value = Value(Types.VOID, null)
        if(expression!=null)
            value = visitExpression(expression)

        value.isReturn = true
        return value
    }

    override fun visitStop(ctx: lclangParser.StopContext?): Value? {
        return Value(Types.VOID, { null }, stop = true)
    }

    override fun visitTypeGet(ctx: lclangParser.TypeGetContext?): Value? {
        return Value(Types.STRING, { visitExpression(ctx!!.expression()).type.text })
    }

    override fun visitExpression(ctx: lclangParser.ExpressionContext): Value {
        when {
            ctx.primitive()!=null -> {
                var value = visitPrimitive(ctx.primitive())
                for(access in ctx.access()){
                    val caller = Caller(fileVisitor, access.start.line, access.stop.line)
                    val classValue = value.get(caller)
                    if(classValue !is LCClass)
                        throw TypeErrorException("excepted class", caller)

                    val primitive = access.primitive()
                    if(primitive.call!=null){
                        val prevCall = primitive.call
                        primitive.call = null

                        value = classValue.visitPrimitive(primitive)
                        value = call(Caller(fileVisitor, prevCall.line, prevCall.line),
                            value, primitive.expression())

                        primitive.call = prevCall
                    }else value = classValue.visitPrimitive(primitive)
                }

                return value
            }


            else -> {
                val leftValue = visitExpression(ctx.expression(0))
                val leftType = leftValue.type
                val left = leftValue.get(Caller(fileVisitor, ctx.expression(0).start.line,
                    ctx.expression(0).stop.line))

                if(ctx.expression().size==1){
                    val value =  when {
                        ctx.not != null -> return Value(Types.BOOL, left==false)
                        ctx.unaryPlus != null -> when(left) {
                            !is Number -> throw TypeErrorException("Operation not supported", Caller(fileVisitor,
                                ctx.unaryPlus.line, ctx.unaryPlus.line))
                            Types.DOUBLE -> Value(Types.DOUBLE, left.toDouble()+1)
                            Types.LONG -> Value(Types.LONG, left.toLong()+1)
                            else -> Value(Types.INT, left.toInt()+1)
                        }

                        ctx.unaryMinus != null -> when(left) {
                            !is Number -> throw TypeErrorException("Operation not supported", Caller(fileVisitor,
                                ctx.unaryMinus.line, ctx.unaryMinus.line))
                            Types.DOUBLE -> Value(Types.DOUBLE, left.toDouble()-1)
                            Types.LONG -> Value(Types.LONG, left.toLong()-1)
                            else -> Value(Types.INT, left.toInt()-1)
                        }

                        else -> leftValue
                    }

                    leftValue.set(Caller(fileVisitor, ctx.start.line, ctx.stop.line), value)
                    return value
                }

                val rightValue = visitExpression(ctx.expression(1))
                val rightType = rightValue.type
                val right = rightValue.get(Caller(fileVisitor, ctx.expression(1).start.line,
                    ctx.expression(1).stop.line))

                if(left is Number&&right is Number) {
                    when {
                        ctx.div != null -> return Value(Types.DOUBLE, left.toDouble() / right.toDouble())
                        ctx.pow != null -> return Value(Types.DOUBLE, left.toDouble().pow(right.toDouble()))
                        ctx.less != null -> return Value(Types.BOOL, left.toDouble() < right.toDouble())
                        ctx.lessEquals != null -> return Value(Types.BOOL, left.toDouble() <= right.toDouble())
                        ctx.larger != null -> return Value(Types.BOOL, left.toDouble() > right.toDouble())
                        ctx.largerEquals != null -> return Value(Types.BOOL, left.toDouble() >= right.toDouble())

                        else -> {
                            val needType = when {
                                leftType== Types.DOUBLE||
                                        rightType== Types.DOUBLE -> Types.DOUBLE
                                leftType== Types.LONG||
                                        rightType== Types.LONG -> Types.LONG
                                else -> Types.INT
                            }

                            when {
                                ctx.multiplication != null -> return Value(needType, when(needType){
                                    Types.DOUBLE -> left.toDouble()*right.toDouble()
                                    Types.LONG -> left.toLong()*right.toLong()
                                    else -> left.toInt()*right.toInt()
                                })

                                ctx.add != null -> return Value(needType, when(needType){
                                    Types.DOUBLE -> left.toDouble()+right.toDouble()
                                    Types.LONG -> left.toLong()+right.toLong()
                                    else -> left.toInt()+right.toInt()
                                })

                                ctx.minus != null -> return Value(needType, when(needType){
                                    Types.DOUBLE -> left.toDouble()-right.toDouble()
                                    Types.LONG -> left.toLong()-right.toLong()
                                    else -> left.toInt()-right.toInt()
                                })
                            }
                        }
                    }
                }

                if(left is StringClass||right is StringClass){
                    when {
                        ctx.add!=null -> return Value(Types.STRING, left.toString()+right)
                        ctx.multiplication!=null&&
                                right is Int||left is Int ->
                            return Value(
                                Types.STRING, StringClass(if(right is Int)
                                left.toString().repeat(right)
                            else right.toString().repeat(left as Int), fileVisitor))
                    }
                }

                if(right is ArrayClass &&left is ArrayClass){
                    when {
                        ctx.add!=null -> return Value(Types.ARRAY, left+right)
                    }
                }

                if(right is Boolean&&left is Boolean){
                    when {
                        ctx.or!=null -> return Value(Types.BOOL,left||right)
                        ctx.and!=null -> return Value(Types.BOOL, left&&right)
                    }
                }

                return when {
                    ctx.nullableOr!=null -> if(!rightType.isAcceptWithoutNullable(leftType))
                        throw TypeErrorException("Unsupported operand types: $leftType " +
                                "${ctx.getChild(1)} $rightType", Caller(fileVisitor,
                                        ctx.start.line, ctx.stop.line))
                    else if(left==null) rightValue else leftValue

                    ctx.equals!=null -> Value(Types.BOOL, { left == right })
                    ctx.notEquals!=null -> Value(Types.BOOL, { left != right })
                    else -> throw TypeErrorException("Unsupported operand types: $leftType " +
                            "${ctx.getChild(1)} $rightType", Caller(fileVisitor,
                                        ctx.start.line, ctx.stop.line))
                }
            }
        }
    }

    override fun visitIfExpr(ctx: lclangParser.IfExprContext): Value {
        val cond = visitExpression(ctx.condition)
        if(cond.get(Caller(fileVisitor, ctx.start.line, ctx.stop.line))!=false){
            return visitExpression(ctx.ifT)
        }

        return visitExpression(ctx.ifF)
    }

    override fun visitNewClass(ctx: lclangParser.NewClassContext): Value {
        val clazz = fileVisitor.classes[ctx.className.text]
        if(clazz!=null)
            return clazz.constructor

        throw MethodNotFoundException("class ${ctx.className.text}",
            Caller(fileVisitor, ctx.start.line, ctx.start.line))
    }

    override fun visitParentnesesExpr(ctx: lclangParser.ParentnesesExprContext): Value {
        return visitExpression(ctx.expression())
    }

    private fun call(caller: Caller, value: Value, expressions: List<lclangParser.ExpressionContext>): Value {
        if(value.type !is CallableType)
            throw TypeErrorException("Value is not callable (it is ${value.type})", caller)

        val argsTypes = ArrayList<Type>()
        val args = ArrayList<Value>()
        for(argument in expressions) {
            val argumentValue = visitExpression(argument)
            argsTypes.add(argumentValue.type)

            args.add(argumentValue)
        }

        val method = value as Method
        if(method.args.size!=argsTypes.size){
            throw TypeErrorException(if(method.args.size>argsTypes.size)
                    "Invalid arguments: few arguments" else "Invalid arguments: too many arguments", caller)
        }

        val notAcceptArg = method.args.isAccept(argsTypes)
        if(notAcceptArg!=-1)
            throw TypeErrorException("Invalid argument $notAcceptArg: excepted ${method.args[notAcceptArg]}",
                Caller(fileVisitor, expressions[notAcceptArg].start.line, expressions[notAcceptArg].stop.line))

        return Value(method.returnType, method.call(caller, args))
    }

    override fun visitPrimitive(ctx: lclangParser.PrimitiveContext): Value {
        var value = ctx.children[0].accept(this) as Value
        for(access in ctx.arrayAccess()){
            val caller = Caller(fileVisitor, access.start.line, access.stop.line)
            val gettable = value.get(caller)
            val orValue = value

            value = if(gettable is ArrayClass) {
                if(access.expression()!=null) {
                    val getValue = visitExpression(access.expression())
                    if (getValue.type.isAccept(Types.INT))
                        gettable[getValue.get(caller) as Int]
                    else throw TypeErrorException("invalid index: excepted int", caller)
                }else Value(Types.ANY, { gettable.last().get(it) }, { c, it ->
                    gettable.add(it)
                    orValue.set(c, Value(Types.ARRAY, { gettable }, orValue.set))
                })
            }else if(gettable is Map<*, *>){
                if(access.expression()==null) throw TypeErrorException("invalid index: map not can add and set value",
                    caller)

                val getValue = visitExpression(access.expression())
                gettable[getValue.get(caller)] as Value
            }else throw TypeErrorException("excepted array or map", caller)
        }

        if(ctx.call!=null) value = call(Caller(fileVisitor, ctx.call.line, ctx.call.line), value, ctx.expression())

        val assignContext = ctx.operation()?.assign()
        if(assignContext!=null){
            val expression = visitExpression(assignContext.expression())
            value.set(Caller(fileVisitor, assignContext.start.line, assignContext.stop.line), expression)

            return expression
        }

        return value
    }
}