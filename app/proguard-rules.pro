# ── Kotlinx Serialization ───────────────────────────────────────
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class **$$serializer { *; }
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class uz.hisobim.app.**$$serializer { *; }
-keepclassmembers class uz.hisobim.app.data.model.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}

# ── Ktor / Supabase ─────────────────────────────────────────────
-keep class io.ktor.** { *; }
-keep class io.github.jan.supabase.** { *; }
-dontwarn org.slf4j.**
-dontwarn io.ktor.**
