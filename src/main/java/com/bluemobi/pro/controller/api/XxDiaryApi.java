package com.bluemobi.pro.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.XxDiaryServerImpl;
import com.bluemobi.sys.page.Page;
import com.bluemobi.utils.CommonUtils;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/api/diary/")
public class XxDiaryApi {

	@Autowired
	private XxDiaryServerImpl xxDiaryServer;

	// 日记列表
	@RequestMapping(value = "diaryList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> diaryList(@RequestParam Map<String, Object> params) {

		Page page = null;
		params.put("sortType", params.get("sortType") == null ? 0 : Integer.parseInt(params.get("sortType").toString()));
		
		try {
			page = xxDiaryServer.diaryList(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.page(page);
	}

	// 日记详情-基本信息
	@RequestMapping(value = "diaryDetailInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> diaryDetail(@RequestParam Map<String, Object> params) {

		Map<String, Object> resultMap = null;
		
		params.put("memberId",params.get("memberId") == null ? -1 : params.get("memberId"));
		try {
			resultMap = xxDiaryServer.diaryDetail(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.map(resultMap, "diary");
	}
	
	// 日记详情-基本信息
	@RequestMapping(value = "diaryDetailMe", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> diaryDetailMe(@RequestParam Map<String, Object> params) {

		Map<String, Object> resultMap = null;
		
		params.put("memberId",params.get("memberId") == null ? -1 : params.get("memberId"));
		try {
			resultMap = xxDiaryServer.diaryDetail(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.map(resultMap, "diary");
	}	
	
	// 日记详情-施工进度列表
	@RequestMapping(value = "diaryDetailWorkList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> diaryDetailWorkList(@RequestParam Map<String, Object> params) {

		List list = new ArrayList();
		try {
			list = xxDiaryServer.diaryDetailWorkList(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.list(list);
	}

	// 单日施工详情-进度基本信息
	@RequestMapping(value = "todayDiaryDetailWorkInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> todayDiaryDetailWorkInfo(@RequestParam Map<String, Object> params) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = xxDiaryServer.todayDiaryDetailWorkInfo(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.map2(resultMap, "diary");
	}

	// 单日施工详情-图片列表
	@RequestMapping(value = "todayDiaryDetailImageList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> todayDiaryDetailImageList(@RequestParam Map<String, Object> params) {

		List imageList = new ArrayList();
		try {
			imageList = xxDiaryServer.todayDiaryDetailImageList(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.list(imageList);
	}

	// 单日施工详情-评论列表
	@RequestMapping(value = "todayDiaryDetailCommentList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> todayDiaryDetailCommentList(@RequestParam Map<String, Object> params) {

		List commentList = new ArrayList();
		try {
			commentList = xxDiaryServer.todayDiaryDetailCommentList(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.list(commentList);
	}

	// 评论单日施工
	@RequestMapping(value = "comment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> comment(@RequestParam Map<String, Object> params, HttpServletRequest request) {

		try {
			String ip = CommonUtils.getRealAddress(request);
			params.put("ip", ip);
			xxDiaryServer.comment(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	@RequestMapping(value = "favorite", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> favorite(@RequestParam Map<String,Object> params) {
		
		try {
			xxDiaryServer.favorite(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	@RequestMapping(value = "unfavorite", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> unfavorite(@RequestParam Map<String,Object> params) {
		
		try {
			xxDiaryServer.unfavorite(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	// 认可日记
	@RequestMapping(value = "accept", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> accept(@RequestParam Map<String,Object> params) {
		try {
			xxDiaryServer.accept(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	@RequestMapping(value = "dynamic", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> dirayDynamic(@RequestParam Map<String,Object> params) {
		
		if(ParamUtils.existEmpty(params, "memberId")) return ResultUtils.error(ErrorCode.ERROR_02);
		Map<String,Object> respMap = null;
		try {
			respMap = xxDiaryServer.dynamic(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.map(respMap == null ? new HashMap<String,Object>(): respMap, "dynamic");
	}
	
	@RequestMapping(value = "dynamicCount", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> dirayDynamicCount(@RequestParam Map<String,Object> params) {
		
		if(ParamUtils.existEmpty(params, "memberId")) return ResultUtils.error(ErrorCode.ERROR_02);
		Map<String,Object> respMap = new HashMap<String,Object>();
		try {
			int count = xxDiaryServer.dynamicCount(params);
			respMap.put("count", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.map2(respMap);
	}
	
	/**
	 * 读取状态
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "readDynamic", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> readDynamic(@RequestParam Map<String,Object> params) {
		
		try {
			xxDiaryServer.readDynamic(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	
}
