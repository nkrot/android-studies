package com.krot.sumorsubtract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
//import android.view.Menu;

public class MainActivity extends Activity {
	//static final int QUERY_OPERANDS_REQUEST = 0;
	static final int QUERY_OPERANDS_REQUEST_FOR_SUM = 1;
	static final int QUERY_OPERANDS_REQUEST_FOR_SUBTRACT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void queryOperandValuesForSum(View view) {
		Intent intent = new Intent(this, QueryOperandValuesActivity.class);
		startActivityForResult(intent, QUERY_OPERANDS_REQUEST_FOR_SUM);
	};

	public void queryOperandValuesForSubtract(View view) {
		Intent intent = new Intent(this, QueryOperandValuesActivity.class);
		startActivityForResult(intent, QUERY_OPERANDS_REQUEST_FOR_SUBTRACT);
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
		if (requestCode == QUERY_OPERANDS_REQUEST_FOR_SUM || requestCode == QUERY_OPERANDS_REQUEST_FOR_SUBTRACT) {

			TextView tv_op1 = (TextView) findViewById(R.id.operand_1);
			TextView tv_op2 = (TextView) findViewById(R.id.operand_2);
			TextView tv_res = (TextView) findViewById(R.id.result);

			if (resultCode == RESULT_OK) {

				String op1 = resultData.getStringExtra("operand_1");
				String op2 = resultData.getStringExtra("operand_2");
				
				int res = 0;
				if (requestCode == QUERY_OPERANDS_REQUEST_FOR_SUM) {
					res = Integer.parseInt(op1) + Integer.parseInt(op2);		
				} else if (requestCode == QUERY_OPERANDS_REQUEST_FOR_SUBTRACT) {
					res = Integer.parseInt(op1) - Integer.parseInt(op2);
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
	}
}
