package com.lzhn.account.common;

import java.util.Arrays;
import java.util.List;

public class Account {
	private String time;
	private List<String> listName;
	private float totalMoney;

	// private float averageMoney;

	public Account(String time, List<String> listName, float totalMoney) {
		super();
		this.time = time;
		this.listName = listName;
		this.totalMoney = totalMoney;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<String> getListName() {
		return listName;
	}

	public void setListName(List<String> listName) {
		this.listName = listName;
	}

	public float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public static String getNamesInList(List<Person> listPerson) {
		StringBuilder builder = new StringBuilder();
		for (Person p : listPerson) {
			builder.append(p.getName() + ",");
		}
		if (builder.length() < 1)
			return "";
		return builder.deleteCharAt(builder.length() - 1).toString();
	}

	public static List<String> parseStringToNames(String nameString) {
		String[] names = nameString.split(",");
		return Arrays.asList(names);
	}
}
