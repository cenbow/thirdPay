package com.lhhy.notify.paynotify;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lhhy.notify.util.NotifyResult;
import com.lhhy.notify.util.PayNotify;
import com.lhhy.pay.model.AlipayConfig;
import com.lhhy.pay.model.Order;
import com.lhhy.pay.service.OrderService;
import com.lhhy.pay.util.payGlobal;
import com.lhhy.pay.util.alipay.mobile.AlipayMobileNotify;

/**
 * 订单支付 业务实现
 * @author csk
 *
 */
public class AliMobilePayNotify implements PayNotify {

	private static final Logger LOG = LoggerFactory.getLogger(AliMobilePayNotify.class);
	
	@Autowired
	private OrderService orderService;
	
	@Override
	public NotifyResult doAfterSuccess(Order orderInfo,
			Map<String, Object> backParams) {

		AlipayConfig aliConfig = orderInfo.getAliConfig();
		
		boolean verify = AlipayMobileNotify.verify(backParams, aliConfig);
		LOG.debug("移动端异步通知验证：" + verify);
		if(verify){//验证成功
			LOG.debug("异步通知验证成功");
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			String trade_status = backParams.get("trade_status").toString();
			String out_trade_no = backParams.get("out_trade_no").toString();
			
			//TRADE_FINISHED表示交易完成
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				LOG.debug("异步通知：交易完成");
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				
			//	OrderTransing order = orderInfoService.findTotalStatusProductTypeByOrderCode(out_trade_no);
				Order order = orderService.findOrderByOrderCode(out_trade_no);
				order.setPayStatus(payGlobal.PAY_STATUS_SUCCESS);
				
				orderService.updateOrderStatus(order);
				
			//	orderInfoService.payLogi(order, out_trade_no, getIp(), ThirdConst.PAY_TYPE_ALI, out_trade_no, aliTradeNo);
				
				//发送短信
			//	sendSms(order, out_trade_no);
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				
				
				LOG.debug("success");	
				
				NotifyResult result= new NotifyResult();
				result.setResult(verify);
				return result;
			}else { //交易未完成
				LOG.debug("fail");
				NotifyResult result= new NotifyResult();
				result.setResult(verify);
				return result;
			}

			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			
		}else{//验证失败
			
			LOG.debug("fail");
			
			NotifyResult result= new NotifyResult();
			result.setResult(false);
			return result;
		}
	}
	
}
