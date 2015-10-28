package com.bluemobi.pro.controller.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.constant.Constant;
import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.service.impl.XxShopServiceImpl;
import com.bluemobi.sys.page.Page;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;

@Controller
@RequestMapping("/api/shop/")
public class XxShopApi {
	
	@Autowired
	private XxShopServiceImpl xxShopService;
	
	// 基础硬装
	@RequestMapping(value = "baseDecor", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> baseDecor() {
		
		List<Map<String,Object>> list = null;
		try {
			list = xxShopService.baseDecor();
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.list(list);
	}
	
	@RequestMapping(value = "search", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> search() {
		
		Map<String,Object> map = null;
		try {
			map = xxShopService.search();
			if(map == null) {
				map = new HashMap<String,Object>();
				map.put("name", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.map2(map);
	}
	
	
	// 获取规格
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getSpec", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getSpec(@RequestParam Map<String,Object> params) {
		
		if(ParamUtils.existEmpty(params, "productId")) return ResultUtils.error(ErrorCode.ERROR_02);
		List specList = new ArrayList();
		try {
			specList = xxShopService.getSpec(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return specList == null ? Collections.EMPTY_MAP : ResultUtils.parse(specList);
	}
	
	//
	// 购物车列表
	@RequestMapping(value = "cartList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> cartList(@RequestParam Map<String,Object> params) {
		Map<String,Object> cart = null;
		try {
			cart = xxShopService.findCart(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.object(cart);
	}
	
	// 加入购物车
	@RequestMapping(value = "addCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addCart(@RequestParam Map<String,Object> params,HttpSession session) {
		
		if(ParamUtils.existEmpty(params,"productId","memberId")) return ResultUtils.error(ErrorCode.ERROR_02);
		String productId = params.get("productId").toString();
		if(params.get("isBase") == null) {
			params.put("isBase", 0);
		}
		
//		Integer quantity = params.get("quantity") == null ? 1 : Integer.parseInt(params.get("quantity").toString());
//		params.put("quantity", quantity);
		String[] carts = productId.split("\\|");
		try {
			for (String string : carts) {
				String[] cartIdAndQuantity = string.split(",");
				
				params.put("productId", cartIdAndQuantity[0]);
				params.put("quantity", cartIdAndQuantity[1]);
				
				params.put("cart_key", session.getId());
				xxShopService.addCart(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	@RequestMapping(value = "modifyCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> modifyCart(@RequestParam Map<String,Object> params) {
		
		if(ParamUtils.existEmpty(params, "memberId","cartItemId","quantity")) return ResultUtils.error(ErrorCode.ERROR_02);
		try {
			xxShopService.modifyCart(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	// 生成订单号
	@RequestMapping(value = "getOrderNo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> generateOrderNo(@RequestParam Map<String,Object> params) {
		
		return null;
	}
	
	// 订单列表
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "orderList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orderList(@RequestParam Map<String,Object> params) {
		
		Page page = null;
		try {
			page = xxShopService.findOrderList(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.page(page);
	}
	
	// 确认订单
	@RequestMapping(value = "createOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> createOrder(@RequestParam Map<String,Object> params) {
		
		if(ParamUtils.existEmpty(params, "productId")) return ResultUtils.error(ErrorCode.ERROR_02);
		String sn = null;
		try {
			sn = xxShopService.createOrder(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("sn", sn);
		return ResultUtils.map2(resultMap);
	}
	
	// 订单详情
	@RequestMapping(value = "orderDetail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> orderDetail(@RequestParam Map<String,Object> params) {
		Map<String,Object> order = null;
		try {
			order = xxShopService.orderDetail(params);
			order.put("expire", order.get("expire") != null ? order.get("expire").toString().subSequence(0, 10) : "");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("order", order);
		return ResultUtils.map2(map);
	}
	
	// orderId
	// addressId
	// expire
	// 修改订单
	@RequestMapping(value = "motifyOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> motify(@RequestParam Map<String,Object> params) {
		
		try {
			xxShopService.motifyOrder(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	// 支付完成后回调方法
	@RequestMapping(value = "notify", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> payNotify(@RequestParam Map<String,Object> params) {
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("operation", Constant.ORDER_PAY);
		try {
			xxShopService.operationOrder(map);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	// 删除购物车
	@RequestMapping(value = "removeCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> removeCart(@RequestParam Map<String,Object> params) {
		
		try {
			xxShopService.removeCartItem(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	// 操作订单
	@RequestMapping(value = "operationOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> operationOrder(@RequestParam Map<String,Object> params) {
		
		if(ParamUtils.existEmpty(params, "operation")) return ResultUtils.error(ErrorCode.ERROR_02);
		try {
			xxShopService.operationOrder(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	// 评论列表
	@RequestMapping(value = "commentList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> commentList(@RequestParam Map<String,Object> params) {
		if(ParamUtils.existEmpty(params, "productId")) return ResultUtils.error(ErrorCode.ERROR_02);
		Page page = null;
		try {
			page = xxShopService.findCommentByProductId(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.page(page);
	}
	
	// 评价商品
	@RequestMapping(value = "comment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> comment(@RequestParam Map<String,Object> params) {
		
		try {
			xxShopService.comments(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.success();
	}
	
	// 初始化支付信息
	public Map<String,Object> initPayment(@RequestParam Map<String,Object> params) {
		return null;
	}
	
	// 查看用户购物车是否有基础硬装
	@RequestMapping(value = "isExistBaseDecor", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> isExistBaseDecor(@RequestParam Map<String,Object> params) {
		
		Map<String,Object> resultMap = null;
		try {
			resultMap = xxShopService.isExistBaseDecor(params);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
		return ResultUtils.map(resultMap, "product");
	}
}
