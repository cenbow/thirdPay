package com.lhhy.notify.util;

import java.util.Map;

import com.lhhy.pay.model.Order;

/**
 * 支付请求成功返回后的业务处理接口
 */
public interface PayNotify {

	/**
	 * 在支付请求成功返回后，处理自己的回调业务
	 * 
	 * @param orderInfo
	 *            封装好的交易信息
	 * @param backParams
	 *            所有返回的参数的 Map,方便特殊的自定义处理
	 * @return CallBackResult，包括支付结果（true ||
	 *         false)，以及需要返回给调用端(Controller,页面)的参数,以及需要跳转的页面等。
	 */
	public NotifyResult doAfterSuccess(Order orderInfo,
			Map<String, Object> backParams);
}
