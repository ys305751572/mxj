package com.bluemobi.pro.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.pro.service.impl.UhoemTaskServiceImpl;
import com.bluemobi.utils.ResultUtils;

// 验收
@Controller
@RequestMapping(value = "/api/accept/")
public class UhoemTaskApi{

	@Autowired
	private UhoemTaskServiceImpl service;
	
	// 查询首页是否有新验收消息
	@RequestMapping(value = "isNewAccept", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> isNewAccept(@RequestParam Map<String,Object> params) {
		
		Map<String,Object> resultMap = null;
		try {
			resultMap = service.isNewAccept(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.map(resultMap, "acceptindex");
	}
	
	@RequestMapping(value = "isJoin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> isJoin(@RequestParam Map<String,Object> params) {
		
		List list = null;
		try {
			service.isJoin(params);
			list = service.findAllTask(params);
			if(list == null) {
				list = new ArrayList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.list(list);
	}
	
	// 任务项列表
	@Deprecated
	@RequestMapping(value = "taskList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> taskList(@RequestParam Map<String,Object> params){
		
		List list = null;
		try {
			list = service.findAllTask(params);
			if(list == null) {
				list = new ArrayList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.list(list);
	}
	
	// 查询用户任务节点列表
	@RequestMapping(value = "taskNodeList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> taskNodeList(@RequestParam Map<String,Object> params) {
		
		List list = null;
		try {
			list = service.findTaskNode(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.list(list);
	}
	
	// 验收任务选项
	@RequestMapping(value = "accept", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> accpetTask(@RequestParam Map<String,Object> params) {
		
		try {
			service.accpetTask(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
}
