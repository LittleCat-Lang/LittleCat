package lclang.libs.lang.classes;

import lclang.LCClass;
import lclang.libs.lang.classes.numbers.IntClass;
import lclang.types.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringClass extends LibraryClass {
    public static final String name = "string";
    public static final Types.MagicType type = new Types.MagicType(name);
    public static final Map<String, StringClass> cache = new HashMap<>();
    public static final StringClass instance = new StringClass();
    public String string;

    private StringClass() {
        super(name);
        constructor = method((caller, args) -> get(args.get(0).toString(caller)),
                Types.ANY, type);
    }

    private StringClass(String string) {
        this();
        this.string = string;
        globals.put("toString", method((caller, lcClasses) -> this, type).asValue());
        globals.put("length", IntClass.get(string.length()).asValue());
        globals.put("charAt", method((caller, args) -> CharClass.get(string.charAt(((IntClass) args.get(0)).value)),
            IntClass.type, CharClass.type).asValue());
        globals.put("toArray", method((caller, lcClasses) -> {
            List<LCClass> classes = new ArrayList<>();

            char[] chars = string.toCharArray();
            for (char character: chars)
                classes.add(CharClass.get(character));

            return new ArrayClass(classes);
        }, ArrayClass.type).asValue());

        globals.put("lower", method((caller, args) -> StringClass.get(string.toLowerCase()), type).asValue());
        globals.put("upper", method((caller, args) -> StringClass.get(string.toUpperCase()), type).asValue());

        globals.put("split", method((caller, args) -> {
            List<LCClass> classes = new ArrayList<>();
            String[] parts = string.split(((StringClass) args.get(0)).string);
            for (String part: parts)
                classes.add(StringClass.get(part));

            return new ArrayClass(classes);
        }, type, type).asValue());

        globals.put("substring", method((caller, args) ->
                        StringClass.get(
                            string.substring(
                                ((IntClass) args.get(0)).value,
                                ((IntClass) args.get(1)).value
                            )
                        ),
                IntClass.type, IntClass.type, type).asValue());

        globals.put("find", method((caller, args) ->
                        IntClass.get(string.indexOf(((CharClass) args.get(0)).value)),
                CharClass.type, type).asValue());

        globals.put("findLast", method((caller, args) ->
                        IntClass.get(string.lastIndexOf(((CharClass) args.get(0)).value)),
                CharClass.type, type).asValue());
    }

    public static StringClass get(String string) {
        if(cache.containsKey(string))
            return cache.get(string);

        StringClass clazz = new StringClass(string);
        cache.put(string, clazz);
        return clazz;
    }
}
