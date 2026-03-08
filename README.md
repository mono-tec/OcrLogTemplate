![Android](https://img.shields.io/badge/platform-Android-green)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)

# OcrLogTemplate

Android OCR logging template application.

OcrLogTemplate は **Android向けOCRログ記録サンプルアプリ**です。

このアプリでは次のような機能の実装例を提供しています。

-   OCRによる文字読み取り
-   OCR結果のログ保存
-   CSV出力
-   プロジェクトドキュメント自動生成

---

# 主な機能

-   **Google ML Kit** を使用したOCR
-   **Room Database** によるログ保存
-   CSV出力機能
-   OCR読取モード設定
-   ライセンス表示画面
-   ドキュメント生成機能

---

# 画面一覧

| 画面 | 説明 |
|-----|-----|
| Home | アプリメニュー |
| Scan | OCR読取および保存 |
| Log | OCR履歴表示 |
| Settings | OCR読取モード設定 |
| License | 使用ライブラリ表示 |

---

## Screenshot

<p align="center">
<img src="docs/docs/OcrLogTemplate/images/user_manual/UI_0101_home.png" width="180">
<img src="docs/docs/OcrLogTemplate/images/user_manual/UI_0201_ocr_describe.png" width="180">
<img src="docs/docs/OcrLogTemplate/images/user_manual/UI_0301_history_list.png" width="180">
<img src="docs/docs/OcrLogTemplate/images/user_manual/UI_0401_settings.png" width="180">
<img src="docs/docs/OcrLogTemplate/images/user_manual/UI_0501_ocr_describe.png" width="180">
</p>

---

# プロジェクト構成

    OcrLogTemplate
    │
    ├ app
    │   └ Androidアプリケーション
    │
    ├ docs
    │   ├ docs/OcrLogTemplate
    │   │   ├ user_manual.md
    │   │   ├ specifications.md
    │   │   ├ internal_design.md
    │   │   ├ screen_flow.md
    │   │   └ test_spec.md
    │   │
    │   └ output
    │       ├ ユーザーマニュアル
    │       ├ 基本仕様書
    │       ├ 内部設計書
    │       ├ 画面遷移図
    │       └ テスト仕様書

---

# ドキュメント生成

本プロジェクトでは以下のテンプレートを利用して  
MarkdownからPDFドキュメントを生成しています。

https://github.com/tsuna-can-se/md2pdf-doc-template

ドキュメント生成の手順については  
上記テンプレートの README を参照してください。

本リポジトリでは `docs` ディレクトリ配下に  
各仕様書の Markdown ファイルを配置しています。

生成されるドキュメント

- ユーザーマニュアル
- 基本仕様書
- 内部設計書
- 画面遷移図
- テスト仕様書

---

# OCRについて

現在のOCRは **ML Kit Latinモデル**を利用しているため  
日本語認識の精度は高くありません。

英数字の読み取り用途を想定しています。

---

# 今後の予定

- ソースコードコメント追加
- UnitTest実装
- OCR精度改善

---

# テンプレートとしての利用

本リポジトリは  
**OCRログアプリの実装例および Androidアプリ開発テンプレート**として公開しています。

アプリとして利用することも可能ですが、  
主な目的は **他のAndroidアプリ開発のベースとして流用すること**です。

---

## テンプレートの特徴

本プロジェクトには以下の構成が含まれています。

- Jetpack Compose UI構成
- Navigation による画面遷移
- Room による履歴保存
- DataStore による設定管理
- CSV出力
- ライセンス画面
- md2pdf-doc-template によるドキュメント生成
- 基本仕様書 / 内部設計書 / テスト仕様書

そのため **アプリ＋ドキュメント一式のテンプレート**として利用できます。

---

## 他アプリへ展開する手順

例として以下のような手順で流用できます。

1. 本リポジトリをコピーして新規プロジェクトを作成  
2. アプリ名 / パッケージ名 / アイコン変更  
3. Home画面メニューを変更  
4. 必要な機能のみ残す  
5. docs配下の仕様書を流用して更新  

---

## 展開用途例

このテンプレートは次のようなアプリに流用できます。

- 設備番号入力アプリ
- バーコード読取アプリ
- 作業ログアプリ
- 点検記録アプリ
- OCR＋API連携アプリ

---

## 日本語OCRについて

本アプリのOCRは **ML Kit Latinモデル**を利用しています。

そのため

- 英数字OCRには対応
- 日本語OCRには完全対応していません

日本語OCRについては  
**別プロジェクトとして実装予定です。**

理由

- OCRエンジンの選定
- モデルサイズ
- 精度検証

などの検討が必要なためです。

英語圏用途では、この実装のままでも問題なく利用できます。

---

# ライセンス

本プロジェクトは **Apache License 2.0** で公開しています。

改変・再配布が可能です。  
詳細は `LICENSE` ファイルを参照してください。

---

# 作者

mono-tec

