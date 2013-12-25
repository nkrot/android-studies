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
        public void computeWithOperands(int op1, int op2);
        // TODO: public void operandsCanceled();
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

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnComputeOrCancelPressedListener) {
            listener = (OnComputeOrCancelPressedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet OperandsFragment.OnComputeOrCancelPressedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void updateOperands() {
        listener.computeWithOperands(getFirstOperand(), getSecondOperand());
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
