package com.example.heisenberg;

public class Item {
	private String name, description;
	private Double cost;
	
	public Item(String name, String description, Double cost){
		this.name = name;
		this.description = description;
		this.cost = cost;
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

}
