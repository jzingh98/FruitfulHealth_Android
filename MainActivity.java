/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import java.util.regex.Pattern;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * recognizes text.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // Use a compound button so either checkbox or switch widgets work.
    private Button scanButton;
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;
    private TextView titleText;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue = (TextView)findViewById(R.id.text_value);
        titleText = (TextView)findViewById(R.id.status_message);
        scanButton = (Button)findViewById(R.id.read_text);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_text).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    // Capture Text
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    //textValue.setText(text);
                    //parseMedication(text);

                    // Interpret Listings
                    String[] separatedListings = separateListings(text);
                    String[] medOnlyListings = parseListings(separatedListings);

                    textValue.setText(Arrays.toString(medOnlyListings));


                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * Array concatenation function
     */
    public static String[] addToStringArray(String[] originalArray, String newItem)
    {
        int currentSize = originalArray.length;
        int newSize = currentSize + 1;
        String[] tempArray = new String[ newSize ];
        for (int i=0; i < currentSize; i++)
        {
            tempArray[i] = originalArray [i];
        }
        tempArray[newSize- 1] = newItem;
        return tempArray;
    }



    /**
     * @param  scannedText: Raw string of scanned text returned by OCRCaptureActivity
     * @return newMedArray: Array of strings corresponding to each graphic scanned
     * Separates the scanned text into an array of strings using the delimeter XXX.
     * Also removes duplicate entries.
     */
    public String[] separateListings(String scannedText) {

        String medNameArray[] = scannedText.split("XXX", 20);

        // Remove duplicates
        Set<String> set = new HashSet<String>();
        Collections.addAll(set, medNameArray);
        String[] newMedArray = set.toArray(new String[0]);

        // DEBUG: Number of entries
        //String a = Integer.toString(medNameArray.length);
        //String b = Integer.toString(newMedArray.length);

        return newMedArray;
    }



    /**
     * @param  scannedArray: Array of scanned text returned by this.separateListings()
     * @return medsOnlyArray: Array of strings corresponding only to medications
     * Removes entries from scannedArray that do not correspond to medications
     */
    public String [] parseListings(String[] scannedArray) {
        String[] medsOnlyArray = new String[0];
        for (String aScannedArray : scannedArray) {
            String[] thisFeatures = parseMedication(aScannedArray);

            if ((thisFeatures[2].equals(""))) {
                Log.d(TAG, "removed: ");
            } else {
                medsOnlyArray = addToStringArray(medsOnlyArray, aScannedArray);
            }
        }
        return medsOnlyArray;
    }



    /**
     * @param  scannedText: String corresponding to an entry
     * @return parsedMedicationArray: Array of strings corresponding to
     *          successfully parsed features of the entry
     * Attempts to parse the name, dosage, unit, action, and instructions from an entry
     */
    public String[] parseMedication(String scannedText) {
        String arr[] = scannedText.split(" ", 100);
        String unit = "";
        String dosage = "";
        String medname = "";
        String action = "";
        String instruction = "";

        // Parse the medication string
        for (int i = 0; i < arr.length; i++) {
            String currentWord = arr[i];
            // Parse units, dosage, and medication name
            if (currentWord.equals("mg") || currentWord.equals("%") ||
                    currentWord.equals("mcg/actuation") || currentWord.equals("gram") ||
                    currentWord.equals("mEq") || currentWord.equals("mg/mL") ||
                    currentWord.equals("unit/mL")) {
                String mednameArray[] = scannedText.split(arr[i-1], 2);
                unit = currentWord;
                dosage = arr[i-1];
                medname = mednameArray[0];
            }
            // Parse action and instruction
            if (currentWord.contains("Take") || currentWord.contains("Instill") ||
                    currentWord.contains("Apply") || currentWord.contains("Spray")) {
                if(currentWord.contains("Take"))
                    action = "Take";
                if(currentWord.contains("Instill"))
                    action = "Instill";
                if(currentWord.contains("Apply"))
                    action = "Apply";
                if(currentWord.contains("Spray"))
                    action = "Spray";
                String intructionArray[] = scannedText.split(arr[i], 2);
                instruction = intructionArray[1];
            }
        }
        // Package contents into a string array
        String[] parsedMedicationArray = new String[5];
        parsedMedicationArray[0] = medname;
        parsedMedicationArray[1] = dosage;
        parsedMedicationArray[2] = unit;
        parsedMedicationArray[3] = action;
        parsedMedicationArray[4] = instruction;

        return parsedMedicationArray;

//        textValue.setText("M: " + medname + "\nD: " + dosage + "\nU: " + unit +
//                "\nA: " + action + "\nI: " + instruction);

//        Intent i = new Intent(this, MedEntry.class);
//        i.putExtra("MedName", medname);
//        i.putExtra("Dosage", dosage);
//        i.putExtra("Unit", unit);
//        i.putExtra("Instruction", instruction);
//        startActivity(i);
    }




    


}
