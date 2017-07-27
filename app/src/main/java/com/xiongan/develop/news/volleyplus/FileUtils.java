package com.xiongan.develop.news.volleyplus;

import android.os.Environment;

import java.io.File;

public class FileUtils {

	public static final String FOLDER_ROOT    = Environment.getExternalStorageDirectory().toString();
	public static final String FOLDER_TEMP  = "OpenFin/Deal/image/temp";
	public static final String FOLDER_CACHE   = "OpenFin/Deal/image/cache";
	
	/**
	 * SDCard是否存在
	 * @return
	 */
	public static boolean isSDCardExist() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
	/**
	 * 判断文件或文件夹是否存在
	 * @param path
	 * @return
	 */
	private static boolean isFileExist(String path) {
		if (path == null || path.equals(""))
			return false;
		File file = new File(path);
		if (file.exists()) 
			return true;
		else {
			//file.mkdirs();
			return false;
		}
	}
	public static void isExistAndCreateFolder(String path) {
		if (path == null || path.equals(""))	return;
		File file = new File(path);
		if (file.exists() == false) 
			file.mkdirs();
	}
	public static void createFolderTemp() {
		isExistAndCreateFolder(getFolderTemp());
	}
	/**
	 * 临时图片存放地址
	 * @return
	 */
	public static String getFolderTemp() {
		String sdcardPath = FOLDER_ROOT +File.separator;
		return sdcardPath + FOLDER_TEMP;
	}
	/**
	 * 缓存图片存放地址
	 * @return
	 */
	public static String getFolderCache() {
		String sdcardPath = FOLDER_ROOT + File.separator;
		return sdcardPath + FOLDER_CACHE;
	}
	
	/**
	 * 清除 缓存文件
	 */
	public static void clearCache() {
		File file = new File(getFolderCache());
		deleteFoder(file);
	}
	/**
	 * 删除文件夹下的所有文件
	 * @param file	要删除的文件夹
	 */
	public static void deleteFiles(File file) {
		if (file!=null && file.exists()) {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				if (files != null) {
					for(int i=0; i < files.length; i++)
						deleteFoder(files[i]);
				}
			}
		}
	}
	/**
	 * 删除文件或文件夹
	 * @param file
	 * @return
	 */
	public static boolean deleteFoder(File file) {
        if (file.exists()) { // 判断文件是否存在
                if (file.isFile()) { // 判断是否是文件
                        file.delete(); // delete()方法 你应该知道 是删除的意思;
                } else if (file.isDirectory()) { // 否则如果它是一个目录
                        File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                        if (files != null) {
                                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                                        deleteFoder(files[i]); // 把每个文件 用这个方法进行迭代
                                }
                        }
                }
                boolean isSuccess = file.delete();
                if (!isSuccess) {
                        return false;
                }
        }
        return true;
	}
	/**
	 * 删除文件
	 * @param path
	 */
	public static void deleteFile(String path) {
		if (path==null) return;
		File file = new File(path);
		if (file.exists())
			file.delete();
	}
}
