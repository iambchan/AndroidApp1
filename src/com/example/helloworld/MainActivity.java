package com.example.helloworld;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.helloworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	
	public final static String EXTRA_MESSAGE= "com.example.helloworld.MESSAGE";
	private ExpenseDbHelper dbHelper;
	public static ArrayList<Expense> expenseRecords = null;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ExpenseDbHelper(getApplicationContext());
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    /*called when user clicks on add new expense button*/
    public void addNewExpense(View view){
    	Intent intent = new Intent(this, AddExpenseActivity.class);
    	startActivity(intent);
    }
    
    // Show expenses - called when user clicks on show expenses button
    // Gets the data from the data base - only shows month by month
    public void showExpenses(View view) {
    	
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	ExpenseDao expenseObj = new ExpenseDao(db);
    	Cursor c = expenseObj.query();
    	int numExpenses = c.getCount();
    	intent.putExtra(EXTRA_MESSAGE, numExpenses);
    		
    	// Get the records and store in an array so we can display it..
    	ArrayList<Expense> expenses = getAllExpenses(c);
    	
    	expenseRecords = expenses;

    	startActivity(intent);
    }
    
    public void deleteAllExpenses(View view) {
    	new AlertDialog.Builder(this)
        .setTitle("Delete All Records")
        .setMessage("Are you sure you want to delete all records?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // continue with delete
            	SQLiteDatabase db = dbHelper.getWritableDatabase();
            	ExpenseDao expenseObj = new ExpenseDao(db);
            	expenseObj.drop();
            }
         })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // do nothing
            }
         })
         .show();
    	
    	

    }
    

    
    public ArrayList<Expense> getAllExpenses(Cursor cursor) {
        ArrayList<Expense> expenses = new ArrayList<Expense>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          Expense expense = cursorToExpense(cursor);
          expenses.add(expense);
          cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return expenses;
      }
    
    private Expense cursorToExpense(Cursor cursor) {
        Expense expense = new Expense();
        expense.setCost(cursor.getDouble(2));
        expense.setType(cursor.getString(1));
        expense.setLocation(cursor.getString(4));
        expense.setDate(convertStringToDate(cursor.getString(3)));
        return expense;
      }
    
    public Date convertStringToDate(String d) {
    	Date date = new Date();
    	return date;
    }
}
