package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class JDocGUI extends JFrame {

    private JTextField pathField;
    private JTextArea consoleArea;
    private JButton selectBtn;
    private JButton generateBtn;
    private File selectedDirectory = null;

    public JDocGUI() {
        // 1. 設定視窗基本屬性
        setTitle("J-Doc Automator 自動化文件產生器");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 讓視窗顯示在螢幕正中央
        setLayout(new BorderLayout(10, 10));

        // 2. 建立上方控制區 (選擇資料夾)
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pathField = new JTextField("請先選擇要掃描的 Java 專案資料夾...");
        pathField.setEditable(false); // 不讓使用者手動亂打字

        selectBtn = new JButton("選擇資料夾");

        topPanel.add(pathField, BorderLayout.CENTER);
        topPanel.add(selectBtn, BorderLayout.EAST);

        // 3. 建立中間按鈕區 (開始生成)
        JPanel midPanel = new JPanel();
        generateBtn = new JButton("開始生成文件");
        generateBtn.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        generateBtn.setEnabled(false); // 還沒選資料夾前，按鈕反灰不給按
        midPanel.add(generateBtn);

        // 4. 建立下方日誌區 (Console 輸出)
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setBackground(Color.BLACK);
        consoleArea.setForeground(Color.GREEN);
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("執行日誌"));

        // 5. 將所有區塊加入視窗
        add(topPanel, BorderLayout.NORTH);
        add(midPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // ==========================================
        // 綁定按鈕的點擊事件 (Event Listeners)
        // ==========================================

        // 當點擊「選擇資料夾」時
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 限定只能選資料夾

                int result = fileChooser.showOpenDialog(JDocGUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedDirectory = fileChooser.getSelectedFile();
                    pathField.setText(selectedDirectory.getAbsolutePath());
                    generateBtn.setEnabled(true); // 解鎖生成按鈕
                    appendLog("已選取資料夾: " + selectedDirectory.getAbsolutePath());
                }
            }
        });

        // 當點擊「開始生成文件」時
        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appendLog("開始執行解析任務...");
                generateBtn.setEnabled(false); // 避免重複連點

                // TODO: 這裡之後要呼叫你寫好的 Main 核心邏輯

                appendLog("任務完成！請查看 docs 資料夾。");
                generateBtn.setEnabled(true);
            }
        });
    }

    // 提供一個方法，讓文字可以寫入黑色的日誌區塊
    public void appendLog(String message) {
        consoleArea.append(message + "\n");
        // 讓卷軸自動滾到最底下
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    // 啟動 GUI 的入口
    public static void main(String[] args) {
        // 確保 GUI 在事件分派執行緒中執行 (Swing 規範)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDocGUI().setVisible(true);
            }
        });
    }
}