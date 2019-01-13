package com.helha.fiche6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class SettingsCurrencies extends AppCompatActivity {

    HashMap<String, Double> taux;
    Set<String> set;

    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_currencies);
        try {
            chargerTaux();
            this.listView = findViewById(R.id.list_view_set_rates);
            final ArrayList<String> listKeys = new ArrayList<String>();

            Iterator it = this.set.iterator();
            while (it.hasNext()){
                String str = it.next().toString();
                listKeys.add(str);
            }

            this.adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listKeys);
            this.listView.setAdapter(adapter);

            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), SetRateActivity.class);
                    intent.putExtra("RateSymbol", listKeys.get(position));
                    startActivity(intent);
                }
            });


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
            this.set = this.taux.keySet();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
            ois.close();
        }
    }

}
