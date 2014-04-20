/*
 *
 *  Copyright (C) Roberto Calvo Palomino
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/. 
 *
 *  Author : Roberto Calvo Palomino <rocapal at gmail dot com>
 *
 */

package es.curso.android.database;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ClientsSQLite extends SQLiteOpenHelper {

	
	public class Client
	{
		int id;
		String name;
		String email;
		String datetime;
	}
	
	final static String DATABASE_NAME = "Clients";
	final static String TABLE_NAME = "client";
	
	public ClientsSQLite(Context context) {
		
		super(context, DATABASE_NAME, null, 1);
	}

	@Override 
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("+
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"name TEXT, email TEXT, datetime DATETIME)");
	}


	public Boolean saveClient (String name, String email, String datetime) {
		
		if (name == null || name.isEmpty() || 
		    email == null || email.isEmpty() || datetime.isEmpty())
		{
			Log.w("ClientsSQLite", "input date is null");
			return false;
		}
		
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES ( null, " + 
				"'" + name+"', '"+email+"', '"+ datetime +"')");
		db.close();
		
		return true;
	}


	public Vector<Client> getClient (String name) {
		
		Vector<Client> result = new Vector<Client>();		
		SQLiteDatabase db = getReadableDatabase();
		
		String query = "";
		if (name != null)
			query = "SELECT * FROM " + TABLE_NAME + " WHERE name='" + name + "'";
		else
			query = "SELECT * FROM " + TABLE_NAME + " ORDER BY datetime DESC";
						
		Cursor cursor = db.rawQuery(query, null);
		
		while (cursor.moveToNext()){
			Client c = new Client();
			c.id = cursor.getInt(0);
			c.name = cursor.getString(1);
			c.email = cursor.getString(2);
			c.datetime = cursor.getString(3);
			result.add(c);
		}
		cursor.close();
		db.close();
		return result;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}


}