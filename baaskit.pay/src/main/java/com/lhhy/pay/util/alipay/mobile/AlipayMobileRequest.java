package com.lhhy.pay.util.alipay.mobile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lhhy.pay.model.AlipayConfig;
import com.lhhy.pay.util.alipay.AlipayCore;
import com.lhhy.pay.util.alipay.sign.RSA;


public class AlipayMobileRequest {
	
	private static final Logger LOG = LoggerFactory.getLogger(AlipayMobileRequest.class);
	 /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    public static Map<String, Object> buildRequestPara(Map<String, Object> sParaTemp, AlipayConfig aliConfig) {
        //除去数组中的空值和签名参数
        Map<String, Object> sPara = AlipayCore.paraFilter(sParaTemp);
     
        //生成签名结果
        String mysign = buildRequestMysign(sPara, aliConfig);
        LOG.debug("对sign做URL编码:");
        try {
        	mysign = URLEncoder.encode(mysign, "UTF-8");
			LOG.debug(mysign);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", aliConfig.getMobile_sign_type());

        return sPara;
    }
    
    
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, Object> sPara, AlipayConfig aliConfig) {
    	String prestr = AlipayCore.createAlipayMobileLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	
    	LOG.debug("签名前的字符串数据：");
        LOG.debug(prestr);
        
        String mysign = "";
        if(aliConfig.getMobile_sign_type().equals("RSA") ) {
        	mysign = RSA.sign(prestr, aliConfig.getPrivate_key(), aliConfig.getInput_charset());
        }
        LOG.debug("签名后的数据：");
        LOG.debug(mysign);
        return mysign;
    }
}
