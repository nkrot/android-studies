package krot.sumorsubtractusingfragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class OperationsFragment extends Fragment {
    static final String OPERATION = "operation";
    static final String OPERAND1 = "operand1";
    static final String OPERAND2 = "operand2";
    static final String RESULT = "result";
    static final String CANCELED = "canceled";

    static final int SUM = 1;
    static final int SUBTRACT = 2;
    static final int RESULT_IS_NONE = 0;
    static final int RESULT_IS_OK = 1;
    static final int RESULT_IS_CANCELED = 2;

    public int operation;
    public int operand1;
    public int operand2;
    public int result;

    private int resultCode = RESULT_IS_NONE;
    private View theView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("OperationsFragment.onCreate()", " was called");

        Bundle args = getArguments();
        if (args == null) {
            // resultCode = RESULT_IS_NONE;

        } else if (args.getBoolean(CANCELED)) {
            resultCode = RESULT_IS_CANCELED;

        } else {
            resultCode = RESULT_IS_OK;
            operand1 = args.getInt(OPERAND1);
            operand2 = args.getInt(OPERAND2);
            operation = args.getInt(OPERATION);
            computeResult();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        //Log.d("OperationsFragment", "onCreateView() was called");

        theView = inflater.inflate(R.layout.operations, container, false);

        switch (resultCode) {
        case RESULT_IS_OK:
            updateView(R.id.operand_1, operand1);
            updateView(R.id.operand_2, operand2);
            updateView(R.id.result, result);
            break;

        case RESULT_IS_CANCELED:
            // updateView(R.id.result, R.string.operation_canceled); // sucks! as R.string.name is an integer
            updateView(R.id.result, getResources().getString(R.string.operation_canceled)); // TODO: how to make it simpler?
            break;
        }

        return theView;
    }

    private void updateView(int viewId, int val) {
        updateView(viewId, String.valueOf(val));
    }

    private void updateView(int viewId, String val) {
        // TODO: why does getView() here return null when called from onCreateView()?
        TextView view = (TextView) theView.findViewById(viewId);
        view.setText(val);
    }

    private void computeResult() {
        switch (operation) {
        case SUM:
            result = operand1 + operand2;
            //TODO: addToHistory(OperationHistory.ADDITION, res);
            break;

        case SUBTRACT:
            result = operand1 - operand2;
            //TODO: addToHistory(OperationHistory.SUBTRACTION, res);
            break;
        }
    }
}
