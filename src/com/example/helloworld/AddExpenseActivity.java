package com.example.helloworld;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddExpenseActivity extends Activity {

	private ExpenseDbHelper dbHelper;
	private static TextView text_date;
	private double addedCost = 0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new ExpenseDbHelper(getApplicationContext());
		setContentView(R.layout.activity_add_expense);
		setCurrentDay();
		setSpinner();
		setCostInput();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void returnToMain(View view) {
		NavUtils.navigateUpFromSameTask(this);
	}

	private void setCostInput() {
		EditText editText = (EditText) findViewById(R.id.editText_cost);
		editText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});
	}
	
	// Hides keyboard when user clicks on parent layout
	public void onClick(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public void setSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.spinner_expense_types);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.expense_types,
				android.R.layout.simple_spinner_item);
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
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	public void saveExpense(View view) {
		Expense expense = new Expense();
		
		EditText editText = (EditText) findViewById(R.id.editText_cost);
		String cost_string = editText.getText().toString();
		if(cost_string.length() > 1) {


		double cost = Double.parseDouble(cost_string);
		editText = (EditText) findViewById(R.id.editText_Description);
		String description = editText.getText().toString();
		Spinner spinner = (Spinner) findViewById(R.id.spinner_expense_types);
		String type = spinner.getSelectedItem().toString();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			String text_date_string = text_date.getText().toString();
			date = (Date) sdf.parse(text_date_string);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		expense.setCost(cost);
		expense.setLocation(description);
		expense.setDate(date);
		expense.setType(type);

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ExpenseDao expenseObj = new ExpenseDao(db);
		expenseObj.insert(expense);

		//Pop-up message confirming that expense was added successfully
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Expense Added");
		alertDialog.setMessage("Expense was successfully added!");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				finishActivity();
			}
		});
		alertDialog.show();
		}
		else {
			//Pop-up message confirming that expense was added successfully
			AlertDialog alertDialog_enterCost = new AlertDialog.Builder(this).create();
			alertDialog_enterCost.setTitle("Enter a valid cost");
			alertDialog_enterCost.setMessage("Please enter a valid cost");
			alertDialog_enterCost.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			alertDialog_enterCost.show();
		}
	}

	public void finishActivity() {
		 Intent returnIntent = new Intent();
		 returnIntent.putExtra("result",addedCost);
		 setResult(RESULT_OK,returnIntent);     
		 this.finish();
	}

	@SuppressLint("NewApi")
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	@SuppressLint("NewApi")
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

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
			text_date = (TextView) getActivity().findViewById(R.id.text_date);
			text_date.setText(new StringBuilder().append(checkDigit(day))
					.append("-").append(checkDigit(month + 1)).append("-")
					.append(year).append(" "));
		}
	}

	public void setDate(int year, int month, int day) {

		text_date = (TextView) findViewById(R.id.text_date);
		text_date.setText(new StringBuilder().append(checkDigit(day))
				.append("-").append(checkDigit(month + 1)).append("-")
				.append(year).append(" "));
	}

	public static String checkDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}

	public void setCurrentDay() {
		final Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		setDate(year, month, day);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
