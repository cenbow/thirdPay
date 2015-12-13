package com.lhhy.pay.util.alipay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lhhy.pay.model.AlipayConfig;
import com.lhhy.pay.util.alipay.sign.MD5;




/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayRequest {
    
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, Object> sPara, AlipayConfig aliConfig) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(aliConfig.getSign_type().equals("MD5") ) {
        	mysign = MD5.sign(prestr, aliConfig.getPrivate_key(), aliConfig.getInput_charset());
        }
        return mysign;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, Object> buildRequestPara(Map<String, Object> sParaTemp, AlipayConfig aliConfig) {
        //除去数组中的空值和签名参数
        Map<String, Object> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara, aliConfig);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", aliConfig.getSign_type());

        return sPara;
    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, Object> sParaTemp, String strMethod, String strButtonName, AlipayConfig aliConfig) {
        //待请求参数数组
        Map<String, Object> sPara = buildRequestPara(sParaTemp, aliConfig);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + aliConfig.getPay_gateway_new()
                      + "_input_charset=" + aliConfig.getInput_charset() + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

        return sbHtml.toString();
    }
    
    /**
     * 建立请求，以表单HTML形式构造，带文件上传功能
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @param strParaFileName 文件上传的参数名
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, Object> sParaTemp, String strMethod, String strButtonName, String strParaFileName, AlipayConfig aliConfig) {
        //待请求参数数组
        Map<String, Object> sPara = buildRequestPara(sParaTemp, aliConfig);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\"  enctype=\"multipart/form-data\" action=\"" + aliConfig.getPay_gateway_new()
                      + "_input_charset=" + aliConfig.getInput_charset() + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        
        sbHtml.append("<input type=\"file\" name=\"" + strParaFileName + "\" />");

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");

        return sbHtml.toString();
    }
    
    
    /**
     * 建立请求，返回Map列表
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static Map<String, Object> buildRequestParam(Map<String, Object> sParaTemp, AlipayConfig aliConfig) {
        //待请求参数数组
        Map<String, Object> sPara = buildRequestPara(sParaTemp, aliConfig);

        return sPara;
    }

}
