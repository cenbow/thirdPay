package com.lhhy.pay.model;

import com.lhhy.pay.util.PayMethod;

public class Order {
	
	private Integer paySource;
	
	private int payStatus;

	private String callBackClass;

	/**
	 * 交易流水号
	 */
	private String out_trade_no;
	
	private String subject;
	
	private double total_fee;//订单中的价格
	
	private String remark;
	
	private String userIp;
	
	private int payType;//支付方式
	
	private String code;//微信支付时
	
	private String state;//微信支付时

	private String ip;//微信移动端支付时
	
	private String deviceInfo;//微信移动端支付时
	
	private AlipayConfig aliConfig;
	
	private WxpayConfig wxConfig;
	
	/**
	 * 支付方式
	 */
	private PayMethod payMethod;
	
	
	public Integer getPayType() {
		return payType;
	}
	
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	
	public Integer getPaySource() {
		return paySource;
	}
	
	public void setPaySource(Integer paySource) {
		this.paySource = paySource;
	}
	
	public int getPayStatus() {
		return payStatus;
	}
	
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public String getCallBackClass() {
		return callBackClass;
	}

	public void setCallBackClass(String callBackClass) {
		this.callBackClass = callBackClass;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public double getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(double total_fee) {
		this.total_fee = total_fee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public PayMethod getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(PayMethod payMethod) {
		this.payMethod = payMethod;
	}

	public AlipayConfig getAliConfig() {
		return aliConfig;
	}

	public void setAliConfig(AlipayConfig aliConfig) {
		this.aliConfig = aliConfig;
	}

	public WxpayConfig getWxConfig() {
		return wxConfig;
	}

	public void setWxConfig(WxpayConfig wxConfig) {
		this.wxConfig = wxConfig;
	}
	
	
}
