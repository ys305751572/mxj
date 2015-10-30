package com.bluemobi.pro.controller.api;

import com.bluemobi.cache.CacheService;
import com.bluemobi.constant.Constant;
import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.UhoemMemberServiceImpl;
import com.bluemobi.utils.*;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/api/uhoemMember")
public class UhoemMemberApi {

    private static String username = "";

    @Autowired
    private UhoemMemberServiceImpl uhoemMemberServiceImpl;

    @Resource(name = "cacheTempCodeServiceImpl")
    private CacheService<String> cacheService;

    /**
     * 登录
     * 请求参数：
     * mobile  手机号 （非必填）
     * password  密码 （非必填）
     * <p/>
     * OR
     * <p/>
     * 如果是第三方登录，就用下面的两个参数登录，（第三方openId和第三方类型）
     * openId：第三方唯一标识  （非必填）
     * type：第三方类型（2、微信  3、QQ  4、新浪微博）        （非必填）
     *
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam Map<String, Object> params) {
        try {
            // 判断是否有openId，如果有，则先通过openId登录，登录失败时调用后台正常登录方法
            if (null != params.get("openId") && !(params.get("openId") + "").equals("")) {
                // 执行openId登录操作
                Map<String, Object> userMap = uhoemMemberServiceImpl.iLoginByOpenId(params);
                if (null == userMap || userMap.size() == 0 || userMap.get("id") == null || "".equals(userMap.get("id").toString())) {
                    // 如果通过openID找不到用户信息，则后台生成一个新用户，同时绑定当前openID，然后返回新生成的用户信息
                    Map<String, Object> userPlus = new HashMap<String, Object>();
                    userPlus.put("type", params.get("type"));

                    // 随机生成新用户名，u+6位随机数，并且须保证用户名不重复
                    Map<String, Object> tempMap = uhoemMemberServiceImpl.iGetMemberInfoByUserName(getRandom(6));
                    while (null != tempMap) {
                        tempMap = uhoemMemberServiceImpl.iGetMemberInfoByUserName(getRandom(6));
                    }

                    userPlus.put("username", username);

                    int flag = uhoemMemberServiceImpl.iRegister(userPlus);
                    // 判断是否注册成功
                    if (1 == flag) {
                        // 注册成功后，绑定openID，同时返回用户信息
                        // 执行绑定操作
                        userMap = new HashMap<String, Object>();
                        userMap.put("userId", userPlus.get("id"));
                        userMap.put("openId", params.get("openId"));
                        userMap.put("type", params.get("type"));
                        if (params.get("type").equals(Constant.LOGIN_TYPE_WEIXIN)) {
                            userMap.put("appId", Constant.LOGIN_WEIXIN_APPID);
                        } else if (params.get("type").equals(Constant.LOGIN_TYPE_QQ)) {
                            userMap.put("appId", Constant.LOGIN_QQ_APPID);
                        } else if (params.get("type").equals(Constant.LOGIN_TYPE_XINLANG)) {
                            userMap.put("appId", Constant.LOGIN_XINLANG_APPID);
                        }
                        uhoemMemberServiceImpl.bindingOpenId(userMap);

                        userMap = uhoemMemberServiceImpl.iLoginByOpenId(params);

                        // 返回用户信息
                        userMap.put("ifFirstLogin", Constant.IF_FIRST_LOGIN_YES);
                        userMap.remove("login_date");
                        Map<String, Object> map = new HashMap<String, Object>();
                        userMap.remove("password");
                        map.put("user", userMap);
                        return ResultUtils.object(map);
                    } else {
                        return ResultUtils.error(ErrorCode.ERROR_01);
                    }
                } else {
                    userMap.put("ifFirstLogin", Constant.IF_FIRST_LOGIN_NO);
                    userMap.remove("login_date");
                    Map<String, Object> map = new HashMap<String, Object>();
                    userMap.remove("password");
                    map.put("user", userMap);
                    return ResultUtils.object(map);
                }
            }

            // 如果没有openId，则执行正常登录流程
            Map<String, Object> result = uhoemMemberServiceImpl.login(params);
            if (null != result) {
                result.remove("login_date");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user", result);
                result.remove("password");
                return ResultUtils.object(map);
            } else {
                return ResultUtils.error(ErrorCode.ERROR_03);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.ERROR_01);
        }
    }

    /**
     * 注册
     * 请求参数：
     * mobile 手机号 （必填）
     * password 密码 （必填）
     *
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public Map<String, Object> register(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "mobile", "password", "content")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            String mobile = (String)params.get("mobile");
            String code = cacheService.get(mobile);
            // 检测验证码是否过期
            if (StringUtils.isEmpty(code)) {
                return ResultUtils.error(ErrorCode.ERROR_11);
            }

            // 检测验证码是否正确
            if (!params.get("content").equals(code)) {
                return ResultUtils.error(ErrorCode.ERROR_10);
            }

            // 验证手机号是否存在
            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("mobile", params.get("mobile"));
            if (uhoemMemberServiceImpl.iCheckMobile(tempMap)) {
                return ResultUtils.error(ErrorCode.ERROR_05);
            } else {
                params.put("type", Constant.LOGIN_TYPE_NONE);
                params.put("username", "");
                int num = uhoemMemberServiceImpl.iRegister(params);
                // 判断是否注册成功
                if (num > 0) {
                    tempMap = uhoemMemberServiceImpl.login(params);
                    tempMap.remove("login_date");
                    cacheService.remove(mobile);
                    return ResultUtils.success();
                } else {
                    return ResultUtils.error(ErrorCode.ERROR_01);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.ERROR_01);
        }
    }

    /**
     * 发送验证码
     * 请求参数：
     * mobile 手机号  （必填）
     * action 类型 （必填）
     *
     * @return
     */
    @RequestMapping("/sendMessage")
    @ResponseBody
    public Map<String, Object> sendMessage(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "mobile", "action")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            // 验证手机号是否存在
            String action = (String) params.get("action");
            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("mobile", params.get("mobile"));
            Map<String, Object> map = uhoemMemberServiceImpl.iGetMemberInfoByMobile(tempMap);
            if ("register".equals(action)) { // 注册
                if (map != null && map.size() > 0) {
                    return ResultUtils.error(ErrorCode.ERROR_05);
                }
            } else { // 找回密码、修改密码
                if (map == null || map.size() == 0) {
                    return ResultUtils.error(ErrorCode.ERROR_06);
                }
            }

            // 生成验证码
            String code = getCode(6);
            System.out.println("code : " + code);
            String mobile = params.get("mobile").toString();
            String result = JavaSmsApi.sendShortMessage(mobile, code);
            // 成功
            if (result.contains("\"msg\":\"OK\"")) {
                cacheService.put(mobile,code);
                return ResultUtils.string("");
            }
            // 失败
            else {
                return ResultUtils.error(ErrorCode.ERROR_09);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.ERROR_01);
        }
    }

    public static String getCode(int length) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < length; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    /**
     * 找回密码/修改密码
     * 请求参数：
     * mobile 手机号 （必填）
     * password 密码 （必填）
     *
     * @return
     */
    @RequestMapping("/modifyPassword")
    @ResponseBody
    public Map<String, Object> modifyPassword(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "mobile", "password", "content")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }

            String mobile = (String)params.get("mobile");
            String code = cacheService.get(mobile);
            // 检测验证码是否过期
            if (StringUtils.isEmpty(code)) {
                return ResultUtils.error(ErrorCode.ERROR_11);
            }

            // 检测验证码是否正确
            if (!params.get("content").equals(code)) {
                return ResultUtils.error(ErrorCode.ERROR_10);
            }

            // 验证手机号是否存在
            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("mobile", params.get("mobile"));
            Map<String, Object> map = uhoemMemberServiceImpl.iGetMemberInfoByMobile(tempMap);
            if (null == map || map.size() == 0) {
                return ResultUtils.error(ErrorCode.ERROR_06);
            }

            // 执行修改密码操作
            int num = uhoemMemberServiceImpl.iUpdatePassword(params);
            if (num > 0) {
                cacheService.remove(mobile);
                return ResultUtils.string("");
            } else {
                return ResultUtils.error(ErrorCode.ERROR_01);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.ERROR_01);
        }
    }

    /**
     * 查询个人信息
     * 请求参数：
     * memberid 用户ID（必填）
     *
     * @return
     */
    @RequestMapping("/getMemberInfo")
    @ResponseBody
    public Map<String, Object> getMemberInfo(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "memberid")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Map<String, Object> result = uhoemMemberServiceImpl.iGetMemberInfo(params);

            // 查询第三方绑定信息
            List<Map<String, Object>> list = uhoemMemberServiceImpl.iFindOpenIdByMemberId(params);
            for (Map<String, Object> map : list) {
                if (map.get("source").equals(Constant.LOGIN_TYPE_WEIXIN)) {
                    result.put("WeiXin", Constant.IF_FIRST_LOGIN_YES);
                } else if (map.get("source").equals(Constant.LOGIN_TYPE_QQ)) {
                    result.put("QQ", Constant.IF_FIRST_LOGIN_YES);
                } else if (map.get("source").equals(Constant.LOGIN_TYPE_XINLANG)) {
                    result.put("XinLang", Constant.IF_FIRST_LOGIN_YES);
                }
            }

            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("user", result);
            result.remove("password");
            return ResultUtils.object(tempMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.ERROR_01);
        }
    }

    /**
     * 修改用户信息
     * 请求参数：
     * memberid  用户ID       （必填）
     * name  用户名         （非必填）
     * gender  性别         （非必填）
     * birth  生日          （非必填）
     * description  签名    （非必填）
     * constellation  星座  （非必填）
     *
     * @return
     */
    @RequestMapping(value = "/updateMemberInfo",  method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateMemberInfo(@RequestParam Map<String, Object> params) {
        try {
            if (ParamUtils.existEmpty(params, "memberid")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            Map<String, Object> memberMap = uhoemMemberServiceImpl.iGetMemberInfo(params);
            if(memberMap.get("name") == null || params.get("name") == null || !memberMap.get("name").toString().equals(params.get("name").toString())) {
            	 if(params.get("name") != null) {
                  	// 昵称需要唯一
                  	String name = uhoemMemberServiceImpl.iGetMemberNameByName(params);
                  	if(name != null) {
                  		return ResultUtils.error(ErrorCode.ERROR_12);
                  	}
                  }
            }
            int num = uhoemMemberServiceImpl.iUpdateInfo(params);
            if (num > 0) {
                Map<String, Object> result = uhoemMemberServiceImpl.iGetMemberInfo(params);
                result.remove("login_date");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user", result);
                result.remove("password");
                return ResultUtils.object(map);
            } else {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.ERROR_01);
        }
    }

    /**
     * 修改用户头像
     * 请求参数：
     * memberid  用户ID       （必填）
     * file  头像           （必填）
     *
     * @return
     */
    @RequestMapping("/updateMemberFaceImage")
    @ResponseBody
    public Map<String, Object> updateMemberFaceImage(@RequestParam Map<String, Object> params,
                                                     @RequestParam(required = false) MultipartFile file) {
        try {
            if (ParamUtils.existEmpty(params, "memberid")) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            if (null == file) {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
            StringMap map = UploadUtils.upload(UploadUtils.changeFile(file));
            params.put("face_image", Constant.domain + map.get("key"));

            // 先删除原先的头像，再修改头像
            params.put("id", params.get("memberid"));
            Map<String, Object> tempMap = uhoemMemberServiceImpl.getMemberInfo(params);
            if (!tempMap.get("face_image").equals("")) {
                UploadUtils.deleteFile(tempMap.get("face_image") + "");
            }

            int num = uhoemMemberServiceImpl.iUpdateInfo(params);
            if (num > 0) {
                return ResultUtils.string("");
            } else {
                return ResultUtils.error(ErrorCode.ERROR_02);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.ERROR_01);
        }
    }

    public static Map<String, Object> getRandom(int length) {
        Random random = new Random();
        Map<String, Object> map = new HashMap<String, Object>();
        String result = "";
        for (int i = 0; i < length; i++) {
            result += random.nextInt(10);
        }
        map.put("username", "u" + result);
        username = "u" + result;
        return map;
    }

    /**
     * 第三方绑定
     */
    @RequestMapping("/bindingOpenId")
    @ResponseBody
    public Map<String, Object> bindingOpenId(@RequestParam Map<String, Object> params) {
        int num = 0;

        if (ParamUtils.existEmpty(params, "memberid", "openId", "type")) {
            return ResultUtils.error(ErrorCode.ERROR_02);
        }

        try {
            Map<String, Object> userThird = uhoemMemberServiceImpl.iCheckBinding(params);
            // 不为空，则表示已经绑定第三方
            if (null != userThird) {
                return ResultUtils.error(ErrorCode.ERROR_08);
            }

            userThird = new HashMap<String, Object>();
            userThird.put("userId", params.get("memberid"));
            userThird.put("openId", params.get("openId"));
            userThird.put("type", params.get("type"));
            if (params.get("type").equals(Constant.LOGIN_TYPE_WEIXIN)) {
                userThird.put("appId", Constant.LOGIN_WEIXIN_APPID);
            } else if (params.get("type").equals(Constant.LOGIN_TYPE_QQ)) {
                userThird.put("appId", Constant.LOGIN_QQ_APPID);
            } else if (params.get("type").equals(Constant.LOGIN_TYPE_XINLANG)) {
                userThird.put("appId", Constant.LOGIN_XINLANG_APPID);
            }

            num = uhoemMemberServiceImpl.bindingOpenId(userThird);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            return ResultUtils.string("");
        } else {
            return ResultUtils.error(ErrorCode.ERROR_01);
        }
    }

    /**
     * 修改用户设备号
     * @param params
     * @return
     */
    @RequestMapping(value = "/modifyUserDeviceId", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyUserDeviceId(@RequestParam Map<String,Object> params) {
    	
    	try {
			uhoemMemberServiceImpl.modifyUserDeviceId(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
    	return ResultUtils.success();
    }
}
