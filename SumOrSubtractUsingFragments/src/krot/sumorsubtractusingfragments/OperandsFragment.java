package krot.sumorsubtractusingfragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class OperandsFragment extends Fragment {
    private OnComputeOrCancelPressedListener listener;

    public interface OnComputeOrCancelPressedListener {
        public void onComputeWithOperands(int op1, int op2);

        public void onCancelOperands();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.operands, container, false);

        Button computeButton = (Button) view.findViewById(R.id.btn_compute);
        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOperands();
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOperands();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnComputeOrCancelPressedListener) {
            listener = (OnComputeOrCancelPressedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OperandsFragment.OnComputeOrCancelPressedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void updateOperands() {
        listener.onComputeWithOperands(getFirstOperand(), getSecondOperand());
    }

    public void cancelOperands() {
        listener.onCancelOperands();
    }

    private int getFirstOperand() {
        return getOperandValueFrom(R.id.query_operand_1);
    }

    private int getSecondOperand() {
        return getOperandValueFrom(R.id.query_operand_2);
    }

    private int getOperandValueFrom(int viewId) {
        String strVal = ((TextView) getView().findViewById(viewId)).getText().toString();
        // TODO: add try-catch here?
        int intVal = Integer.parseInt(strVal);
        return intVal;
    }
}
