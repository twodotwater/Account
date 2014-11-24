package com.zzha.utils.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class FileUtils {
	private static final String TAG = "FileUtils";

	public static final int FILE_SELECT = 0xff;
	private static final String FILEDIRECTORY = "";

	private static final int WHAT_PROGRESS_SHOW = 0xa0;

	private static final int WHAT_PROGRESS = 0xa1;

	private static final int WHAT_IOEXCEPTION = 0xe0;

	/**
	 * 指定文件保存路径
	 * 
	 * @return
	 */
	public static String getFilePath() {
		File file = new File(FILEDIRECTORY);
		if (!file.exists())
			file.mkdirs();
		return file.getAbsolutePath() + File.separator;
	}

	/**
	 * 获取文件列表
	 * 
	 * @param suffix
	 *            文件后缀（即文件格式）
	 * @return
	 */
	public static ArrayList<File> getFiles(final String suffix) {
		File directory = new File(getFilePath());
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
		File[] files = directory.listFiles(filter);// 列出目录下文件
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
	public boolean isExternalStorageWritable() {
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

	/**
	 * 向电脑传输文件
	 * 
	 * @param is
	 *            socket输入流
	 * @param os
	 *            socket输出流
	 * @param filePath
	 *            文件路径
	 * @param handler
	 *            handler发送消息，刷新进度条
	 */
	public static void uploadFile(InputStream is, OutputStream os,
			String filePath, Handler handler) {
		FileInputStream fis = null;
		byte[] buffer = new byte[1024];
		int len = 0;
		int progress = 0;
		int total = 0;
		try {
			Log.i(TAG, "--文件路径:" + filePath);
			File file = new File(filePath);
			total = (int) file.length();
			Log.i(TAG, "--文件大小:" + total);
			os.write(Integer.toString(total).getBytes("utf-8"));
			os.flush();
			os.write(file.getName().getBytes("utf-8"));
			os.flush();
			// 接收确认发送标记
			int resultCode = is.read();
			Log.i(TAG, "--resultCode:" + resultCode);
			if (resultCode == 1) {
				// 准备接收
				handler.obtainMessage(WHAT_PROGRESS_SHOW, progress, total)
						.sendToTarget();
				Thread.sleep(500);
				fis = new FileInputStream(file);
				while ((len = fis.read(buffer)) != -1) {
					os.write(buffer, 0, len);
					os.flush();
					progress += len;
					handler.obtainMessage(WHAT_PROGRESS, progress, total)
							.sendToTarget();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			handler.obtainMessage(WHAT_IOEXCEPTION, progress, total)
					.sendToTarget();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String saveFile(Context context, String name) {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + File.separator + "zzha" + File.separator + name);
		// 写入文件流
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write("zzha".getBytes());
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file.getAbsolutePath();

	}

}
