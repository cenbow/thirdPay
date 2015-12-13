package com.lhhy.pay.model;

public class WxPayParams {
	private String appid;
	private String prepayid;
	private String partnerid;
	private String packageV;
	private String noncestr;
	private String timestamp;
	private String sign;
	
	public WxPayParams(String appid, String prepayid, String partnerid,
			String packageV, String noncestr, String timestamp, String sign) {
		this.appid = appid;
		this.prepayid = prepayid;
		this.partnerid = partnerid;
		this.packageV = packageV;
		this.noncestr = noncestr;
		this.timestamp = timestamp;
		this.sign = sign;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPackageV() {
		return packageV;
	}
	public void setPackageV(String packageV) {
		this.packageV = packageV;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
