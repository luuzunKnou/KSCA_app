package com.luuzun.ksca.domain;

public class Schedule {
	private String code;
	private String offer;
	private String simpleDate;

	@Override
	public String toString() {
		return "Schedule{" +
				"code='" + code + '\'' +
				", offer='" + offer + '\'' +
				", simpleDate='" + simpleDate + '\'' +
				'}';
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public String getSimpleDate() {
		return simpleDate;
	}

	public void setSimpleDate(String simpleDate) {
		this.simpleDate = simpleDate;
	}
}
