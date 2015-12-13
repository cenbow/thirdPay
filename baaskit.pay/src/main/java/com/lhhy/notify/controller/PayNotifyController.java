package com.lhhy.notify.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhhy.notify.handler.PayNotifyHandler;
import com.lhhy.notify.util.Global;
import com.lhhy.notify.util.NotifyResult;
import com.lhhy.pay.model.AlipayConfig;
import com.lhhy.pay.model.Order;
import com.lhhy.pay.model.WxpayConfig;
import com.lhhy.pay.service.OrderService;

/**
 * 接收同步和异步通知
 * 
 * @author csk
 *
 */
public class PayNotifyController {
	
	private static final Logger LOG = LoggerFactory.getLogger(PayNotifyController.class);
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 支付宝同步回调
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/return")
	public String normalReturn(HttpServletRequest request, Model model) {
		
		String out_trade_no = request.getParameter("out_trade_no");
		//此处省略根据订单编号查询订单的代码
		//..................
		
		Order order = orderService.findOrderByOrderCode(out_trade_no);
		
		order.setPaySource(Global.PAY_SRC_PC);
		order.setPayType(Global.PAY_TYPE_ALI);
		
		AlipayConfig aliConfig = new AlipayConfig();
		order.setAliConfig(aliConfig);
		
		PayNotifyHandler handler = new PayNotifyHandler(request, order);
		NotifyResult result = handler.handleCallback(order);
		model.addAllAttributes(result.getData());
		return result.skipToNextProcess();
	}

	/**
	 * 支付宝异步回调
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	/*@RequestMapping(value = "/notify") */
	public void notifyJump(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String out_trade_no = request.getParameter("out_trade_no");
		//此处省略根据订单编号查询订单的代码
		//..................
		
		Order order = orderService.findOrderByOrderCode(out_trade_no);
		
		order.setPaySource(Global.PAY_SRC_PC);
		order.setPayType(Global.PAY_TYPE_ALI);
		
		AlipayConfig aliConfig = new AlipayConfig();
		order.setAliConfig(aliConfig);
		
		PayNotifyHandler handler = new PayNotifyHandler(request, order);
		NotifyResult result = handler.handleCallback(order);
		response.getWriter().write(result.success() ? "success" : "fail");
	}
	
	/**
	 * 支付宝移动端异步回调
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	/*@RequestMapping(value = "/notify") */
	public void notifyMobileJump(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String out_trade_no = request.getParameter("out_trade_no");
		//此处省略根据订单编号查询订单的代码
		//..................
		
		Order order = orderService.findOrderByOrderCode(out_trade_no);
		
		AlipayConfig aliConfig = new AlipayConfig();
		order.setAliConfig(aliConfig);
		
		
		order.setPaySource(Global.PAY_SRC_MOBILE);
		order.setPayType(Global.PAY_TYPE_ALI);
		
		PayNotifyHandler handler = new PayNotifyHandler(request, order);
		NotifyResult result = handler.handleCallback(order);
		response.getWriter().write(result.success() ? "success" : "fail");
	}
	
	
	/**
	 * 微信同步回调
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/return")
	public String wxNormalReturn(HttpServletRequest request, Model model) {
		
		String out_trade_no = request.getParameter("out_trade_no");
		//此处省略根据订单编号查询订单的代码
		//..................
		
		Order order = orderService.findOrderByOrderCode(out_trade_no);
		
		order.setPaySource(Global.PAY_SRC_PC);
		order.setPayType(Global.PAY_TYPE_WX);
		
		WxpayConfig wxConfig = new WxpayConfig();
		order.setWxConfig(wxConfig);
		
		PayNotifyHandler handler = new PayNotifyHandler(request, order);
		NotifyResult result = handler.handleCallback(order);
		model.addAllAttributes(result.getData());
		return result.skipToNextProcess();
	}
	
	
	/**
	 * 微信异步回调
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	/*@RequestMapping(value = "/notify") */
	public String wxNotifyJump(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String out_trade_no = request.getParameter("out_trade_no");
		//此处省略根据订单编号查询订单的代码
		//..................
		
		Order order = orderService.findOrderByOrderCode(out_trade_no);
		
		order.setPaySource(Global.PAY_SRC_PC);
		order.setPayType(Global.PAY_TYPE_WX);
		
		WxpayConfig wxConfig = new WxpayConfig();
		order.setWxConfig(wxConfig);
		
		PayNotifyHandler handler = new PayNotifyHandler(request, order);
		NotifyResult result = handler.handleCallback(order);
	//	response.getWriter().write(result.success() ? "success" : "fail");
		
		
		String repondXML = result.getData().get("result").toString();
		LOG.debug("＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋支付通知(WxPayNotify)＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋");
		
		return repondXML;
	}
	

	
	public static void main(String[] args) {
		PayNotifyController p = new PayNotifyController();
		
		try {
			p.notifyJump(null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
