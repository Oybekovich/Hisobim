# Hisobim — Android (Kotlin / Jetpack Compose)

Do'kon egalari uchun qarz daftari ilovasi. Avval **React Native (Expo)** da yozilgan edi;
endi to'liq **native Kotlin + Jetpack Compose** ga ko'chirildi (Liquid Glass dizayn).

## Funksiyalar

- **Mijozlar** — qo'shish, tahrirlash, o'chirish, qidirish; har biri uchun qarz/to'lov tarixi
- **Qarz / to'lov** — musbat summa = qarz, manfiy = to'lov; `total_debt` avtomatik hisoblanadi (trigger)
- **Hisobot** — umumiy qarzdorlik, mijoz/qarzdor/to'lagan sonlari, eng ko'p qarzdorlar
- **Sozlamalar** — do'kon nomini tahrirlash, profil, ilovadan chiqish
- **Kirgan qurilmalar** — hisobga kirgan qurilmalarni ko'rish va ularni masofadan chiqarish
  (`user_devices` + `revoke_device` RPC orqali haqiqiy sessiya o'chiriladi)
- **Admin panel** — login maydoniga `admin` yozib kiriladi (maxsus akkaunt): barcha
  do'konlarni ko'rish (login/email, mijoz soni, qarz), login/parolni o'zgartirish,
  hisobni o'chirish (parol ko'rilmaydi — faqat qayta o'rnatiladi)
- **Autentifikatsiya** — email/parol (Supabase Auth), parolni ko'rish/yashirish, form validatsiya

## Texnologiyalar

| Qatlam | Texnologiya |
|---|---|
| Til / UI | Kotlin 2.1.20, Jetpack Compose (BOM 2024.12.01), Material 3 |
| Navigatsiya | Navigation Compose |
| Holat (state) | ViewModel + Compose `State` / `StateFlow` |
| Backend | Supabase (`supabase-kt` 3.1.4: auth + postgrest) + Ktor 3.1.2 |
| Saqlash | DataStore (sessiya + faol do'kon) |
| Shrift | Manrope (variable font, `FontVariation`) |
| Build | AGP 8.7.3, Gradle 8.10.2, JDK 21 |

- `applicationId`: `uz.hisobim.app`
- `minSdk` = 26 (variable shrift og'irliklari uchun), `targetSdk`/`compileSdk` = 34

## Ishga tushirish

Android Studio'da loyiha ildizini oching va **Run** bosing, yoki terminaldan:

```bash
# Debug APK yig'ish
./gradlew :app:assembleDebug

# Ulangan qurilma/emulyatorga o'rnatish
./gradlew :app:installDebug
```

Supabase URL va anon kalit `app/build.gradle.kts` ichida `BuildConfig` orqali beriladi
(eski `eas.json` dagi qiymatlar bilan bir xil).

## Arxitektura — RN → Kotlin moslik

| React Native | Kotlin |
|---|---|
| `src/theme.ts` | `ui/theme/` (Color, Type, Dimens, Shadow, Theme) |
| `src/types/index.ts` | `data/model/Models.kt` |
| `src/lib/supabase.ts` | `data/remote/SupabaseProvider.kt` + `DataStoreSessionManager.kt` |
| `src/services/*.ts` | `data/repo/*Repository.kt` |
| `src/stores/*` (zustand) | `ui/AppViewModel.kt` + `data/local/ShopPreferences.kt` |
| `src/hooks/*` (React Query) | har ekran `*ViewModel.kt` + `UiState` |
| `src/utils/*` | `util/Format.kt`, `util/Validation.kt`, `util/Initials.kt` |
| `src/components/*.tsx` | `ui/components/*.kt` |
| `app/*` (expo-router) | `ui/<feature>/*Screen.kt` + `ui/navigation/HisobimNavGraph.kt` |

> **RN'da mavjud bo'lmagan yangi qismlar:** `ui/admin/` (admin panel),
> `ui/devices/` + `data/repo/DeviceRepository.kt` (kirgan qurilmalar),
> `data/repo/AdminRepository.kt`, `data/local/DeviceIdProvider.kt`.

## Dizayn fidelity (e'tiborli nuqtalar)

- **Glass effekt**: `expo-blur` BlurView o'rniga gradient fon ustida oq qatlam
  (kartalar va inputlar to'liq oq; nav bar — suzuvchi yumaloq panel; segment yarim shaffof).
- **Ikonkalar**: Ionicons → eng yaqin Material Icons (`ui/components/AppIcons.kt` da
  markazlashtirilgan).
- **Ranglar, spacing, radius, tipografika**: `theme.ts` dan 1:1 ko'chirildi.

## Backend (Supabase)

Sxema va seed `supabase/migration.sql`, `supabase/seed.sql` da.

- **Jadvallar:** `shops`, `customers`, `debts`, `shop_users`, `user_devices`
- **Trigger:** `debts` → `customers.total_debt` avtomatik sinxronlanadi
- **RLS:** har bir do'kon egasi faqat o'z ma'lumotini ko'radi (`is_shop_owner`)
- **RPC funksiyalari** (`SECURITY DEFINER`, xavfsiz):
  - `revoke_device` — qurilma sessiyasini o'chirish ("Kirgan qurilmalar")
  - `is_admin`, `admin_list_accounts`, `admin_update_email`, `admin_update_password`,
    `admin_delete_user` — admin panel (faqat admin chaqira oladi)

## Eski React Native kodi

Butun RN/Expo loyihasi `legacy-react-native/` papkasiga ko'chirildi (o'chirilmadi —
git tarixida va o'sha papkada saqlanadi).
