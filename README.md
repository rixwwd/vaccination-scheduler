COVID-19 Vaccination Scheduling Application for learning Spring Boot
===========================================


## これは何?

COVID-19の予防接種の予約をスケジュールすることを題材として、[Spring Boot](https://spring.io/projects/spring-boot)を学習するためのアプリケーションです。

## 機能

### vaccination-scheduler-admin

管理者用アプリケーション。
施設や、接種の時間枠等のマスタデータのメンテナンスを行うことができる。

### vaccination-scheduler-public

一般公開用アプリケーション。
予約を行うことができる。


## 想定するワークフロー

### A 役所の準備作業

1. 予防接種の会場を登録する。
1. 会場の時間枠とその枠の収容人数を設定する。
1. ワクチンの配送予定日と場所を設定する。
1. 住民の登録を行う。
1. 登録と同時に、クーポン、予約サイト(accination-scheduler-public)のログインID、パスワードを発行する。
クーポン、ログインID、パスワードを住民に送付する。

### B 住民の準備作業

1. 予約サイト(accination-scheduler-public)に送付されたログインID、パスワードでログインする。
1. 場所と時間を選択して、予約を行う。
1. 予約後、都合が悪い場合は予約を取り消す。➙取り消し後はキャンセル待ちの住民に通知する。

### C 当日

1. 住民は予約した時間に、予約した場所に行く。
1. 問診票を記入する。
1. 受付でクーポン、予約番号を提示し、本人確認を行う。
1. ワクチンの接種を受ける。

## インストール

### Heroku

クローンする。

```sh
git clone https://github.com/rixwwd/vaccination-scheduler.git
heroku login
```

vaccination-scheduler-adminのインストール

```sh
cd vaccination-scheduler/vaccination-scheduler-admin
./mvnw clean heroku:deploy
```

vaccination-scheduler-publicのインストール

```sh
cd vaccination-scheduler/vaccination-scheduler-public
./mvnw clean heroku:deploy
```
