package com.example.heisenberg;

public class Item {
	private String name, description, location;
	private Double cost;
	private int discount;
	
	public Item(String name, String description, Double cost, String location, int discount){
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.location = location;
		this.discount = discount;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public Double getCost(){
		return cost;
	}
	
	public String getLocation(){
		return location;
	}
	
	public int getDiscount(){
		return discount;
	}

}
