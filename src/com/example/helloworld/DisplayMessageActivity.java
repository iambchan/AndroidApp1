package com.example.helloworld;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.helloworld.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ArrayList<Expense> expenses = MainActivity.expenseRecords;
		
		Intent intent = getIntent();
		int recordCount = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, -1);
		String message = Integer.toString(recordCount);

		
		Expense e = null;
		TableLayout tbl = (TableLayout)findViewById(R.id.ExpenseTable);
		for(int i = 0; i < recordCount; i++) {
			e = expenses.get(i);
			if(e != null)
			{
		    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
				String formattedDate = sdf.format(e.getDate());
				addNewRow(tbl, formattedDate, String.valueOf(e.getCost()), e.getDescription());
			}
		}
		


	}
	
	private void addNewRow(TableLayout tbl, String date, String cost, String description) {
		LayoutInflater inflater = getLayoutInflater();	
		TableRow newRow = (TableRow)inflater.inflate(R.layout.row, tbl, false);
		
		TextView col_1 = (TextView)newRow.findViewById(R.id.Col_1);
		col_1.setText(date);
		TextView col_2 = (TextView)newRow.findViewById(R.id.Col_2);
		col_2.setText(cost);
		TextView col_3 = (TextView)newRow.findViewById(R.id.Col_3);
		col_3.setText(description);
		
		tbl.addView(newRow);
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
