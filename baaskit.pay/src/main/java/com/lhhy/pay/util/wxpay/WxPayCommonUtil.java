package com.lhhy.pay.util.wxpay;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.lhhy.framework.core.IDGenerator;
import com.lhhy.framework.utils.Md5;
import com.lhhy.framework.utils.SignUtils;
import com.lhhy.framework.utils.Utils;
import com.lhhy.pay.model.Order;
import com.lhhy.pay.model.WxpayConfig;
import com.lhhy.pay.util.wxpay.sign.WxPaySign;

public class WxPayCommonUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(WxPayCommonUtil.class);
	
	
	public static String CreateNoncestr(int length) {
		// String chars =
		// "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		// StringBuilder res = new StringBuilder();
		// Random rd = new Random();
		// for (int i = 0; i < length; i++) {
		// res += chars.indexOf(rd.nextInt(chars.length() - 1));
		// }
		Long ctx = System.currentTimeMillis();
		return ctx.toString();
	}

	public static String CreateNoncestr() {

		return CreateNoncestr(16);
	}

	/**
	 * @Description：sign签名
	 * @param characterEncoding
	 *            编码格式
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String createSign(String characterEncoding,
			Map<String, Object> parameters,String key) {
		StringBuilder sb = new StringBuilder();
		Set<?> es = parameters.entrySet();
		Iterator<?> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = WxPaySign.MD5Encode(sb.toString(), characterEncoding)
				.toUpperCase();
		return sign;
	}
	
	
	public static String createSign(Map<String, Object> map,String key){
		if (null == map || map.isEmpty()) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if(!"sign".equals(entry.getKey())){
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(entry.getKey()).append("=").append(entry.getValue());
			}
		}
		sb.append("&key=").append(key);
		return Md5.encoderByMd5(sb.toString()).toUpperCase();
	}

	/**
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getRequestXml(Map<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<?> es = parameters.entrySet();
		Iterator<?> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			// String v = (String)entry.getValue();
			Object v = entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
					|| "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		// 这一步最关键 我们把字符转为 字节后,再使用“ISO8859-1”进行编码，得到“ISO8859-1”的字符串
		try {
			return sb.toString();
			// return new String(sb.toString().getBytes(), "ISO8859-1");
			// //该返回APP支付适用
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @Description：返回给微信的参数
	 * @param return_code
	 *            返回编码
	 * @param return_msg
	 *            返回信息
	 * @return
	 */
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	/**
	 * 获取prepayid
	 * 
	 * @param xmlParams
	 *            xml参数
	 * @return
	 */
	public static String getPrepayid(String xmlParams,String payUrl) {
		String prepayid = null;

		// 发起POST请求
		Map<String, Object> mapFromXML = null;
		try {
			mapFromXML = XMLparse.doXMLParse(HttpsRequestHelper.doHttpsRequest(
					payUrl, "POST", xmlParams));
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (null != mapFromXML) {
			// MyLogger.info(MyLogger.printMap(mapFromXML));

			prepayid = (String) mapFromXML.get("prepay_id");
			if (prepayid == null) {
				// 获取失败
				// MyLogger.info("获取失败 errcode:{} errmsg:{}" +
				// mapFromXML.get("return_code") +
				// mapFromXML.get("return_msg"));
				throw new RuntimeException("订单号错误");
			}

		}
		return prepayid;
	}

	/**
	 * 微信查询订单状态
	 * 
	 * @param Map
	 *            <String,String
	 * @return
	 */
	public static Map<String, Object> WxPayOrderQuery(String out_trade_no,WxpayConfig wxConfig) {

		String nonce_str = WxPayCommonUtil.CreateNoncestr();// 随机数
		// 将参数构成map
		Map<String, Object> paramsMap = Utils.newTreeMap();
		paramsMap.put("appid", wxConfig.getAppid());
		paramsMap.put("mch_id", wxConfig.getMch_id());
		paramsMap.put("nonce_str", nonce_str.toLowerCase());
		paramsMap.put("out_trade_no", out_trade_no);

		String sign = WxPayCommonUtil.createSign("UTF-8", paramsMap, wxConfig.getApi_key());

		paramsMap.put("sign", sign);

		String xmlParams = WxPayCommonUtil.getRequestXml(paramsMap);

		// 发起POST请求
		Map<String, Object> mapFromXML = null;
		try {
			mapFromXML = XMLparse.doXMLParse(HttpsRequestHelper.doHttpsRequest(
					wxConfig.getCheck_order_url(), "POST", xmlParams));
		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (null != mapFromXML) {
			//MyLogger.info(MyLogger.printMap(mapFromXML));

			String return_code = (String) mapFromXML.get("return_code");
			if (return_code == null) {
				// 获取失败
				/*MyLogger.info("获取失败 errcode:{} errmsg:{}"
						+ mapFromXML.get("return_code")
						+ mapFromXML.get("return_msg"));*/
				throw new RuntimeException("订单号错误");
			}

		}
		return mapFromXML;

	}
	
	public static String FormatBizQueryParaMap(Map<String, String> paraMap,
			boolean urlencode) {

		String buff = "";
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
					paraMap.entrySet());

			Collections.sort(infoIds,
					new Comparator<Map.Entry<String, String>>() {
						public int compare(Map.Entry<String, String> o1,
								Map.Entry<String, String> o2) {
							return (o1.getKey()).toString().compareTo(
									o2.getKey());
						}
					});

			for (int i = 0; i < infoIds.size(); i++) {
				Map.Entry<String, String> item = infoIds.get(i);
				// System.out.println(item.getKey());
				if (item.getKey() != "") {

					String key = item.getKey();
					String val = item.getValue();
					if (urlencode) {
						val = URLEncoder.encode(val, "utf-8");

					}
					buff += key.toLowerCase() + "=" + val + "&";

				}
			}

			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return buff;
	}
	
	public static Map<String, String> doXMLParse(String xml) {

		Map<String, String> map = new HashMap<String, String>();
		// 将编码改为UTF-8,并去掉换行符\空格等
		xml = xml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
		//去掉空白 换行符
		final StringBuilder sb = new StringBuilder(xml.length());
		char c;
		for(int i = 0; i < xml.length(); i++){
			c = xml.charAt(i);
			if(c != '\n' && c != '\r' && c != ' '){
				sb.append(c);
			}
		}
		xml = sb.toString();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			StringReader reader = new StringReader(xml);
			InputSource inputSource = new InputSource(reader);
			Document document = documentBuilder.parse(inputSource);
			// 1.获取xml文件的根元素
			Element element = document.getDocumentElement();
			// 2.获取根元素下的所有子标签
			NodeList nodeList = element.getChildNodes();
			// 3.遍历子标签集合
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				map.put(node.getNodeName(), node.getFirstChild().getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	
	/**
	 * 获取接口访问凭证
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static WxAccessToken getToken(WxpayConfig wxConfig) {
		WxAccessToken token = null;
		String requestUrl = wxConfig.getToken_url().replace("APPID", wxConfig.getAppid()).replace("APPSECRET", wxConfig.getApp_secrect());
		// 发起GET请求获取凭证
		JSONObject jsonObject = new JSONObject(HttpsRequestHelper.doHttpsRequest(requestUrl, "GET", null));

		if (null != jsonObject) {
			try {
				token = new WxAccessToken();
				token.setAccessToken(jsonObject.getString("access_token"));
				token.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				token = null;
				// 获取token失败
				//MyLogger.info("获取token失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return token;
	}
	
	
	public static String urlEncodeUTF8(String source){
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 获取openid
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @param WxOpenidCode 上一步获得的code
	 * @return
	 */
	public static String getOpenid(WxpayConfig wxConfig,String WxOpenidCode) {
		String openid = null;
		String Url = wxConfig.getOauth2_url().replace("APPID", wxConfig.getAppid()).
				replace("SECRET", wxConfig.getApp_secrect()).replace("CODE", WxOpenidCode);

		String requestUrl = Url.replace("APPID", wxConfig.getAppid()).
				replace("SECRET", wxConfig.getApp_secrect()).replace("CODE", WxOpenidCode);
		System.out.println(requestUrl);
		// 发起GET请求获取凭证
		JSONObject jsonObject = new JSONObject(HttpsRequestHelper.doHttpsRequest(requestUrl, "GET", null));

		if (null != jsonObject) {
			try {
				openid =jsonObject.getString("openid");
			} catch (JSONException e) {
				e.printStackTrace();
				// 获取失败
				//MyLogger.info("获取失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return openid;
	}
	
	
	public static String xmlData(Order order){
		String orderCode = order.getOut_trade_no();
		String orderDesc = order.getRemark();
		double payAmount = order.getTotal_fee();
		String ip = order.getIp();
		String deviceInfo = order.getDeviceInfo();
		WxpayConfig wxConfig = order.getWxConfig();
		
		final Map<String, String> map = Utils.newTreeMap();
		map.put("device_info", deviceInfo);
		map.put("spbill_create_ip", ip);                    //终端IP,必须
		map.put("nonce_str", WxPayCommonUtil.CreateNoncestr());
		map.put("body", orderDesc);                    //商品或支付简要描述
//		map.put("attach", "支付测试");     
		map.put("out_trade_no", orderCode);             //商户订单号,唯一

		map.put("total_fee", Utils.double2fen(payAmount * 100));    //订单总金额, 分
		map.put("notify_url", wxConfig.getServer_address() + wxConfig.getNotify_url());       //接收微信支付异步通知回调地址 
		map.put("trade_type", "APP");
		map.put("appid", wxConfig.getAppid());
		map.put("mch_id", wxConfig.getMch_id());
		
		String map2Str = SignUtils.map2String(map);
		String sign = Md5.encoderByMd5(map2Str + "&key=" + wxConfig.getApi_key()).toUpperCase();
		
		return xmlData(map, sign);
	}

	public static String xmlData(Map<String, String> bizObj, String sign)
			throws RuntimeException {
		StringBuilder sb = new StringBuilder("<xml>");

		for(String s : bizObj.keySet()){
			sb.append('<').append(s.toLowerCase()).append('>')
			 .append(bizObj.get(s)).append('<').append('/').append(s.toLowerCase()).append('>').append('\n');
		}
		
		sb.append("<sign>").append(sign).append("</sign>").append("</xml>");
		return sb.toString();
	}

	
	@SuppressWarnings("deprecation")
	public static String gotoRequest(Order order){
		//处理其他商品类型的逻辑校验 TODO
		WxpayConfig wxConfig = order.getWxConfig();
        String wxRed_Url = wxConfig.getServer_address() + wxConfig.getRedirect_uri_v1();//微信支付重定向的url
        wxRed_Url = URLEncoder.encode(wxRed_Url);
        
        LOG.debug("token={}", wxConfig.getToken());
        //该url用于获得code(用于获取openid)并跳回支付
        String resultUrl =  wxConfig.getRequest_oauth2_url().
					        		replace("APPID", wxConfig.getAppid()).
					        		replace("RED_URL", wxRed_Url).
					        		//随机生成订单号后12位（无用,预防预支付id不能重复）
					        		//TODO: orderCode + ':' + token　
					        		replace("ORDERID", order.getOut_trade_no()+IDGenerator.getID12()+":"+(wxConfig.getToken() == null ? "" : wxConfig.getToken()));
        return resultUrl;
	}
	
}

