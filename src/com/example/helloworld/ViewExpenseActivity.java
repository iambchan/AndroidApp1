package com.example.helloworld;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class ViewExpenseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_expense);
		
		Intent intent = getIntent();
		ArrayList<String> selectedItem = intent.getExtras().getStringArrayList("selectedExpense");
		String description = selectedItem.get(2);
		String cost = selectedItem.get(1);
		String type = selectedItem.get(4);
		String date = selectedItem.get(0);
		String id = selectedItem.get(3);
		
        EditText edit_description = (EditText) findViewById(R.id.editText_Description);
        edit_description.setText(description);
        EditText edit_cost = (EditText) findViewById(R.id.editText_cost);
        edit_cost.setText(cost);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_expense, menu);
		return true;
	}

}
