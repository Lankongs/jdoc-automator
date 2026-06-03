package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MarkdownGenerator {

    /**
     * 將字串內容寫入指定的 Markdown 檔案
     * @param fileName 想要儲存的檔案名稱 (例如: "README.md")
     * @param content  要寫入的 Markdown 格式字串
     */
    public static void generateFile(String fileName, String content) {
        // 1. 指定輸出目錄 (這裡我們放在專案根目錄下的一個 docs 資料夾)
        File outputDir = new File("docs");

        // 如果 docs 資料夾不存在，就自動建立它
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        // 2. 組合完整的檔案路徑 (例如: "docs/README.md")
        File outputFile = new File(outputDir, fileName);

        // 3. 使用 try-with-resources 語法，確保寫完後自動關閉檔案資源
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            // 將內容寫入檔案
            writer.write(content);

            System.out.println(" Markdown 文件已成功生成: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println(" 寫入檔案失敗: " + fileName);
            e.printStackTrace();
        }
    }
}