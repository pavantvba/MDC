package com.mdc.mdc;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created by pavant on 10/9/15.
 */
public class PriceCalculator extends Activity {

    //ArrayList<String> codeList = new ArrayList<String>(Arrays.asList("U","M","H","D","V","C","A","E","I","O"));
    ArrayList<String> codeList = new ArrayList<String>();
    CheckBox saveCheckBox;
    TextView finalPriceTextView;
    RadioButton codeRadioButton;
    EditText priceText;
    EditText expenseText;
    EditText percentageText;
    SaveData saveData;
    TextView mdcTextView;
    TextView priceCalText;
    String CLASSNAME = getClass().getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pricecalculator);
        mdcTextView = (TextView) findViewById(R.id.mdcText);
        mdcTextView.setText(MainActivity.selectedBusinessName);
        saveCheckBox = (CheckBox)findViewById(R.id.saveBox);
        codeRadioButton = (RadioButton) findViewById(R.id.codeRadio);
        priceText = (EditText) findViewById(R.id.price);
        finalPriceTextView = (TextView) findViewById(R.id.finalPrice);
        finalPriceTextView.setTextSize(20);
        finalPriceTextView.setHighlightColor(Color.RED);
        saveCheckBox = (CheckBox)findViewById(R.id.saveBox);
        percentageText = (EditText) findViewById(R.id.percentageNumber);
        expenseText = (EditText) findViewById(R.id.expenseNumber);
        priceText = (EditText) findViewById(R.id.price);
        priceCalText = (TextView) findViewById(R.id.priceCalText);
        saveData = new SaveData(this.getApplicationContext());
        readPercentageData(); // Read and assigning values to text boxes which were stored in file
        populateCodeArray();

        expenseText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (saveCheckBox.isChecked())
                    saveCheckBox.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        percentageText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(saveCheckBox.isChecked())
                    saveCheckBox.setChecked(false);

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void populateCodeArray(){

        ContentValues contentValues = saveData.fetchData("CodeString", MainActivity.selectedBusinessName);
        Log.d(CLASSNAME, "CodeString " + contentValues.toString());
        String codeString = contentValues.get("CodeString").toString();
        for (int i=0; i < codeString.length(); i++) {

            codeList.add(""+codeString.charAt(i));

        }

    }

    public void generateCode(View view) {

        String decodeStringVal = "";

        String purchagePrice = priceText.getText().toString();

        Log.d("Price Calculator", "Entered Data:" + purchagePrice);

        if(purchagePrice.length() > 0) {


            if (codeRadioButton.isChecked()) {
                // Toast.makeText(this, "Code Radio Button Selected", Toast.LENGTH_SHORT).show();

                for (int i = 0; i < purchagePrice.length(); i++) {
                    String letter = purchagePrice.substring(i, i + 1);
                    Log.d("OuterLoop ", "Letter : " + letter + " ::: " + codeList.indexOf(letter));


                    if (codeList.indexOf(letter) != -1) {
                        decodeStringVal = decodeStringVal + "" + codeList.indexOf(letter);
                    }
                }

                if (decodeStringVal.length() > 0) {
                    finalPriceTextView.setText("Cost : " + decodeStringVal);
                } else {
                    finalPriceTextView.setText("Sorry! Please enter the correct code.");
                }

            } else {

                int purchageNumber = new Integer(purchagePrice).intValue();

                //long purchagePrice_TenPercentage = Math.round(purchageNumber + (purchageNumber * 0.1));

                long purchagePrice_TenPercentage = Math.round(purchageNumber + (purchageNumber * ((new Float(expenseText.getText().toString()).floatValue()) / 100)));

                Log.d(CLASSNAME, "Purchage Cost : " + purchageNumber + ":::" + (new Float(expenseText.getText().toString()).floatValue()) / 100);

                String purchagePrice_TenPercentage_str = new Long(purchagePrice_TenPercentage).toString();

                String purchageCode = "";
                for (int i = 0; i < purchagePrice_TenPercentage_str.length(); i++) {
                    int indexVal = new Integer(purchagePrice_TenPercentage_str.substring(i, i + 1)).intValue();
                    Log.d("Price Calculator :", "" + indexVal);
                    purchageCode = purchageCode + codeList.get(indexVal);
                }

                //long sellingPrice = Math.round(purchagePrice_TenPercentage + (purchagePrice_TenPercentage * 0.7));
                Log.d(CLASSNAME, "Selling Cost : " + purchagePrice_TenPercentage + ":::" + (new Float(percentageText.getText().toString()).floatValue()) / 100);

                long sellingPrice = Math.round(purchagePrice_TenPercentage + (purchagePrice_TenPercentage * (new Float(percentageText.getText().toString()).floatValue() / 100)));


                String displayString = "Price Info \n\n Purchase Price: " + purchageNumber + "\n\n Actual Cost (" + expenseText.getText() + "%):" + purchagePrice_TenPercentage +
                        "\n\n Selling Price (" + percentageText.getText() + "%):" + sellingPrice + "\n\n CodeString:" + purchageCode;


                finalPriceTextView.setText(displayString);
            }
        } else
        {
            Toast.makeText(this, "Please enter purchase cost ", Toast.LENGTH_SHORT).show();
        }
    }


    public void changeInputType(View view) {

        //Toast.makeText(this, "Code Radio Button Selected.. Clicked", Toast.LENGTH_SHORT).show();

        if (codeRadioButton.isChecked()) {
            priceText.setText("");
            priceText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            priceCalText.setText("Code String");
            //priceText.setAllCaps(true);
            expenseText.setEnabled(false);
            percentageText.setEnabled(false);
            saveCheckBox.setEnabled(false);

        } else {
            priceText.setText("");
            priceText.setInputType(InputType.TYPE_CLASS_NUMBER);
            priceCalText.setText("Purchase Cost");
            expenseText.setEnabled(true);
            percentageText.setEnabled(true);
            saveCheckBox.setEnabled(true);
        }
    }

    public void savePercentageData(View view){
        if(saveCheckBox.isChecked()){
            String expensPerStr =  expenseText.getText().toString();
            String sellPerStr = percentageText.getText().toString();

            ContentValues cvalues = new ContentValues();
            cvalues.put("ExpensePercentage",expensPerStr);
            cvalues.put("SellingPercentage", sellPerStr);

            if(saveData.storeData("Update",cvalues, MainActivity.selectedBusinessName)) {
                Toast.makeText(this,"Expense Percentage & Sell Percentage saved successfully!" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Unexpected issue while saving Expense percentage & Sell percentage values! Please check", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readPercentageData(){

        ContentValues expPerStr = saveData.fetchData("ExpensePercentage",MainActivity.selectedBusinessName);
        ContentValues sellPerStr = saveData.fetchData("SellingPercentage", MainActivity.selectedBusinessName);

        expenseText.setText(expPerStr.get("ExpensePercentage").toString());
        percentageText.setText(sellPerStr.get("SellingPercentage").toString());

        if(expPerStr != null  || sellPerStr != null) {
            saveCheckBox.setChecked(true);
        }

    }

    public void closeApp(View view){

        finish();

    }


}
