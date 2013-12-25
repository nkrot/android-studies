package krot.sumorsubtractusingfragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity
        implements OperandsFragment.OnComputeOrCancelPressedListener,
        OperationsFragment.OnSetOperationListener {

    private OperationsFragment operationsFragment;
    private OperandsFragment operandsFragment;
    private int currentOperation;

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

    public void onSetOperation(int operation) {
        currentOperation = operation;
        showOperandsFragment();
    }

    public void onComputeWithOperands(int op1, int op2) {
        //Log.d("computeWithOperands()", "received op1=" + String.valueOf(op1) + " and op2 = " + String.valueOf(op2));

        operationsFragment = new OperationsFragment();
        Bundle args = new Bundle();
        args.putInt(OperationsFragment.OPERAND1, op1);
        args.putInt(OperationsFragment.OPERAND2, op2);
        args.putInt(OperationsFragment.OPERATION, currentOperation);
        operationsFragment.setArguments(args);

        showOperationsFragment();
    }

    public void onCancelOperands() {
        operationsFragment = new OperationsFragment();

        Bundle args = new Bundle();
        args.putBoolean(OperationsFragment.CANCELED, true);
        operationsFragment.setArguments(args);

        showOperationsFragment();
    }

    public void showOperandsFragment() {
        operandsFragment = new OperandsFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.phone_container, operandsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showOperationsFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.phone_container, operationsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        // Log.d("computeWithOperands()", " transaction was committed");
    }
}
