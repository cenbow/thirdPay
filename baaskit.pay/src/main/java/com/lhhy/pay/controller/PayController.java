package com.lhhy.pay.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhhy.framework.core.IDGenerator;
import com.lhhy.framework.utils.Md5;
import com.lhhy.framework.utils.SignUtils;
import com.lhhy.framework.utils.Utils;
import com.lhhy.notify.paynotify.AliMobilePayNotify;
import com.lhhy.notify.paynotify.AliPayNotify;
import com.lhhy.notify.paynotify.WxMobilePayNotify;
import com.lhhy.notify.paynotify.WxPayNotify;
import com.lhhy.pay.handler.PayHandler;
import com.lhhy.pay.handler.PaymentManager;
import com.lhhy.pay.model.AlipayConfig;
import com.lhhy.pay.model.Order;
import com.lhhy.pay.model.WxpayConfig;
import com.lhhy.pay.service.OrderService;
import com.lhhy.pay.util.PayMethod;
import com.lhhy.pay.util.payGlobal;
import com.lhhy.pay.util.wxpay.WxPayCommonUtil;

public class PayController {

	private static final Logger LOG = LoggerFactory.getLogger(PayController.class);
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 支付宝PC端发起支付请求
	 * @return
	 */
	@RequestMapping("/testAli")
	public String testAliPay() {
		Order order = new Order();
		order.setOut_trade_no("NO01");
		order.setTotal_fee(99.99);
		order.setCallBackClass(AliPayNotify.class.getName());
		order.setPayMethod(PayMethod.aliPay);
		
		order.setPayType(payGlobal.PAY_TYPE_ALI_PC);

		AlipayConfig aliConfig = new AlipayConfig();
		order.setAliConfig(aliConfig);
		
		PayHandler handler = PaymentManager.getPayRequestHandler(order
				.getPayMethod());
		
		String temp = handler.forwardToPay(order);
	//	model.addAttribute("form", temp);
		return temp;
	}
	
	
	/**
	 * 支付宝移动端发起支付请求
	 * @return
	 */
	@RequestMapping("/testMobile")
	public String testMobile(){
		Order order = new Order();
		order.setOut_trade_no("NO01");
		order.setTotal_fee(99.99);
		order.setSubject("sss");
		order.setCallBackClass(AliMobilePayNotify.class.getName());
		order.setPayMethod(PayMethod.aliMobilePay);
		order.setPayType(payGlobal.PAY_TYPE_ALI_MOBILE);
		order.setRemark("测试测试");
		
		AlipayConfig aliConfig = new AlipayConfig();
		order.setAliConfig(aliConfig);
		
		PayHandler handler = PaymentManager.getPayRequestHandler(order
				.getPayMethod());
		
		String temp = handler.forwardToPay(order);
		return temp;
	}
	
	
	/**
	 * 请求微信获取openid
	 * 
	 * 注：微信发起支付请求前，需要向微信获取用户的 唯一标识
	 * 
	 * @param orderCode
	 * @param token
	 */
	@RequestMapping("/requestWxOpenId")
	public void requestWxOpenId(String orderCode, String token){
		
		/*
		 * 此处省略判断订单状态等代码
		 * 
		 * 
		 */
		
		Order order = new Order();
		
		AlipayConfig aliConfig = new AlipayConfig();
		order.setAliConfig(aliConfig);
		
		
		String resultUrl = WxPayCommonUtil.gotoRequest(order);
		
		try {
			this.response.sendRedirect(resultUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 微信PC端支付请求
	 * @param code
	 * @param state
	 */
	@RequestMapping("/testWxPay")
	public void testWxPay(String code,String state) {
		Order order = new Order();
		order.setOut_trade_no("NO01");
		order.setTotal_fee(99.99);
		order.setCallBackClass(WxPayNotify.class.getName());
		order.setPayMethod(PayMethod.wxPay);
		
		//取出token部分
		String token = state.substring(state.indexOf(":") + 1);
		//去掉token部分
		state = state.substring(0, state.indexOf(":"));
		
		order.setCode(code);
		order.setState(state);
		
		order.setPayType(payGlobal.PAY_TYPE_WX_PC);
		
		WxpayConfig wxConfig = new WxpayConfig();
		wxConfig.setToken(token);
		order.setWxConfig(wxConfig);

		PayHandler handler = PaymentManager.getPayRequestHandler(order
				.getPayMethod());
		
		String resultUrl = handler.forwardToPay(order);
		
		try {
			this.response.sendRedirect(resultUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 微信移动端支付请求
	 * @param orderNo
	 * @param ip
	 * @param deviceInfo
	 */
	public void testWxAppPay(String orderNo, String ip, String deviceInfo){
		/*
		 * 此处省略订单状态查询，判断等代码。。
		 * 。。。。。。
		 */
		
		
		Order order = new Order();
		order.setTotal_fee(99.99);
		order.setCallBackClass(WxMobilePayNotify.class.getName());
		order.setPayMethod(PayMethod.wxMobilePay);
		order.setRemark("a s s test  ");
		
		orderNo = orderNo + IDGenerator.getID12();
		
		order.setOut_trade_no(orderNo);
		order.setIp(ip);
		order.setDeviceInfo(deviceInfo);
		
		order.setPayType(payGlobal.PAY_TYPE_WX_MOBILE);
		
		WxpayConfig wxConfig = new WxpayConfig();
		order.setWxConfig(wxConfig);
		
		PayHandler handler = PaymentManager.getPayRequestHandler(order
				.getPayMethod());
		
		String prepayId = handler.forwardToPay(order);
		
		if(StringUtils.isNotBlank(prepayId)){
			//预支付ID获取成功
			Map<String, String> map = Utils.newTreeMap();
			map.put("appid", wxConfig.getAppid());
			map.put("partnerid", wxConfig.getMch_id());
			map.put("prepayid", prepayId);
			map.put("package", "Sign=WXPay");
			map.put("noncestr", WxPayCommonUtil.CreateNoncestr());
			map.put("timestamp", (System.currentTimeMillis() / 1000) + ""); //时间戳
			String bizMap = SignUtils.map2String(map);
			final String sign = Md5.encoderByMd5(bizMap + "&key=" + wxConfig.getApi_key());
			map.put("sign", sign.toUpperCase());
			//讲package换成package_str
			map.remove("package");
			map.put("package_str", "Sign=WXPay");
			
		//	return new Result(ResultType.OK, "获取成功", map);
		}else{
			LOG.warn("微信预支付失败：");
		//	return new Result(ResultType.ERR, "预支付失败，请稍后重试");
		}
		
	}
	
	
	
	public static void main(String[] args) {
		PayController p = new PayController();
		String tt = p.testAliPay();
		System.out.println(tt);
	}
	
}
