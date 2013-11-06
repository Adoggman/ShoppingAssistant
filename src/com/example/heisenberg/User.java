package com.example.heisenberg;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;

public class User {
	private String email;
	private boolean admin;
	private String name;
	
	public static final String PREFS_NAME = "UserFile";
	
	public static boolean loggedIn() {
		return false;
	}
	
	// Can only be used in main thread :(
	public User(String email, String name, boolean admin) {
		this.email = email;
		this.name = name;
		this.admin = admin;
	}
	
	public void logIn(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString("email",email);
		editor.putString("name", name);
		editor.putBoolean("admin", admin);
		editor.putBoolean("loggedIn", true);
		
		editor.commit();
	}
	
	public void logOut(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString("email","");
		editor.putString("name", "");
		editor.putBoolean("admin", false);
		editor.putBoolean("loggedIn", false);
		
		editor.commit();
	}
	
	/**
	 * 
	 * @return null if nobody logged in, logged in user if someone is logged in
	 */
	public static User getLoggedInUser(Activity activity) {
		SharedPreferences settings = activity.getPreferences(0);
		if (settings.getBoolean("loggedIn", false) == false) {
			return null;
		}
		User user = new User(settings.getString("email", ""), settings.getString("name", ""), settings.getBoolean("admin", false));
		return user;
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public String getName() {
		return name;
	}
}
