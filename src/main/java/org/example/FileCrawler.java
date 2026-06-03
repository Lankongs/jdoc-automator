package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileCrawler {

    /**
     * 給定一個資料夾路徑，回傳該資料夾（含子資料夾）下所有的 .java 檔案
     * @param directoryPath 想要掃描的根目錄路徑
     * @return 包含所有 .java 檔案的 List
     */
    public static List<File> findJavaFiles(String directoryPath) {
        List<File> javaFiles = new ArrayList<>();
        File rootDir = new File(directoryPath);

        // 基本防呆：確認這是一個存在的資料夾
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            System.err.println(" 錯誤：找不到指定的資料夾，或它不是一個目錄！路徑：" + directoryPath);
            return javaFiles;
        }

        // 呼叫遞迴方法開始搜尋
        scanDirectory(rootDir, javaFiles);
        return javaFiles;
    }

    /**
     * 遞迴掃描資料夾的內部邏輯
     */
    private static void scanDirectory(File currentDir, List<File> resultList) {
        // 列出當前資料夾下的所有檔案與子資料夾
        File[] files = currentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果遇到子資料夾，就「自己呼叫自己」(遞迴) 繼續往下找
                    scanDirectory(file, resultList);
                } else if (file.getName().endsWith(".java")) {
                    // 如果是檔案，且副檔名是 .java，就加入清單中
                    resultList.add(file);
                }
            }
        }
    }
}
