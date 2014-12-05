package com.lzhn.utils.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

public class FileUtils {
	private static final String TAG = "FileUtils";

	public static final int FILE_SELECT = 0xff;

	private static final int WHAT_PROGRESS_SHOW = 0xa0;

	private static final int WHAT_PROGRESS = 0xa1;

	private static final int WHAT_IOEXCEPTION = 0xe0;

	public static File getExternalStorageDirecory() {
		return Environment.getExternalStorageDirectory();
	}

	public static String getExternalStoragePath() {
		return getExternalStorageDirecory().getAbsolutePath();
	}

	/**
	 * 指定文件保存路径
	 * 
	 * @return
	 */
	public static String getDirectory(String directory) {
		File file = new File(directory);
		if (!file.exists())
			file.mkdirs();
		return file.getAbsolutePath() + File.separator;
	}

	/**
	 * 获取文件列表
	 * 
	 * @param directory
	 *            文件目录
	 * @param suffix
	 *            文件后缀（即文件格式）
	 * @return
	 */
	public static ArrayList<File> getListFiles(String directory, final String suffix) {
		File fileDirectory = new File(directory);
		FileFilter filter = new FileFilter() {// 过滤器
			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile()) {
					if (suffix != null && !pathname.getName().endsWith(suffix)) {
						return false;
					}
					return true;
				}
				return false;
			}
		};
		File[] files = fileDirectory.listFiles(filter);// 列出目录下文件
		ArrayList<File> fileList = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			fileList.add(files[i]);
		}
		return fileList;
	}

	/**
	 * 外部存储可写
	 * 
	 * @return
	 */
	public static boolean isExternalStorageWritable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 调用文件选择器来选择文件
	 * 
	 * @param Context
	 **/
	public static void showFileChooser(Context context) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			((Activity) context).startActivityForResult(
					Intent.createChooser(intent, "请选择一个的文件"), FILE_SELECT);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(context, "请安装文件管理器！", 0).show();
		}
	}

}
