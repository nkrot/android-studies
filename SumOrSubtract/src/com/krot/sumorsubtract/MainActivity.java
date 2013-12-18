package com.krot.sumorsubtract;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
//import android.view.Menu;

public class MainActivity extends Activity {
	static final int QUERY_OPERANDS_REQUEST_FOR_SUM = 1;
	static final int QUERY_OPERANDS_REQUEST_FOR_SUBTRACT = 2;
	
	protected OperationHistory operationHistory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d("onCreate was triggered", "zzz");

		if (savedInstanceState == null) {
			operationHistory = new OperationHistory();
		} else {
			// TODO: restore history from saved object
			Log.d("Restore from saved", "mmm. how?");
		}
	}

	public void queryOperandValuesForSum(View view) {
		Intent intent = new Intent(this, QueryOperandValuesActivity.class);
		startActivityForResult(intent, QUERY_OPERANDS_REQUEST_FOR_SUM);
	};

	public void queryOperandValuesForSubtract(View view) {
		Intent intent = new Intent(this, QueryOperandValuesActivity.class);
		startActivityForResult(intent, QUERY_OPERANDS_REQUEST_FOR_SUBTRACT);
	};

	public void findCurrentInHistory(View view) {	
		TextView tvOperationId = (TextView) findViewById(R.id.ev_history_operation_id);
		String operationId = tvOperationId.getText().toString();

		if (operationId.length() == 0) {
			// TODO: what should we do if nothing was input? perhaps it makes sense
			// disabling the = button when the edit box is empty? Then this branch is useless
			// Log.d("Empty operation id", operationId);
		} else {
			// the inputType was set to numeric, that restricts input to non-negative numbers
			operationHistory.goToItemAt(Integer.parseInt(operationId));
			showHistoryItem();
		}
	};

	public void findPreviousInHistory(View view) {	
		operationHistory.goToPreviousItem();
		showHistoryItem();
	};

	public void findNextInHistory(View view) {
		operationHistory.goToNextItem();
		showHistoryItem();
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
		if (requestCode == QUERY_OPERANDS_REQUEST_FOR_SUM || requestCode == QUERY_OPERANDS_REQUEST_FOR_SUBTRACT) {

			TextView tv_op1 = (TextView) findViewById(R.id.operand_1);
			TextView tv_op2 = (TextView) findViewById(R.id.operand_2);
			TextView tv_res = (TextView) findViewById(R.id.result);

			if (resultCode == RESULT_OK) {

				String op1 = resultData.getStringExtra(QueryOperandValuesActivity.OPERAND1);
				String op2 = resultData.getStringExtra(QueryOperandValuesActivity.OPERAND2);
				
				int res = 0;
				if (requestCode == QUERY_OPERANDS_REQUEST_FOR_SUM) {
					res = Integer.parseInt(op1) + Integer.parseInt(op2);
					addToHistory(OperationHistory.ADDITION, res);
	
				} else if (requestCode == QUERY_OPERANDS_REQUEST_FOR_SUBTRACT) {
					res = Integer.parseInt(op1) - Integer.parseInt(op2);
					addToHistory(OperationHistory.SUBTRACTION, res);
				}

				tv_op1.setText(op1);
				tv_op2.setText(op2);
				tv_res.setText(String.valueOf(res));

			} else if (resultCode == RESULT_CANCELED ) {
				// reset all text view to defaults
				tv_op1.setText(R.string.the_1st_operand);
				tv_op2.setText(R.string.the_2nd_operand);
				tv_res.setText(R.string.operation_canceled);
			}
		}
	};
	
	protected void showHistoryItem() {
		
		if (operationHistory.mostRecentQueryWasInvalid()) {
			// TODO: how to pass additional info into the dialog? e.g. the invalid value
			showInvalidHistoryItemDialog();
		}
		
		// TODO: should show the last user's request! and not show anything if there is no valid user queries in the past
		// scenario: 
		//  1) accomplish a computation (history gets an item)
		//  2) input an invalid number i operation id box
		//  3) let the dialog appear, close it
		/// 4) the 0th item is the history is shown. while NOTHING should be shown
		if (! operationHistory.isEmpty()) {
			TextView viewOperationId     = (TextView) findViewById(R.id.ev_history_operation_id);
			TextView viewOperationName   = (TextView) findViewById(R.id.tv_history_operation_name);
			TextView viewOperationResult = (TextView) findViewById(R.id.tv_history_result_value);
		
			viewOperationId.setText(String.valueOf(operationHistory.getHistoryItemId()));
			viewOperationName.setText(operationHistory.getOperation());
			viewOperationResult.setText(String.valueOf(operationHistory.getResult()));
		}
	}
	
	protected void addToHistory(String opName, int opResult) {
		operationHistory.addItem(opName, opResult);
		TextView viewHistorySize = (TextView) findViewById(R.id.history_size_value);
		viewHistorySize.setText(String.valueOf(operationHistory.size()));
	}
	
	protected void showInvalidHistoryItemDialog() {
		DialogFragment dialog = new InvalidHistoryItemDialogFragment();
		dialog.show(getFragmentManager(), "invalid_history_item");
	}
}
