package com.helha.fiche6;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

/**
 * Created by Olivier on 06-09-18.
 * Modified by Ciliberto Angelo on 02-01-19
 */
public class DeviseSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private String[] libellesDevises;
    private int icones[];
    private int position;
    private Context context;

    public DeviseSpinnerAdapter(Context mainActivity, int[] icones, String[] libellesDevises) {
        super();
        this.context = mainActivity;
        this.icones = icones;
        this.libellesDevises = libellesDevises;
    }

    @Override
    public int getCount() {
        return this.icones.length;
    }

    @Override
    public Object getItem(int i) {
        return this.libellesDevises[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        this.position = i;
        int imageView = this.icones[i];
        String libelleDevise = this.libellesDevises[i];
        ViewDevise vueDevise = new ViewDevise(this.context, imageView, libelleDevise);
        return vueDevise;
    }
}
