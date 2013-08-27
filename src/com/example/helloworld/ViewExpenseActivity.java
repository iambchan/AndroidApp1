package com.example.helloworld;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewExpenseActivity extends Activity {
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_expense);

		Intent intent = getIntent();
		ArrayList<String> selectedItem = intent.getExtras().getStringArrayList(
				"selectedExpense");
		String description = selectedItem.get(2);
		String cost = selectedItem.get(1);
		String type = selectedItem.get(4);
		String date = selectedItem.get(0);
		id = selectedItem.get(3);

		EditText edit_description = (EditText) findViewById(R.id.editText_Description);
		edit_description.setText(description);
		EditText edit_cost = (EditText) findViewById(R.id.editText_cost);
		edit_cost.setText(cost);
		TextView edit_date = (TextView) findViewById(R.id.text_date);
		edit_date.setText(date);
		Spinner edit_spinner = (Spinner) findViewById(R.id.spinner_expense_types);
		edit_spinner.setSelection(getTypeIndex(type));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_expense, menu);
		return true;
	}

	private int getTypeIndex(String type) {
		int typeIndex = -1;
		if (type.equalsIgnoreCase("Food"))
			typeIndex = 0;
		else if (type.equalsIgnoreCase("Transportation"))
			typeIndex = 1;
		else if (type.equalsIgnoreCase("Utilities"))
			typeIndex = 2;
		else if (type.equalsIgnoreCase("Home"))
			typeIndex = 3;
		else if (type.equalsIgnoreCase("Gift"))
			typeIndex = 4;
		else if (type.equalsIgnoreCase("Miscellaneous"))
			typeIndex = 5;
		return typeIndex;
	}

	@SuppressWarnings("deprecation")
	public void deleteEntry(View view) {
		SQLiteDatabase db = MainActivity.dbHelper.getReadableDatabase();
		ExpenseDao expenseObj = new ExpenseDao(db);
		int deletedCount = expenseObj.delete(id);
		
		if (deletedCount > 0) {
			// Pop-up message confirming that expense was deleted successfully
			AlertDialog alertDialog_deleted = new AlertDialog.Builder(this)
			.create();
			alertDialog_deleted.setTitle("Delete successful");
			alertDialog_deleted.setMessage("Expense was deleted!");
			alertDialog_deleted.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finishActivity();
						}
					});
			alertDialog_deleted.show();
		}
	}
	
	public void updateEntry(View view) {
		ContentValues cv = new ContentValues();
		cv.put("Field1","Bob");
		cv.put("Field2","19");
		cv.put("Field2","Male");
	}
	
	private void finishActivity() {
		this.finish();
	}

}
