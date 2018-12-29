package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.Arrays;


public class MedScannedListings extends Activity implements View.OnClickListener {

    public TextView titleView;
    public Button saveButton;
    LinearLayout linearLayout;
    protected String[] ScannedListings; // Accepted array of medications
    protected int resultsSum = 0;       // Used to keep track of successful configurations
    private boolean[] beenConfigured;   // True if medication has been successfully configured

    private static final int RC_MED_ENTRY = 100;
    private static final String TAG = "MedScannedListings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_scanned_listings);

        // Load intent extras
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                ScannedListings = null;
            } else {
                ScannedListings = extras.getStringArray("ScannedListings");
            }
        } else {
            ScannedListings = (String[]) savedInstanceState.getSerializable("ScannedListings");
        }

        // Configure views
        titleView = (TextView)findViewById(R.id.text_view_1);
        linearLayout = (LinearLayout) findViewById(R.id.medscannedlistings_layout);

        // Create buttons for each medication
        assert ScannedListings != null;
        int buttonIDs = 0;
        for (String ScannedListing : ScannedListings) {
            String[] medFeatures = MainActivity.parseMedication(ScannedListing);
            String medName = medFeatures[0];
            createNewView(linearLayout, medName, buttonIDs);
            buttonIDs++;
        }

        // Initialize beenConfigured
        beenConfigured = new boolean[ScannedListings.length];

        // Create save button and set it to not clickable
        saveButton = new Button(this);
        saveButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        saveButton.setText("Save");
        saveButton.setId((int)99);
        saveButton.setPadding(0, 5, 0, 5);
        saveButton.setVisibility(View.INVISIBLE);
        saveButton.setOnClickListener(this);
        linearLayout.addView(saveButton);




    }




    public void createNewView(LinearLayout linearLayout, String medName, int buttonID){

        // Add button
        Button newButton = new Button(this);
        newButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newButton.setText(medName);
        newButton.setId(buttonID);
        newButton.setTextColor(ContextCompat.getColor(this, R.color.red));
        newButton.setOnClickListener(this);
        linearLayout.addView(newButton);
    }



    @Override
    public void onClick(View v) {

        if ((int)v.getId() == 99)
        {
            titleView.setText("Save Clicked");
        } else {
            // show a message with the button's ID
            Toast toast = Toast.makeText(this, "You clicked button " + v.getId(), Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent(this, ConfirmMed.class);
            intent.putExtra("Medication", ScannedListings[v.getId()]);
            intent.putExtra("Id", v.getId());
            startActivityForResult(intent, RC_MED_ENTRY);

        }






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If configurations correct are returned, the corresponding beenConfigured is set to true
        if (requestCode == RC_MED_ENTRY) {
            if(resultCode == Activity.RESULT_OK){
                // Get extras
                boolean result = data.getBooleanExtra("result", false);
                int id = data.getIntExtra("id", -1);
                // Update configurations
                if (id != -1 && result)
                {
                    beenConfigured[id] = true;
                    Button thisButton = (Button)findViewById(id);
                    thisButton.setTextColor(ContextCompat.getColor(this, R.color.green));
                }

                titleView.setText(Boolean.toString(beenConfigured[id]));
            }
            if (resultCode == Activity.RESULT_CANCELED) {

                titleView.setText("Pussy");
            }
        }

        if (areAllTrue(beenConfigured)){
            saveButton.setVisibility(View.VISIBLE);
        }


    }

    // True if all booleans in an array are tru
    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }




}
