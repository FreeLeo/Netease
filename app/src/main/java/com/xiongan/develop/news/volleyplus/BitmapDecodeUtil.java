package com.xiongan.develop.news.volleyplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * TODO<压缩图片至指定宽度，分成3个标准，最小的图为屏幕宽度的1/4>
 */

public class BitmapDecodeUtil {

    /**
     * 图片的宽度最大基数值
     */
    private final static int REQUIRED_WIDTH = 400;
    /**
     * 图片宽度与屏幕宽度之比，默认为2
     */
    public final static int SCALE_SMALL = 1;
    public final static int SCALE_MEDIUM = 2;
    public final static int SCALE_BIG = 4;
    /**
     * 不进行压缩处理
     */
    public final static int SCALE_NULL = 0;

    /**
     * 通过图片路径 获取bitmap对象
     *
     * @param path
     * @return
     */
    public static Bitmap decodePath(String path) {
        return decodePath(path, SCALE_MEDIUM);
    }

    public static Bitmap decodePath(String path, int scale) {
        File file = new File(path);
        if (file.exists()) {
            return decodeFile(file, scale * REQUIRED_WIDTH);
        }
        return null;
    }

    public static Bitmap decodePath(String path, int width, int height) {
        File file = new File(path);
        if (file.exists()) {
            return decodeFile(file, width, height);
        }
        return null;
    }

    /**
     * 通过文件路径 获取bitmap对象
     *
     * @param f
     * @return
     */
    private static Bitmap decodeFile(File f) {
        return decodeFile(f, SCALE_MEDIUM);
    }

    public static Bitmap decodeFile(File f, int width) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getAbsolutePath(), o);

        int scale = getScale(o, width);
        // decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeFile(f.getAbsolutePath(), o2);
    }

    public static Bitmap decodeFile(File f, int width, int height) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getAbsolutePath(), o);
        int scale = getScale(o, width, height);

        o.inSampleSize = scale;
        o.inJustDecodeBounds = false;
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        o.inPurgeable = true;
        o.inInputShareable = true;

        try {
            return BitmapFactory.decodeFile(f.getAbsolutePath(), o);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过流 获取bitmap对象
     *
     * @param is
     * @return
     */
    private static Bitmap decodeStream(InputStream is) {
        return decodeStream(is, SCALE_MEDIUM);
    }

    public static Bitmap decodeStream(InputStream is, int scale) {
        try {
            byte[] array = readStream(is);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new ByteArrayInputStream(array), null, o);

            int scale1 = getScale(o, scale * REQUIRED_WIDTH);
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale1;
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(array), null, o2);
            array = null;
            return bitmap;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过图片id 获取bitmap对象
     *
     * @param context
     * @param id
     * @return
     */
    private static Bitmap decodeResource(Context context, int id) {
        return decodeResource(context, id, SCALE_MEDIUM);
    }

    public static Bitmap decodeResource(Context context, int id, int scale) {
        InputStream is = context.getResources().openRawResource(id);
        return decodeStream(is, scale * REQUIRED_WIDTH);
    }

    /*
     * 得到图片字节流 数组大小
     * */
    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 得到缩放倍数
     *
     * @param o
     * @return
     */
    private static int getScale(BitmapFactory.Options o, int width) {
        if (width == 0)
            return 1;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < width)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        return scale;
    }

    private static int getScale(BitmapFactory.Options op, int width, int height) {
        if (width == 0 || height == 0) {
            return 1;
        }
        int originalWidth = op.outWidth;
        int originalHeight = op.outHeight;
        int inSampleSize = 1;
        if (originalWidth > width || originalHeight > height) {
            int halfWidth = originalWidth / 2;
            int halfHeight = originalHeight / 2;
            while ((halfWidth / inSampleSize > width)
                    &&(halfHeight / inSampleSize > height)) {
                inSampleSize *= 2;

            }
        }
        return inSampleSize;
    }
}
