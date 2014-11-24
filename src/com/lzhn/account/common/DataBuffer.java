package com.lzhn.account.common;

import java.util.ArrayList;
import java.util.List;

public class DataBuffer {
	private static List<Person> listPerson = new ArrayList<Person>();
	private static Person currentPerson;// 当前正在添加的person
	private static float totalMoney;
	private static float averageMoney;
	private static String time;

	public static void addPerson(Person person) {
		listPerson.add(person);
	}

	public static void removePerson(Person p) {
		listPerson.remove(p);
	}

	public static Person removePerson(String name) {
		for (Person p : listPerson) {
			if (p.getName().equals(name)) {
				listPerson.remove(p);
				return p;
			}
		}
		return null;
	}

	public static List<Person> getListPerson() {
		return listPerson;
	}

	public static void setListPerson(List<Person> listPerson) {
		DataBuffer.listPerson = listPerson;
	}

	public static float getTotalMoney() {
		return totalMoney;
	}

	public static void setTotalMoney(float totalMoney) {
		DataBuffer.totalMoney = totalMoney;
	}

	public static float getAverageMoney() {
		return averageMoney;
	}

	public static void setAverageMoney(float averageMoney) {
		DataBuffer.averageMoney = averageMoney;
	}

	public static String getTime() {
		return time;
	}

	public static void setTime(String time) {
		DataBuffer.time = time;
	}

	public static Person getCurrentPerson() {
		return currentPerson;
	}

	public static void setCurrentPerson(Person currentPerson) {
		DataBuffer.currentPerson = currentPerson;
	}

}
