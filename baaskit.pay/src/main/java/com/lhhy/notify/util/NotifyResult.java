package com.lhhy.notify.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 回调处理结果
 * @author csk
 *
 */
public class NotifyResult {
	private boolean result;
	private Map<String, Object> data = new HashMap<String, Object>();
	private String successUrl;
	private String failureUrl;

	/**
	 * 跳转至下一个处理流程
	 * 
	 * @return
	 */
	public String skipToNextProcess() {
		if (StringUtils.isEmpty(this.successUrl)
				|| StringUtils.isEmpty(this.failureUrl)) {
			throw new IllegalArgumentException(
					"没有设置待跳转的Url，不能进行跳转。请确保successUrl与failureUrl已被正确初始化");
		}
		if (success()) {
			/*
			 * 
			 * 支付成功 
			 * 
			 * 修改订单状态
			 * 
			 * 发送短信等等。。。。。
			 * 
			 */
			
			
			return this.successUrl;
		}
		return this.failureUrl;
	}

	public boolean success() {
		return this.isResult();
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	
	
	
	
}


