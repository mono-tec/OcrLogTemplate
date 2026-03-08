---
document_title: OcrLogTemplate 基本仕様書
dest: ./output/OcrLogTemplate_基本仕様書.pdf
---

<!-- 表紙 -->
<div class="cover">
  <div class="title">OcrLogTemplate 基本仕様書</div>
  <div class="version">v1.0.0</div>
  <div class="date">2026-03-08</div>
  <div class="logo">

   ![logo](/docs/_shared-images/logo.svg)

  </div>
  <div class="copyrights">OcrLogTemplate Project</div>
</div>

<div class="page-break"></div>

---

<!-- omit from toc -->
# 目次

- [1. 文書の目的](#1-文書の目的)
- [2. システム概要](#2-システム概要)
- [3. システム構成](#3-システム構成)
- [4. 機能仕様](#4-機能仕様)
  - [4.1 OCR読取機能](#41-OCR読取機能)
  - [4.2 履歴管理機能](#42-履歴管理機能)
  - [4.3 CSV出力機能](#43-CSV出力機能)
  - [4.4 設定機能](#44-設定機能)
  - [4.5 ライセンス表示機能](#45-ライセンス表示機能)
- [5. データ仕様](#5-データ仕様)
- [6. CSV出力仕様](#6-CSV出力仕様)
- [7. Android権限仕様](#7-android権限仕様)
- [8. 使用ライブラリ](#8-使用ライブラリ)
- [9. 使用リソース](#9-使用リソース)
- [10. 制約事項](#10-制約事項)
- [11. 今後の拡張](#11-今後の拡張)
- [付録 改訂履歴](#付録-改訂履歴)

---

# 1. 文書の目的

本書は **OcrLogTemplate アプリケーションの基本仕様**を定義する。

本アプリは Android スマートフォンのカメラを利用し、
OCR により文字を読み取り履歴として保存するアプリケーションである。

---

# 2. システム概要

OcrLogTemplate は Android スマートフォンのカメラ機能を利用し、
文字を OCR 認識しログとして保存するアプリケーションである。

主な用途

- 設備番号読み取り
- シリアル番号記録
- 作業ログ保存
- OCR入力補助

---

# 3. システム構成

本システムは以下の構成で動作する。

```text
Android Camera
↓
ML Kit OCR
↓
Text Processor
↓
Room Database
↓
CSV Export
```

---

# 4. 機能仕様

## 4.1 OCR読取機能

カメラを利用して文字を読み取る。

処理内容

- カメラ画像取得
- OCR解析（ML Kit）
- 文字抽出
- 設定モードによるフィルタ

---

## 4.2 履歴管理機能

OCR結果を履歴として保存する。

保存データ

- 読取日時
- OCR文字
- 処理結果

履歴画面では以下操作が可能

- 履歴一覧表示
- CSV出力
- 履歴削除

---

## 4.3 CSV出力機能

履歴データをCSVとして出力する。

保存先
```text
Download/OCRLog/
```


---

## 4.4 設定機能

OCR読取モードを設定する。

設定項目

|設定|説明|
|---|---|
|ASCIIモード|半角英数字のみ抽出|
|全文字モード|OCR検出文字をそのまま表示|

---

## 4.5 ライセンス表示機能

アプリで使用しているライブラリ情報を表示する。

---

# 5. データ仕様

履歴データは Room Database に保存する。

保存項目

|項目|内容|
|---|---|
|timestamp|読取日時|
|text|OCR文字|
|result|処理結果|

---

# 6. CSV出力仕様

CSV形式
```csv
timestamp,text,result
2026/03/08 09:41:17,PURE MALT,OK
```
保存先
```text
Download/OCRLog/
```


---

# 7. Android権限仕様

本アプリでは以下の権限を使用する。

|権限|用途|
|---|---|
|CAMERA|OCR読取|
|READ_MEDIA_IMAGES|CSV保存|
|WRITE_EXTERNAL_STORAGE|CSV出力|

---

# 8. 使用ライブラリ

|ライブラリ|用途|
|---|---|
|Jetpack Compose|UI|
|CameraX|カメラ制御|
|ML Kit Text Recognition|OCR|
|Room|データベース|
|DataStore|設定保存|
|Navigation Compose|画面遷移|

---

# 9. 使用リソース

## 9.1 アイコン生成

生成画像は Android Studio の Image Asset 機能を利用し
アプリアイコンとして使用している。

使用機能

- Android Studio
- Image Asset
- Adaptive Icon

---

# 10. 制約事項

本アプリは Google ML Kit Text Recognition を利用している。

そのため、日本語文字の認識精度は完全ではない。

本バージョンでは ASCII 文字（半角英数字・記号・スペース）を
主な対象文字としている。

---

# 11. 今後の拡張

日本語OCRについては別プロジェクトとして研究を進める予定である。

将来的には以下の方式の検討を行う。

- 日本語OCRモデルの導入
- OCRエンジンの切替
- AI OCRサービスの利用

---

# 付録 改訂履歴

|版数|日付|内容|
|---|---|---|
|v1.0.0|2026-03-08|初版|