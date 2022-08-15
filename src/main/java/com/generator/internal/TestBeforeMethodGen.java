package com.generator.internal;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class TestBeforeMethodGen {
    private Class<?> c;
    private String fieldName;

    public TestBeforeMethodGen(Class<?> c, String fieldName) {
        this.c = c;
        this.fieldName = fieldName;
    }

    private List<String> genBeforeMethodInternal()  {
        List<String> res = new LinkedList<String>();
        ValueGen g = new ValueGen(c, null, null);
        res.add(StringGen.assignment(fieldName, g.generate()));
        return Util.addTabs(res, 1);
    }

    public String gen() {
        List<String> methodSrcCode = new LinkedList<String>();
        methodSrcCode.add("@Before");
//        methodSrcCode.add("@SuppressWarnings({\"rawtypes\", \"unchecked\"})");
        methodSrcCode.add("public void beforeEach() throws Exception {");
        methodSrcCode.addAll(genBeforeMethodInternal());
        methodSrcCode.add("}");
        return String.join(StringGen.ls, Util.addTabs(methodSrcCode, 1)) + StringGen.ls;
    }
}
