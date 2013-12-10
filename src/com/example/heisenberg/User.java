package com.example.heisenberg;

import android.content.SharedPreferences;
import android.app.Activity;

public class User {
	private String email;
	private boolean admin;
	private String name;
	
	// The name of the preferences file to store on android device
	public static final String PREFS_NAME = "UserFile";
	
	// Can only be used in main thread :(
	public User(String email, String name, boolean admin) {
		this.email = email;
		this.name = name;
		this.admin = admin;
	}
	
	// check to see if a user is logged in
	public static boolean loggedIn(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,0);
		return settings.getBoolean("loggedIn", false);
	}
	
	// log a user in
	public void logIn(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString("email",email);
		editor.putString("name", name);
		editor.putBoolean("admin", admin);
		editor.putBoolean("loggedIn", true);
		
		editor.commit();
	}
	
	// log a user out
	public void logOut(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString("email","");
		editor.putString("name", "");
		editor.putBoolean("admin", false);
		editor.putBoolean("loggedIn", false);
		
		editor.commit();
	}
	
	// return null if nobody logged in, logged in user if someone is logged in
	public static User getLoggedInUser(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,0);
		if (settings.getBoolean("loggedIn", false) == false) {
			return null;
		}
		return new User(settings.getString("email", ""), settings.getString("name", ""), settings.getBoolean("admin", false));
	}
	
	// get user email
	public String getEmail() {
		return email;
	}
	
	// check if user is an admin
	public boolean isAdmin() {
		return admin;
	}
	
	// get user name
	public String getName() {
		return name;
	}
}
