/**
 * 
 * AbstractStatusCheckResult.java
 * 版本所有 深圳市蜂鸟娱乐有限公司 2013-2014
 */
package com.hummingbird.common.face.statuscheck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author john huang
 * 2015年3月5日 下午6:54:25
 * 本类主要做为状态报告类，这个类
 */
public class AbstractStatusCheckResult implements StatusCheckResult {

	/**
	 * 状态级别，分为3分，2-异常，需要马上报警，1-报告，可能有问题，或存在一些需要报告的内容，这也需要报告，由运维判断是否处理，0-正常，不需要报告
	 */
	protected int statusLevel=0;
	
	protected String report;
	/**
	 * 功能名称
	 */
	protected String functionName="未命名";
	
	protected List<StatusCheckResult> items = new ArrayList<StatusCheckResult>();

	/**
	 * 功能代码
	 */
	private String funcCode;
	
	public AbstractStatusCheckResult(){
		
	}
	public AbstractStatusCheckResult(String functionName){
		this.functionName = functionName;
	}
	
	
	
	public AbstractStatusCheckResult(int statusLevel, String report) {
		super();
		this.statusLevel = statusLevel;
		this.report = report;
	}
	
	public AbstractStatusCheckResult(String functionName, int statusLevel,
			String report) {
		super();
		this.functionName = functionName;
		this.statusLevel = statusLevel;
		this.report = report;
	}
	/* (non-Javadoc)
	 * @see com.hummingbird.common.face.statuscheck.IStatusCheckResult#getStatusLevel()
	 */
	public int getStatusLevel() {
		//return statusLevel;
		int max=statusLevel;
		if(max==2){
			return max;
		}
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			StatusCheckResult statusCheckItemResult = (StatusCheckResult) iterator
					.next();
			if(statusCheckItemResult.getStatusLevel()>max){
				max = statusCheckItemResult.getStatusLevel();
				if(max==2){
					return max;
				}
			}
		}
		return max;
	}
	
	public String getReport(){
		return report;
	}

	

	
	public void addItem(StatusCheckResult item){
		this.items.add(item);
	}
	

	/**
	 * @return the functionName
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	/**
	 * @param statusLevel the statusLevel to set
	 */
	public void setStatusLevel(int statusLevel) {
		this.statusLevel = statusLevel;
	}

	/* (non-Javadoc)
	 * @see com.hummingbird.common.face.statuscheck.StatusCheckResult#getResultReport()
	 */
	@Override
	@JsonIgnore
	public String getResultReport() {
		return getReport();
	}

	/* (non-Javadoc)
	 * @see com.hummingbird.common.face.statuscheck.StatusCheckResult#getSubStatusCheckResult()
	 */
	@Override
	public List<StatusCheckResult> getSubStatusCheckResult() {
		return items;
	}
	
	public void  setSubStatusCheckResult(ArrayList<AbstractStatusCheckResult> itemlist){
		if(items==null){
			items = new ArrayList<StatusCheckResult>();
		}
		else{
			items.clear();
		}
		items.addAll(itemlist);
	}
	

	/* (non-Javadoc)
	 * @see com.hummingbird.common.face.statuscheck.StatusCheckResult#getFuncname()
	 */
	@Override
	@JsonIgnore
	public String getFuncname() {
		return getFunctionName();
	}

	/**
	 * @param report the report to set
	 */
	public void setReport(String report) {
		this.report = report;
	}
	/* (non-Javadoc)
	 * @see com.hummingbird.common.face.statuscheck.StatusCheckResult#isNormal()
	 */
	@Override
	@JsonIgnore
	public boolean isNormal() {
		return 0==statusLevel;
	}
	/* (non-Javadoc)
	 * @see com.hummingbird.common.face.statuscheck.StatusCheckResult#getFuncCode()
	 */
	@Override
	public String getFuncCode() {
		return funcCode;
	}
	/**
	 * @param funcCode the funcCode to set
	 */
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractStatusCheckResult [statusLevel=" + statusLevel
				+ ", report=" + report + ", functionName=" + functionName
				+ ", items=" + items + ", funcCode=" + funcCode + "]";
	}

	@JsonIgnore
	public String getStatusLevelCN(){
		switch (statusLevel) {
		case 0:
			return "正常";
		case 1:
			return "告警";
		case 2:
			return "警报";

		default:
			return "正常";
		}
		
	}

}
