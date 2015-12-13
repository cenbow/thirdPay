package com.lhhy.pay.service;

import com.lhhy.pay.model.Order;

public interface OrderService {
	public boolean checkOrderInfo(String out_trade_no);
	
	
	public Order findOrderByOrderCode(String out_trade_no);
	
	
	public void updateOrderStatus(Order order);
}
