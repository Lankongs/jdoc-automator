package org.example;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. 指定要掃描的專案資料夾路徑 (這裡我們先掃描當前專案的 src 資料夾)
        String targetDirectory = "src";

        System.out.println("🔍 開始掃描目錄: " + targetDirectory);

        // 2. 呼叫我們剛剛寫的 FileCrawler，拿到檔案清單
        List<File> filesToProcess = FileCrawler.findJavaFiles(targetDirectory);

        System.out.println("✅ 掃描完成，共找到 " + filesToProcess.size() + " 個 Java 檔案。");
        System.out.println("========================================\n");

        // 3. 遍歷清單，把每一個檔案交給 JavaParser 處理
        for (File file : filesToProcess) {
            try {
                System.out.println("📄 正在解析: " + file.getName());

                // 將實體檔案餵給 JavaParser
                CompilationUnit cu = StaticJavaParser.parse(file);

                // 嘗試抓出類別名稱
                cu.getPrimaryTypeName().ifPresent(className -> {
                    System.out.println("  -> 找到主類別: " + className);
                });

                System.out.println("---");

            } catch (Exception e) {
                System.err.println("❌ 解析失敗: " + file.getName());
                e.printStackTrace();
            }
        }

        System.out.println("\n🎉 所有檔案處理完畢！");
    }
}