package com.bluemobi.pro.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.cassandra.cli.CliParser.exitStatement_return;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluemobi.constant.Constant;
import com.bluemobi.pay.Payment;
import com.bluemobi.pro.entity.CartItemVO;
import com.bluemobi.pro.entity.CartVO;
import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.BaseService;
import com.bluemobi.utils.DateUtils;
import com.echoliv.common.component.file.FileHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class XxShopServiceImpl extends BaseService {

	public static final String PRIFIX = XxShopServiceImpl.class.getName();

	@Autowired
	private XxMemberServiceImpl xxMemberServiceImpl;

	public List<Map<String, Object>> baseDecor() throws Exception {
		List<Map<String, Object>> baselist = this.getBaseDao().getList(PRIFIX + ".findBaseDecor");
		return baselist;
	}

	public Map<String, Object> search() throws Exception {
		return this.getBaseDao().get(PRIFIX + ".search", null);
	}

	// 根据商品ID获取商品规格
	public Map<String,Object> getSpec(Map<String, Object> params) throws Exception {

		String productId = params.get("productId").toString();
		List<Map<String, Object>> pidList = this.getProductById(Long.parseLong(productId));
		List specList = null;
		List specvalueList = null;
		if (pidList != null) {
			specList = getSpecList(pidList);
			if (specList != null) {
				specvalueList = getSpecvalueList(pidList);
				addValuetoSpec(specList, specvalueList);
			}
		}
		Map<String,Map<String,Object>> m = getSpecProduct(pidList);
		
		Map<String,Object> allMap = new HashMap<String,Object>();
		allMap.put("spec", specList);
		allMap.put("array", m);
		return allMap;
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Map<String,Object>> getSpecProduct(List<Map<String, Object>> pidList) throws Exception {
		List list = this.getBaseDao().getList(PRIFIX + ".getSpecByPid", pidList);
		Map<String,Map<String,Object>> returnMap = new HashMap<String,Map<String,Object>>();
		String pid = "";
		Map<String,Object> map = null;
		Map<String,Object> productMap = null;
		StringBuffer buffer = null;
		for (Object object : list) {
			
			if(object != null) {
				map = (Map<String,Object>) object;
				String pid2 = map.get("productId").toString();
				if(!pid.equals(pid2)) {
					
					if(buffer != null && buffer.toString().length() > 0) {
						returnMap.put(buffer.toString(), productMap);
					}
					buffer = new StringBuffer();
					pid = pid2;
					productMap = new HashMap<String,Object>();
					productMap.put("productId", map.get("productId"));
					productMap.put("price", map.get("price"));
					productMap.put("productName", map.get("productName"));
					productMap.put("productFullName", map.get("productFullName"));
					productMap.put("image", map.get("image"));
					productMap.put("pmcId", map.get("pmcId"));
					buffer.append(map.get("vid"));
				}
				else {
					buffer.append(",").append(map.get("vid"));
				}
			}
		}
		if(buffer != null && buffer.length() > 0) {
			returnMap.put(buffer.toString(), productMap);
		}
		return returnMap;
	}

	//
	// 这里需要根据商品id知道goodsid，并根据goodsid查询这一类的商品id
	private List getProductById(Long productId) throws Exception {
		return this.getBaseDao().getList(PRIFIX + ".getProductByPid", productId);
	}

	private List getSpecvalueList(List<Map<String, Object>> pidList) throws Exception {

		List<Long> pids = new ArrayList<Long>();
		for (Map<String, Object> map : pidList) {
			pids.add(Long.parseLong(map.get("id").toString()));
		}
		return this.getBaseDao().getList(PRIFIX + ".getSpecvalueList", pids);
	}

	private List getSpecList(List<Map<String, Object>> pidList) throws Exception {

		List<Long> pids = new ArrayList<Long>();
		for (Map<String, Object> map : pidList) {
			pids.add(Long.parseLong(map.get("id").toString()));
		}
		if (pids != null && pids.size() > 0) {
			return this.getBaseDao().getList(PRIFIX + ".getSpecList", pids);
		} else {
			return null;
		}
	}

	public void addValuetoSpec(List specList, List specvalueList) {
		HashMap<String, Object> specMap = new HashMap<String, Object>();
		for (Object object : specList) {
			if (object != null) {
				Map<String, Object> spec = (Map<String, Object>) object;
				specMap.put(spec.get("id").toString(), spec);
			}
		}

		HashMap<String, Object> specValuesMap = new HashMap<String, Object>();
		for (Object object : specvalueList) {
			if (object != null) {
				Map<String, Object> spec = (Map<String, Object>) object;
				specValuesMap.put(spec.get("id").toString(), spec);
			}
		}

		for (Entry<String, Object> entity : specValuesMap.entrySet()) {
			Map<String, Object> map = (Map<String, Object>) entity.getValue();
			Object obj = map.get("specification");
			if (obj != null) {
				Object obj1 = specMap.get(obj.toString());
				Map<String, Object> map1 = (Map<String, Object>) obj1;
				Object obj2 = map1.get("list");
				if (obj2 != null) {
					List list = (List) obj2;
					list.add(map);
				} else {
					List list = new ArrayList();
					list.add(map);
					map1.put("list", list);
				}
			}
		}
	}

	// 加入购物车
	@Transactional
	public Integer addCart(Map<String, Object> params) throws Exception {

		Integer flag = null;
		Map<String, Object> _return = isExist(params);
		int isBase = Integer.parseInt(params.get("isBase").toString());
		if (isBase == 1) {
			// 爆款硬装
			Map<String, Object> itemMap = this.getBaseDao().get(PRIFIX + ".findCartItemId2", params);
			if (itemMap != null && itemMap.get("cartItemId") != null) {
				removeCartItem(itemMap);
			}
		}

		if (_return != null && Integer.parseInt(_return.get("cart_num").toString()) > 0) {
			params.put("cart", _return.get("id"));
			flag = updateCartCount(params);
		} else {
			Long cartId = createCart(params);
			params.put("cartId", cartId);
			params.put("flag", 0);
			flag = createCartItem(params);
		}
		return flag;
	}

	// 更新购物车数量
	private Integer updateCartCount(Map<String, Object> params) throws Exception {
		return this.getBaseDao().update(PRIFIX + ".updateCartCount", params);
	}

	// 判断购物车是否已存在该商品
	public Map<String, Object> isExist(Map<String, Object> params) throws Exception {
		return this.getBaseDao().get(PRIFIX + ".countCart", params);
	}

	private Integer createCartItem(Map<String, Object> params) throws Exception {
		params.put("create_date", DateUtils.getCurrentTime());
		params.put("modify_date", DateUtils.getCurrentTime());
		return this.getBaseDao().save(PRIFIX + ".insertCartItem", params);
	}

	private Long createCart(Map<String, Object> params) throws Exception {
		params.put("create_date", DateUtils.getCurrentTime());
		params.put("modify_date", DateUtils.getCurrentTime());

		// params.put("cart_key", CommonUtils.generateUUID());

		if (params.get("memberId") != null) {
			params.put("member", params.get("memberId"));
		}
		Long cartId = -1L;

		Long _cartId = this.getBaseDao().getObject(PRIFIX + ".findCartIdBbyMemebrId", params);
		if (_cartId == null) {
			this.getBaseDao().save(PRIFIX + ".insertCart", params);
			cartId = Long.parseLong(params.get("id").toString());
		} else {
			cartId = _cartId;
		}
		return cartId;
	}

	// 创建订单
	@Transactional
	public String createOrder(Map<String, Object> params) throws Exception {

		params.put("productIds", params.get("productId"));
		
		String sn = FileHelper.getTimeFileName();

		Map<String, Object> orderMap = new HashMap<String, Object>();

		String createTime = DateUtils.getCurrentTime();
		String motifyTime = DateUtils.getCurrentTime();

		orderMap.put("create_date", createTime);
		orderMap.put("modify_date", motifyTime);

		orderMap.put("member", params.get("memberId"));
		orderMap.put("sn", sn);

		orderMap.put("amount_paid", 0);
		orderMap.put("coupon_discount", 0);
		orderMap.put("fee", 0);
		orderMap.put("freight", 0);
		orderMap.put("is_allocated_stock", 0);
		orderMap.put("is_invoice", 0);
		orderMap.put("offset_amount", 0);
		orderMap.put("point", 0);
		orderMap.put("promotion_discount", 0);
		orderMap.put("payment_status", 0);
		orderMap.put("shipping_status", 0);
		orderMap.put("tax", 0);

		orderMap.put("payment_status", Constant.ORDER_STATUS_WAIT_PAY);
		orderMap.put("shipping_status", Constant.SHIP_NOT);

		Object addressId = params.get("addressId");
		Map<String, Object> addressMap = null;
		if (addressId != null) {
			addressMap = xxMemberServiceImpl.findByAddressId(addressId.toString());
		}
		if (addressMap == null)
			return null;

		// 完善地址
		orderMap.put("address", addressMap.get("address"));
		orderMap.put("area_name", addressMap.get("area_name"));
		orderMap.put("consignee", addressMap.get("consignee"));
		orderMap.put("phone", addressMap.get("phone"));
		orderMap.put("zip_code", addressMap.get("zip_code"));
		orderMap.put("area", addressMap.get("area"));

		// 支付方式
		Object paymentMethodId = params.get("paymentMethodId");
		if (paymentMethodId == null || paymentMethodId.equals(""))
			paymentMethodId = "1";
		// Map<String,Object> pay = this.getBaseDao().get(PRIFIX +
		// ".findPaymementMethodNameById", paymentMethodId.toString());
		// if(pay != null) {
		// orderMap.put("payment_method", paymentMethodId);
		// orderMap.put("payment_method_name", pay.get("name"));
		// }

		orderMap.put("payment_method", paymentMethodId);
		orderMap.put("payment_method_name", "");

		// 配送方式
		Object shippingMethodId = params.get("shippingMethodId");
		if (shippingMethodId == null || shippingMethodId.equals(""))
			shippingMethodId = "1";
		// Map<String,Object> ship = this.getBaseDao().get(PRIFIX +
		// ".findShipmementMethodNameById", shippingMethodId.toString());
		// if(ship != null) {
		// orderMap.put("shipping_method", shippingMethodId);
		// orderMap.put("shipping_method_name", ship.get("name"));
		// }

		orderMap.put("shipping_method", shippingMethodId);
		orderMap.put("shipping_method_name", "");

		orderMap.put("expire", params.get("expire"));
		orderMap.put("memo", params.get("memo"));
		orderMap.put("order_status", Constant.ORDER_STATUS_WAIT_PAY);

		// 创建订单
		this.getBaseDao().save(PRIFIX + ".insertOrder", orderMap);

		// =========================创建订单项=================================
		if (StringUtils.isNotBlank(params.get("productId").toString())) {
			String[] pids = params.get("productId").toString().split("\\|");

			for (String pid : pids) {
				String[] productId = pid.split(",");
				params.put("productId", productId[0]);
				params.put("quantity", productId[1]);
				createOrderItems(params, orderMap.get("id"));
			}
		}

		// ===========================修改xx_cart.member值======================================
//		params.put("cartId", orderMap.get("id"));
//		this.getBaseDao().update(PRIFIX + ".updateCartMember", params);

		// 删除对应的购物车数据
		
//		deleteCart(params);

		return sn;
	}
	
	/**
	 * 删除购物车
	 * @param params
	 */
	private void deleteCart(Map<String, Object> params) {
		long memberId = Long.parseLong(params.get("memberId").toString());
		String productIdstr = params.get("productIds") == null ? "" : params.get("productIds").toString();
		if (StringUtils.isNotBlank(productIdstr)) {
			String[] productIds = productIdstr.split("\\|");
			Map<String, Object> cartMap = new HashMap<String, Object>();
			cartMap.put("memberId", memberId);
			try {
				for (String product : productIds) {
					cartMap.put("productId", product.split(",")[0]);
					this.getBaseDao().delete(PRIFIX + ".deleteCartByProductId", cartMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createOrderItems(Map<String, Object> params, Object id) throws NumberFormatException, Exception {

		Map<String, Object> orderItem = new HashMap<String, Object>();

		Object productId = params.get("productId");

		Map<String, Object> productMap = this.getBaseDao().get(PRIFIX + ".findProductDetail",
				Long.parseLong(productId.toString()));

		orderItem.put("orders", id);

		orderItem.put("is_gift", productMap.get("is_gift"));
		orderItem.put("return_quantity", 0);
		orderItem.put("shipped_quantity", 0);
		orderItem.put("sn", productMap.get("sn"));
		orderItem.put("thumbnail", productMap.get("image"));

		orderItem.put("full_name", productMap.get("full_name"));
		orderItem.put("name", productMap.get("name"));

		long product_main_category = productMap.get("product_main_category") == null ? 0
				: Long.parseLong(productMap.get("product_main_category").toString());
		if (product_main_category == 6) {
			// 基础硬装价格需要乘以面积
			double price = Double.parseDouble(productMap.get("price").toString());
			double coveredArea = this.getBaseDao().getObject(PRIFIX + ".findCoveredAreaByMember", params);
			orderItem.put("price", (price * coveredArea));
		} else {
			orderItem.put("price", productMap.get("price"));
		}

		orderItem.put("weight", productMap.get("weight"));

		orderItem.put("create_date", DateUtils.getCurrentTime());
		orderItem.put("modify_date", DateUtils.getCurrentTime());

		orderItem.put("quantity", params.get("quantity"));

		orderItem.put("product", params.get("productId"));
		if (params.get("cartItemId") != null) {
			orderItem.put("cart_item_id", params.get("cartId"));
		}
		this.getBaseDao().save(PRIFIX + ".insertOrderItems", orderItem);
	}

	public void motifyOrder(Map<String, Object> params) throws Exception {

		if (params.get("addaressId") != null) {
			Map<String, Object> addressMap = this.getBaseDao().get(PRIFIX + ".findByAddressId", params);
			if (addressMap != null) {
				params.put("address", params.get("address"));
				params.put("area_name", params.get("area_name"));
				params.put("consignee", params.get("consignee"));
				params.put("phone", params.get("phone"));
				params.put("zip_code", params.get("zip_code"));
				params.put("area", params.get("area"));
			}
		}
		this.getBaseDao().update(PRIFIX + ".motifyOrder", params);
	}

	// 查询商品评价
	public Page findCommentByProductId(Map<String, Object> params) throws Exception {
		int currentPage = (params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString()) - 1);
		int pageSize = (params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString()));

		Page page = this.getBaseDao().page(PRIFIX + ".findCommentByProductId", params, currentPage, pageSize);
		Collection collection = page.getRows();
		for (Object object : collection) {
			if (object != null) {
				Map<String, Object> map = (Map<String, Object>) object;
				Object create_date = map.get("create_date");
				// Object buy_time = map.get("buy_time");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				if (create_date != null)
					map.put("create_date", sdf.format(create_date));
				// if(buy_time != null &&
				// StringUtils.isNotBlank(buy_time.toString()))map.put("buy_time",
				// sdf.format(buy_time.toString()));
			}
		}
		return page;
	}

	/**
	 * 检测该订单是否已评价
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Boolean isCommented(Map<String,Object> params) throws Exception {
		//0：待支付 1：已支付 2：已取消3：已收货4：已评价
		int status = this.getBaseDao().getObject(PRIFIX + ".findOrderStatus", params);
		if(status == 4) { return Boolean.valueOf(true); }
		return Boolean.valueOf(false);
	}
	
	// 一次评价多个商品
	@Transactional
	public void comments(Map<String, Object> params) throws Exception {

		String commentsInfo = (params.get("list") == null ? "" : params.get("list").toString());
		Gson gson = new GsonBuilder().create();
		List<Map<String, Object>> infoList = gson.fromJson(commentsInfo, List.class);
		for (Map<String, Object> map : infoList) {
			map.put("memberId", params.get("memberId"));
			map.put("item", params.get("orderId"));
			comment(map);
		}
		Map<String, Object> orderParamMap = new HashMap<String, Object>();
		orderParamMap.put("order_status", 4);
		orderParamMap.put("orderId", params.get("orderId"));
		this.getBaseDao().update(PRIFIX + ".motifyOrderStatus", orderParamMap);
	}

	// 评价商品
	public void comment(Map<String, Object> params) throws Exception {

		params.put("create_date", DateUtils.getCurrentTime());
		params.put("modify_date", DateUtils.getCurrentTime());

		params.put("ip", "");
		params.put("is_show", (byte) 1);

		this.getBaseDao().save(PRIFIX + ".insertComment", params);
	}

	// private Long findCartItemId(Map<String, Object> params) throws Exception
	// {
	// return this.getBaseDao().get(PRIFIX + ".findCartItemId", params);
	// }

	// 订单详情
	public Map<String, Object> orderDetail(Map<String, Object> params) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> orderMap = this.getBaseDao().get(PRIFIX + ".findOrderDetail", params);
		orderMap.put("create_date",
				orderMap.get("create_date") != null ? sdf.format((Date) orderMap.get("create_date")) : "");

		List orders = new ArrayList();
		orders.add(orderMap.get("id").toString());
		List<Map<String, Object>> list = this.getBaseDao().getList(PRIFIX + ".listItem", orders);

		Map<String, Object> addressMap = new HashMap<String, Object>();
		addressMap.put("address", orderMap.get("address"));
		addressMap.put("area_name", orderMap.get("area_name"));
		addressMap.put("phone", orderMap.get("phone"));
		addressMap.put("zip_code", orderMap.get("zip_code"));
		addressMap.put("area", orderMap.get("area"));
		addressMap.put("consignee", orderMap.get("consignee"));

		orderMap.remove("address");
		orderMap.remove("area_name");
		orderMap.remove("phone");
		orderMap.remove("zip_code");
		orderMap.remove("area");
		orderMap.remove("consignee");

		orderMap.put("address", addressMap);

		// 分类基础硬装
		for (Map<String, Object> item : list) {
			if (item.get("product_main_category") != null
					&& Integer.parseInt(item.get("product_main_category").toString()) == 6) {
				// 基础硬装
				Map<String, Object> baseMap = this.getBaseDao().getObject(PRIFIX + ".findBaseDecor", item.get("id"));
				item.put("image", baseMap.get("image"));
				item.put("linkUrl", baseMap.get("link_url"));
				item.put("isbase", "1");
			}
		}

		orderMap.put("orderItems", list);

		return orderMap;
	}

	// 订单操作
	public void operationOrder(Map<String, Object> params) throws Exception {
		Integer operation = Integer.parseInt(params.get("operation").toString());
		switch (operation) {
		case Constant.ORDER_CONFIRM:

			break;
		case Constant.ORDER_RECEICE: // 确认收货
			confirmReceive(params);
			break;
		case Constant.ORDER_CANCEL: // 取消
			cancel(params);
			break;
		case Constant.ORDER_PAY:// 支付 (回调改变状态)
			pay(params);
			break;
		default:
			break;
		}
		this.getBaseDao().update(PRIFIX + ".motifyOrderStatus", params);
	}

	// 支付
	private void pay(Map<String, Object> params) {
		params.put("order_status", Constant.PAY_HAVE);
		params.put("payment_status", Constant.PAY_HAVE);
	}

	// 取消订单
	private void cancel(Map<String, Object> params) {
		params.put("order_status", Constant.ORDER_STATUS_QUIT);
	}

	// 确认收货
	private void confirmReceive(Map<String, Object> params) {
		params.put("shipping_status", Constant.SHIP_RECEIVE);
		params.put("order_status", Constant.ORDER_RECEICE);
	}

	@Transactional
	public void removeOrder(Map<String, Object> params) throws Exception {
		this.getBaseDao().delete(PRIFIX + ".deleteOrder", params);
		this.getBaseDao().delete(PRIFIX + ".deleteOrderItem", params);
	}

	// 删除购物车
	@Transactional
	public void removeCartItem(Map<String, Object> params) throws Exception {
		// this.getBaseDao().delete(PRIFIX + ".deleteCart", params);
		String cartIds = params.get("cartItemId").toString();
		String[] cartIdss = cartIds.split(",");
		for (String cartItemId : cartIdss) {
			Map<String, Object> countMap = this.countCartNumByCartItemId(Long.parseLong(cartItemId));
			Integer count = countMap.get("cartnum") == null ? null
					: Integer.parseInt(countMap.get("cartnum").toString());
			Long cartId = countMap.get("cartId") == null ? null : Long.parseLong(countMap.get("cartId").toString());

			this.getBaseDao().delete(PRIFIX + ".deleteCartItem", Long.parseLong(cartItemId));
			// 如果cartitem只有一条数据，则删除cart对应记录
			if (count != null && count.intValue() == 1) {
				this.removeCart(cartId);
			}

		}
	}

	// 删除购物车
	public void removeCart(long cartId) throws Exception {
		this.getBaseDao().delete(PRIFIX + ".deleteCart", cartId);
	}

	private Map<String, Object> countCartNumByCartItemId(long itemId) throws Exception {
		return this.getBaseDao().get(PRIFIX + ".countCartNumByCartItemId", itemId);
	}

	// 查询订单列表
	public Page findOrderList(Map<String, Object> params) throws Exception {
		params.put("type", params.get("type") == null ? 0 : Integer.parseInt(params.get("type").toString()));
		Integer pageNum = params.get("pageNum") == null ? 0 : (Integer.parseInt(params.get("pageNum").toString()) - 1);
		Integer pageSize = params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString());
		Page page = this.getBaseDao().page(PRIFIX + ".findOrderByMemberId", params, pageNum, pageSize);
		Collection collection = page.getRows();

		HashMap<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		List orders = new ArrayList();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (Object object : collection) {
			if (object != null) {
				Map<String, Object> orderMap = (Map<String, Object>) object;
				orderMap.put("create_date",
						orderMap.get("create_date") != null ? sdf.format((Date) orderMap.get("create_date")) : "");
				map.put(orderMap.get("id").toString(), orderMap);
				orders.add(orderMap.get("id").toString());
			}
		}
		if (page.getRows() != null && page.getRows().size() > 0) {
			List<Map<String, Object>> list = this.getBaseDao().getList(PRIFIX + ".listItem", orders);
			for (Map<String, Object> item : list) {

				Object orderObj = item.get("orders");
				if (orderObj != null) {
					Map<String, Object> orderMap = map.get(orderObj.toString());
					if (orderMap != null) {
						Object itemListObj = orderMap.get("orderItems");
						if (itemListObj != null) {
							List<Map<String, Object>> itemList = (List<Map<String, Object>>) itemListObj;
							item.remove("orders");
							itemList.add(item);
							// orderMap.put("orderItems", itemList);
						} else {
							List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
							item.remove("orders");
							itemList.add(item);
							orderMap.put("orderItems", itemList);
						}
					}
					if (item.get("product_main_category") != null
							&& Integer.parseInt(item.get("product_main_category").toString()) == 6) {
						// 基础硬装
						Map<String, Object> baseMap = this.getBaseDao().getObject(PRIFIX + ".findBaseDecor",
								item.get("id"));
						item.put("image", baseMap.get("image"));
						item.put("linkUrl", baseMap.get("link_url"));
						item.put("isbase", "1");
					}
				}
			}
		}
		return page;
	}

	// 订单列表
	public Map<String, Object> findCart(Map<String, Object> params) throws Exception {
		List<CartVO> cartList = this.getBaseDao().getList(PRIFIX + ".findCartList", params);
		Map<String, List> resultMap = new HashMap<String, List>();

		init(resultMap);
		for (CartVO cartVO : cartList) {
			List<CartItemVO> cartItemList = cartVO.getList();
			for (CartItemVO cartItemVO : cartItemList) {
				int pcid = cartItemVO.getProduct().getPcid().intValue();
				if (pcid == 1 || pcid == 2 || pcid == 4) {
					resultMap.get("group").add(cartItemVO);
				} else if (pcid == 5) {
					resultMap.get("space").add(cartItemVO);
				} else if (pcid == 3) {
					resultMap.get("alone").add(cartItemVO);
				} else if (pcid == 6) {
					long productId = cartItemVO.getProduct().getPid();
					Map<String, Object> baseMap = this.getBaseDao().getObject(PRIFIX + ".findBaseDecor", productId);
					if (baseMap != null && baseMap.size() > 0) {
						cartItemVO.getProduct().setImage(baseMap.get("image").toString());
						cartItemVO.getProduct().setLinkUrl(baseMap.get("link_url").toString());
					}
					resultMap.get("basic").add(cartItemVO);
				}
			}
		}
		HashMap<String, Object> totalMap = new HashMap<String, Object>();
		totalMap.put("cart", resultMap);
		return totalMap;
	}

	private void init(Map<String, List> resultMap) {
		List<CartItemVO> spaceList = new ArrayList<CartItemVO>();
		List<CartItemVO> groupList = new ArrayList<CartItemVO>();
		List<CartItemVO> aloneList = new ArrayList<CartItemVO>();
		List<CartItemVO> basic = new ArrayList<CartItemVO>();

		resultMap.put("space", spaceList);
		resultMap.put("group", groupList);
		resultMap.put("alone", aloneList);
		resultMap.put("basic", basic);
	}

	public void toEntityByAlipay(HttpServletRequest request) throws Exception {

		String status = request.getParameter("trade_status").toString(); // 状态；
		String sn = request.getParameter("out_trade_no").toString(); // 订单号
		if ("SUCCESS".equals(status) || "FINISH".equals(status)) {
			savePayment(sn);
		}
	}

	public void toEntityByWeixin(HttpServletRequest request) throws Exception {

		String sn = request.getParameter("out_trade_no");
		String status = request.getParameter("result_code");

		if ("SUCCESS".equals(status)) {
			savePayment(sn);
		}
	}

	// 创建记录对象
	@Transactional
	public Payment savePayment(String sn) throws Exception {

		Payment payment = new Payment();
		Map<String, Object> orderMap = this.getBaseDao().getObject(PRIFIX + ".findOrderBySn", sn);
		Map<String, Object> paymentMap = this.getBaseDao().getObject(PRIFIX + ".findPaymentBySn", sn);

		if (paymentMap == null) {
			Double price = 0.0;
			if (orderMap != null) {
				String _sn = orderMap.get("sn") != null ? orderMap.get("sn").toString() : "";
				if (StringUtils.isNotBlank(_sn)) {
					List<Map<String, Object>> ordetItems = this.getBaseDao().getList(PRIFIX + ".findOrderItemByOrderId",
							sn);
					for (Map<String, Object> map : ordetItems) {
						if (map.get("price") != null) {
							price += Double.parseDouble(map.get("price").toString());
						}
					}
					payment.setCreate_date(DateUtils.getCurrentTime());
					payment.setMotify_date(DateUtils.getCurrentTime());
					payment.setAccout("");
					payment.setBank("");
					payment.setMemo("");

					payment.setSn(sn);
					payment.setType(0);
					payment.setMethod(0);
					payment.setStatus(0);
					payment.setPaymemntMethod(orderMap.get("payment_method_name") == null ? "1"
							: orderMap.get("payment_method_name").toString());
					payment.setFee(0.0);
					payment.setAmount(price);
					payment.setPaymentPluginId("alipayBankPlugin");
					payment.setExpire(orderMap.get("expire") == null ? "" : orderMap.get("expire").toString());
					payment.setOrders(Long.parseLong(orderMap.get("id").toString()));
					payment.setMember(
							orderMap.get("member") == null ? 0L : Long.parseLong(orderMap.get("member").toString()));

					this.getBaseDao().save(PRIFIX + ".insertPayment", payment);
				}
			}
			Map<String, Object> opMap = new HashMap<String, Object>();
			opMap.put("operation", Constant.ORDER_PAY);
			opMap.put("orderId", orderMap.get("id"));
			this.operationOrder(opMap);
		}
		return payment;
	}

	// 查看用户购物车是否有基础硬装
	public Map<String, Object> isExistBaseDecor(Map<String, Object> params) throws Exception {

		List<Map<String, Object>> list = this.getBaseDao().getList(PRIFIX + ".findBaseDecorByMemberId", params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new HashMap<String, Object>();
	}

	// 修改购物车信息
	public void modifyCart(Map<String, Object> params) throws Exception {
		this.getBaseDao().update(PRIFIX + ".modifyCart", params);
	}

	public static void main(String[] args) {
		// String xml =
		// "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type>"
		// +
		// "<fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id>"
		// +
		// "<nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>"
		// +
		// "<out_trade_no><![CDATA[1409811653]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code>"
		// +
		// "<sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id><time_end><![CDATA[20140903131540]]></time_end>"
		// +
		// "<total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";

		// XxShopServiceImpl x = new XxShopServiceImpl();
		// try {
		// x.toEntityByWeixin(xml);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }\

	}
}
