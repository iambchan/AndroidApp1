package com.example.helloworld;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.helloworld.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	
	public final static String EXTRA_MESSAGE= "com.example.helloworld.MESSAGE";
	private ExpenseDbHelper dbHelper;

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
    	//List<Expense> expenses = getAllExpenses(c);
    	//ArrayList<String> expenseStrings = getExpenseStrings(expenses);
    	
    	//intent.putStringArrayListExtra("ListOfExpenses", expenseStrings);

    	startActivity(intent);
    	
    }
    
    private ArrayList<String> getExpenseStrings(List<Expense> listOfExpenses) {
    	ArrayList<String> es = new ArrayList<String>();
    	Expense e = new Expense(); 
    	for(int i = 0; i < listOfExpenses.size(); i++) {
    		e = listOfExpenses.get(i);
    		es.add("placeholder");
    	}
    	return es;
    	
    }
    
    public List<Expense> getAllExpenses(Cursor cursor) {
        List<Expense> expenses = new ArrayList<Expense>();

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
