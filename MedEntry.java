package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;


public class MedEntry extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView nameView;
    private TextView dosageView;
    private TextView instructionView;
    private int numDoses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_entry);
        findViewById(R.id.save_button).setOnClickListener(this);

        // Load intent extras
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
        nameView = (TextView)findViewById(R.id.enter_name);
        dosageView = (TextView)findViewById(R.id.enter_dosage);
        instructionView = (TextView)findViewById(R.id.enter_instructions);

        nameView.setText(MedName);
        dosageView.setText(Dosage + Unit);
        instructionView.setText(Instruction);


        // Populate Spinner
        Spinner spinner = (Spinner) findViewById(R.id.doses_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dose_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set Listener
        spinner.setOnItemSelectedListener(this);

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
            Intent intent = new Intent(this, MedNotificationConfigurations.class);
            startActivity(intent);
        }


    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        //nameView.setText((String)parent.getItemAtPosition(pos));
        numDoses = Integer.parseInt((String) parent.getItemAtPosition(pos));

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        nameView.setText("Wet slippery pussy");
    }



}
