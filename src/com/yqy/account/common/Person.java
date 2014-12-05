package com.yqy.account.common;

import java.util.ArrayList;
import java.util.List;

public class Person {
	private String name;
	private List<Float> money;
	private float totalMoney;
	private float diffMoney;
	private String time;

	public Person(String name) {
		this(name, null);
	}

	public Person(String name, List<Float> money) {
		super();
		this.name = name;
		setMoney(money);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Float> getMoney() {
		return money;
	}

	public void setMoney(List<Float> money) {
		if (money == null)
			money = new ArrayList<Float>();

		this.money = money;
		this.totalMoney = calculateTotalMoney(money);
	}

	public void addMoney(List<Float> money) {
		this.money.addAll(money);
		this.totalMoney += calculateTotalMoney(money);
	}

	/**
	 * 计算总金额
	 * 
	 * @return
	 */
	public float getTotalMoney() {

		return Math.round(totalMoney);
	}

	// public void setTotalMoney(float totalMoney) {
	// this.totalMoney = totalMoney;
	// }

	public float getDiffMoney() {
		return diffMoney;
	}

	public void setDiffMoney(float diffMoney) {
		this.diffMoney = diffMoney;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static float calculateTotalMoney(List<Float> money) {
		float tMoney = 0;
		for (float m : money) {
			tMoney += m;
		}
		return tMoney;
	}

	public static String parseMoneyToString(Person p) {
		StringBuilder builder = new StringBuilder();
		for (float money : p.getMoney()) {
			builder.append(money + "+");
		}
		if (builder.length() > 1)
			return builder.substring(0, builder.length() - 1);

		return "0";
	}

	public static List<Float> parseStringToMoney(String moneyString) {
		List<Float> listMoney = new ArrayList<Float>();
		String[] strs = moneyString.split("\\+");
		for (int i = 0; i < strs.length; i++) {
			listMoney.add(Float.parseFloat(strs[i]));
		}
		return listMoney;
	}

	public static Person getPersonByName(List<Person> listPerson, String name) {
		for (Person person : listPerson) {
			if (person.getName().equals(name)) {
				return person;
			}
		}
		return null;
	}

	/**
	 * 获取person详细支付信息。Note：用于显示在详细信息对话框中
	 * 
	 * @param p
	 * @return
	 */
	public static String getPersonDetails(Person p) {
		StringBuilder builder = new StringBuilder();
		builder.append("姓名：" + p.getName()).append("\n");
		int count = 0;
		for (float money : p.getMoney()) {
			if (count % 5 == 0)
				builder.append("\n");
			builder.append(money + "+");
			count++;
		}
		if (builder.length() > 1) {
			builder.deleteCharAt(builder.length() - 1);
			builder.append("\n\n总金额：\t" + p.getTotalMoney() + "\t元\n");
			builder.append("\n差额：\t" + p.getDiffMoney() + "\t元\n");
		} else {
			builder.append("没有支付信息……\n");
		}
		return builder.toString();
	}
}
