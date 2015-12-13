package com.lhhy.pay.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lhhy.framework.utils.Assert;
import com.lhhy.framework.utils.Utils;
import com.lhhy.pay.model.Order;
import com.lhhy.pay.model.WxpayConfig;
import com.lhhy.pay.util.PayMethod;
import com.lhhy.pay.util.wxpay.WxPayCommonUtil;

public class WxPayHandler extends PayHandler implements PayMethod {


	private static final Logger LOG = LoggerFactory.getLogger(WxPayHandler.class);
	
	@Override
	protected Map<String, Object> buildPayParam(Order order) {
		
		String state = order.getState();
		WxpayConfig wxConfig = order.getWxConfig();
		
		//1、根据code获得用户的openid
		String openid = WxPayCommonUtil.getOpenid(wxConfig, order.getCode());
		Assert.isEmpty(openid, "获取用户的openid失败。");
		LOG.debug("-----------------------获得的openid是={}-------------------", openid);
		//2、根据openid+配置信息订单号查询出来的订单信息+秘钥构造 预支付xml参数
		String 	notify_url          = wxConfig.getServer_address() + wxConfig.getNotify_url();
		LOG.debug("-----------------------获得的notify_url是={}-------------------", notify_url);
		String 	nonce_str           = WxPayCommonUtil.CreateNoncestr();//随机数
		String 	out_trade_no        = state;//这里订单号末尾+了12位，用于预防预支付id
		//以下參數与订单相关*
		Double total = order.getTotal_fee();
		Double total_fee = (total.doubleValue() * 100);
		
		//将参数构成map
		Map<String,Object> paramsMap = new TreeMap<String,Object>();
		paramsMap.put("appid", wxConfig.getAppid());
		paramsMap.put("openid", openid);
		paramsMap.put("mch_id", wxConfig.getMch_id());
		paramsMap.put("nonce_str", nonce_str.toLowerCase());
		paramsMap.put("body", order.getRemark());
		paramsMap.put("out_trade_no", out_trade_no);
		paramsMap.put("total_fee", Utils.double2fen(total_fee)); //修改转成整数，导致丢失精度的问题
		paramsMap.put("spbill_create_ip", wxConfig.getSpbill_create_ip());
		paramsMap.put("notify_url", notify_url);
		paramsMap.put("trade_type", wxConfig.getTrade_type());
		
		return paramsMap;
	}

	
	@Override
	protected Map<String, Object> doSign(Map<String, Object> params,Order order) {
		WxpayConfig wxConfig = order.getWxConfig();
		String key = wxConfig.getApi_key();
		//签名
		String 	sign = WxPayCommonUtil.createSign("UTF-8", params, key);
				
		params.put("sign", sign);
				
		String xml = WxPayCommonUtil.getRequestXml(params);
		LOG.debug("请求的xml为{}",xml);
				
		//3、访问统一下单api，取得prepayid
		String prepayid = WxPayCommonUtil.getPrepayid(xml, wxConfig.getPay_url());
		LOG.debug("-----------------------访问统一下单api，取得prepayid={}-------------------",prepayid);
				
		//4、根据prepayid等信息构造调用JSAPI支付需要的json数据
		Long timeStampLong = System.currentTimeMillis()/1000;
		String timeStamp = timeStampLong.toString();
		String nonceStr = WxPayCommonUtil.CreateNoncestr();
		String packageStr = "prepay_id="+ prepayid;
				
		Map<String,Object> payparamsMap = new TreeMap<String,Object>();
		payparamsMap.put("appId", params.get("appId"));
		payparamsMap.put("timeStamp", timeStamp);
		payparamsMap.put("nonceStr", nonceStr);
		payparamsMap.put("package", packageStr);
		payparamsMap.put("signType","MD5");
				
		//签名
		String paySign  = WxPayCommonUtil.createSign("UTF-8", payparamsMap, key);
		
		
		Map<String,Object> resultMap = new TreeMap<String,Object>();
		resultMap.put("appId", payparamsMap.get("appId"));
		resultMap.put("prepayid", prepayid);
		resultMap.put("mch_id", params.get("mch_id"));
		resultMap.put("packageStr", packageStr);
		resultMap.put("nonceStr", nonceStr);
		resultMap.put("timeStamp", timeStamp);
		resultMap.put("sign", paySign);
		
		return resultMap;
	}

	/**
	 * 请求微信获取预支付
	 */
	@Override 
	protected String buildPaymentString(Map<String, Object> params, Order order) {
		
		WxpayConfig wxConfig = order.getWxConfig();
		//构造返回串
		String JSAPI_Pay_params = wxConfig.getWxpay_JSAPI_json_base().
									replace("APPID", params.get("appId").toString()).
									replace("TIMESTAMP", params.get("timeStamp").toString()).
									replace("NONCESTR", params.get("nonceStr").toString()).
									replace("PREPAY_ID", params.get("prepayid").toString()).
									replace("PAYSIGN", params.get("sign").toString());
		
		//5、将数据返回到页面给到客户端，等待支付
		Map<String, String > PayParamsMap = new HashMap<String, String>();
		PayParamsMap.put("JSAPI_Pay_params",JSAPI_Pay_params);
		PayParamsMap.put("od", order.getState());//out_trade_no
		
		String resultUrl = wxConfig.getServer_address() +
				wxConfig.getPay_confirm_url()+
					"?JSAPI_Pay_params=" + JSAPI_Pay_params +
					"&orderCode=" + order.getState() + 
					"&token=" + wxConfig.getToken(); 

		return resultUrl;
	}
	
}
