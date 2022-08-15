package com.generator.internal;

import java.util.LinkedList;
import java.util.List;

public class TestBeforeMethodGen {
    private final Class<?> clazz;
    private final String fieldName;

    public TestBeforeMethodGen(Class<?> clazz, String fieldName) {
        this.clazz = clazz;
        this.fieldName = fieldName;
    }

    private List<String> genBeforeMethodInternal()  {
        List<String> res = new LinkedList<>();
        ValueGen g = new ValueGen(clazz, null, null);
        res.add(StringGen.assignment(fieldName, g.generate()));
        return Util.addTabs(res, 1);
    }

    public String gen() {
        List<String> methodSrcCode = new LinkedList<>();
        methodSrcCode.add("@Before");
        methodSrcCode.add("public void beforeEach() throws Exception {");
        methodSrcCode.addAll(genBeforeMethodInternal());
        methodSrcCode.add("}");
        return String.join(StringGen.ls, Util.addTabs(methodSrcCode, 1)) + StringGen.ls;
    }
}
