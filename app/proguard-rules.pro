-optimizationpasses 5
-target 1.7
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarning

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-useuniqueclassmembernames
-keepattributes *Annotation*,Exceptions,Signature,SourceFile,LineNumberTable,InnerClass,EnclosingMethod
-dontskipnonpubliclibraryclasses -dontskipnonpubliclibraryclassmembers

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
   native <methods>;
}

-keepclasseswithmembernames class * {
   public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
   public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
   public static **[] values();
   public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

-keep class org.apache.http.** {
    *;
 }

-keepclassmembers class **.R$* {
    public static <fields>;
}


-dontwarn org.apache.**

-keep class com.unbelievable.library.** {*;}
-keep class com.xiongan.develop.news.volleyplus.** {*;}

-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

-keep class com.xiongan.develop.news.bean.**{*;}

-keepclassmembers class ** {

    public void onEvent*(**);

}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }


-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}


-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-keep class com.lib.android.volley.** { *; }

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keep @com.google.common.annotations.VisibleForTesting class *
-keepclassmembers class * {
    @com.google.common.annotations.VisibleForTesting *;
}

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.green.ui.fragment.route** { *; }#保持实体数据结构接口不被混淆(也就是被GSON注解的实体结构) 此处xxx.xxx是自己接口的包名

#-keep class com.xxx.xxx.** { *; }#保持WEB接口不被混淆 此处xxx.xxx是自己接口的包名

-keep class * extends android.os.IInterface
-keep class * extends android.os.Binder


-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**
