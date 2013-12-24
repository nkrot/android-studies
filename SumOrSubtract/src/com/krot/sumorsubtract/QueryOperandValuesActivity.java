package com.krot.sumorsubtract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QueryOperandValuesActivity extends Activity {
    public static final String OPERAND1 = "operand_1";
    public static final String OPERAND2 = "operand_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_operand_values);
        bindControlsToEvents();
    }

    private void bindControlsToEvents() {
        Button computeButton = (Button) findViewById(R.id.btn_do);
        computeButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                compute(v);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                cancel(v);
            }
        });
    }

    public void compute(View view) {
        TextView tv_op1 = (TextView) findViewById(R.id.query_operand_1);
        TextView tv_op2 = (TextView) findViewById(R.id.query_operand_2);

        Intent resultData = new Intent();
        resultData.putExtra(OPERAND1, tv_op1.getText().toString());
        resultData.putExtra(OPERAND2, tv_op2.getText().toString());

        setResult(RESULT_OK, resultData);
        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
