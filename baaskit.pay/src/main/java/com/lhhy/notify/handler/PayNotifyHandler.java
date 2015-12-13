package com.lhhy.notify.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lhhy.notify.paynotify.AliMobilePayNotify;
import com.lhhy.notify.paynotify.AliPayNotify;
import com.lhhy.notify.paynotify.WxMobilePayNotify;
import com.lhhy.notify.paynotify.WxPayNotify;
import com.lhhy.notify.util.Global;
import com.lhhy.notify.util.NotifyResult;
import com.lhhy.notify.util.PayNotify;
import com.lhhy.pay.model.Order;
import com.lhhy.pay.util.wxpay.XMLparse;

public class PayNotifyHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(PayNotifyHandler.class);

	// 送过去的交易回调处理类
	private PayNotify notifyHandler;

	// 所有返回的参数，原样封装
	private Map<String, Object> backParams;

	
	public PayNotifyHandler(HttpServletRequest request, Order order) {
		try {
			if (order.getPayType() == Global.PAY_TYPE_ALI) {
				if(order.getPaySource() == Global.PAY_SRC_MOBILE){
					this.notifyHandler = AliMobilePayNotify.class.newInstance();
				}else{
					this.notifyHandler = AliPayNotify.class.newInstance();
				}
				this.backParams = buildParam(request);
			}
			if (order.getPayType() == Global.PAY_TYPE_WX) {
				if(order.getPaySource() == Global.PAY_SRC_MOBILE){
					this.notifyHandler = WxMobilePayNotify.class.newInstance();
				}else{
					this.notifyHandler = WxPayNotify.class.newInstance();
				}
				this.backParams = buildWxParam(request);
			}
			
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	
	public NotifyResult handleCallback(Order orderInfo) {
		NotifyResult result;
		try {
			result = notifyHandler.doAfterSuccess(orderInfo, this.backParams);
			return result;
		} catch (Exception e) {
			LOG.error("支付返回处理时发生了错误", e);
			e.printStackTrace();
		}
		result = new NotifyResult();
		result.setResult(false);
		return result;
	}

	
	private Map<String, Object> buildParam(HttpServletRequest request) {
		
		// 获取GET过来反馈信息
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (String name : requestParams.keySet()) {
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}
	
	
	private Map<String, Object> buildWxParam(HttpServletRequest request){
		try {
			//获取输入流
			InputStream inputStream = request.getInputStream();
			
			// 从输入流读取返回内容
			InputStreamReader inputStreamReader = null;
			
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
		
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			
			String notify_lines = buffer.toString();
			Map<String, Object> paramsMapFromXML = XMLparse.doXMLParse(notify_lines);
			
			if(paramsMapFromXML != null){
				LOG.debug("paramsMapFromXML:{}",paramsMapFromXML);
			}else{
				LOG.info("提取参数错误");
				LOG.debug("提取参数错误",paramsMapFromXML);
			}
			LOG.info("校验及处理业务");
			return paramsMapFromXML;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return null;
	}
	
}
