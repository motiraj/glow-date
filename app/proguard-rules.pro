# Google Play Store R8 / ProGuard Rules for Glow Date

# Keep Jetpack Compose & Material3
-keep class androidx.compose.** { *; }
-keepclassmembers class * extends androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Glance App Widgets
-keep class androidx.glance.** { *; }
-keepclassmembers class * extends androidx.glance.** { *; }
-keep class * implements androidx.glance.appwidget.action.ActionCallback { public <init>(); }
-dontwarn androidx.glance.**

# Keep WorkManager & Workers
-keep class androidx.work.** { *; }
-keep class * extends androidx.work.ListenableWorker { *; }
-keep class * extends androidx.work.Worker { *; }
-keep class * extends androidx.work.CoroutineWorker { *; }
-dontwarn androidx.work.**

# Keep Jetpack DataStore
-keep class androidx.datastore.** { *; }
-keep class androidx.datastore.preferences.core.** { *; }

# Keep Kotlin Coroutines & Serialization
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }
-keep class kotlinx.serialization.** { *; }

# Keep Lifecycle & ViewModels
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel { *; }

# Keep Google Play Services & AdMob
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keep class com.google.android.gms.ads.** { *; }
-keepclassmembers class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Keep all App Data Models & Calculators
-keep class com.example.bs_addateconverteragecalculator.data.** { *; }
-keepclassmembers class com.example.bs_addateconverteragecalculator.data.** { *; }
-keep class com.example.bs_addateconverteragecalculator.calculator.** { *; }
-keepclassmembers class com.example.bs_addateconverteragecalculator.calculator.** { *; }
-keep class com.example.bs_addateconverteragecalculator.worker.** { *; }
-keep class com.example.bs_addateconverteragecalculator.widget.** { *; }

# General Android
-dontwarn android.support.**
