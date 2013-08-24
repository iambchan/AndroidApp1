package com.example.helloworld;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DisplayExpensesActivity extends Activity {

	private ExpenseDbHelper dbHelper;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_expenses);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			getActionBar().setDisplayHomeAsUpEnabled(true);

		dbHelper = new ExpenseDbHelper(getApplicationContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ExpenseDao expenseObj = new ExpenseDao(db);
		Cursor c = expenseObj.query();
		int numExpenses = c.getCount();
		ArrayList<Expense> expenses = getAllExpenses(c);

		Expense e = null;
		TableLayout tbl = (TableLayout) findViewById(R.id.ExpenseTable_content);
		for (int i = 0; i < numExpenses; i++) {
			e = expenses.get(i);
			if (e != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
				String formattedDate = sdf.format(e.getDate());
				String cost = new DecimalFormat("##.00").format(e.getCost());
				addNewRow(tbl, formattedDate, cost,
						e.getDescription(),Long.toString(e.getId()), e.getType());
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

	public ArrayList<Expense> getAllExpenses(Cursor cursor) {
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

	private Expense cursorToExpense(Cursor cursor) {
		Expense expense = new Expense();
		expense.setId(cursor.getLong(0));
		expense.setCost(cursor.getDouble(2));
		expense.setType(cursor.getString(1));
		expense.setLocation(cursor.getString(4));
		expense.setDate(convertStringToDate(cursor.getString(3)));
		return expense;
	}

	public Date convertStringToDate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy");
		Date date = new Date();
		try {
			date = sdf.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		};
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

}
