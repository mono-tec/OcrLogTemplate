---
document_title: OcrLogTemplate ユーザーマニュアル
dest: ./output/OcrLogTemplate ユーザーマニュアル.pdf
---

<!-- 表紙 -->
<div class="cover">
  <div class="title">OcrLogTemplate ユーザーマニュアル</div>
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

- [1. はじめに](#1-はじめに)
  - [1.1 主な用途](#11-主な用途)
  - [1.2 対象読者](#12-対象読者)
- [2. システムについて](#2-システムについて)
  - [2.1 システム概要](#21-システム概要)
  - [2.2 動作環境](#22-動作環境)
- [3. 画面移動機能](#3-画面移動機能)
  - [3.1 Home画面](#31-Home画面)
  - [3.2 上部メニュー](#32-上部メニュー)
  - [3.3 下部メニュー](#33-下部メニュー)
- [4. 機能画面](#4-機能画面)
  - [4.1 OCR読取画面](#41-OCR読取画面)
  - [4.2 履歴画面](#42-履歴画面)
  - [4.3 設定画面](#43-設定画面)
  - [4.4 ライセンス画面](#44-ライセンス画面)

- [5. エラー時の対処](#5-エラー時の対処)
- [6. よくある質問](#6-よくある質問)

- [付録 改訂履歴](#付録-改訂履歴)

---

# 1. はじめに

本書は **OcrLogTemplate アプリの操作方法**を説明するユーザーマニュアルです。

OcrLogTemplate はスマートフォンのカメラを使用して文字をOCR読み取りし、  
ログとして保存するアプリケーションです。

### 1.1 主な用途

- 設備番号入力
- シリアル番号記録
- 作業ログの保存
- OCR入力の補助

### 1.2 対象読者

本マニュアルは以下の利用者を対象としています。

- 個人開発者

---

# 2. システムについて

## 2.1 システム概要

本アプリは以下の機能を提供します。

<center>

| 機能 | 説明 |
|-----|-----|
| OCR読取 | カメラで文字を読み取る |
| 文字確認 | OCR結果を確認・修正 |
| 履歴保存 | OCR結果を履歴として保存 |
| CSV出力 | 履歴データをCSVとして出力 |
| 設定 | OCR読取モード設定 |
| ライセンス表示 | 使用ライブラリ表示 |

</center>

---

## 2.2 動作環境

<center>

| 項目 | 内容 |
|-----|-----|
| OS | Android |
| Androidバージョン | Android 10以上 |
| 必須機能 | カメラ |
| OCRライブラリ | Google ML Kit |

</center>

---

# 3. 画面移動機能

## 3.1 Home画面

アプリ起動時に表示される画面です。

<center>
<img src="./images/user_manual/UI_0101_home.png" width="30%" alt="Home画面">

| ボタン | 説明 |
|------|------|
| Scan | OCR読取画面 |
| Log History | 履歴画面 |
| Settings | 設定画面 |
| Libraries / Licenses | ライセンス表示 |
| Exit | アプリ終了 |
</center>

---

## 3.2 上部メニュー

TOP以外の画面では上部にメニューが表示されます。

<center>
<img src="./images/user_manual/UI_0102_header_menu.png" width="30%" alt="上部メニュー">

| ボタン | 機能 |
|------|------|
| HOME | Home画面へ戻る |
</center>

---

## 3.3 下部メニュー

Scan / Log 画面では下部メニューが表示されます。

<center>
<img src="./images/user_manual/UI_0103_footer_menu.png" width="30%" alt="下部メニュー">

| ボタン | 機能 |
|------|------|
| Scan | OCR画面 |
| Log | 履歴画面 |
</center>

!!! info 注意
License画面では下部メニューは表示されません。
!!!

---

# 4. 機能画面

## 4.1 OCR読取画面

カメラで文字を読み取る画面です。

<center>
<img src="./images/user_manual/UI_0201_ocr_describe.png" width="30%" alt="読込送信画面">
</center>

### 操作手順

1. 「Scan」を押す  
2. カメラが起動する  
3. 読取枠に文字を合わせる  

<center>
<img src="./images/user_manual/UI_0202_ocr_capture.png" width="30%" alt="OCR読み取り">
</center>

4. OCR結果が表示される  
5. 必要に応じて文字を編集  
6. 「保存」を押す  

<center>
<img src="./images/user_manual/UI_0203_send_execute.png" width="30%" alt="文字送信">
</center>

履歴に保存されます。

---

## 4.2 履歴画面

OCR読取履歴を表示する画面です。

履歴画面には、以下の3機能があります。

1. 履歴を最新より表示します。
2. 履歴をAndroid端末にCSVとして出力します。
3. 履歴を削除します。

### 4.3.1 履歴表示

<center>
<img src="./images/user_manual/UI_0301_history_list.png" width="30%" alt="履歴画面">

| 項目 | 内容 |
|-----|-----|
| timestamp | 読取日時 |
| text | 読取文字 |
| result | 処理結果 |
</center>

---

### 4.3.2 CSV出力

履歴データをCSVとして出力できます。

<center>
<img src="./images/user_manual/UI_0302_history_export_csv.png" width="30%" alt="CSV出力">
</center>

#### 保存先

```text
Download/OCRLog/
```

PCに接続することで画像を取得することができます。
<center>
<img src="./images/user_manual/UI_0303_history_download.png" width="30%" alt="ダウンロード">
</center>

```csv
timestamp,text,result
2026/03/08 09:41:17,PURE MALT,OK
```

### 4.3.3 履歴削除

履歴データを削除できます。

<center>
<img src="./images/user_manual/UI_0304_history_delete.png" width="30%" alt="履歴削除">
</center>

---

## 4.4 設定画面

OCRの文字抽出方法を設定します。

<center>
<img src="./images/user_manual/UI_0401_settings.png" width="30%" alt="設定画面">

| 設定 | 説明 |
|-----|-----|
| ASCIIモード | 半角英数字のみ読取 |
| 全文字モード | OCR検出文字をそのまま表示 |
</center>


!!! info 使用上の注意
ML Kit OCRでは日本語認識が完全ではありません。
!!!

---

## 4.5 ライセンス画面

アプリで使用しているライブラリを表示します。

<center>
<img src="./images/user_manual/UI_0501_ocr_describe.png" width="30%" alt="ライセンス画面">
</center>

### 主な使用ライブラリ

<center>

| ライブラリ | ライセンス |
|------------|------------|
| Jetpack Compose | Apache License 2.0 |
| CameraX | Apache License 2.0 |
| Room | Apache License 2.0 |
| DataStore | Apache License 2.0 |
| Navigation Compose | Apache License 2.0 |
| Material Icons | Apache License 2.0 |
| ML Kit Text Recognition | Google Terms |

</center>

---

# 5. エラー時の対処

<center>

| エラー | 原因 | 対処 |
|------|------|------|
| OCR失敗 | 文字が不鮮明 | 再読込 |
| カメラエラー | 権限未許可 | カメラ権限許可 |
| 保存失敗 | ストレージ問題 | 再起動 |

</center>

---

# 6. よくある質問

### OCRが認識しない

半角英数字であるか確認してください。

### 日本語が認識できない

ML Kitの制限により、日本語認識は完全ではありません。

### CSVはどこに保存されますか？

Download/OCRLog フォルダに保存されます。

---

# 付録 改訂履歴

| 版数 | 日付 | 内容 |
|-----|------|------|
| v1.0.0 | 2026-03-08 | 初版 |

