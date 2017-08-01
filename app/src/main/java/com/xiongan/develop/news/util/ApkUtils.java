package com.xiongan.develop.news.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.unbelievable.library.android.utils.ToastUtils;
import com.xiongan.develop.news.R;

import java.io.File;

/**
 * Created by admin on 2017/8/1.
 */

public class ApkUtils {
    public static int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return Integer.MAX_VALUE;
        }
    }

    public static void install(Context context,String apkPath) {
        File file = new File(apkPath);
        if(!file.exists()){
            ToastUtils.toastL(context,context.getString(R.string.file_not_exist));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
