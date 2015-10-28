package com.bluemobi.pro.entity;

/**
 * 日记详情-每日计划进度
 * @author yesong
 *
 */
public class ProcessVO {

	private Long pid;
	private String process_name;
	private Integer state;
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getProcess_name() {
		return process_name;
	}
	public void setProcess_name(String process_name) {
		this.process_name = process_name;
	}
}
