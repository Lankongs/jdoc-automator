package org.example;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. 設定要掃描的專案資料夾 (這裡設定為掃描自己專案的 src 資料夾)
        String targetDirectory = "src";
        System.out.println("[系統啟動] J-Doc Automator 開始執行...");
        System.out.println("正在掃描目錄: " + targetDirectory);

        // 2. 呼叫搜索兵：找出所有 .java 檔案
        List<File> javaFiles = FileCrawler.findJavaFiles(targetDirectory);
        System.out.println("共找到 " + javaFiles.size() + " 個 Java 檔案，準備進行語法解析。");
        System.out.println("--------------------------------------------------");

        // 3. 準備「字串收集箱」(StringBuilder)，先寫入 Markdown 的大標題
        StringBuilder mdContent = new StringBuilder();
        mdContent.append("# 專案 API 說明文件 (自動生成)\n\n");
        mdContent.append("> 本文件由 J-Doc Automator 自動掃描原始碼生成。\n\n");
        mdContent.append("---\n\n");

        // 4. 開始大迴圈：讓大腦 (JavaParser) 依序解析每個檔案
        for (File file : javaFiles) {
            try {
                // 讀取並解析檔案產生 AST
                CompilationUnit cu = StaticJavaParser.parse(file);

                // 找出檔案中的所有類別
                cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDecl -> {

                    // 【寫入類別名稱】 (Markdown H2)
                    String className = classDecl.getNameAsString();
                    mdContent.append("## Class: `").append(className).append("`\n\n");

                    // 嘗試抓取類別的 Javadoc 註解
                    classDecl.getJavadoc().ifPresent(javadoc -> {
                        mdContent.append("**描述:** ").append(javadoc.getDescription().toText()).append("\n\n");
                    });

                    mdContent.append("###  Methods (方法清單)\n\n");

                    // 找出該類別下的所有方法
                    classDecl.getMethods().forEach(method -> {
                        // 【寫入方法名稱】 (Markdown H4)
                        mdContent.append("#### `").append(method.getNameAsString()).append("`\n");

                        // 寫入存取權限與回傳值
                        mdContent.append("* **權限:** `").append(method.getAccessSpecifier().asString()).append("`\n");
                        mdContent.append("* **回傳:** `").append(method.getTypeAsString()).append("`\n");

                        // 寫入參數清單
                        mdContent.append("* **參數:** ");
                        if (method.getParameters().isEmpty()) {
                            mdContent.append("無\n");
                        } else {
                            for (Parameter param : method.getParameters()) {
                                mdContent.append("`").append(param.getTypeAsString()).append(" ").append(param.getNameAsString()).append("` ");
                            }
                            mdContent.append("\n");
                        }
                        mdContent.append("\n"); // 每個方法之間留一行空白
                    });

                    mdContent.append("---\n\n"); // 每個類別之間畫一條分隔線
                });

            } catch (Exception e) {
                System.err.println("解析失敗跳過: " + file.getName());
            }
        }

        // 5. 呼叫輸出機：將收集好的 Markdown 字串寫成實體檔案
        System.out.println("語法解析完成，正在產出 Markdown 文件...");
        MarkdownGenerator.generateFile("Project_API.md", mdContent.toString());

        System.out.println("[系統結束] 任務圓滿完成！");
    }
}