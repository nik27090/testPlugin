package generator;

import java.util.List;

/**
 *
 */
public class StringGen {
    public static String ls = System.lineSeparator();

    public static String packageStatement(String packageName) {
        return "package " + packageName + ";" + ls + ls;
    }

    public static String importStatement(String importClassStr) {
        return "import " + importClassStr + ";" + ls;
    }

    public static String setStatement(String returnType, String varName,
                                                     String value) {
        if (returnType.equals("void")) {
            return value + ";";
        }
        return returnType + " " + varName + " = " + value + ";";
    }

    public static String comment(String content) {
        return "// " + content;
    }

    // "varName = value;"
    public static String assignment(String varName,
                                          String value) {
        return varName + " = " + value + ";";
    }

    // "varName.methodName(arg1, arg2)"
    public static String invoke(String varName, String methodName, List<String> args) {
        return varName + "." + methodName + "(" + String.join(", ", args) + ")";
    }

    public static String genField(String modifier, String className, String fieldName) {
        return "\t" + modifier + " " + className + " " + fieldName + ";" + StringGen.ls;
    }
}
