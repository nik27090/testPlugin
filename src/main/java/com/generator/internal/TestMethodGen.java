package com.generator.internal;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;

public class TestMethodGen {
    private final Method method;

    public TestMethodGen(Method method) {
        this.method = method;
    }

    public String gen() {
        List<String> methodSrcCode = new LinkedList<>();
        methodSrcCode.add("@Test");
        methodSrcCode.add("public void " + method.getName() + "Test() throws Exception {");
        methodSrcCode.add("}");
        return String.join(StringGen.ls, Util.addTabs(methodSrcCode, 1)) + StringGen.ls;
    }
}
