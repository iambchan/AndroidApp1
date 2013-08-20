package com.example.helloworld;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddExpenseActivity extends Activity {

	private ExpenseDbHelper dbHelper;
	private static TextView text_date;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new ExpenseDbHelper(getApplicationContext());
		setContentView(R.layout.activity_add_expense);
		setCurrentDay();
	    setSpinner();
	}
	
	public void setSpinner() {
    	Spinner spinner = (Spinner) findViewById(R.id.Spinner_expense_type);
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
    	        R.array.expense_types, android.R.layout.simple_spinner_item);
    	// Specify the layout to use when the list of choices appears
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	spinner.setAdapter(adapter);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_expense, menu);
		return true;
	}

	
	// saves expense
    @SuppressLint("SimpleDateFormat")
	public void saveExpense(View view){
    	Expense expense = new Expense();
    	
    	// get the expense information from fields
    	EditText editText = (EditText) findViewById(R.id.editText_Description);
    	String description = editText.getText().toString();
    	editText = (EditText) findViewById(R.id.editText_cost);
    	String cost_string = editText.getText().toString();
    	double cost = Double.parseDouble(cost_string);
    	Spinner spinner = (Spinner) findViewById(R.id.Spinner_expense_type);
    	String type = spinner.getSelectedItem().toString();
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	Date date = null;
		try {
			String text_date_string = text_date.getText().toString();
			date = (Date) sdf.parse(text_date_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	expense.setCost(cost);
    	expense.setLocation(description);
    	expense.setDate(date);
    	expense.setType(type);
    	
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
    	ExpenseDao expenseObj = new ExpenseDao(db);
    	expenseObj.insert(expense);
    }
    
    @SuppressLint("NewApi") public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    
    @SuppressLint("NewApi") public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
			}
			
			
			public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
//				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//				String formattedDate = sdf.format(new Date(year, month, day));
//				text_date.setText(formattedDate);
				
				text_date = (TextView) getActivity().findViewById(R.id.text_date);
				text_date.setText(new StringBuilder().append(checkDigit(day))
						.append("-").append(checkDigit(month+1)).append("-").append(year).append(" "));
			}
		}
    
    public void setDate(int year, int month, int day)
    {
    	
    	text_date = (TextView)findViewById(R.id.text_date);
    	text_date.setText(new StringBuilder().append(checkDigit(day))
				.append("-").append(checkDigit(month+1)).append("-").append(year).append(" "));
//    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		String formattedDate = sdf.format(new Date(year, month, day));
//		text_date.setText(formattedDate);
    }
    
    public static String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }
    
    public void setCurrentDay() {
    	final Calendar calendar = Calendar.getInstance();

    	int year = calendar.get(Calendar.YEAR);
    	int month = calendar.get(Calendar.MONTH);
    	int day = calendar.get(Calendar.DAY_OF_MONTH);
    	
    	setDate(year, month, day);
    }
}
