package com.generator.internal;

public class StringGen {
    public static String ls = System.lineSeparator();

    public static String assignment(String varName, String value) {
        return varName + " = " + value + ";";
    }
}
