package com.bluemobi.pro.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluemobi.sys.service.BaseService;

@Service
public class UhoemTaskServiceImpl extends BaseService {

	public static final String PRIFIX = UhoemTaskServiceImpl.class.getName();

	// 查询验收任务
	public List<Map<String, Object>> findAllTask(Map<String,Object> params) throws Exception {
		List<Map<String, Object>> list = this.getBaseDao().getList(PRIFIX + ".findTaskList", params);
		
		for (Map<String, Object> map : list) {
			if(Integer.parseInt(map.get("status").toString()) == 3) {
				map.put("ctime", map.get("redo_time"));
			}
		}
		return list;
	}

	// 查询任务节点
	@Transactional
	public List<Map<String, Object>> findTaskNode(Map<String, Object> params) throws Exception {
		
		Integer status = this.getBaseDao().getObject(PRIFIX + ".findTaskById", params);
		if(status != null) {
			if(status == 0) {
				params.put("status", 1);
				this.getBaseDao().update(PRIFIX + ".updateTaskStatus", params);
			}
			else if(status == 3){
				params.put("status", 4);
				this.getBaseDao().update(PRIFIX + ".updateTaskStatus", params);
			}
		}
		
		List<Map<String, Object>> taskNodes = this.getBaseDao().getList(PRIFIX + ".findTaskNodeList", params);
		return taskNodes;
	}

	// 验收选项
	@Transactional
	public void accpetTask(Map<String, Object> params) throws Exception {

		int all = params.get("all") == null ? 0 : Integer.parseInt(params.get("all").toString()); // 默认非全选
		int accept = params.get("accept") == null ? 1 : Integer.parseInt(params.get("accept").toString()); // 默认认可
		
		if(accept == 1) {
			// 认可
			if (all == 1) {
				params.put("status",2);
				this.getBaseDao().update(PRIFIX + ".updateTaskStatus", params);
			}

			String nodes = params.get("nodes").toString();
			String projectId = params.get("projectId").toString();
			String memberId = params.get("memberId").toString();
			String[] nodess = nodes.split(",");
			for (String nodeId : nodess) {

				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("memberId", memberId);
				paramMap.put("nodeId", nodeId);
				paramMap.put("projectId", projectId);
				this.getBaseDao().save(PRIFIX + ".updateNodeStatus", paramMap);
			}
		}
		else {
			// 不认可
			params.put("status", 3);
			this.getBaseDao().update(PRIFIX + ".updateTaskStatus", params);
		}
	}
	
	// 判断是否有新验收
	public Map<String,Object> isNewAccept(Map<String,Object> params) throws Exception{
		
		List<Map<String,Object>> memberTaskList = this.getBaseDao().getList(PRIFIX + ".findTaskList", params);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String projectname = null;
		String address = null;
		String baseusername = null;
		String ctime = null;
		int status = 0;
		int memberTaskId = 0;
		int accept = 0;
		int index = 1;
		
		if(memberTaskList != null && memberTaskList.size() > 0) {
			for (Map<String, Object> map : memberTaskList) {
				++index;
				status = map.get("status") == null ? 0 : Integer.parseInt(map.get("status").toString());
				if(status == 0 || status == 3 || (index == memberTaskList.size())) {
					Map<String,Object> rMap = this.getBaseDao().get(PRIFIX + ".findProjectName", map);
					
					if(rMap != null) {
						projectname = rMap.get("projectname") == null ? "" : rMap.get("projectname").toString();
						address = rMap.get("address") == null ? "" : rMap.get("address").toString();
						baseusername = this.getBaseDao().get(PRIFIX + ".findBaseUsername", map);
						ctime = map.get("ctime") == null ? "" : map.get("ctime").toString();
						memberTaskId = Integer.parseInt(map.get("memberTaskId").toString());
						accept = Integer.parseInt(map.get("accept") == null ? "0" : map.get("accept").toString());
					}
					break;
				}
			}
			if(memberTaskId != 0) {
				resultMap.put("baseUserName", baseusername == null ? "" : baseusername);
				resultMap.put("projectName", projectname == null ? "" : projectname);
				resultMap.put("address", address);
				resultMap.put("ctime", ctime);
				resultMap.put("status", status == 0 ? 1 : status);
				resultMap.put("memberTaskId", memberTaskId);
			}
			else {
				resultMap.put("status", "0");
			}
			
		}
		return resultMap;
	}
	
	private boolean last() {
		return false;
	}

	// 是否亲自参加
	public void isJoin(Map<String,Object> params) throws Exception {
		this.getBaseDao().update(PRIFIX + ".updateMemberTask", params);
	}
	
	public static void main(String[] args) {
		String s = "白云街道|啦啦啦";
		s = s.replaceAll("\\|", "");
		System.out.println(s);
	}
}
