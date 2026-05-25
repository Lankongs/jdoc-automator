package org.example;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Main {
    public static void main(String[] args) {
        // 1. 模擬一段準備被解析的 Java 程式碼字串
        String testCode = "public class StudentAssignment {\n" +
                "    public void submitHomework(String name) {\n" +
                "        System.out.println(\"作業已繳交\");\n" +
                "    }\n" +
                "}";

        try {
            // 2. 呼叫 JavaParser 開始解析字串
            CompilationUnit cu = StaticJavaParser.parse(testCode);

            // 3. 測試提取類別名稱與方法名稱
            cu.getClassByName("StudentAssignment").ifPresent(classDecl -> {
                System.out.println("====== [JavaParser 測試成功] ======");
                System.out.println("成功偵測到類別: " + classDecl.getNameAsString());

                classDecl.getMethods().forEach(method -> {
                    System.out.println("內含方法: " + method.getNameAsString());
                    System.out.println("方法回傳值: " + method.getTypeAsString());
                });
                System.out.println("=================================");
            });

        } catch (Exception e) {
            System.err.println("❌ 語法解析失敗，請確認 JavaParser 是否正確引入。");
            e.printStackTrace();
        }
    }
}