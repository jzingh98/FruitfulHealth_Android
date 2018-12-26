package com.google.android.gms.samples.vision.ocrreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class MedNotificationConfigurations extends Activity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_notification_configurations);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.config_layout);



        for (int i = 0; i < 3; i++) {
            createNewView(linearLayout);
        }




    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save_button) {
            // Launch scanner
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


    }


    public void createNewView(LinearLayout linearLayout){

        // Add button
        Button newButton = new Button(this);
        newButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        newButton.setText("Set Notification Time");

        linearLayout.addView(newButton);


    }



}


