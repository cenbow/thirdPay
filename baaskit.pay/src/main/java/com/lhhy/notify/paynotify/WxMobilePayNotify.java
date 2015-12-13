package com.lhhy.notify.paynotify;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lhhy.framework.utils.Utils;
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
public class WxMobilePayNotify implements PayNotify {

	@Autowired
	private OrderService orderService;
	
	@Override
	public NotifyResult doAfterSuccess(Order orderInfo,
			Map<String, Object> backParams) {
		WxpayConfig wxConfig = orderInfo.getWxConfig();
		//获取两个必然返回的参数
		String return_code = backParams.get("return_code").toString();
		Map<String,Object> respondMapx = Utils.newTreeMap();
		//判断通信结果
		if(!"SUCCESS".equalsIgnoreCase(return_code)){
			//如果通信出错
			respondMapx.put("return_code", "FAIL");
			respondMapx.put("return_msg" , "通信错误");
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("result", WxPayCommonUtil.getRequestXml(respondMapx));
			NotifyResult nResult =new NotifyResult();
			nResult.setData(result);
			nResult.setFailureUrl(wxConfig.getFall_url());
			nResult.setResult(false);
			return nResult;	
		}
		String out_trade_no = backParams.get("out_trade_no").toString();
		Order order = orderService.findOrderByOrderCode(out_trade_no);
		order.setPayStatus(payGlobal.PAY_STATUS_SUCCESS);
		
		orderService.updateOrderStatus(order);
		
		//订单支付逻辑
	//	respondMapx = orderUpdateService.wxPayLogic(params, ThirdConst.PAY_NOTIFY_ASYN, getIp());
		//返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", WxPayCommonUtil.getRequestXml(respondMapx));
		NotifyResult nResult =new NotifyResult();
		nResult.setData(result);
		nResult.setSuccessUrl(wxConfig.getSuccess_url());
		nResult.setResult(true);
		return nResult;	
	}
	
}
