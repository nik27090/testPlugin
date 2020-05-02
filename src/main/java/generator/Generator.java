package generator;

import createAllTests.AllTestsAction;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Generator {

    final static Random rnd = new Random();

    private static String firstCharSmall(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }



    private static String getValueByType(Class<?> typ, String parName) {
        String ls = System.lineSeparator();
        String res = "\t\tfinal " + typ.getCanonicalName() + " " +
                parName + " = ";
        String typName = typ.getSimpleName();

        if (typName.equals("char")) {
            char rndChar = (char)(rnd.nextInt(127 - 32) + 32);
            res += "'" + rndChar + "'";
        } else if (typName.equals("int")) {
            res += String.valueOf(rnd.nextInt());
        } else if (typName.equals("float")) {
            res += String.valueOf((float)rnd.nextDouble());
        } else if (typName.equals("double")) {
            res += String.valueOf(rnd.nextDouble());
        } else if (typName.equals("boolean")) {
            res += rnd.nextInt(2) == 1 ? "true" : "false";
        } else if (typName.equals("String")) {
            int len = rnd.nextInt(10) + 1;
            String str = "";
            for (int i = 0; i < len; i++) {
                char rndChar = (char)(rnd.nextInt(123-97) + 97);
                str += rndChar;
            }
            res += "\"" + str + "\"";
        } else if (typ.isArray()) {
            res += "new " + typ.getComponentType().getCanonicalName() + "[" + rnd.nextInt(50) + "];" + ls;
            res += "\t\tfor (int i = 0; i < " + parName + ".length; i++) {" + ls;
            res += "\t\t\t" + parName + "[i] = new " + typ.getComponentType().getCanonicalName() + "();" + ls;
            res += "\t\t}" + ls;
            return res;
        } else {
            List<String> arguments = new LinkedList<String>();
            if (typ.getConstructors().length != 0) {
                Constructor<?> ctor = typ.getConstructors()[0];
                int parCnt = ctor.getParameterCount();
                for (int i = 0; i < parCnt; i++) {
                    Class<?> parType = ctor.getParameterTypes()[i];
                    String ctorParName = parName + "_" + i;
                    res = getValueByType(parType, ctorParName) + res;
                    arguments.add(ctorParName);
                }
            }
            res += "new " + typ.getCanonicalName() + "(" + String.join(", ", arguments) + ")";
        }
        return res + ";" + ls;
    }

    public void run(List<Class> listClass){
        List<String> classes = new LinkedList<String>();
        String ls = System.lineSeparator();
        for (Class cl : listClass) {
            String classDesc = "public class " + cl.getSimpleName() + "Test {" + ls;

            classDesc = classDesc + "\tprivate " + cl.getSimpleName() +" " + firstCharSmall(cl.getSimpleName()) + ";" + ls;
            List<String> methods = new LinkedList<String>();
            for (Method m : cl.getDeclaredMethods()) {
                if (!Modifier.isPublic(m.getModifiers())) {
                    continue;
                }
                String methodDesc = "\t@Test" + ls;
                methodDesc += "\tpublic void " + m.getName() + "Test() throws Exception {" + ls;
                methodDesc += "\t\t// Setup" + ls;
                int parCnt = m.getParameterCount();
                List<String> arguments = new LinkedList<String>();
                for (int i = 0; i < parCnt; i++) {
                    Class<?> parType = m.getParameterTypes()[i];
                    String parName = "arg" + i;
                    methodDesc += getValueByType(parType, parName);
                    arguments.add(parName);
                }
                methodDesc += "\t\t" + ls;
                methodDesc += "\t\t// Run the test" + ls;
                String resPart = "";
                if (!m.getReturnType().getName().equals("void")) {
                    String typeMethod = m.getReturnType().getName();
                    if (typeMethod.endsWith(";")){
                        typeMethod = typeMethod.substring(0, typeMethod.length() - 1);
                    }
                    int countBracket = StringUtils.countMatches(typeMethod, "[");
                    if (countBracket != 0){
                       typeMethod = typeMethod.replace("[", "");
                       char first = typeMethod.charAt(0);
                       typeMethod = typeMethod.substring(1);
                       if (typeMethod.equals("")) {
                           switch (first) {
                               case 'Z': typeMethod = "boolean"; break;
                               case 'B': typeMethod = "byte"; break;
                               case 'C': typeMethod = "char"; break;
                               case 'D': typeMethod = "double"; break;
                               case 'F': typeMethod = "float"; break;
                               case 'I': typeMethod = "int"; break;
                               case 'J': typeMethod = "long"; break;
                               case 'S': typeMethod = "short"; break;
                               default: System.out.println("This symbol is not supported: " + first);
                           }
                       }
                       typeMethod = typeMethod + StringUtils.repeat("[]", countBracket);
                    }
                    resPart = typeMethod + " res = ";
                }
                methodDesc += "\t\t" + resPart +
                        firstCharSmall(cl.getSimpleName()) + "." +
                        m.getName() + "(" + String.join(", ", arguments) + ");" + ls;
                methodDesc += "\t}" + ls;
                methods.add(methodDesc);
            }
            classDesc += String.join(ls, methods);
            classDesc += "}" + ls;
            classDesc = "import org.junit.jupiter.api.Test;" + ls + ls + classDesc;
            classDesc = "package " + cl.getCanonicalName().substring(0,cl.getCanonicalName().lastIndexOf("."))
                    + ";" + ls + classDesc;
            classes.add(classDesc);

            String baseDir = AllTestsAction.project.getBasePath();
            String middleDir = "src" + File.separator + "test" +
                    File.separator + "java";
            String dir = cl.getCanonicalName().replace(".", File.separator);
            String filePath = baseDir + File.separator + middleDir + File.separator + dir + "Test.java";
            File theFile = new File(filePath);
            File fullDirFile = theFile.getParentFile();
            if (!fullDirFile.exists()) {
                fullDirFile.mkdirs();
            }
            try (PrintWriter writer = new PrintWriter(filePath, "utf-8")) {
                writer.print(classDesc);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(String.join(ls, classes));
    }

}
