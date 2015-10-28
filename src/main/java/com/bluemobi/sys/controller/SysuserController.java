package com.bluemobi.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.bluemobi.constant.Constant;
import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.impl.SysuserServiceImpl;
import com.bluemobi.utils.CookiesUtils;
import com.bluemobi.utils.SessionUtils;

/**
 * @Description 本地登陆用户管理controllor
 * @date 2014-12-25
 * @author 涂毅恒
 */
@Controller
public class SysuserController {
	@Autowired
	private SysuserServiceImpl sysuserService;
	/**
     * @Description 跳转到登陆页
     * @param
     * @return
     * @date 2014-12-25 17:59:51
     * @author 龙哲
     */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(String name,String password,Model model,HttpServletRequest request) {
	  //登录名回显
        //读取cookie
        Cookie[] cookies = request.getCookies();
        //判断cookie不等于null
        if(cookies!=null){
            for (int i = 0; i < cookies.length; i++) {
                //cookie key判断
                if("userName".equals(cookies[i].getName())){
                    //会话容器存值
                    model.addAttribute("userName", cookies[i].getValue());
                }
            }
        }
		return "login";
	}

	/**
     * @Description 执行登陆操作
     * @param
     * @return
     * @date 2014-12-25 17:59:51
     * @author 龙哲
	 * @param response 
     */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String auth(String name,String password,String status, Model model, HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("password", password);
			Map<String, Object> user = sysuserService.getLogin(map);
			if(user==null){ //登录失败
				model.addAttribute("message", "用户名密码错误");
				return "login";
			}else {	//登录成功
				SessionUtils.put(Constant.CURRENT_USER, user);
				// 定义账户密码的生命周期，这里是一个月。单位为秒
                int loginMaxAge = 30 * 24 * 60 * 60; 
                //如果选择了记住账号就将账号放在cookie中
                if (null != status && status.equals("on")) {
                    //cookie存值
                    CookiesUtils.addCookie(response, "userName", name, loginMaxAge);
                }else{
                    //删除cookie
                    CookiesUtils.addCookie(response, "userName", "", 0);
                }
				return "redirect:/website/users/index";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "登录失败");
			return "login";
		}
	}

	/**
     * @Description 执行登出操作
     * @param
     * @return
     * @date 2014-12-25 17:59:51
     * @author 龙哲
     */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(SessionStatus status) {
		status.setComplete();
		SessionUtils.clear();
		return "redirect:/website/users/index";
	}
	/**
     * @Description 显示首页
     * @param
     * @return
     * @date 2014-12-25 17:59:51
     * @author lvjunjun
     */
	/**
     * @Description 首页跳转
     * @param
     * @return
     * @date 2014-12-25 15:02:51
     * @author 龙哲
     */
    @RequestMapping(value = { "/homepage"})
    public String homepage() {
            return "main/main/homepage";
    }
    //已下为后台帐号管理
    /**
     * @Description 进入后台管理页面
     * @param
     * @return
     * @date 2015-01-09 9:54:51
     * @author 吕俊军
     */
    @RequestMapping("/website/sysuser/sysuserlist")
    public String hospitallist() {
        return "main/sysuser/sysuserlist";
    }
    /**
     * @Description 查询后台帐号列表
     * @param
     * @return
     * @date 2015-01-12 10:11:51
     * @author 吕俊军
     */
    @RequestMapping("/website/sysuser/allsysuser")
    @ResponseBody
    public Page<List<Map<String, Object>>> allsysuser(@RequestParam Map<String, Object> params) {
        String rows = params.get("rows").toString();
        String pages = params.get("page").toString();
        Page<List<Map<String, Object>>> page = sysuserService.page(params, Integer.valueOf(pages),Integer.valueOf(rows));
        return page;
    }
    /**
     * @Description 进入增加后台帐号页面
     * @param
     * @return
     * @date 2015-01-12 10:47:51
     * @author 吕俊军
     */
    @RequestMapping("/website/sysuser/toaddsysuser")
    public String toaddsysuser() {
        return "main/sysuser/addsysuser";
    }
    /**
     * @Description 新增后台帐号
     * @param
     * @return
     * @date 2015-01-12 10:11:51
     * @author 吕俊军
     */
    @RequestMapping("/website/sysuser/addsysuser")
    @ResponseBody
    public String addsysuser(@RequestParam Map<String, Object> params) {
        try {
            return sysuserService.save(params)+"";
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0+"";
        }
    }
    /***
     * @Description 进入修改后台帐号页面
     * @param
     * @return
     * @date 2015-01-012 11:02:51
     * @author 吕俊军
     */
    @RequestMapping("/website/sysuser/toupdatesysuser")
    public String toupdatesysuser(Model model, String id){
        Map<String, Object> params= new HashMap<String, Object>();
        params.put("id", id);
        Map<String, Object> sysuser = new HashMap<String, Object>();
        try {
            sysuser = sysuserService.findOne(params);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("sysuser",sysuser);
        return "main/sysuser/updatesysuser";
    }
    /**
     * @Description 修改后台帐号
     * @param
     * @return
     * @date 2015-01-12 11:36:51
     * @author 吕俊军
     */
    @RequestMapping("/website/sysuser/updatesysuser")
    @ResponseBody
    public String updatesysuser(@RequestParam Map<String, Object> params) {
        try {
            return sysuserService.update(params)+"";
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0+"";
        }
    }

    /**
     * @Description 进入修改密码页面
     * @param
     * @return
     * @date 2015-01-12 10:47:51
     * @author 吕俊军
     */
    @RequestMapping("/website/sysuser/toupdatepassword")
    public String toupdatepassword(Model model) {
        Map<String, Object> params= (Map<String, Object>) SessionUtils.get(Constant.CURRENT_USER);
        Map<String, Object> sysuser = new HashMap<String, Object>();
        try {
            sysuser = sysuserService.findOne(params);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("sysuser",sysuser);
        return "main/sysuser/updatepassword";
    }
    /**
     * @Description 修改密码
     * @param
     * @return
     * @date 2015-01-12 11:36:51
     * @author 吕俊军
     */
    @RequestMapping("/website/sysuser/updatepassword")
    @ResponseBody
    public String updatepassword(@RequestParam Map<String, Object> params) {
        try {
            return sysuserService.updatepassword(params)+"";
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0+"";
        }
    }
    
    @RequestMapping("/download")
    public String download() {
        return "download";
    }
    
    @RequestMapping("/test")
    public String departmentlist() {
    	return "test";
    }
}
