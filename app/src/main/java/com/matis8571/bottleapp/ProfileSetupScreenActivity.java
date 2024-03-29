package com.matis8571.bottleapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class ProfileSetupScreenActivity extends AppCompatActivity {
    private static final String TAG = "ProfileSetupScreen";

    TextView setupMessageText;
    Button setupToMainButton, submitButton, setupToFilterSetupButton;
    EditText nameEdit, weightEdit, filterEfficiencyEdit, bottleCapacityEdit;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setup_layout);
        Log.d(TAG, "onCreate: Starting");

        setupToFilterSetupButton = findViewById(R.id.setupToFilterSetupButton);
        setupToMainButton = findViewById(R.id.setupToMainButton);
        submitButton = findViewById(R.id.submitChangesButton);
        setupMessageText = findViewById(R.id.profileMessage);
        nameEdit = findViewById(R.id.amountToDrink);
        weightEdit = findViewById(R.id.setupWeight);
        filterEfficiencyEdit = findViewById(R.id.setupFilterEfficiency);
        bottleCapacityEdit = findViewById(R.id.setupBottleCapacity);

        setupMessageText.setText("Input the following:");

        setupToMainButton.setOnClickListener(view -> {
            Log.d(TAG, "onClick: setupToMainButton");
            Intent setupToMainButtonIntent = new Intent(ProfileSetupScreenActivity.this, MainActivity.class);
            startActivity(setupToMainButtonIntent);
        });

        submitButton.setOnClickListener(view -> {
            //enable showProfileButton if every variable from profileSetupScreen is set
            if (weightEdit.getText().toString().isEmpty() || nameEdit.getText().toString().isEmpty() ||
                    bottleCapacityEdit.getText().toString().isEmpty() ||
                    filterEfficiencyEdit.getText().toString().isEmpty()
            ) {
                Log.d(TAG, "onClick: submitButton (empty fields)");
                Toast.makeText(ProfileSetupScreenActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();

            } else {
                Log.d(TAG, "onClick: submitButton");
                //assign user input to a String array variables
                int userWeight = Integer.parseInt(weightEdit.getText().toString());
                int bottleCapacity = Integer.parseInt(bottleCapacityEdit.getText().toString());
                int filterEfficiency = Integer.parseInt(filterEfficiencyEdit.getText().toString());
                String profileName = nameEdit.getText().toString();

                if (userWeight < 0 || userWeight > 150) {
                    Toast.makeText(ProfileSetupScreenActivity.this, "Incorrect weight!\n0 - 150kg", Toast.LENGTH_SHORT).show();
                } else if (bottleCapacity < 0 || bottleCapacity > 5000) {
                    Toast.makeText(ProfileSetupScreenActivity.this, "Incorrect bottle capacity!\n0 - 5000ml", Toast.LENGTH_SHORT).show();
                } else if (filterEfficiency < 0 || filterEfficiency > 300) {
                    Toast.makeText(ProfileSetupScreenActivity.this, "Incorrect filter efficiency!\n0 - 300l", Toast.LENGTH_SHORT).show();
                } else if (isNumeric(profileName)) {
                    Toast.makeText(ProfileSetupScreenActivity.this, "Name contains numbers!", Toast.LENGTH_SHORT).show();
                } else {
                    //in order to send any variables between activities use SharedPreferences
                    // create variable name userProfilePrefs, and then a name to identify it with "userProfilePrefs"
                    // lastly set up a Context mode
                    SharedPreferences userProfilePrefs = getSharedPreferences("userProfilePrefs", Context.MODE_PRIVATE);
                    //create SharedPreferences.Editor and assigns previously created SharedPreferences variable to it
                    SharedPreferences.Editor userProfilePrefsEditor = userProfilePrefs.edit();
                    //to send specific variables put them under custom name "userWeight"
                    // and then call which one you want to send int userWeight
                    userProfilePrefsEditor.putInt("userWeight", userWeight);
                    userProfilePrefsEditor.putInt("bottleCapacity", bottleCapacity);
                    userProfilePrefsEditor.putInt("filterEfficiency", filterEfficiency);
                    userProfilePrefsEditor.putString("profileName", profileName);
                    //after setting everything .apply(); to the Editor
                    userProfilePrefsEditor.apply();

                    Toast.makeText(ProfileSetupScreenActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();

                    Intent setupToFilterSetupButtonIntent = new Intent(ProfileSetupScreenActivity.this, FilterSetupActivity.class);
                    startActivity(setupToFilterSetupButtonIntent);
                }
            }
        });

        setupToFilterSetupButton.setOnClickListener(v -> {
            Log.d(TAG, "onClick: setupToFilterSetupButton");

            Intent setupToFilterSetupButtonIntent = new Intent(ProfileSetupScreenActivity.this, FilterSetupActivity.class);
            startActivity(setupToFilterSetupButtonIntent);
        });
    }

    /**
     * A compiled representation of a regular expression.
     * A regular expression, specified as a string, must first be compiled into an instance of this class.
     * The resulting pattern can then be used to create a Matcher object that can match arbitrary character sequences
     * against the regular expression.
     */
    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * Regex(regular expression) -?\d+(\.\d+)? matches numeric Strings consisting of the positive or negative integer and floats.
     * -? – this part identifies if the given number is negative, the dash “–” searches for dash
     * literally and the question mark “?” marks its presence as an optional one
     * \d+ – this searches for one or more digits
     * (\.\d+)? – this part of regex is to identify float numbers. Here we're searching for one or more digits
     * followed by a period. The question mark, in the end, signifies that this complete group is optional.
     */
    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}