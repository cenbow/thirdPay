package com.lhhy.notify.paynotify;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lhhy.notify.util.NotifyResult;
import com.lhhy.notify.util.PayNotify;
import com.lhhy.pay.model.Order;
import com.lhhy.pay.model.WxpayConfig;
import com.lhhy.pay.service.OrderService;
import com.lhhy.pay.util.payGlobal;
import com.lhhy.pay.util.wxpay.WxPayCommonUtil;

/**
 * 订单支付 业务实现
 * @author csk
 *
 */
public class WxPayNotify implements PayNotify {

	private static final Logger LOG = LoggerFactory.getLogger(WxPayNotify.class);
	
	@Autowired
	private OrderService orderService;
	
	@Override
	public NotifyResult doAfterSuccess(Order orderInfo,
			Map<String, Object> backParams) {
		
		NotifyResult result = new NotifyResult();
		WxpayConfig wxConfig = orderInfo.getWxConfig();
		
		String out_trade_no = backParams.get("out_trade_no").toString();
		
		//同步支付方式，支微信查询支付结果
		//根据微信交易号查询
		Map<String, Object> OrderQueryMap = WxPayCommonUtil.WxPayOrderQuery(out_trade_no, wxConfig);
		backParams.putAll(OrderQueryMap);
		
		//如订单存在且未支付正常，继续处理
		String result_code  = (String)backParams.get("result_code");
		
		//如果result_code为'SUCCESS'
		if("SUCCESS".equalsIgnoreCase(result_code)){
			//支付校验
			LOG.debug("微信订单状态正常out_trade_no:{}", out_trade_no);
			
			//1.校验签名，如通过下一步，否则直接返回错误
			Map<String,Object> paramsMap = new TreeMap<String,Object>(backParams);
			//TODO:区分JSAPI与APP的支付
			String tradeType = (String)backParams.get("trade_type");
			String vsign = "";
			String key = wxConfig.getApi_key();
			if("APP".equals(tradeType)){
				vsign = WxPayCommonUtil.createSign(paramsMap,key);
			} else {
				vsign = WxPayCommonUtil.createSign("UTF-8",paramsMap,key);
			}
			
			LOG.debug("vsign={}",vsign);
			
			//2.a 如果校验通过
			if((vsign!=null && vsign.equals( backParams.get("sign")))){
				
				/*
				 * 此处省略订单支付成功。进行跳转的代码
				 * 
				 */
				
				Order order = orderService.findOrderByOrderCode(out_trade_no);
				order.setPayStatus(payGlobal.PAY_STATUS_SUCCESS);
				
				orderService.updateOrderStatus(order);
				
				
				
				result.setResult(true);
				result.setSuccessUrl(wxConfig.getSuccess_url());
			}else{
				//2.b 签名校验失败
				result.setResult(false);
				result.setFailureUrl(wxConfig.getFall_url());
			}
		}else{
			//微信告知支付失败
			//2.b 签名校验失败
			result.setResult(false);
			result.setFailureUrl(wxConfig.getFall_url());
		}
		
		//2.b 签名校验失败
		result.setResult(false);
		result.setFailureUrl(wxConfig.getFall_url());
		return result;

	}
	
}
