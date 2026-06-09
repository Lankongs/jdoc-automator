# 專案 API 說明文件 (自動生成)

> 本文件由 J-Doc Automator 自動掃描原始碼生成。

---

##  Class: `FileCrawler`

###  Methods (方法清單)

#### `findJavaFiles`
> ** AI 解析:** 這段程式碼的功能是掃描指定資料夾及其子資料夾，並回傳所有 .java 檔案的列表。

* **權限:** `public`
* **回傳:** `List<File>`
* **參數:** `String directoryPath` 

#### `scanDirectory`
> ** AI 解析:** 這段程式碼透過遞迴方式掃描資料夾，將所有副檔名為 .java 的檔案加入結果清單中。

* **權限:** `private`
* **回傳:** `void`
* **參數:** `File currentDir` `List<File> resultList` 

---

##  Class: `JDocGUI`

###  Methods (方法清單)

#### `appendLog`
> ** AI 解析:** 這段程式碼透過 SwingUtilities 確保在多執行緒環境下安全地將訊息追加到 UI 的文字區域並自動滾動到最底部。

* **權限:** `private`
* **回傳:** `void`
* **參數:** `String message` 

#### `clearLog`
> ** AI 解析:** 這段程式碼的功能是清空控制台顯示區的文本內容。

* **權限:** `private`
* **回傳:** `void`
* **參數:** 無

#### `resetForm`
> ** AI 解析:** 這段程式碼的功能是將生成按鈕和選擇按鈕重新啟用。

* **權限:** `private`
* **回傳:** `void`
* **參數:** 無

#### `main`
> ** AI 解析:** 這段程式碼在事件派發線程中啟動一個可視化的 JDocGUI 窗口。

* **權限:** `public`
* **回傳:** `void`
* **參數:** `String[] args` 

---

##  Class: `LLMClient`

###  Methods (方法清單)

#### `askAI`
> ** AI 解析:** 這段程式碼將 Java 方法原始碼傳給 OpenAI，並獲取其中文解釋。

* **權限:** `public`
* **回傳:** `String`
* **參數:** `String methodCode` 

---

##  Class: `Main`

###  Methods (方法清單)

#### `main`
> ** AI 解析:** 這段程式碼自動掃描指定資料夾中的所有 Java 檔案，解析其結構與註解，並生成一份 Markdown 格式的 API 說明文件。

* **權限:** `public`
* **回傳:** `void`
* **參數:** `String[] args` 

---

##  Class: `MarkdownGenerator`

###  Methods (方法清單)

#### `generateFile`
> ** AI 解析:** 這段程式碼將指定字串內容寫入一個 Markdown 檔案，並在必要時創建存放資料夾。

* **權限:** `public`
* **回傳:** `void`
* **參數:** `String fileName` `String content` 

---

