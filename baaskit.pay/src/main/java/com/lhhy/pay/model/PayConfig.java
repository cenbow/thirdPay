package com.lhhy.pay.model;

public class PayConfig {
	private String server_address;//服务器IP
	private String return_url;//同步回调URL
	private String notify_url;//异步回调URL
	private String success_url;//同步支付成功时，跳转地址
	private String fall_url;//同步支付失败时，跳转地址
	
//	private String sign_type;//加密方式
	
	
	public String getServer_address() {
		return server_address;
	}
	
	public void setServer_address(String server_address) {
		this.server_address = server_address;
	}
	
	public String getReturn_url() {
		return return_url;
	}
	
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	
	public String getNotify_url() {
		return notify_url;
	}
	
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	
	public String getSuccess_url() {
		return success_url;
	}
	
	public void setSuccess_url(String success_url) {
		this.success_url = success_url;
	}
	
	public String getFall_url() {
		return fall_url;
	}
	
	public void setFall_url(String fall_url) {
		this.fall_url = fall_url;
	}
	
}
