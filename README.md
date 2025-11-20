# Flattening – Parcelable & Serialization Playground

English | [فارسی](#فارسی)

## Overview
Flattening is a multi-module Android sample that contrasts three object-flattening approaches:
- **feature-parcelable**: Classic `Parcelable` plus `@Parcelize`, nested models, and sealed states.
- **feature-serialization**: Java `Serializable` alongside `kotlinx.serialization` JSON round-trips.
- **feature-mixed**: A hybrid model implementing both paradigms with a quick benchmark and decision guide.

The `app` module stitches the labs together with a simple Compose UI and bottom navigation.

## Modules
- `:app` – Compose navigation shell that hosts each lab screen.
- `:feature-parcelable` – Parcelable/@Parcelize demonstrations with manual and generated code.
- `:feature-serialization` – Serializable basics, transient fields, and Kotlinx JSON encoding.
- `:feature-mixed` – Hybrid model plus micro-benchmark and guidance on when to choose each approach.

---

## فارسی
پروژه **Flattening** یک نمونه‌ی چندماژوله اندروید است که سه روش تخت‌سازی داده را مقایسه می‌کند:
- **feature-parcelable**: پیاده‌سازی دستی `Parcelable` و استفاده از `@Parcelize` برای مدل‌های تودرتو و حالت‌های Sealed.
- **feature-serialization**: کار با `Serializable` جاوا در کنار رفت و برگشت JSON با `kotlinx.serialization`.
- **feature-mixed**: مدل ترکیبی که هر دو روش را پیاده می‌کند به‌همراه بنچمارک ساده و راهنمای انتخاب.

ماژول `app` با رابط کاربری Compose و نوار ناوبری پایین، تمام این دموها را کنار هم می‌آورد.

## ماژول‌ها
- `:app` – پوسته ناوبری Compose که میزبان صفحه‌های آزمایش است.
- `:feature-parcelable` – نمایش‌های Parcelable و `@Parcelize` با کد دستی و تولید‌شده.
- `:feature-serialization` – مبانی Serializable، فیلدهای transient و کدنویسی JSON با Kotlinx.
- `:feature-mixed` – مدل هیبریدی به‌همراه میکرو بنچمارک و راهنمای انتخاب ابزار مناسب.
