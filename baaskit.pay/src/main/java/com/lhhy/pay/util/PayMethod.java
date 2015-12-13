package com.lhhy.pay.util;

import com.lhhy.pay.handler.AliPayMobileHandler;
import com.lhhy.pay.handler.AliPayHandler;
import com.lhhy.pay.handler.WxMobilePayHandler;
import com.lhhy.pay.handler.WxPayHandler;

public interface PayMethod {
	public PayMethod aliPay = new AliPayHandler();
	
	public PayMethod aliMobilePay = new AliPayMobileHandler();

	public PayMethod wxPay = new WxPayHandler();
	
	public PayMethod wxMobilePay = new WxMobilePayHandler();
}
