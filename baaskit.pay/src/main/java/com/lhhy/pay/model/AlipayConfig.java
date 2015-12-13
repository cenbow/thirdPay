package com.lhhy.pay.model;

public class AlipayConfig extends PayConfig {
//	private String pay_interface;//支付宝交易接口
	
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串 注：签约的支付宝账号对应的支付宝唯一用户号。
	private String partner;
	
	// 收款支付宝唯一数字账号
	private String seller_id;
	
	// 收款支付宝账号
	private String seller_email;
	
	// 支付类型
	private String payment_type;
	
	// 订单超时时间
	private String it_b_pay;
	
	// 支付宝扫码方式
	private String qr_pay_mode;
	
	// 字符编码格式 目前支持 gbk 或 utf-8
	private String input_charset = "utf-8";
	
	// 商户的私钥
	private String private_key;
	
	// 支付宝的公钥
	private String public_key;
	
	
    //支付宝提供给商户的服务接入网关URL(新)
	private String pay_gateway_new = "https://mapi.alipay.com/gateway.do?";

	//支付宝消息验证地址
	private String https_verify_url = "https://mapi.alipay.com/gateway.do?service=notify_verify&";
	
	// 接口名称。 必填
	// 注：即时到账交易接口、纯网关接口、大额信用卡create_direct_pay_by_user接口、快捷支付前置接口
	private String service = "create_direct_pay_by_user";
	
	// PC签名方式
	private String sign_type = "MD5";
	
	// 移动端接口名称。 必填
	private String mobile_service = "mobile.securitypay.pay";
	
	// 移动端签名方式 
	private String mobile_sign_type = "RSA";
	
	
	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getIt_b_pay() {
		return it_b_pay;
	}

	public void setIt_b_pay(String it_b_pay) {
		this.it_b_pay = it_b_pay;
	}

	public String getQr_pay_mode() {
		return qr_pay_mode;
	}

	public void setQr_pay_mode(String qr_pay_mode) {
		this.qr_pay_mode = qr_pay_mode;
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getPublic_key() {
		return public_key;
	}

	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}

	public String getPay_gateway_new() {
		return pay_gateway_new;
	}

	public String getHttps_verify_url() {
		return https_verify_url;
	}

	public String getService() {
		return service;
	}

	public String getSign_type() {
		return sign_type;
	}

	public String getMobile_service() {
		return mobile_service;
	}

	public String getMobile_sign_type() {
		return mobile_sign_type;
	}
	
	
	
}
