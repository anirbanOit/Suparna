package com.oit.test.demodemoproject;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by OPTLPTP061 on 06-09-2017.
 */

public class SpinnerAct extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            parent.getItemAtPosition(pos);
            parent.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
}

