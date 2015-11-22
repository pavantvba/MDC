package com.mdc.mdc;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.List;
import java.util.ArrayList;
import android.widget.AdapterView;



public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    SaveData saveData;
    //String storedPwd;
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase sqLiteDatabaseRead;
    int requestCode = 100;
    public static String  selectedBusinessName;
    Spinner bussinessSpinner;
    String CLASSNAME = this.getClass().getName();
    TextView mdcTextView;
    //ForgotPwd forgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Context context = getActivity();

        saveData = new SaveData(getApplicationContext());
        Log.d("MainActivity","Before getWriteableDatabase");
        sqLiteDatabase = saveData.getWritableDatabase();
        //sqLiteDatabase.
        Log.d("MainActivity", "AFTER getWriteableDatabase");



        if (!checkBussinessRecordExisting()){
            Intent setPwdIntent = new Intent(this,SetPassword.class);
            startActivityForResult(setPwdIntent, requestCode);
        }

        setContentView(R.layout.activity_main);
        bussinessSpinner = (Spinner)findViewById(R.id.spinnerBusinessNames);
        populateBussinessSpinner();
        mdcTextView = (TextView)findViewById(R.id.mdcText);
    }

    public void addBusiness(View view){

        Intent setPwdIntent = new Intent(this,SetPassword.class);
        startActivityForResult(setPwdIntent, requestCode);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){

        Log.d(CLASSNAME, "Selected Item..: " + parent.getSelectedItem().toString());
        selectedBusinessName = parent.getSelectedItem().toString();
        mdcTextView.setText(selectedBusinessName);
    }

    public void onNothingSelected(AdapterView<?> parent){

    }

    public void populateBussinessSpinner(){

        ContentValues bussinessNames = saveData.fetchData("BussinessName", "All");
        List<String> bussinessList = new ArrayList<String>();
        for (int a = 0; a < bussinessNames.size(); a++) {

            bussinessList.add(bussinessNames.get("BussinessName" + a).toString());
        }


        ArrayAdapter<String> bussinessAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bussinessList);
        bussinessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bussinessSpinner.setAdapter(bussinessAdapter);
        bussinessSpinner.setOnItemSelectedListener(this);

    }

    public boolean checkBussinessRecordExisting(){

        boolean check = true;

        if(saveData.fetchData("BussinessName", "All").size() == 0){
            check = false;
        }

        return check;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d(CLASSNAME,"Control in onActivityResult method.."+ requestCode + " : " + resultCode);
        populateBussinessSpinner();

        if (requestCode == 100){
            if(resultCode == RESULT_OK){
                Log.d("MainActivity", "Setpassword Intent successfully finished JOB");
                Bundle b = data.getExtras();
                Toast.makeText(this, b.getString("Message"), Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == 200){
            Toast.makeText(this, "Settings Activity abruptly closed..", Toast.LENGTH_SHORT).show();
        }
    }

    public void validatePwd(View view){

        selectedBusinessName =  bussinessSpinner.getSelectedItem().toString();

        EditText pwd = (EditText) findViewById(R.id.pwdText);

        String enteredPwd = pwd.getText().toString();
        if(enteredPwd.length() > 0 ) {
            String dbPwd = (saveData.fetchData("Password", selectedBusinessName)).get("Password").toString();

            if (enteredPwd.equalsIgnoreCase(dbPwd)) {
                //Toast.makeText(this,pwd.getText(),Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "Before Creating Intent");
                Intent intent = new Intent(this, PriceCalculator.class);

                //intent.putExtra("SaveData", saveData);
                Log.d("MainActivity", "Intent created");
                startActivityForResult(intent, requestCode);
                Log.d("MainActivity", "Intent started");
            } else {
                Toast.makeText(this, "Please enter the correct password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Please enter the password", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void forgotPassword(View view){

        Intent forgotPwdIntent = new Intent(this,ForgotPwd.class);
        startActivityForResult(forgotPwdIntent, requestCode);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
