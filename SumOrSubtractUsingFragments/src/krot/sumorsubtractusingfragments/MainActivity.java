package krot.sumorsubtractusingfragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        if (findViewById(R.id.phone_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            OperationsFragment mainFragment = new OperationsFragment();
            getFragmentManager().beginTransaction().add(R.id.phone_container, mainFragment).commit();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    public void queryOperandValuesForSum(View view) {
        Log.d("Main", "Plus was pressed");

        OperandsFragment operandsFragment = new OperandsFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.phone_container, operandsFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    };

    public void queryOperandValuesForSubtract(View view) {
        Log.d("Main", "Minus was pressed");
        //        Intent intent = new Intent(this, QueryOperandValuesActivity.class);
        //        startActivityForResult(intent, QUERY_OPERANDS_REQUEST_FOR_SUBTRACT);
    };

    public void compute(View view) {
        Log.d("Main", "Compute was pressed");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.
    }

    public void cancel(View view) {
        Log.d("Main", "Cancel was pressed");
    }
}
