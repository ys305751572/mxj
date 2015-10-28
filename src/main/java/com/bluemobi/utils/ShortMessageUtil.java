package com.bluemobi.utils;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
public class ShortMessageUtil {
	/**
     * @Description 云片网 短信发送 依赖httpclient包
     *            String uname 用户帐号; String pword 用户密码;
     * @return "success"登录成功 "error.infowrong"登录失败
     * @date 2014-7-30 下午03:24:46
     * @author 龙哲
	 * @throws java.io.IOException
	 * @throws ClientProtocolException 
     */
	public static String testFluent(String mobile,String code) throws ClientProtocolException, IOException {
			Content content = Request
			.Post("http://yunpian.com/v1/sms/tpl_send.json")
			.addHeader("charset", "utf-8")
			.bodyForm(
					Form.form().add("apikey","a7e4767def13ebd3ec4ef70a2c6874ae")
								.add("tpl_id","2")
								.add("mobile", mobile)
								.add("tpl_value","#code#="+code+"&#company#=平和悦")
								.build()
					)
			.execute().returnContent();
			System.out.println("short message:"+content);
			return content.toString();
	}
}