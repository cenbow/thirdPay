package com.lhhy.pay.model;

public class WxpayConfig extends PayConfig {
	private String pay_url;// 微信支付统一接口(POST)
	private String pay_confirm_url;// 微信支付的确认地址
	private String pay_result_h5;// html5适用
	private String pay_result_h5_no_login;//
	private String appid;// 服务号的应用号
	private String app_secrect;// 服务号的应用密码
	private String token;// 服务号的配置token
	private String mch_id;// 商户号
	private String trade_type;// 交易类型
	private String api_key;// API密钥
	private String request_oauth2_url;// 请求oauth2授权的url
	private String redirect_uri_v1;// oauth2授权时回调页面
	private String token_url;// 获取token接口(GET)
	private String oauth2_url;// oauth2授权接口(GET)
	private String check_order_url;// 订单查询接口(POST)
	private String spbill_create_ip;// 支付终端IP
	private String wxpay_JSAPI_json_base = " \"appId\":\"APPID\",\n"
			+ " \"timeStamp\":\"TIMESTAMP\",\n"
			+ " \"nonceStr\":\"NONCESTR\",\n"
			+ " \"package\":\"prepay_id=PREPAY_ID\",\n"
			+ " \"signType\":\"MD5\",\n" + " \"paySign\":\"PAYSIGN\"\n";

	public String getPay_url() {
		return pay_url;
	}

	public void setPay_url(String pay_url) {
		this.pay_url = pay_url;
	}

	public String getPay_confirm_url() {
		return pay_confirm_url;
	}

	public void setPay_confirm_url(String pay_confirm_url) {
		this.pay_confirm_url = pay_confirm_url;
	}

	public String getPay_result_h5() {
		return pay_result_h5;
	}

	public void setPay_result_h5(String pay_result_h5) {
		this.pay_result_h5 = pay_result_h5;
	}

	public String getPay_result_h5_no_login() {
		return pay_result_h5_no_login;
	}

	public void setPay_result_h5_no_login(String pay_result_h5_no_login) {
		this.pay_result_h5_no_login = pay_result_h5_no_login;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getApp_secrect() {
		return app_secrect;
	}

	public void setApp_secrect(String app_secrect) {
		this.app_secrect = app_secrect;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	public String getRequest_oauth2_url() {
		return request_oauth2_url;
	}

	public void setRequest_oauth2_url(String request_oauth2_url) {
		this.request_oauth2_url = request_oauth2_url;
	}

	public String getRedirect_uri_v1() {
		return redirect_uri_v1;
	}

	public void setRedirect_uri_v1(String redirect_uri_v1) {
		this.redirect_uri_v1 = redirect_uri_v1;
	}

	public String getToken_url() {
		return token_url;
	}

	public void setToken_url(String token_url) {
		this.token_url = token_url;
	}

	public String getOauth2_url() {
		return oauth2_url;
	}

	public void setOauth2_url(String oauth2_url) {
		this.oauth2_url = oauth2_url;
	}

	public String getCheck_order_url() {
		return check_order_url;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public void setCheck_order_url(String check_order_url) {
		this.check_order_url = check_order_url;
	}

	public String getWxpay_JSAPI_json_base() {
		return wxpay_JSAPI_json_base;
	}

}
