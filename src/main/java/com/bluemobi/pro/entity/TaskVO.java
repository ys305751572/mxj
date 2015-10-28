package com.bluemobi.pro.entity;

import java.util.List;

/**
 * 验收任务
 * 
 * @author yesong
 *
 */
public class TaskVO {

	private Long id;
	private String name;

	private List<TaskNodeVO> nodes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TaskNodeVO> getNodes() {
		return nodes;
	}

	public void setNodes(List<TaskNodeVO> nodes) {
		this.nodes = nodes;
	}

}
