package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;


public class MedSelector extends Activity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_selector);

        findViewById(R.id.scan_button).setOnClickListener(this);
        findViewById(R.id.enter_button).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scan_button) {
            // Launch scanner
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.enter_button) {
            // Launch manual entry
            Intent intent = new Intent(this, MedEntry.class);
            startActivity(intent);
        }


    }

}
