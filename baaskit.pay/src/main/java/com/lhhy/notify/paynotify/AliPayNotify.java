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
import com.lhhy.pay.util.alipay.AlipayNotify;

/**
 * 订单支付 业务实现
 * @author csk
 *
 */
public class AliPayNotify implements PayNotify {
	
	private static final Logger LOG = LoggerFactory.getLogger(AliPayNotify.class);
	
	@Autowired
	private OrderService orderService;
	
	@Override
	public NotifyResult doAfterSuccess(Order orderInfo,
			Map<String, Object> backParams) {
		AlipayConfig aliConfig = orderInfo.getAliConfig();
		String trade_status = backParams.get("trade_status").toString();
		String out_trade_no = backParams.get("out_trade_no").toString();
	//	String aliTradeNo = backParams.get("trade_no").toString();
		
		/*OrderTransing order = orderInfoService.findTotalStatusProductTypeByOrderCode(out_trade_no);
		if(order.getStatus()==2||order.getStatus()==12){
			LOG.debug("订单已经支付完成");
			url = "/alipay/payResult.jsp";
			request.setAttribute("orderCode", out_trade_no);
		}else{*/
			//计算得出通知验证结果
		
		Order order = orderService.findOrderByOrderCode(out_trade_no);
		if(order.getPayStatus() == payGlobal.PAY_STATUS_SUCCESS){
			
			return null;
		}
		
			boolean verify_result = AlipayNotify.verify(backParams, aliConfig);
			LOG.debug("支付宝同步验证：" + verify_result);
			if(verify_result){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码
				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——    
				LOG.debug("同步通知验证通过");
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
				//	orderInfoService.payLogi(order, out_trade_no, getIp(), ThirdConst.PAY_TYPE_ALI, out_trade_no, aliTradeNo);
					
					
					order.setPayStatus(payGlobal.PAY_STATUS_SUCCESS);
					
					orderService.updateOrderStatus(order);
					
					//发送短信
				//	sendSms(order, out_trade_no);
					
					LOG.debug("订单支付成功");
					NotifyResult result= new NotifyResult();
					result.setResult(verify_result);
					result.setSuccessUrl(aliConfig.getSuccess_url());
					return result;
				} else { //交易未完成
					LOG.debug("订单未支付完成");
					NotifyResult result= new NotifyResult();
					result.setResult(verify_result);
					result.setFailureUrl(aliConfig.getFall_url());
					return result;
				}
				
				//该页面可做页面美工编辑
				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				
			}else{
				//该页面可做页面美工编辑
				LOG.debug("验证失败");
				NotifyResult result= new NotifyResult();
				result.setResult(verify_result);
				result.setFailureUrl(aliConfig.getFall_url());
				return result;
			}
	
	}
}
