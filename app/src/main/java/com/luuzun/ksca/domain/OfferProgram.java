package com.luuzun.ksca.domain;

public class OfferProgram {
	private String code; 
	private String program;
	private String regMonth;
	private String simpleBeginDate;
	private String simpleEndDate;
	private String color;

	@Override
	public String toString() {
		return "OfferProgram{" +
				"code='" + code + '\'' +
				", program='" + program + '\'' +
				", regMonth='" + regMonth + '\'' +
				", simpleBeginDate='" + simpleBeginDate + '\'' +
				", simpleEndDate='" + simpleEndDate + '\'' +
				", color='" + color + '\'' +
				'}';
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getRegMonth() {
		return regMonth;
	}

	public void setRegMonth(String regMonth) {
		this.regMonth = regMonth;
	}

	public String getSimpleBeginDate() {
		return simpleBeginDate;
	}

	public void setSimpleBeginDate(String simpleBeginDate) {
		this.simpleBeginDate = simpleBeginDate;
	}

	public String getSimpleEndDate() {
		return simpleEndDate;
	}

	public void setSimpleEndDate(String simpleEndDate) {
		this.simpleEndDate = simpleEndDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
