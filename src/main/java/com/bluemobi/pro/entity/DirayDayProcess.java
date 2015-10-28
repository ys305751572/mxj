package com.bluemobi.pro.entity;

import java.util.List;

// 日记详情 -单日进度
public class DirayDayProcess {

	private Long id;
	private String create_date;
	private String day;
	private String weather;
	private String summary;
	
	private Integer image_count;
	private Integer review_count;

	private Integer is_verify;

	private List<Process> processes;

	private List<BaseUserVO> baseusers;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getImage_count() {
		return image_count;
	}

	public void setImage_count(Integer image_count) {
		this.image_count = image_count;
	}

	public Integer getReview_count() {
		return review_count;
	}

	public void setReview_count(Integer review_count) {
		this.review_count = review_count;
	}

	public Integer getIs_verify() {
		return is_verify;
	}

	public void setIs_verify(Integer is_verify) {
		this.is_verify = is_verify;
	}

	public List<Process> getProcesses() {
		return processes;
	}

	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}

	public List<BaseUserVO> getBaseusers() {
		return baseusers;
	}

	public void setBaseusers(List<BaseUserVO> baseusers) {
		this.baseusers = baseusers;
	}

}
