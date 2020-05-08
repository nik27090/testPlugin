package generator;

import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.List;

public class Generator {
    private String genForClass(Class c) {
        String className = c.getSimpleName();
        if (className.length() <= 0) {
            return "";
        }
        String fieldName = Util.firstCharSmall(c.getSimpleName());
        String packageName = c.getCanonicalName().substring(0, c.getCanonicalName().lastIndexOf("."));
        String srcCode = StringGen.packageStatement(packageName);
        srcCode += StringGen.importStatement("org.junit.jupiter.api.Test");
        srcCode += StringGen.importStatement("org.junit.jupiter.api.BeforeEach");
        srcCode += StringGen.ls;
        srcCode += "public class " + className + "Test {" + StringGen.ls;
        srcCode += StringGen.genField("private", className, fieldName) + StringGen.ls;
        List<String> methodSrcCodeList = new LinkedList<String>();
        TestBeforeMethodGen beforeGen = new TestBeforeMethodGen(c, fieldName);
        srcCode += beforeGen.gen();
        srcCode += StringGen.ls;
        TestMethodGen methodGen;
        for (Method m : c.getDeclaredMethods()) {
            if (!Modifier.isPublic(m.getModifiers())) {
                continue;
            }
            methodGen = new TestMethodGen(m, fieldName);
            String methodSrcCode = methodGen.gen();
            methodSrcCodeList.add(methodSrcCode);
        }
        srcCode += String.join(StringGen.ls, methodSrcCodeList);
        srcCode += "}" + StringGen.ls;
        return srcCode;
    }

    public void run(List<Class> classList){
        List<String> srcCodeList = new LinkedList<String>();
        for (Class c : classList) {
            if (c.getSimpleName().length() <= 0) {
                continue;
            }
            String srcCode = genForClass(c);
            Util.createFile(c, srcCode);
            Util.printFile(srcCode);
            System.out.println(StringGen.ls);
            srcCodeList.add(srcCode);
        }
    }
}
