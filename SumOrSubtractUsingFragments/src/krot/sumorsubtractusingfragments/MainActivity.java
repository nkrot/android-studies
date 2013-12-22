package krot.sumorsubtractusingfragments;

import android.app.Activity;
import android.os.Bundle;

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

}
