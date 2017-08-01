package com.xiongan.develop.news.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;

import com.unbelievable.library.android.download.DownloadManagerUtils;
import com.unbelievable.library.android.utils.ToastUtils;
import com.xiongan.develop.news.R;
import com.xiongan.develop.news.bean.UpgradeBean;
import com.xiongan.develop.news.util.ApkUtils;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import static com.xiongan.develop.news.MyApplication.getContext;

/**
 * Created by admin on 2017/8/1.
 */

public class CommonDialog {
    public static void showUpgrade(final Context context ,final UpgradeBean bean) {
        if(bean.versionCode <= ApkUtils.getVersion(context)){
            return;
        }
        final Handler handler = new Handler();
        Dialog dialog =
                new AlertDialog.Builder(context)
                        .setCancelable(!bean.forceUpgrade)
                        .setTitle(R.string.upgrade)
                        .setMessage(bean.des)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ToastUtils.toastL(context,context.getString(R.string.downloading));
                                final long requestId = DownloadManagerUtils.request(getContext(), bean.apkUrl, "xiongan", bean.versionName+".apk");
                                final Timer timer = new Timer();
                                TimerTask task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                boolean isFinish = DownloadManagerUtils.isFinish(context,requestId);
                                                if(isFinish){
                                                    timer.cancel();
                                                    String str = DownloadManagerUtils.getLocalFileUriByRequestId(context,requestId);
                                                    ApkUtils.install(context, Environment.getExternalStorageDirectory() + "/" + str.substring(str.indexOf("xiongan"),str.length()));
                                                }
                                            }
                                        });
                                    }
                                };
                                timer.schedule(task,5*1000,5*1000);
                            }
                        })
                        .show();
        //用于不关闭对话框
        if(bean.forceUpgrade) {
            try {
                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                field.set(dialog, false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
