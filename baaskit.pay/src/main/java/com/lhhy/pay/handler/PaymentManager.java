package com.lhhy.pay.handler;

import java.util.HashMap;
import java.util.Map;

import com.lhhy.pay.util.PayMethod;

public class PaymentManager {
	private static Map<PayMethod, Class<? extends PayHandler>> payServiceMap = new HashMap<PayMethod, Class<? extends PayHandler>>();

	/**
	 * 将默认的方式初始化管理，并提供 addPayService（）以方便运时添加
	 */
	static {
		// 支付宝，实现类
		addPayService(PayMethod.aliPay, AliPayHandler.class);
		
		addPayService(PayMethod.aliMobilePay, AliPayMobileHandler.class);
		// 财富通，实现类
		addPayService(PayMethod.wxPay, WxPayHandler.class);
	}

	/**
	 * 得到支付实例
	 * 
	 * @param payMethod
	 * @return
	 */
	public static PayHandler getPayRequestHandler(PayMethod payMethod) {
		try {
			return payServiceMap.get(payMethod).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 提供的对外接口,以方便运行时添加
	 * 
	 * @param payMethod
	 * @param payService
	 */
	public static void addPayService(PayMethod payMethod,
			Class<? extends PayHandler> payService) {
		payServiceMap.put(payMethod, payService);
	}

}
