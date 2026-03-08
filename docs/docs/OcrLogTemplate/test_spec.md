---
document_title: OcrLogTemplate テスト仕様書
dest: ./output/OcrLogTemplate_テスト仕様書.pdf
---

<!-- 表紙 -->
<div class="cover">
  <div class="title">OcrLogTemplate テスト仕様書</div>
  <div class="version">v1.0.0</div>
  <div class="date">2026-03-08</div>
  <div class="logo">

   ![logo](/docs/_shared-images/logo.svg)

  </div>
  <div class="copyrights">OcrLogTemplate Project</div>
</div>

<div class="page-break"></div>

---

# 1. テスト概要

本書は **OcrLogTemplate アプリケーションの動作確認**を目的としたテスト仕様書である。  
画面操作、OCR機能、ログ保存機能について正常系および異常系の確認を行う。

---

## 1.1 テスト目的

- アプリが仕様通り動作することを確認する
- OCR機能が文字を認識できることを確認する
- OCR履歴が正しく保存されることを確認する
- CSV出力が正常に生成されることを確認する

---

## 1.2 テスト範囲

対象機能

- Home画面
- Scan画面（OCR読取）
- Log画面（履歴表示）
- Settings画面
- CSV出力
- ライセンス表示

対象外

- ML Kit内部処理
- Android OS内部処理

---

# 2. テスト項目一覧

| No | 区分 | 項目 | 条件 | 期待結果 |
|----|------|------|------|-----------|
| 1 | 画面 | Home画面表示 | アプリ起動 | Home画面表示 |
| 2 | 画面 | Scan画面遷移 | Scan押下 | Scan画面表示 |
| 3 | OCR | OCR読取 | 英数字文字 | 正しく文字認識 |
| 4 | OCR | OCR読取 | 日本語文字 | 認識精度低 |
| 5 | DB | ログ保存 | OCR保存 | 履歴保存 |
| 6 | DB | 履歴表示 | Log画面 | 履歴一覧表示 |
| 7 | CSV | CSV出力 | 履歴あり | CSV生成 |
| 8 | CSV | CSV出力 | 履歴なし | 空CSV生成 |
| 9 | 設定 | OCRモード変更 | ASCII/ALL | 設定保存 |
| 10 | UI | License画面 | License押下 | ライセンス表示 |

---

# 3. 詳細テスト仕様

---

## 3.1 Home画面

### 手順

1. アプリ起動

### 期待結果

- Home画面が表示される
- Scan / Log / Settings / License が表示される

---

## 3.2 OCR読取

### 事前条件

カメラ権限許可

### 手順

1. Scan画面を開く  
2. 読込開始押下  
3. OCR枠に文字を配置  

### 期待結果

- OCR文字が画面に表示される

---

## 3.3 履歴画面

### 手順

1. Log画面を開く

### 期待結果

- OCR履歴が表示される
- timestamp / text / result が表示される

---

## 3.4 CSV出力

### 手順

1. Log画面を開く  
2. CSV出力ボタン押下  

### 期待結果

- Download/OCRLog フォルダにCSV生成

---

## 3.5 設定画面

### 手順

1. Settings画面を開く  
2. OCRモード変更  

### 期待結果

- 設定が保存される
- 再起動後も設定が保持される

---

# 4. テスト環境

| 項目 | 内容 |
|------|------|
| OS | Android |
| 開発環境 | Android Studio |
| OCR | Google ML Kit |
| DB | SQLite (Room) |

---

# 5. テスト結果記録

| No | テスト項目 | 結果 | 備考 |
|----|------------|------|------|
| 1 | Home表示 | | |
| 2 | OCR読取 | | |
| 3 | 履歴保存 | | |
| 4 | CSV出力 | | |
| 5 | 設定保存 | | |

---

# 改訂履歴

| 版数 | 日付 | 内容 |
|------|------|------|
| v1.0.0 | 2026-03-08 | 初版 |