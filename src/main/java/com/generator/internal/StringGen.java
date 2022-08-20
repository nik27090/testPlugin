package com.generator.internal;

import java.util.List;

public class StringGen {
    public static String ls = System.lineSeparator();

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

    public static String assignment(String varName, String value) {
        return varName + " = " + value + ";";
    }

    public static String invoke(String varName, String methodName, List<String> args) {
        return varName + "." + methodName + "(" + String.join(", ", args) + ")";
    }
}
