package generator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class TestMethodGen {
    private Method method;
    private String fieldName;

    private TypeVariable<Method>[] typeParameters;
    private Type[] typeValues;

    public TestMethodGen(Method method, String fieldName) {
        this.method = method;
        this.fieldName = fieldName;
        typeParameters = method.getTypeParameters();
        typeValues = TypeChooser.chooseTypeParameterValues(typeParameters);
    }

    // type is void:
    //   "value;"
    // else:
    //   "Type varName = value;"
    private String genSetStatement(Type type, String varName,
                                   String value) {
        String typeName = Util.getTypeName(type, typeParameters, typeValues);
        return StringGen.setStatement(typeName, varName, value);
    }

    private List<String> genMethodInternal() {
        Type retType = method.getGenericReturnType();
        List<String> res = new LinkedList<String>();

        res.add(StringGen.comment("Setup"));
        Parameter[] parameters = method.getParameters();
        List<String> arguments = new LinkedList<String>();
        for (int i = 0; i < parameters.length; i++) {
            Type parType = method.getGenericParameterTypes()[i];
            String argName = parameters[i].getName();
            ValueGen g = new ValueGen(parType, typeParameters, typeValues);
            res.add(genSetStatement(parType, argName, g.generate()));
            arguments.add(argName);
        }

        res.add("");

        res.add(StringGen.comment("Run the test"));
        String value = StringGen.invoke(fieldName, method.getName(), arguments);
        res.add(genSetStatement(retType, "res", value));
        return Util.addTabs(res, 1);
    }

    public String gen(int i) {
        List<String> methodSrcCode = new LinkedList<String>();
        methodSrcCode.add("@Test");
        methodSrcCode.add("@SuppressWarnings({\"rawtypes\", \"unchecked\"})");
        methodSrcCode.add("public void " + method.getName() + i + "Test() throws Exception {");
        methodSrcCode.addAll(genMethodInternal());
        methodSrcCode.add("}");
        return String.join(StringGen.ls, Util.addTabs(methodSrcCode, 1)) + StringGen.ls;
    }
}
