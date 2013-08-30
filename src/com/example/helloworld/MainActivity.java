package com.example.helloworld;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import com.example.helloworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	protected static ExpenseDbHelper dbHelper;
	private ExpenseDao expenseObj;
	private static TextView textView_TotalExpensesThisMonth;
	private static Double totalExpensesThisMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ExpenseDbHelper(getApplicationContext());
        setContentView(R.layout.activity_main);
        showTotalExpensesThisMonth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // called when user clicks on add new expense button
    public void addNewExpense(View view){
    	Intent intent = new Intent(this, AddExpenseActivity.class);
    	startActivityForResult(intent, 1);
    	//startActivity(intent);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	  if (requestCode == 1) {

    	     if(resultCode == RESULT_OK){      
    	    	 showTotalExpensesThisMonth();       
    	     }
    	     if (resultCode == RESULT_CANCELED) {    
    	         //Write your code if there's no result
    	     }
    	  }
    	}//onActivityResult
    
    // Show expenses - called when user clicks on show expenses button
    public void showExpenses(View view) {
    	Intent intent = new Intent(this, DisplayExpensesActivity.class);
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
    
    public void showTotalExpensesThisMonth() {
    	// Get current Month and Year
    	int currentMonth_int = Calendar.getInstance().get(Calendar.MONTH) + 1;
    	String currentMonth = getMonString(currentMonth_int);
    	int currentYear_int = Calendar.getInstance().get(Calendar.YEAR);
    	String currentYear = Integer.toString(currentYear_int);
    	
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	expenseObj = new ExpenseDao(db);
    	Cursor cursor = expenseObj.queryCurMonth_expenses(currentMonth, currentYear);
    	//Cursor cursor = expenseObj.queryByMonth();
    	// Add up all of this month's expenses
    	totalExpensesThisMonth = 0.0;
    	 cursor.moveToFirst();
         while (!cursor.isAfterLast()) {
        	 Double cost = cursor.getDouble(2);
           totalExpensesThisMonth += cost;
           cursor.moveToNext();
         }
         cursor.close();
    	
         String total = new DecimalFormat("##.00").format(totalExpensesThisMonth);
         textView_TotalExpensesThisMonth = (TextView) findViewById(R.id.textView_totalAmt);
         textView_TotalExpensesThisMonth.setText(total);
    	
    }
    
	private String getMonString(int month) {
        String monthString;
        switch (month) {
            case 1:  monthString = "Jan";
                     break;
            case 2:  monthString = "Feb";
                     break;
            case 3:  monthString = "Mar";
                     break;
            case 4:  monthString = "Apr";
                     break;
            case 5:  monthString = "May";
                     break;
            case 6:  monthString = "Jun";
                     break;
            case 7:  monthString = "Jul";
                     break;
            case 8:  monthString = "Aug";
                     break;
            case 9:  monthString = "Sep";
                     break;
            case 10: monthString = "Oct";
                     break;
            case 11: monthString = "Nov";
                     break;
            case 12: monthString = "Dec";
                     break;
            default: monthString = "Invalid month";
                     break;
        }
        return monthString;
	}
	
	public void emailData() {
		Cursor cursor = expenseObj.query();
		ArrayList<Expense> expenses = DisplayExpensesActivity.getAllExpenses(cursor);
		String email = composeEmail(expenses); 
		
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		//i.putExtra(Intent.EXTRA_EMAIL, new String[]{"chanbessie@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "My Expense Tracker: Expense Records");
		i.putExtra(Intent.EXTRA_TEXT, email);
		
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(MainActivity.this, "There are no email clients installed", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private String composeEmail(ArrayList<Expense> expenses) {
		String email = "";
		String eol = System.getProperty("line.separator");
		String date, cost, description, type;
		date = "Date";
		cost = "Cost";
		description = "Description";
		type = "type";
		String formatter = "%-15s%-20s%-70s%-20s";
		email = "Expense records sent from My Expense Tracker App" + eol;
		String expense = String.format(formatter, date, cost, description, type);
		email = email + expense + eol; // headings
		email = email + "-------------------------------------------------------------" +
				"------------------------------------------------------------------------------------" + eol;
		String temp;
		Expense e;
		for(int i = 0; i < expenses.size(); i++) {
			e = expenses.get(i);
			date = DisplayExpensesActivity.convertDateToString(e.getDate());
			cost = Double.toString(e.getCost());
			description = e.getDescription();
			type = e.getType();
			expense = String.format(formatter, date, cost, description, type);
			email = email + expense + eol;
		}
		return email;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.action_delete_data:
	    	deleteAllExpenses(this.findViewById(android.R.id.content));
	        return true;
	    case R.id.action_email_data:
	    	emailData();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
    
}
