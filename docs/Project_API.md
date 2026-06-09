# 專案 API 說明文件 (自動生成)

> 本文件由 J-Doc Automator 自動掃描原始碼生成。

---

##  Class: `FileCrawler`

###  Methods (方法清單)

#### `findJavaFiles`
> ** AI 解析:** 此程式碼從給定的資料夾路徑中遞迴搜尋並回傳所有的 .java 檔案列表。

* **權限:** `public`
* **回傳:** `List<File>`
* **參數:** `String directoryPath` 

#### `scanDirectory`
> ** AI 解析:** 這段程式碼遞迴掃描指定資料夾，將所有副檔名為 .java 的檔案收集到結果清單中。

* **權限:** `private`
* **回傳:** `void`
* **參數:** `File currentDir` `List<File> resultList` 

---

##  Class: `JDocGUI`

###  Methods (方法清單)

#### `appendLog`
> ** AI 解析:** 此程式碼在多執行緒環境中安全地將訊息附加到 UI 的文字區域並自動捲動至底部。

* **權限:** `private`
* **回傳:** `void`
* **參數:** `String message` 

#### `clearLog`
> ** AI 解析:** 這段程式碼的功能是清空控制台區域的顯示內容。

* **權限:** `private`
* **回傳:** `void`
* **參數:** 無

#### `resetForm`
> ** AI 解析:** 這段程式碼的功能是重置表單狀態，並在事件處理線程中啟用生成和選擇按鈕。

* **權限:** `private`
* **回傳:** `void`
* **參數:** 無

#### `main`
> ** AI 解析:** 這段程式碼在事件調度線程中啟動一個新的 JDocGUI 窗口並設其可見。

* **權限:** `public`
* **回傳:** `void`
* **參數:** `String[] args` 

---

##  Class: `LLMClient`

###  Methods (方法清單)

#### `askAI`
> ** AI 解析:** 這段程式碼用來傳送 Java 方法原始碼到 OpenAI，並回傳其中文解釋。

* **權限:** `public`
* **回傳:** `String`
* **參數:** `String methodCode` 

---

##  Class: `MarkdownGenerator`

###  Methods (方法清單)

#### `generateFile`
> ** AI 解析:** 這段程式碼將指定的 Markdown 內容寫入一個名為 fileName 的檔案，並確保其存放在 docs 資料夾中。

* **權限:** `public`
* **回傳:** `void`
* **參數:** `String fileName` `String content` 

---

