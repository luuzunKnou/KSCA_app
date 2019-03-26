
package com.luuzun.ksca.domain;

import java.io.Serializable;

public class SCC implements Serializable {
	private String areaCode; 
	private String branchCode;
	private String sccCode;
	private String dong; 
	private String name; 
	private String address; 
	private String simpleRegDate;
	private float site;
	private float building;
	private int member;
	private int male;
	private int female;
	private String own;
	private String tel;
	private String president;
	private String phone ;

	@Override
	public String toString() {
		return "SCC{" +
				"areaCode='" + areaCode + '\'' +
				", branchCode='" + branchCode + '\'' +
				", sccCode='" + sccCode + '\'' +
				", dong='" + dong + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", simpleRegDate='" + simpleRegDate + '\'' +
				", site=" + site +
				", building=" + building +
				", member=" + member +
				", male=" + male +
				", female=" + female +
				", own='" + own + '\'' +
				", tel='" + tel + '\'' +
				", president='" + president + '\'' +
				", phone='" + phone + '\'' +
				'}';
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getSccCode() {
		return sccCode;
	}

	public void setSccCode(String sccCode) {
		this.sccCode = sccCode;
	}

	public String getDong() {
		return dong;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSimpleRegDate() {
		return simpleRegDate;
	}

	public void setSimpleRegDate(String simpleRegDate) {
		this.simpleRegDate = simpleRegDate;
	}

	public float getSite() {
		return site;
	}

	public void setSite(float site) {
		this.site = site;
	}

	public float getBuilding() {
		return building;
	}

	public void setBuilding(float building) {
		this.building = building;
	}

	public int getMember() {
		return member;
	}

	public void setMember(int member) {
		this.member = member;
	}

	public int getMale() {
		return male;
	}

	public void setMale(int male) {
		this.male = male;
	}

	public int getFemale() {
		return female;
	}

	public void setFemale(int female) {
		this.female = female;
	}

	public String getOwn() {
		return own;
	}

	public void setOwn(String own) {
		this.own = own;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPresident() {
		return president;
	}

	public void setPresident(String president) {
		this.president = president;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
