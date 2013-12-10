package com.example.heisenberg;

/**
 * Item class to hold info about an item and easily pass between views
 **/
public class Item {
	private String name, description, location, start, end;
	private Double cost;
	private int discount;

	public Item(String name, String description, Double cost, String location, int discount, String start, String end){
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.location = location;
		this.discount = discount;
		this.start = start;
		this.end = end;
	}
	
	// return name of item
	public String getName(){
		return name;
	}
	
	// return description
	public String getDescription(){
		return description;
	}
	
	// return cost
	public Double getCost(){
		return cost;
	}
	
	// return location
	public String getLocation(){
		return location;
	}
	
	// return discount
	public int getDiscount(){
		return discount;
	}
	
	// return start date
	public String getStartDate(){
		return start;
	}
	
	// return end date
	public String getEndDate(){
		return end;
	}

}
