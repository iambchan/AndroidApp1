package com.example.helloworld;

import java.util.Date;


public class Expense {
	private int id;
	private String type;
	private double cost;
	private Date date;
	private String description;
	public String getDescription() {
		return description;
	}
	public void setLocation(String location) {
		this.description = location;
	}
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	
}
