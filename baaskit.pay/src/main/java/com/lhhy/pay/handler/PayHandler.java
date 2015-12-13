package com.lhhy.pay.handler;

import java.util.Map;

import com.lhhy.pay.model.Order;

public abstract class PayHandler {

	public final String forwardToPay(Order order) {
		Map<String, Object> params = buildPayParam(order);
		
		// 签名，并追加签名参数
		Map<String, Object>	paramsMap = doSign(params,order);
		
		return buildPaymentString(paramsMap, order);
	}

	protected abstract Map<String, Object> buildPayParam(Order order);

	protected abstract Map<String, Object> doSign(Map<String, Object> params, Order order);
	
	protected abstract String buildPaymentString(Map<String, Object> params, Order order);
}
