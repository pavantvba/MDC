package com.mdc.mdc;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by pavant on 11/20/15.
 */
public class ForgotPwd extends Activity implements AdapterView.OnItemSelectedListener {

    SaveData saveData;
    Spinner spinnerBusinessNames;
    String CLASSNAME = getClass().getName();
    TextView hintQueDisplay;
    TextView pwdShowText;
    EditText pwdHintAns;

    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        saveData = new SaveData(getApplicationContext());
        setContentView(R.layout.forgot_pwd);
        spinnerBusinessNames = (Spinner) findViewById(R.id.spinnerBusinessNames);
        hintQueDisplay = (TextView) findViewById(R.id.hintQuestion);
        pwdHintAns = (EditText) findViewById(R.id.pwdHintAns);
        pwdShowText = (TextView) findViewById(R.id.pwdShow);
        populateBussinessSpinner();
    }

    public void populateBussinessSpinner(){

        ContentValues bussinessNames = saveData.fetchData("BussinessName", "All");
        List<String> bussinessList = new ArrayList<String>();
        for (int a = 0; a < bussinessNames.size(); a++) {

            bussinessList.add(bussinessNames.get("BussinessName" + a).toString());
        }


        ArrayAdapter<String> bussinessAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bussinessList);
        bussinessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBusinessNames.setAdapter(bussinessAdapter);
        spinnerBusinessNames.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){

        Log.d(CLASSNAME, "Selected Item..: " + parent.getSelectedItem().toString());

        ((TextView)findViewById(R.id.busName)).setText(parent.getSelectedItem().toString());
        ContentValues cvalues = saveData.fetchData("PwdhintQuestion", parent.getSelectedItem().toString());
        Log.d(CLASSNAME, "Selected Item Pwdhint Question..: " + cvalues.get("PwdhintQuestion").toString());
        //cvalues.get("PwdhintQuestion");
        hintQueDisplay.setText(cvalues.get("PwdhintQuestion").toString());

    }

    public void validateAnswerandretreivePwd(View view){

        ContentValues cvalues = saveData.fetchData("Password", spinnerBusinessNames.getSelectedItem().toString());
        ContentValues cvalues1 = saveData.fetchData("Pwdhintanswer", spinnerBusinessNames.getSelectedItem().toString());
        Log.d(CLASSNAME, "Pwd & Hint Answer " + cvalues.get("Password").toString() + " : " +  cvalues1.get("Pwdhintanswer").toString());
        Log.d(CLASSNAME, "Entered hint answer : " + pwdHintAns.getText());
        String enteredPwd = pwdHintAns.getText().toString().trim();
        //cvalues.get("Pwdhintanswer");
        if(enteredPwd.equalsIgnoreCase(cvalues1.get("Pwdhintanswer").toString().trim())){
            pwdShowText.setText(cvalues.get("Password").toString());

        } else {
            Toast.makeText(this,"Entered answer is not matching.. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> parent){

    }

    public void closeForgotPwd(View view){
        finish();
    }
}
