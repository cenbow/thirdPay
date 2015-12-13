package com.lhhy.pay.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lhhy.pay.model.Order;
import com.lhhy.pay.model.WxpayConfig;
import com.lhhy.pay.util.PayMethod;
import com.lhhy.pay.util.wxpay.HttpsRequestHelper;
import com.lhhy.pay.util.wxpay.WxPayCommonUtil;

public class WxMobilePayHandler extends PayHandler implements PayMethod {


	private static final Logger LOG = LoggerFactory.getLogger(WxMobilePayHandler.class);
	
	/*
	 * 已经写好在helper类中，此处就不写了
	 * 
	 */
	@Override
	protected Map<String, Object> buildPayParam(Order order) {
		
		
		return null;
	}

	
	/*
	 * 已经写好在helper类中，此处就不写了
	 * 
	 */
	@Override
	protected Map<String, Object> doSign(Map<String, Object> params,Order order) {
		
		
		
		return null;
	}

	/**
	 * 请求微信获取预支付
	 */
	@Override
	protected String buildPaymentString(Map<String, Object> params, Order order) {
		//微信支付显示的商品名称
		WxpayConfig wxConfig = order.getWxConfig();
		final String postContext = WxPayCommonUtil.xmlData(order);
		LOG.debug("本地－〉向微信请求xml内容：{}}", postContext);
		//预支付
		final String res = HttpsRequestHelper.doHttpsRequest(wxConfig.getPay_url(), "POST", postContext);
		LOG.debug("微信->本地返回内容：{}", res);
						
		Map<String, String> result = WxPayCommonUtil.doXMLParse(res);
		LOG.debug("微信xml解析结果：{}", result);
		String prepayId = null;
				
		if("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code"))){//预支付成功
			prepayId= result.get("prepay_id");
			LOG.debug("订单号：{}， 获取微信预支付成功，返回prepay_id={}", order.getOut_trade_no(), prepayId);
		} else if("SUCCESS".equals(result.get("return_code")) && 
				 "OUT_TRADE_NO_USED".equals(result.get("err_code"))){ //有些订单号不能重复申请prepayId
			LOG.warn("微信返回：OUT_TRADE_NO_USED (订单号重复)");
		}
		return prepayId;
	}
	
}
