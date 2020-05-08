package generator;

import createAllTests.AllTestsAction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 *
 */
public class Util {
    private static final Random rnd = new Random();

    public static String firstCharSmall(String str) {
        if (str.length() <= 0) {
            return "";
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static void createFile(Class c, String srcCode) {
        String baseDir = AllTestsAction.project.getBasePath();
        String middleDir = "src" + File.separator + "test" +
                File.separator + "java";
        String dir = c.getCanonicalName().replace(".", File.separator);
        String filePath = baseDir + File.separator + middleDir + File.separator + dir + "Test.java";
        File theFile = new File(filePath);
        File fullDirFile = theFile.getParentFile();
        if (!fullDirFile.exists()) {
            fullDirFile.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(filePath, "utf-8")) {
            writer.print(srcCode);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printFile(String srcCode) {
        System.out.println(srcCode);
    }

    public static List<String> addTabs(List<String> src, int count) {
        List<String> res = new LinkedList<String>();
        for (String line : src) {
            String tabs = "";
            for (int i = 0; i < count; i++) {
                tabs += "\t";
            }
            res.add(tabs + line);
        }
        return res;
    }

    public static int findType(TypeVariable[] typeVariables, TypeVariable findThis) {
        String typeName = findThis.getName();
        for (int i = 0; i < typeVariables.length; i++) {
            if (typeVariables[i].getName().equals(typeName)) {
                return i;
            }
        }
        return -1;
    }

    public static String getTypeName(Type t, TypeVariable[] typeVariables, Type[] typeValues) {
        String res = t.getTypeName();
        if (typeVariables != null && typeValues != null
            && typeVariables.length > 0 && typeValues.length == typeVariables.length) {
            for (int i = 0; i < typeVariables.length; i++) {
                String pattern = "\\b" + Pattern.quote(typeVariables[i].getName()) + "\\b";
                String value = typeValues[i].getTypeName();
                res = res.replaceAll(pattern, value);
            }
        }
        return res;
    }

    public static int rndRange(int min, int max) {
        return rnd.nextInt(max - min + 1) + min;
    }

    public static int getArrayDepth(Type t) {
        int count = 0;
        while ((t instanceof GenericArrayType)
            || (t instanceof Class<?> && ((Class<?>)t).isArray())) {
            count++;
            if (t instanceof GenericArrayType) {
                t = ((GenericArrayType)t).getGenericComponentType();
            } else {
                t = ((Class<?>)t).getComponentType();
            }
        }
        return count;
    }

    public static Type getArrayChildType(Type t) {
        while ((t instanceof GenericArrayType)
                || (t instanceof Class<?> && ((Class<?>)t).isArray())) {
            if (t instanceof GenericArrayType) {
                t = ((GenericArrayType)t).getGenericComponentType();
            } else {
                t = ((Class<?>)t).getComponentType();
            }
        }
        return t;
    }

    public static String repeat(String str, int count) {
        String res = "";
        for (int i = 0; i < count; i++) {
            res += str;
        }
        return res;
    }
}
