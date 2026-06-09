package org.example;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class JDocGUI extends JFrame {

    private JTextField pathField;
    private JTextArea consoleArea;
    private JButton selectBtn;
    private JButton generateBtn;
    private File selectedDirectory = null;

    public JDocGUI() {
        // 1. 設定視窗基本屬性
        setTitle("J-Doc Automator 自動化文件產生器");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 視窗居中
        setLayout(new BorderLayout(10, 10));

        // 2. 上方控制區
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pathField = new JTextField("請先選擇要掃描的 Java 專案資料夾...");
        pathField.setEditable(false);
        selectBtn = new JButton(" 選擇資料夾");

        topPanel.add(pathField, BorderLayout.CENTER);
        topPanel.add(selectBtn, BorderLayout.EAST);

        // 3. 中間按鈕區
        JPanel midPanel = new JPanel();
        generateBtn = new JButton("開始生成文件");
        generateBtn.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        generateBtn.setEnabled(false); // 初始反灰
        midPanel.add(generateBtn);

        // 4. 下方日誌區 (小黑窗)
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setBackground(new Color(30, 30, 30)); // 暗色背景
        consoleArea.setForeground(new Color(50, 250, 50)); // 駭客綠字體
        consoleArea.setFont(new Font("微軟正黑體", Font.PLAIN, 13));
        consoleArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "執行日誌 (Console Log)"));

        // 5. 組合畫面
        add(topPanel, BorderLayout.NORTH);
        add(midPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // ==========================================
        // 事件監聽與核心邏輯整合
        // ==========================================

        // 按鈕：選擇資料夾
        selectBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = fileChooser.showOpenDialog(JDocGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedDirectory = fileChooser.getSelectedFile();
                pathField.setText(selectedDirectory.getAbsolutePath());
                generateBtn.setEnabled(true); // 啟用生成功能
                clearLog();
                appendLog("[系統提示] 已成功選取目標專案。");
            }
        });

        // 按鈕：開始生成文件 (串聯所有模組)
        generateBtn.addActionListener(e -> {
            // 使用新執行緒 (Thread) 處理 I/O 與解析，防止 GUI 視窗凍結
            new Thread(() -> {
                try {
                    // UI 狀態控制
                    generateBtn.setEnabled(false);
                    selectBtn.setEnabled(false);
                    clearLog();

                    appendLog(" [系統啟動] J-Doc Automator 開始執行...");
                    appendLog(" 正在掃描目錄: " + selectedDirectory.getName());

                    // 呼叫模組1：檔案遍歷
                    List<File> javaFiles = FileCrawler.findJavaFiles(selectedDirectory.getAbsolutePath());
                    appendLog(" 掃描完成，共找到 " + javaFiles.size() + " 個 Java 檔案。");
                    appendLog("--------------------------------------------------");

                    if (javaFiles.isEmpty()) {
                        appendLog(" 未偵測到任何 .java 檔案，任務終止。");
                        resetForm();
                        return;
                    }

                    // 建立 Markdown 收集箱
                    StringBuilder mdContent = new StringBuilder();
                    mdContent.append("# 專案 API 說明文件 (自動生成)\n\n");
                    mdContent.append("> 本文件由 J-Doc Automator 自動掃描原始碼生成。\n\n");
                    mdContent.append("---\n\n");

                    // 呼叫模組2：JavaParser 批次語法解析
                    for (File file : javaFiles) {
                        appendLog(" 正在解析: " + file.getName());

                        CompilationUnit cu = StaticJavaParser.parse(file);

                        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDecl -> {
                            String className = classDecl.getNameAsString();
                            mdContent.append("##  Class: `").append(className).append("`\n\n");

                            classDecl.getJavadoc().ifPresent(javadoc -> {
                                mdContent.append("**描述:** ").append(javadoc.getDescription().toText()).append("\n\n");
                            });

                            mdContent.append("###  Methods (方法清單)\n\n");

                            classDecl.getMethods().forEach(method -> {
                                mdContent.append("#### `").append(method.getNameAsString()).append("`\n");

                                // 【新增】在這裡呼叫 AI！
                                appendLog("  正在呼叫 AI 分析方法: " + method.getNameAsString() + "...");

                                // method.toString() 會把這個方法的「完整原始碼」抓出來餵給 AI
                                String aiExplanation = LLMClient.askAI(method.toString());

                                // 把 AI 的解釋寫入 Markdown
                                mdContent.append("> ** AI 解析:** ").append(aiExplanation).append("\n\n");

                                mdContent.append("* **權限:** `").append(method.getAccessSpecifier().asString()).append("`\n");
                                mdContent.append("* **回傳:** `").append(method.getTypeAsString()).append("`\n");

                                // 【極度重要】防止免費 API 額度被鎖的保護機制
                                try {
                                    // 讓程式暫停 3 秒。因為免費版 Gemini 一分鐘只能呼叫 15 次。
                                    // 如果沒有這個，你的程式 1 秒內丟 50 個方法過去，API 會直接封鎖你。
                                    Thread.sleep(3000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                mdContent.append("* **參數:** ");

                                if (method.getParameters().isEmpty()) {
                                    mdContent.append("無\n");
                                } else {
                                    for (Parameter param : method.getParameters()) {
                                        mdContent.append("`").append(param.getTypeAsString()).append(" ").append(param.getNameAsString()).append("` ");
                                    }
                                    mdContent.append("\n");
                                }
                                mdContent.append("\n");
                            });
                            mdContent.append("---\n\n");
                        });
                    }

                    // 呼叫模組3：Markdown 文件產出
                    appendLog(" 語法解析完成，正在寫入本地磁碟...");
                    MarkdownGenerator.generateFile("Project_API.md", mdContent.toString());

                    appendLog("\n [系統結束] 說明文件已成功產出至 docs/Project_API.md！");

                } catch (Exception ex) {
                    appendLog(" 發生非預期錯誤: " + ex.getMessage());
                } finally {
                    resetForm();
                }
            }).start();
        });
    }

    private void appendLog(String message) {
        // 使用 SwingUtilities 確保多執行緒下的 UI 渲染安全
        SwingUtilities.invokeLater(() -> {
            consoleArea.append(message + "\n");
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        });
    }

    private void clearLog() {
        consoleArea.setText("");
    }

    private void resetForm() {
        SwingUtilities.invokeLater(() -> {
            generateBtn.setEnabled(true);
            selectBtn.setEnabled(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JDocGUI().setVisible(true));
    }
}