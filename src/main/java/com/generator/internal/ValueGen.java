package com.generator.internal;

import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class ValueGen {
    private final static Random rnd = new Random();

    private final Type type;
    private final TypeVariable<Method>[] methodTypeParameters;
    private final Type[] methodTypeValues;

    public ValueGen(Type type, TypeVariable<Method>[] methodTypeParameters,
                    Type[] methodTypeValues) {
        this.type = type;
        this.methodTypeParameters = methodTypeParameters;
        this.methodTypeValues = methodTypeValues;
    }

    private String genPrimitive(Class<?> c) {
        String dataType = getTypeName(c);
        String res = null;
        switch (dataType) {
            case "boolean":
            case "java.lang.Boolean":
                res = rnd.nextInt(2) == 1 ? "true" : "false";
                break;
            case "byte":
            case "java.lang.Byte":
                res = String.valueOf((byte) rnd.nextInt());
                break;
            case "char":
            case "java.lang.Character":
                char rndChar = (char) (rnd.nextInt(127 - 32) + 32);
                res = "'" + rndChar + "'";
                break;
            case "double":
            case "java.lang.Double":
                res = String.valueOf(rnd.nextDouble());
                break;
            case "float":
            case "java.lang.Float":
                res = String.valueOf((float) rnd.nextDouble());
                break;
            case "int":
            case "java.lang.Integer":
                res = String.valueOf(rnd.nextInt());
                break;
            case "long":
            case "java.lang.Long":
                res = rnd.nextLong() + "L";
                break;
            case "short":
            case "java.lang.Short":
                res = String.valueOf((short) rnd.nextInt());
                break;
        }
        return res;
    }

    private String genString() {
        int len = Util.rndRange(1, 4);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char rndChar = (char)(rnd.nextInt(123-97) + 97);
            str.append(rndChar);
        }
        return "\"" + str + "\"";
    }

    private String genArrayInitializer(Type type) {
        int elementsCount = Util.rndRange(1, 3);
        Type subtype;
        if (type instanceof GenericArrayType) {
            // e.g. type is "List<String>[][]" or "List<String>[]"
            GenericArrayType gat = (GenericArrayType) type;
            // e.g. subtype is "List<String>[]" or "List<String>" respectively
            subtype = gat.getGenericComponentType();
        } else {
            // e.g. type is "String[][]" or "int[]"
            Class<?> c = (Class<?>) type;
            // e.g. subtype is "String[]" or "int"
            subtype = c.getComponentType();
        }
        List<String> values = new LinkedList<String>();
        for (int i = 0; i < elementsCount; i++) {
            values.add(genValue(subtype));
        }
        String result = String.join(", ", values);
        return "{ " + result + " }";
    }

    private String resolveListType(ParameterizedType pt) {
        String listType = null;
        Type baseType = pt.getRawType();
        if (baseType != null) {
            String baseTypeName = getTypeName(baseType);
            if (baseTypeName.equals("java.util.List")
                    || baseTypeName.equals("java.util.LinkedList")
                    || baseTypeName.equals("java.util.ArrayList")) {
                if (baseTypeName.equals("java.util.LinkedList")) {
                    listType = "java.util.LinkedList";

                } else {
                    listType = "java.util.ArrayList";
                }
            }
        }
        return listType;
    }

    private boolean isType(ParameterizedType pt) {
        Type baseType = pt.getRawType();
        if (baseType == null) {
            return false;
        }
        String baseTypeName = getTypeName(baseType);
        return baseTypeName.equals("java.lang.reflect.Type")
                || baseTypeName.equals("java.lang.Class");
    }

    private String genValueForType(ParameterizedType pt) {
        Type baseType = pt.getRawType();
        if (baseType == null) {
            return "?";
        }
        String baseTypeName = getTypeName(baseType);
        String className = "?";
        if (baseTypeName.equals("java.lang.reflect.Type")) {
            className = "java.lang.Object";
        } else if (baseTypeName.equals("java.lang.Class")) {
            className = getTypeName(pt.getActualTypeArguments()[0]);
        }
        return "(" + getTypeName(pt) + ") java.lang.Class.forName(\"" + className + "\")";
    }

    @SneakyThrows
    private String genArrayValue(Type t) {
        if (t instanceof GenericArrayType) {
            int arrayDepth = Util.getArrayDepth(t);
            Type childType = Util.getArrayChildType(t);
            Type rawType = childType;
            if (childType instanceof ParameterizedType) {
                rawType = ((ParameterizedType)childType).getRawType();
            }
            String castingPart = "(" + getTypeName(t) + ") ";
            String mainPart = "new " + getTypeName(rawType) + Util.repeat("[]", arrayDepth);
            return castingPart + mainPart + genArrayInitializer(t);
        }
        return "new " + getTypeName(t) + genArrayInitializer(t);
    }

    private Constructor<?> getPublicConstructor(Class<?> c) {
        Constructor<?>[] constructorArray = c.getConstructors();
        for (Constructor<?> constructor : constructorArray) {
            int modifiers = constructor.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                return constructor;
            }
        }
        return null;
    }

    private String genClassValue(Class<?> c) {
        return genClassValue(c, false);
    }

    @SneakyThrows
    private String genClassValue(Class<?> c, boolean onlyArgs) {
        if (c.isInterface()) {
            return "null /*interface cannot be instantiated*/";
        }
        if (Modifier.isAbstract(c.getModifiers())) {
            return "null /*abstract classes cannot be instantiated*/";
        }
        List<String> arguments = new LinkedList<>();
        Constructor<?> ctor = getPublicConstructor(c);
        if (ctor == null) {
            return "null /*no public constructor available*/";
        }
        TypeVariable[] typeParameters = ctor.getTypeParameters();
        Type[] typeValues = TypeChooser.chooseTypeParameterValues(typeParameters);
        int parCnt = ctor.getParameterCount();
        for (int i = 0; i < parCnt; i++) {
            Type parType = ctor.getGenericParameterTypes()[i];
            ValueGen gen = new ValueGen(parType, typeParameters, typeValues);
            String value = gen.generate();
            arguments.add(value);
        }
        if (onlyArgs) {
            return String.join(", ", arguments);
        }
        return "new " + getTypeName(c) + "(" + String.join(", ", arguments) + ")";
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.getTypeName().equals("java.lang.Short") ||
                clazz.getTypeName().equals("java.lang.Boolean") ||
                clazz.getTypeName().equals("java.lang.Byte") ||
                clazz.getTypeName().equals("java.lang.Character") ||
                clazz.getTypeName().equals("java.lang.Double") ||
                clazz.getTypeName().equals("java.lang.Float") ||
                clazz.getTypeName().equals("java.lang.Integer") ||
                clazz.getTypeName().equals("java.lang.Long");
    }

    private String genValue(Type t) {
        if (t instanceof TypeVariable<?>) {
            // e.g. T for: "<T> void foo(List<T> some) {}"
            TypeVariable<?> tv = (TypeVariable<?>) t;
            int typeIndex = Util.findType(methodTypeParameters, tv);
            return genValue(methodTypeValues[typeIndex]);
        } else if (t instanceof ParameterizedType) {
            // e.g. List<Integer>, Foo<Object>"
            ParameterizedType pt = (ParameterizedType) t;
            String listType = resolveListType(pt);
            if (listType != null) {
                Type childType = pt.getActualTypeArguments()[0];
                int elemCnt = Util.rndRange(1, 3);
                List<String> valueList = new LinkedList<>();
                for (int i = 0; i < elemCnt; i++) {
                    valueList.add("add(" + genValue(childType) + ");");
                }
                String values = String.join("", valueList);
                String listTypeSpecial = listType + "<" + getTypeName(childType) + ">";
                return "new " + listTypeSpecial + "(){{" + values + "}}";
            }
            if (isType(pt)) {
                return genValueForType(pt);
            }
            Type theRawType = pt.getRawType();
            if (theRawType instanceof Class<?>) {
                Class<?> cc = (Class<?>) theRawType;
                Constructor<?> pc = getPublicConstructor(cc);
                if (pc == null || cc.isInterface() || Modifier.isAbstract(cc.getModifiers())) {
                    return "null /*abstract generic class/interface cannot be instantiated*/";
                }
                return "new " + getTypeName(pt) + "(" + genClassValue(cc, true) + ")";
            }
            return "null /*failed to instantiate complex generic class or interface*/";
        } else if (t instanceof GenericArrayType) {
            return genArrayValue(t);
        } else if (t instanceof Class<?>) {
            Class<?> c = (Class<?>) t;
            if (isPrimitive(c)) {
                if (getTypeName(c).equals("void")) {
                    throw new RuntimeException("Cannot generate value for void-type");
                }
                return genPrimitive(c);
            }
            if (getTypeName(c).equals("java.lang.String")) {
                return genString();
            }
            if (c.isArray()) {
                // It's important to use "t" here, because array can be "List<Integer>[]",
                // which is GenericArrayType
                return genArrayValue(t);
            }
            // Usual class
            return genClassValue(c);
        } else {
            return "null /*unknown instance type = " + getTypeName(t.getClass()) + "*/";
        }
    }

    public String generate() {
        return genValue(type);
    }

    private String getTypeName(Type t) {
        return Util.getTypeName(t, methodTypeParameters, methodTypeValues);
    }
}
