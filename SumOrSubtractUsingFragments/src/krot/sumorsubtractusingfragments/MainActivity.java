package krot.sumorsubtractusingfragments;

import krot.sumorsubtractusingfragments.OperandsFragment.OnComputeOrCancelPressedListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity
        implements OnComputeOrCancelPressedListener {
    static final int SUM = 1;
    static final int SUBTRACT = 2;

    private OperationsFragment operationsFragment;
    private OperandsFragment operandsFragment;
    private int currentOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        if (findViewById(R.id.phone_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            operationsFragment = new OperationsFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.phone_container, operationsFragment)
                    .commit();
        }
    }

    public void queryOperandValuesForSum(View view) {
        Log.d("Main", "Plus was pressed");
        currentOperator = SUM;
        showOperandsFragment();
    };

    public void queryOperandValuesForSubtract(View view) {
        Log.d("Main", "Minus was pressed");
        currentOperator = SUBTRACT;
        showOperandsFragment();
    };

    public void onComputeWithOperands(int op1, int op2) {
        Log.d("computeWithOperands()", "received op1=" + String.valueOf(op1) + " and op2 = " + String.valueOf(op2));

        int res = 0;
        switch (currentOperator) {
        case SUM:
            res = op1 + op2;
            //TODO: addToHistory(OperationHistory.ADDITION, res);

        case SUBTRACT:
            res = op1 - op2;
            //TODO: addToHistory(OperationHistory.SUBTRACTION, res);
        }

        Log.d("Result", String.valueOf(res));

        operationsFragment = new OperationsFragment();
        operationsFragment.updateWithComputed(op1, op2, res);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.phone_container, operationsFragment);

        transaction.addToBackStack(null);

        transaction.commit();
        Log.d("computeWithOperands()", " transaction was committed");
    }

    public void cancel(View view) {
        Log.d("Main", "Cancel was pressed");
        // operationsFragment.updateWithCanceled();
    }

    public void showOperandsFragment() {
        operandsFragment = new OperandsFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.phone_container, operandsFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void showOperationsFragment() {
        operationsFragment = new OperationsFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.phone_container, operationsFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
