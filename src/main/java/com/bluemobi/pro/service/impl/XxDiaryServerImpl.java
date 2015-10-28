package com.bluemobi.pro.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluemobi.pro.entity.DirayDayProcess;
import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.BaseService;
import com.bluemobi.utils.DateUtils;

@Service
public class XxDiaryServerImpl extends BaseService{

	public static final String PRIFIX = XxDiaryServerImpl.class.getName();
	
	// 日记列表
		@SuppressWarnings("rawtypes")
		public Page diaryList(Map<String,Object> params) throws Exception {
			
			// TODO 缺少日记首页图片字段 
			Integer pageNum = params.get("pageNum") == null ? 0 :( Integer.parseInt(params.get("pageNum").toString()) - 1);
			Integer pageSize = params.get("pageSize") == null ? 10 : Integer.parseInt(params.get("pageSize").toString());
			return this.getBaseDao().page(PRIFIX + ".page", params, pageNum, pageSize);
		}
		
		// 日记详情-基本信息
		@Transactional
		public Map<String,Object> diaryDetail(Map<String,Object> params) throws Exception {
			
			Map<String,Object> resultMap = this.getBaseDao().getObject(PRIFIX + ".diaryDetailInfo", params);
			List favoriteMap = this.getBaseDao().getList(PRIFIX + ".getFavorite", params);
			if(resultMap != null && resultMap.size() > 0) {
				if(favoriteMap != null && favoriteMap.size() > 0) {
					resultMap.put("favorite", "1");
				}
				else {
					resultMap.put("favorite", "0");
				}
			}
			return resultMap;
		}
		
		// 日记详情-施工进度列表
		public List diaryDetailWorkList(Map<String,Object> params) throws Exception {
			
			List<DirayDayProcess> list = this.getBaseDao().getList(PRIFIX + ".detailWorkList", params);
			return list;
		}
		
		// 单日施工详情-进度基本信息
		public Map<String,Object> todayDiaryDetailWorkInfo(Map<String,Object> params) throws Exception {
			return this.getBaseDao().getObject(PRIFIX + ".diaryWorkInfo", params);
		}
		
		// 单日施工详情-图片列表
		public List todayDiaryDetailImageList(Map<String,Object> params) throws Exception {
			return this.getBaseDao().getList(PRIFIX + ".diaryItemImageList", params);
		}
		
		// 单日施工详情-评论列表
		public List todayDiaryDetailCommentList(Map<String,Object> params) throws Exception {
			return this.getBaseDao().getList(PRIFIX + ".diaryItemCommentList", params);
		}
		
		// 评论单日施工
		public void comment(Map<String,Object> params) throws Exception {
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("create_date", DateUtils.getCurrentTime());
			paramMap.put("modify_date", DateUtils.getCurrentTime());
			paramMap.put("content", params.get("content"));
			paramMap.put("ip", params.get("ip"));
			paramMap.put("member", params.get("memberId"));
			paramMap.put("reply_member", params.get("replyMemberId"));
			paramMap.put("diary_item", params.get("diaryItemId"));
			this.getBaseDao().save(PRIFIX + ".comment", paramMap);
		}
		
		@Transactional
		public void favorite(Map<String,Object> params) throws Exception {
			
			this.getBaseDao().save(PRIFIX + ".favorite", params);
			this.getBaseDao().save(PRIFIX + ".updateFavoriteCount", params);
		}
		
		public void accept(Map<String,Object> params) throws Exception {
			this.getBaseDao().update(PRIFIX + ".accept", params);
		}
		
		/**
		 * 我的日记动态
		 * @param params
		 * @return
		 * @throws Exception
		 */
		public Map<String,Object> dynamic(Map<String,Object> params) throws Exception {
			List<Map<String,Object>> commentList = this.getBaseDao().getList(PRIFIX + ".findDairyComment", params);
			List<Map<String,Object>> reviewList = this.getBaseDao().getList(PRIFIX + ".findDairyReview", params);
			List<Map<String,Object>> favoriteList = this.getBaseDao().getList(PRIFIX + ".findDairyFavorite", params);
			
			Map<String,Object> allMap = new HashMap<String,Object>();
			allMap.put("comment", commentList);
			allMap.put("review", reviewList);
			allMap.put("favorite", favoriteList);
			return allMap;
		}
}
