/**
 * 
 */
package com.bluemobi.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class ParamUtils {

	// 判断map里的参数是否有为空
	public static Boolean existEmpty(Map<String,Object> params,String ... key) {
		for (int i = 0; i < key.length; i++) {
			if(StringUtils.isEmpty((String) params.get(key[i]))){
				return true;
			}
		}
		return false;
	}
}