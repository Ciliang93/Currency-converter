package com.helha.fiche6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Ciliberto Angelo Ressource: Fiche6_labo From Olivier Legrand
 * Modified for Android Exam
 */

public class MainActivity extends AppCompatActivity {
    private static final int NB_DEVISES = 4;
    public static final String NOM_FICHIER_TAUX = "taux";
    private Spinner spinnerDevise1;
    private Spinner spinnerDevise2;
    private EditText editTextMontant;
    private TextView textViewResultat;
    private SpinnerAdapter adapter;
    private String[] devises;
    private String[] libellesDevises;
    private int icones[];
    private HashMap<String, Double> taux;
    private int deviseSpinner1;
    private int deviseSpinner2;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // tableau de devises
        this.devises = getResources().getStringArray(R.array.tableau_devises);

        // tableau de libellés de devises
        this.libellesDevises = getResources().getStringArray(R.array.tableau_devises_libelles);

        this.sharedPreferences = this.getSharedPreferences("com.helha.fiche6", Context.MODE_PRIVATE);

        //On vérifie si c'est la première fois qu'on utilise l'app
        if (!(sharedPreferences.contains("Visited"))) {
            //Si oui, on sauve une ShPr visited, on initialise les taux avec la méthode et on les sauve sur un fichier
            sharedPreferences.edit().putBoolean("Visited", true).apply();
            initialiserTaux();
            try {
                sauverTaux();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                chargerTaux();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //Recup des devises choisies sauvées dans les préfs
        chargerDevisesSelectionnees();

        initialiserIcones();
        initialiserUI();

    }


    private void sauverTaux() throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(NOM_FICHIER_TAUX, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.taux);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
            oos.close();
        }
    }

    private void chargerTaux() throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(NOM_FICHIER_TAUX);
            ois = new ObjectInputStream(fis);

            this.taux = (HashMap<String, Double>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
            ois.close();
        }
    }

    private void chargerDevisesSelectionnees() {
        //A la première ouverture de l'application, on sette les devises à 1 et 3
        if (this.sharedPreferences.contains("Devise1") && this.sharedPreferences.contains("Devise2")) {
            this.deviseSpinner1 = this.sharedPreferences.getInt("Devise1", -1);
            this.deviseSpinner2 = this.sharedPreferences.getInt("Devise2", -1);
        } else {
            this.deviseSpinner1 = 1;
            this.deviseSpinner2 = 3;
        }
    }

    private void sauverDevisesSelectionnees() {
        this.sharedPreferences.edit().putInt("Devise1", this.spinnerDevise1.getSelectedItemPosition()).apply();
        this.sharedPreferences.edit().putInt("Devise2", this.spinnerDevise2.getSelectedItemPosition()).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // à compléter
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initialiserTaux() {
        this.taux = new HashMap<String, Double>();
        this.taux.put("USD -> EUR", 0.859311);
        this.taux.put("USD -> GBP", 0.773245);
        this.taux.put("USD -> CHF", 0.969296);
        this.taux.put("EUR -> USD", 1.16349);
        this.taux.put("EUR -> GBP", 0.899664);
        this.taux.put("EUR -> CHF", 1.12824);
        this.taux.put("GBP -> USD", 1.29356);
        this.taux.put("GBP -> EUR", 1.11181);
        this.taux.put("GBP -> CHF", 1.25419);
        this.taux.put("CHF -> USD", 1.03140);
        this.taux.put("CHF -> EUR", 0.886544);
        this.taux.put("CHF -> GBP", 0.797395);
    }

    //initialise les icones des devises
    private void initialiserIcones() {
        this.icones = new int[NB_DEVISES];
        this.icones[0] = R.drawable.chf;
        this.icones[1] = R.drawable.euro;
        this.icones[2] = R.drawable.gbp;
        this.icones[3] = R.drawable.usd;
    }

    private void initialiserUI() {
        //Recup des refs des vues de l'ui
        this.editTextMontant = (EditText) findViewById(R.id.editTextMontant);
        this.spinnerDevise1 = (Spinner) findViewById(R.id.spinnerDevise1);
        this.spinnerDevise2 = (Spinner) findViewById(R.id.spinnerDevise2);
        this.textViewResultat = (TextView) findViewById(R.id.textViewResultat);

        // adapter
        this.adapter = new DeviseSpinnerAdapter(this, this.icones, this.libellesDevises);

        // spinner 1
        this.spinnerDevise1.setAdapter(adapter);
        this.spinnerDevise1.setSelection(deviseSpinner1);
        this.spinnerDevise1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                effacerResultat();
                sauverDevisesSelectionnees();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // spinner 2
        this.spinnerDevise2.setAdapter(adapter);
        this.spinnerDevise2.setSelection(deviseSpinner2);
        this.spinnerDevise2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                effacerResultat();
                sauverDevisesSelectionnees();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // editTextMontant
        this.editTextMontant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                effacerResultat();
            }
        });
    }

    public void convertirMontant(View view) {
        String stringMontant = this.editTextMontant.getText().toString();
        if (stringMontant.isEmpty()) {
            Toast.makeText(this, R.string.pas_montant, Toast.LENGTH_LONG).show();
            return;
        }
        double montant = Double.parseDouble(stringMontant);

        int dev1Position = this.spinnerDevise1.getSelectedItemPosition();
        int dev2Position = this.spinnerDevise2.getSelectedItemPosition();

        String devise1 = this.devises[dev1Position];
        String devise2 = this.devises[dev2Position];

        double resultat = convertirMontant(montant, devise1, devise2);
        String resultatFormate = String.format("%.2f %s = %f %s", montant, devise1, resultat, devise2);
        hideKeyboard(this);
        this.textViewResultat.setText(resultatFormate);
    }

    public void effacerResultat() {
        this.textViewResultat.setText("");
    }

    private double convertirMontant(double montant, String devise1, String devise2) {
        double resultat = 0;
        if (devise1.equals(devise2)) {
            return montant;
        }
        String clef = devise1 + " -> " + devise2;
        Double tauxDeChange = this.taux.get(clef);
        if (tauxDeChange != null) {
            resultat = montant * tauxDeChange;
            Toast.makeText(this, getResources().getString(R.string.toast_conversion_ok_text) + " : " + clef + " " + getResources().getString(R.string.toast_conversion_ok_rate_text) + " " + tauxDeChange, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.toast_conversion_ko)+" "+ clef, Toast.LENGTH_LONG);
        }
        return resultat;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_modifier_taux:
                goToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        effacerResultat();
        try {
            chargerTaux();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsCurrencies.class);
        startActivity(intent);
    }

}
