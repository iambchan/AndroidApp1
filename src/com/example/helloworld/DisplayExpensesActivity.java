package com.example.helloworld;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.example.helloworld.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DisplayExpensesActivity extends Activity {

	private ExpenseDbHelper dbHelper;
	private ExpenseDao expenseObj = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_expenses);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			getActionBar().setDisplayHomeAsUpEnabled(true);

		dbHelper = new ExpenseDbHelper(getApplicationContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		expenseObj = new ExpenseDao(db);
		//Cursor c = expenseObj.query();
		Cursor c = expenseObj.queryCurMonth_expenses(MainActivity.currentMonth, MainActivity.currentYear);
		populateYearField();
		setSpinnerCurrentMonth();

		ArrayList<Expense> expenses = getAllExpenses(c);
		populateTable(expenses);
	}
	
	private void populateTable(ArrayList<Expense> expenses) {
		Expense e = null;
		TableLayout tbl = (TableLayout) findViewById(R.id.ExpenseTable_content);
		for (int i = 0; i < expenses.size(); i++) {
			e = expenses.get(i);
			if (e != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
				String formattedDate = sdf.format(e.getDate());
				String cost = new DecimalFormat("##.00").format(e.getCost());
				addNewRow(tbl, formattedDate, cost, e.getDescription(),
						Long.toString(e.getId()), e.getType());
			}
		}
	}

	private void addNewRow(TableLayout tbl, String date, String cost,
			String description, String id, String type) {
		LayoutInflater inflater = getLayoutInflater();
		TableRow newRow = (TableRow) inflater.inflate(R.layout.row, tbl, false);

		TextView col_1 = (TextView) newRow.findViewById(R.id.Col_1);
		col_1.setText(date);
		TextView col_2 = (TextView) newRow.findViewById(R.id.Col_2);
		col_2.setText(cost);
		TextView col_3 = (TextView) newRow.findViewById(R.id.Col_3);
		col_3.setText(description);
		TextView col_4 = (TextView) newRow.findViewById(R.id.Col_4);
		col_4.setText(id);
		TextView col_5 = (TextView) newRow.findViewById(R.id.Col_5);
		col_5.setText(type);

		tbl.addView(newRow);
	}

	public static ArrayList<Expense> getAllExpenses(Cursor cursor) {
		ArrayList<Expense> expenses = new ArrayList<Expense>();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Expense expense = cursorToExpense(cursor);
			expenses.add(expense);
			cursor.moveToNext();
		}
		cursor.close();
		return expenses;
	}

	public static Expense cursorToExpense(Cursor cursor) {
		Expense expense = new Expense();
		expense.setId(cursor.getLong(0));
		expense.setCost(cursor.getDouble(2));
		expense.setType(cursor.getString(1));
		expense.setLocation(cursor.getString(4));
		expense.setDate(convertStringToDate(cursor.getString(3)));
		return expense;
	}

	public static String convertDateToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		return sdf.format(d);
	}
	
	public static Date convertStringToDate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy");
		Date date = new Date();
		try {
			date = sdf.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		;
		return date;
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

	public void onRowClick(View view) {
		TableRow t = (TableRow) view;
		TextView textView0 = (TextView) t.getChildAt(0);
		TextView textView1 = (TextView) t.getChildAt(1);
		TextView textView2 = (TextView) t.getChildAt(2);
		TextView textView3 = (TextView) t.getChildAt(3);
		TextView textView4 = (TextView) t.getChildAt(4);
		ArrayList<String> selectedRow = new ArrayList<String>();

		selectedRow.add(textView0.getText().toString());
		selectedRow.add(textView1.getText().toString());
		selectedRow.add(textView2.getText().toString());
		selectedRow.add(textView3.getText().toString());
		selectedRow.add(textView4.getText().toString());

		Intent intent = new Intent(this, ViewExpenseActivity.class);
		intent.putStringArrayListExtra("selectedExpense", selectedRow);
		startActivity(intent);
	}
	
	// Sets the month spinner to current month
	public void setSpinnerCurrentMonth() {
		int curMonth = Calendar.getInstance().get(Calendar.MONTH);
		Spinner monthSpinner = (Spinner) findViewById(R.id.spinner_month);
		monthSpinner.setSelection(curMonth - 1);
	}

	// Populates the year spinner with years found in the database
	public void populateYearField() {
		Cursor cursor = expenseObj.queryDistinctDates();
		ArrayList<Integer> years = new ArrayList<Integer>();
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		years.add(curYear);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Date date = convertStringToDate(cursor.getString(0));
			int year = date.getYear() + 1900;
			if (!years.contains(year))
				years.add(year);
			cursor.moveToNext();
		}

		cursor.close();
		Spinner yearSpinner = (Spinner) findViewById(R.id.spinner_year);
		ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
				this, android.R.layout.simple_spinner_item, years);
		yearSpinner.setAdapter(spinnerArrayAdapter);
		// the first item on our spinner list is the current year which we
		// manually added as default
		//int index = findCurYrIndex(yearSpinner, curYear);
		yearSpinner.setSelection(0);
	}
	
	private int findCurYrIndex(Spinner yearSpinner, int currentYr) {
		// Finds the index of the current year in the spinner
		// loop through spinner and find index of currentYr
		for(int i=0; i<yearSpinner.getCount(); i++) {
			int spinnerYr = (Integer) yearSpinner.getAdapter().getItem(i);
			if(spinnerYr == currentYr) {
				return i;
			}
		}
		return -1;
	}
	
	public void refreshTable(View view) {
		// Get spinners for month and year
		Spinner yearSpinner = (Spinner) findViewById(R.id.spinner_year);
		String year = yearSpinner.getSelectedItem().toString();
		
		Spinner monthSpinner = (Spinner) findViewById(R.id.spinner_month);
		String month = monthSpinner.getSelectedItem().toString();
		
		// Run sql query
		Cursor cursor;
		if(month.equalsIgnoreCase("All"))
			cursor = expenseObj.queryYearly_expenses(year);
		else cursor  = expenseObj.queryCurMonth_expenses(month, year);
		ArrayList<Expense> expenses = getAllExpenses(cursor);
		TableLayout table = (TableLayout) findViewById(R.id.ExpenseTable_content);       
		table.removeAllViews();
		populateTable(expenses);
		
		
		// Re-populate expense table
	}


}
