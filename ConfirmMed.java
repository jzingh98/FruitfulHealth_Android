package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;


public class ConfirmMed extends Activity implements View.OnClickListener{

    private TextView nameView;
    private TextView dosageView;
    private TextView instructionView;
    private String medEntry;
    private int medId;
    String MedName;
    String Dosage;
    String Unit;
    String Instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_med);
        findViewById(R.id.save_button).setOnClickListener(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                medEntry = "Medication";
                medId = -1;
            } else {
                medEntry = extras.getString("Medication");
                medId = extras.getInt("Id");
            }
        } else {
            medEntry = (String) savedInstanceState.getSerializable("Medication");
            medId = (int)savedInstanceState.getSerializable("Id");
        }


        // Update views with received extras
//        nameView = (TextView)findViewById(R.id.name_view);
//        dosageView = (TextView)findViewById(R.id.dosage_view);
//        instructionView = (TextView)findViewById(R.id.instruction_view);
        // Call function to update views

//        nameView.setText(MedName);
//        dosageView.setText(Dosage + Unit);
//        instructionView.setText(Instruction);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save_button) {
            // Return to MedScannedListings
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", correctlyConfigured());
            returnIntent.putExtra("id", medId);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        }
    }



    public boolean correctlyConfigured(){

        return true;
    }






}
