package krot.sumorsubtractusingfragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

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

        //       if (findViewById(R.id.main_container) != null) {
        //           if (savedInstanceState != null) {
        //               return;
        //           }

        if (isTablet()) {
            Log.d("onCreate()", " in Tablet mode");

            operationsFragment = new OperationsFragment();
            operandsFragment = new OperandsFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.operations_container, operationsFragment);
            ft.add(R.id.operands_container, operandsFragment);
            ft.commit();

        } else {
            Log.d("onCreate()", " in Handset mode");
            operationsFragment = new OperationsFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.main_container, operationsFragment)
                    .commit();
        }

        //       }
    }

    public void onSetOperation(int operation) {
        currentOperation = operation;
        operandsFragment = new OperandsFragment();
        showOperandsFragment();
    }

    public void onComputeWithOperands(int op1, int op2) {
        // TODO: is it appropriate?
        //FragmentManager fm = getFragmentManager();
        //fm.popBackStack(); // remove operandsFragment from stack
        //FragmentTransaction ft = fm.beginTransaction();

        // fill in fields in the view
        operationsFragment = new OperationsFragment(); // TODO: why not OperationsFragment(op1, op2) ?
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (isTablet()) {
            transaction.replace(R.id.operands_container, operandsFragment);
        } else {
            transaction.replace(R.id.main_container, operandsFragment); // using .add here leads to two fragments being shown at once!
            // transaction.addToBackStack(null); // TODO: not necessary as I'm never using popBackStack()
        }
        transaction.commit();
    }

    public void showOperationsFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (isTablet()) {
            transaction.replace(R.id.operations_container, operationsFragment);
        } else {
            transaction.replace(R.id.main_container, operationsFragment);
        }
        // transaction.addToBackStack(null); // TODO: not necessary as I'm never using popBackStack()
        transaction.commit();
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.has_two_panes);
    }

}
