package com.lhhy.pay.util.wxpay;

/**
 * 微信基础访问凭证ＪＢＯ
 *
 */
public class WxAccessToken {

	private String accessToken;
	private int expiresIn;
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
		
	}

	public String getAccessToken( ) {
		 return this.accessToken;
	}

	public int getExpiresIn( ) {
		return this.expiresIn ;
		
	}

}
