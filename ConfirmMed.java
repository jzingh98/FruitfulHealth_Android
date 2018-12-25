package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;


public class ConfirmMed extends Activity implements View.OnClickListener{

    private TextView nameView;
    private TextView dosageView;
    private TextView instructionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_med);
        findViewById(R.id.save_button).setOnClickListener(this);

        // Load intent extras
        String newString;
        String MedName;
        String Dosage;
        String Unit;
        String Instruction;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                MedName = "ERROR";
                Dosage = "ERROR";
                Unit = "ERROR";
                Instruction = "ERROR";
            } else {
                MedName = extras.getString("MedName");
                Dosage = extras.getString("Dosage");
                Unit = extras.getString("Unit");
                Instruction = extras.getString("Instruction");
            }
        } else {
            MedName = (String) savedInstanceState.getSerializable("MedName");
            Dosage = (String) savedInstanceState.getSerializable("Dosage");
            Unit = (String) savedInstanceState.getSerializable("Unit");
            Instruction = (String) savedInstanceState.getSerializable("Instruction");
        }


        // Update text views with received extras
        nameView = (TextView)findViewById(R.id.name_view);
        dosageView = (TextView)findViewById(R.id.dosage_view);
        instructionView = (TextView)findViewById(R.id.instruction_view);

        nameView.setText(MedName);
        dosageView.setText(Dosage + Unit);
        instructionView.setText(Instruction);

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

}
