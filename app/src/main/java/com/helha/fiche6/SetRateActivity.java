package com.helha.fiche6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SetRateActivity extends AppCompatActivity {

    private HashMap<String, Double> taux;

    private EditText editText;
    private TextView textView;

    private String chosenKey;

    private double oldValue;
    private double newValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rate);

        Intent intent = getIntent();

        this.chosenKey = intent.getStringExtra("RateSymbol");

        this.textView = findViewById(R.id.chose_rate_symbol);
        this.textView.setText(chosenKey);

        this.editText = findViewById(R.id.edit_text_rate);

        try {
            chargerTaux();
            if (taux.containsKey(chosenKey)) {
                this.oldValue = taux.get(chosenKey);
                editText.setText(oldValue + "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void chargerTaux() throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput("taux");
            ois = new ObjectInputStream(fis);
            this.taux = (HashMap<String, Double>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
            ois.close();
        }
    }

    public void modifyRate(View view) throws IOException {
        this.newValue = Double.parseDouble(String.valueOf(this.editText.getText()));
        if(this.newValue == this.oldValue){
            Toast.makeText(this, R.string.failure_new_rate, Toast.LENGTH_LONG).show();
        }else{
            this.taux.remove(chosenKey);
            this.taux.put(chosenKey, this.newValue);
            MainActivity.hideKeyboard(this);
            Toast.makeText(this, R.string.success_new_rate, Toast.LENGTH_LONG).show();
            try {
                sauverTaux();
                this.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sauverTaux() throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput("taux", MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.taux);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
            oos.close();
        }
    }
}
