package com.lhhy.pay.handler;

import java.util.Map;

import com.lhhy.framework.utils.Utils;
import com.lhhy.pay.model.AlipayConfig;
import com.lhhy.pay.model.Order;
import com.lhhy.pay.util.PayMethod;
import com.lhhy.pay.util.alipay.AlipayRequest;
import com.lhhy.pay.util.alipay.mobile.AlipayMobileRequest;

/**
 * 支付宝移动端实现类
 * 
 * @author csk
 *
 */
public class AliPayMobileHandler extends PayHandler implements
		PayMethod {

	@Override
	protected Map<String, Object> buildPayParam(Order order) {
		
		AlipayConfig aliConfig = order.getAliConfig();
		//把请求参数打包成数组
		Map<String, Object> sParaTemp = Utils.newTreeMap();
				
		sParaTemp.put("service", aliConfig.getMobile_service());
		sParaTemp.put("partner", aliConfig.getPartner());
		sParaTemp.put("seller_id", aliConfig.getSeller_id());
		sParaTemp.put("_input_charset", aliConfig.getInput_charset());
		sParaTemp.put("payment_type", aliConfig.getPayment_type());
		sParaTemp.put("it_b_pay", aliConfig.getIt_b_pay());
		sParaTemp.put("notify_url", aliConfig.getNotify_url());
				
		sParaTemp.put("show_url", "m.alipay.com");//注：ios的例子里面不加本参数就报错，原因未知
		sParaTemp.put("out_trade_no", order.getOut_trade_no());
		sParaTemp.put("subject", order.getSubject());
		sParaTemp.put("total_fee", order.getTotal_fee());
		sParaTemp.put("body", order.getRemark());
		
		return sParaTemp;
	}


	@Override
	protected Map<String, Object> doSign(Map<String, Object> params,Order order) {
		Map<String, Object> sPara = AlipayMobileRequest.buildRequestPara(params, order.getAliConfig());
		return sPara;
	}

	@Override
	protected String buildPaymentString(Map<String, Object> params, Order order) {
		String requesthtml = AlipayRequest.buildRequest(params, "get", "确认", order.getAliConfig());
		return requesthtml;
	}

}
