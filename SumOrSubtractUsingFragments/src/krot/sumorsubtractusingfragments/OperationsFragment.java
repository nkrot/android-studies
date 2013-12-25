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
    public int operand1;
    public int operand2;
    public int result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.d("OperationsFragment", "onCreateView() was called");
        return inflater.inflate(R.layout.operations, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("OperationsFragment", " onViewCreated was called");

        updateView(R.id.operand_1, operand1);
        updateView(R.id.operand_2, operand2);
        updateView(R.id.result, result);
    }

    public void updateWithComputed(int op1, int op2, int res) {
        operand1 = op1;
        operand2 = op2;
        result = res;
    }

    public void updateWithCanceled() {
        updateView(R.id.operand_1, R.string.the_1st_operand);
        updateView(R.id.operand_2, R.string.the_2nd_operand);
        updateView(R.id.result, R.string.operation_canceled);
    }

    private void updateView(int viewId, int val) {
        updateView(viewId, String.valueOf(val));
    }

    private void updateView(int viewId, String val) {
        if (getView() == null) {
            Log.d("updateView()", " oh, fuck. getView() returned null");
        }
        TextView view = (TextView) getView().findViewById(viewId);
        if (view == null) {
            Log.d("updateView", "can not find the view with ID=" + String.valueOf(viewId));
        }
        view.setText(val);
    }
}
