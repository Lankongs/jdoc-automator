# 專案 API 說明文件 (自動生成)

> 本文件由 J-Doc Automator 自動掃描原始碼生成。

---

##  Class: `FileCrawler`

###  Methods (方法清單)

#### `findJavaFiles`
> ** AI 解析:** 此程式碼會從指定的資料夾路徑遞迴搜尋並返回所有的 .java 檔案清單。

* **權限:** `public`
* **回傳:** `List<File>`
* **參數:** `String directoryPath` 

#### `scanDirectory`
> ** AI 解析:** 此段程式碼用於遞迴掃描指定資料夾及其子資料夾，將所有.java檔案加入結果清單中。

* **權限:** `private`
* **回傳:** `void`
* **參數:** `File currentDir` `List<File> resultList` 

---

##  Class: `JDocGUI`

###  Methods (方法清單)

#### `appendLog`
> ** AI 解析:** 這段程式碼在多執行緒環境中安全地將訊息追加到 UI 的控制項上並自動滾動到底部。

* **權限:** `private`
* **回傳:** `void`
* **參數:** `String message` 

#### `clearLog`
> ** AI 解析:** 這段程式碼的功能是清空控制台區域的文字內容。

* **權限:** `private`
* **回傳:** `void`
* **參數:** 無

#### `resetForm`
> ** AI 解析:** 這段程式碼的功能是重置表單，使生成和選擇按鈕可用。

* **權限:** `private`
* **回傳:** `void`
* **參數:** 無

#### `main`
> ** AI 解析:** 這段程式碼在 Swing 的事件派發線程中啟動一個 JDocGUI 的可視化界面。

* **權限:** `public`
* **回傳:** `void`
* **參數:** `String[] args` 

---

##  Class: `LLMClient`

###  Methods (方法清單)

#### `askAI`
> ** AI 解析:** 這段程式碼透過 OpenAI API 將傳入的 Java 方法原始碼轉換為中文解釋並回傳結果。

* **權限:** `public`
* **回傳:** `String`
* **參數:** `String methodCode` 

---

##  Class: `Main`

###  Methods (方法清單)

#### `main`
> ** AI 解析:** 這段程式碼自動掃描指定資料夾中的 Java 檔案，解析其類別和方法，並生成包含 API 說明的 Markdown 文件。

* **權限:** `public`
* **回傳:** `void`
* **參數:** `String[] args` 

---

##  Class: `MarkdownGenerator`

###  Methods (方法清單)

#### `generateFile`
> ** AI 解析:** 這段程式碼將指定的 Markdown 字串內容寫入一個名為 fileName 的檔案，並確保檔案所在的 docs 資料夾存在。

* **權限:** `public`
* **回傳:** `void`
* **參數:** `String fileName` `String content` 

---

