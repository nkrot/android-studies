package com.krot.sumorsubtract;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * TODO
 * 1. try using android 2.2 in emulator to get onRestoreInstanceState() called
 * 2. BUG: check that the number of operations is correctly displayed once onRestoreInstanceState finished
 * 
 * */

public class MainActivity extends Activity {
    static final int QUERY_OPERANDS_REQUEST_FOR_SUM = 1;
    static final int QUERY_OPERANDS_REQUEST_FOR_SUBTRACT = 2;
    static final int INVALID_HISTORY_ITEM_DLG = 1;
    static final String OPERATION_HISTORY = "operation_history";

    protected OperationHistory operationHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "was called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindControlsToEvents();

        if (savedInstanceState == null) {
            operationHistory = new OperationHistory();
        }
        // else-branch is unnecessary, onRestoreInstanceState() is called anyway
    }

    private void bindControlsToEvents() {

        Button sumButton = (Button) findViewById(R.id.btn_add);
        sumButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                queryOperandValuesForSum(v);
            }
        });

        Button subtractButton = (Button) findViewById(R.id.btn_subtract);
        subtractButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                queryOperandValuesForSubtract(v);
            }
        });

        Button historyFindButton = (Button) findViewById(R.id.btn_history_find);
        historyFindButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                findCurrentInHistory(v);
            }
        });

        Button historyPreviousButton = (Button) findViewById(R.id.btn_history_previous);
        historyPreviousButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                findPreviousInHistory(v);
            }
        });

        Button historyNextButton = (Button) findViewById(R.id.btn_history_next);
        historyNextButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                findNextInHistory(v);
            }
        });
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
        TextView operationIdView = (TextView) findViewById(R.id.ev_history_operation_id);
        String operationId = operationIdView.getText().toString();

        if (operationId.length() == 0) {
            // TODO: what should we do if nothing was input? perhaps it makes
            // sense disabling the = button when the edit box is empty? 
            // Then this branch is useless
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
        if (requestCode == QUERY_OPERANDS_REQUEST_FOR_SUM
                || requestCode == QUERY_OPERANDS_REQUEST_FOR_SUBTRACT) {

            TextView firstOperandView = (TextView) findViewById(R.id.operand_1);
            TextView secondOperandView = (TextView) findViewById(R.id.operand_2);
            TextView operationResultView = (TextView) findViewById(R.id.result);

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

                firstOperandView.setText(op1);
                secondOperandView.setText(op2);
                operationResultView.setText(String.valueOf(res));

            } else if (resultCode == RESULT_CANCELED) {
                // reset all text view to defaults
                firstOperandView.setText(R.string.the_1st_operand);
                secondOperandView.setText(R.string.the_2nd_operand);
                operationResultView.setText(R.string.operation_canceled);
            }
        }
    };

    @SuppressWarnings("deprecation")
    protected void showHistoryItem() {
        if (operationHistory.wasMostRecentQueryInvalid()) {
            showDialog(INVALID_HISTORY_ITEM_DLG);

            // clear invalid user query
            TextView operationIdView = (TextView) findViewById(R.id.ev_history_operation_id);
            operationIdView.setText("");
        }

        if (operationHistory.hadValidQueries()) {
            TextView operationIdView = (TextView) findViewById(R.id.ev_history_operation_id);
            TextView operationNameView = (TextView) findViewById(R.id.tv_history_operation_name);
            TextView operationResultView = (TextView) findViewById(R.id.tv_history_result_value);

            operationIdView.setText(String.valueOf(operationHistory.getHistoryItemId()));
            operationNameView.setText(operationHistory.getOperation());
            operationResultView.setText(String.valueOf(operationHistory.getResult()));
        }
    }

    protected void addToHistory(String opName, int opResult) {
        operationHistory.addItem(opName, opResult);
        TextView historySizeView = (TextView) findViewById(R.id.history_size_value);
        historySizeView.setText(String.valueOf(operationHistory.size()));
    }

    @Override
    protected Dialog onCreateDialog(int dialogId) {
        // http://www.vogella.com/articles/AndroidDialogs/article.html#tutorial_alertdialog

        switch (dialogId) {
        case INVALID_HISTORY_ITEM_DLG:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String msg = getString(R.string.invalid_history_item_id).concat(": ")
                    .concat(String.valueOf(operationHistory.getInvalidItemId()));

            builder.setMessage(msg)
                    .setPositiveButton(R.string.gotyou,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // an empty body is valid as well
                                    //dialog.dismiss();
                                }
                            });

            Dialog dialog = builder.create();
            dialog.show();
        }

        return super.onCreateDialog(dialogId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("onSaveInstanceState", " was called");
        outState.putParcelable(OPERATION_HISTORY, operationHistory);
    }

    // TODO: this event is never triggered
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("onRestoreInstanceState", " was called");
        operationHistory = savedInstanceState.getParcelable(OPERATION_HISTORY);
    }
}
