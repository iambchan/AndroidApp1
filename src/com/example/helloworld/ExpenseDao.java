package com.example.helloworld;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

// Data access object
public class ExpenseDao {
	private SQLiteDatabase db;
	
	public ExpenseDao(SQLiteDatabase db) { 
		this.db = db;
	}
	
	public void insert(Expense expense) {
		ContentValues values = new ContentValues();
		//values.put(ExpenseEntry.COLUMN_NAME_ENTRY_ID, expense.getId());
		values.put(ExpenseEntry.COLUMN_NAME_COST, expense.getCost());
		values.put(ExpenseEntry.COLUMN_NAME_TYPE, expense.getType());
		values.put(ExpenseEntry.COLUMN_NAME_DESCRIPTION, expense.getDescription());
		values.put(ExpenseEntry.COLUMN_NAME_DATE, expense.getDate().toString());
		db.insert(ExpenseEntry.TABLE_NAME, null, values);
	}
	
	// Returns all expenses from database
	public Cursor query(){
		// SELECT * FROM EXPENSES
		return db.query(ExpenseEntry.TABLE_NAME, null, null, null, null, null, null);
	}
	
	public Cursor queryByMonth() {
		//"SELECT * FROM EXPENSES WHERE DATE LIKE \"%Aug%\"";
		return db.query(ExpenseEntry.TABLE_NAME, null,"date like ?" , new String[]{"%"+"Aug"+"%"+"2013"}, null, null, null);
	}
	
	// Gets the cost column of all expenses in given month
	public Cursor queryCurMonth_expenses(String month, String year) {
		//"SELECT * FROM EXPENSES WHERE DATE LIKE \"%Aug%\"";
		String[] colsToSelect = new String[] { ExpenseEntry.COLUMN_NAME_COST, ExpenseEntry.COLUMN_NAME_DATE };
		return db.query(ExpenseEntry.TABLE_NAME, colsToSelect, "date like ?" , new String[]{"%"+month+"%"+year}, null, null, null);
	}

	
	public void drop() {
		db.execSQL(SQL_DELETE_ENTRIES);
		// need to recreate the table with no entries
		db.execSQL(SQL_CREATE_ENTRIES);
		
	}
	
	
	
	public static abstract class ExpenseEntry implements BaseColumns {
		public static final String TABLE_NAME = "expenses";
		public static final String COLUMN_NAME_ENTRY_ID = "entryid";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_COST = "cost";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_DESCRIPTION = "location";
		
	}
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_ENTRIES = 
			"CREATE TABLE " + ExpenseEntry.TABLE_NAME + " ("
			+ ExpenseEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY,"
			+ ExpenseEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP 
			+ ExpenseEntry.COLUMN_NAME_COST + TEXT_TYPE + COMMA_SEP
			+ ExpenseEntry.COLUMN_NAME_DATE + " DATE" + COMMA_SEP
			+ ExpenseEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE
			+ " )";
	
	private static final String SQL_DELETE_ENTRIES = 
			"DROP TABLE IF EXISTS " + ExpenseEntry.TABLE_NAME;
}
