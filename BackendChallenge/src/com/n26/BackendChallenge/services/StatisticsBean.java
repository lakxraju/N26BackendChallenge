package com.n26.BackendChallenge.services;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatisticsBean {
	public Double sum;
	public Double average;
	public Double min;
	public Double max;
	public Long count;
	public StatisticsBean(){
		
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	
}
