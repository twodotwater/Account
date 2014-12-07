package com.yqy.account.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.lzhn.utils.file.FileUtils;
import com.lzhn.utils.print.ConvertUtils;
import com.lzhn.utils.print.LogUtils;

public class FileManager {

	private static final String FILE_DIRECTORY = "Account";
	private static final String FILE_SUFFIX = ".acc";

	public static String saveFile() {
		long cur = System.currentTimeMillis();
		String fileName = ConvertUtils.formatDate(ConvertUtils.PATTERN_8, cur)
				+ FILE_SUFFIX;
		String time = ConvertUtils.formatDate(ConvertUtils.PATTERN_6, cur);
		String filePath = FileUtils.getDirectory(FileUtils
				.getExternalStoragePath() + File.separator + FILE_DIRECTORY)
				+ fileName;
		LogUtils.printExcp(filePath, filePath);
		FileWriter fw = null;
		try {
			fw = new FileWriter(filePath);
			fw.append(time).append(" " + DataBuffer.getTotalMoney())
					.append(" " + DataBuffer.getAverageMoney());
			fw.append("\r\n");
			for (Person p : DataBuffer.getListPerson()) {
				fw.append(p.getName())
						.append(" " + Person.parseMoneyToString(p))
						.append(" " + p.getTotalMoney())
						.append(" " + p.getDiffMoney());
				fw.append("\r\n");
			}
			fw.flush();
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
