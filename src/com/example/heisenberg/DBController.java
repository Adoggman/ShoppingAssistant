package com.example.heisenberg;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DBController  extends SQLiteOpenHelper {
  private static final String LOGCAT = null;
  private SQLiteDatabase database;
  
  public DBController(Context applicationcontext) {
    super(applicationcontext, "androidsqlite.db", null, 2);
    Log.d(LOGCAT,"Created");
  }
 
  @Override
  public void onCreate(SQLiteDatabase database) {
    String query;
    query = "CREATE TABLE items ( itemId INTEGER PRIMARY KEY, itemName TEXT, itemDescription TEXT, itemCost REAL)";
    database.execSQL(query);
    Log.d(LOGCAT,"items Created");
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
    String query;
    query = "DROP TABLE IF EXISTS items";
    database.execSQL(query);
    onCreate(database);
  }
  
  private void openDB() throws SQLException {
	  database = this.getWritableDatabase();
  }
  
  private void closeDB() {
	  if (database != null)
		  database.close();
  }
 
  public void insertItem(String name, String description, Double cost) {
    ContentValues cv = new ContentValues();
    cv.put("itemName", name);
    cv.put("itemDescription", description);
    cv.put("itemCost", cost);
    openDB();
    database.insert("items", null, cv);
    closeDB();
  }
  
  public void resetTable(){
	  openDB();
	  database.execSQL("DELETE FROM items");
	  closeDB();
  }
 
  public int updateItem(HashMap<String, String> queryValues) {
    SQLiteDatabase database = this.getWritableDatabase();  
    ContentValues values = new ContentValues();
    values.put("itemName", queryValues.get("itemName"));
    return database.update("items", values, "itemId" + " = ?", new String[] { queryValues.get("itemId") });
  }
 
  public void deleteItem(String id) {
    Log.d(LOGCAT,"delete");
    SQLiteDatabase database = this.getWritableDatabase();  
    String deleteQuery = "DELETE FROM  items where itemId='"+ id +"'";
    Log.d("query",deleteQuery);   
    database.execSQL(deleteQuery);
  }
 
  public ArrayList<HashMap<String, String>> getAllItems() {
    ArrayList<HashMap<String, String>> wordList;
    wordList = new ArrayList<HashMap<String, String>>();
    String selectQuery = "SELECT  * FROM items";
    SQLiteDatabase database = this.getWritableDatabase();
    Cursor cursor = database.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("itemId", cursor.getString(0));
        map.put("itemName", cursor.getString(1));
        wordList.add(map);
      } while (cursor.moveToNext());
    }
    return wordList;
  }
  
  public Item getItem(String id){
	  openDB();
	  Item i = null;
	  Cursor cursor = database.rawQuery("SELECT * FROM items where itemId = '" + id + "'", null);
	  if (cursor.moveToFirst()){
		  // Columns: 1 - name, 2 - description, 3 - cost
		  i = new Item(cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)));
	  }
	  closeDB();
	  return i;
  }
}