
package com.luuzun.ksca.domain;

import java.io.Serializable;

public class SCC implements Serializable {
	private String areaCode; 
	private String branchCode;
	private String sccCode;
	private String dong; 
	private String name; 
	private String address; 
	private String regDate;
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
				", regDate='" + regDate + '\'' +
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

	public String getBranchCode() {
		return branchCode;
	}

	public String getSccCode() {
		return sccCode;
	}

	public String getDong() {
		return dong;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getRegDate() {
		return regDate;
	}

	public float getSite() {
		return site;
	}

	public float getBuilding() {
		return building;
	}

	public int getMember() {
		return member;
	}

	public int getMale() {
		return male;
	}

	public int getFemale() {
		return female;
	}

	public String getOwn() {
		return own;
	}

	public String getTel() {
		return tel;
	}

	public String getPresident() {
		return president;
	}

	public String getPhone() {
		return phone;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public void setSccCode(String sccCode) {
		this.sccCode = sccCode;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public void setSite(float site) {
		this.site = site;
	}

	public void setBuilding(float building) {
		this.building = building;
	}

	public void setMember(int member) {
		this.member = member;
	}

	public void setMale(int male) {
		this.male = male;
	}

	public void setFemale(int female) {
		this.female = female;
	}

	public void setOwn(String own) {
		this.own = own;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setPresident(String president) {
		this.president = president;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
