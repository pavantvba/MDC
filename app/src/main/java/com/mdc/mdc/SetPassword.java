package com.mdc.mdc;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.content.Intent;
/**
 * Created by pavant on 10/14/15.
 */
public class SetPassword extends Activity {

    Spinner spinner;
    SaveData saveData;
    String CLASSNAME = this.getClass().getName();
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        saveData = new SaveData(this.getApplicationContext());
        setContentView(R.layout.setpwd_layout);
        spinner = (Spinner)findViewById(R.id.pwdHintQuestion);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.pwdHintQuestions_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

    }

    public void savePassword(View view){

        ContentValues contentValues = new ContentValues();

        EditText bussinessName = (EditText) findViewById(R.id.businessEdit);
        contentValues.put("BussinessName", bussinessName.getText().toString());

        EditText entNewPwd = (EditText) findViewById(R.id.enterNewPwd);
        EditText reentNewPwd = (EditText) findViewById(R.id.reentNewPwd);

        Spinner spinnerPwdHintQues = (Spinner) findViewById(R.id.pwdHintQuestion);
        contentValues.put("PwdhintQuestion",spinnerPwdHintQues.getSelectedItem().toString());

        EditText editTextPwdAns = (EditText) findViewById(R.id.pwdHintAns);
        contentValues.put("Pwdhintanswer", editTextPwdAns.getText().toString());

        EditText editTextExpense = (EditText) findViewById(R.id.expenseNumber);
        contentValues.put("ExpensePercentage", editTextExpense.getText().toString());

        EditText editTextPercn = (EditText) findViewById(R.id.percentageNumber);
        contentValues.put("SellingPercentage", editTextPercn.getText().toString());

        EditText etZero = (EditText) findViewById(R.id.zeroNum);
        String strCode = etZero.getText().toString();

        EditText etOne = (EditText) findViewById(R.id.oneNum);
        strCode = strCode + etOne.getText().toString();

        EditText etTwo = (EditText) findViewById(R.id.twoNum);
        strCode = strCode + etTwo.getText().toString();

        EditText etThree = (EditText) findViewById(R.id.threeNum);
        strCode = strCode + etThree.getText().toString();

        EditText etFour = (EditText) findViewById(R.id.fourNum);
        strCode = strCode + etFour.getText().toString();

        EditText etFive = (EditText) findViewById(R.id.fiveNum);
        strCode = strCode + etFive.getText().toString();

        EditText etSix = (EditText) findViewById(R.id.sixNum);
        strCode = strCode + etSix.getText().toString();

        EditText etSeven = (EditText) findViewById(R.id.sevenNum);
        strCode = strCode + etSeven.getText().toString();

        EditText etEight = (EditText) findViewById(R.id.eightNum);
        strCode = strCode + etEight.getText().toString();

        EditText etNine = (EditText) findViewById(R.id.nineNum);
        strCode = strCode + etNine.getText().toString();

        contentValues.put("CodeString", strCode);

        Log.d("Storing Values ...", contentValues.toString());

        if(entNewPwd.getText().toString().equals(reentNewPwd.getText().toString())){

            contentValues.put("Password", entNewPwd.getText().toString());
            if(saveData.storeData("Insert", contentValues, MainActivity.selectedBusinessName)) {
                Toast.makeText(this, "Values Successfully Saved..", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Message", "Settings are save successfully");
                setResult(100, resultIntent);
                Log.d("SetPassword", "Before closing.... ");

                finish();

            } else {
                Toast.makeText(this, "Error occurred while storing the settings..", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this,"Password entered not matching.. please check", Toast.LENGTH_SHORT).show();
        }
    }

    public void closeAction(View view){
        Log.d(CLASSNAME, "CloseAction");
        //finishActivity(200);
        finish();

    }

}
