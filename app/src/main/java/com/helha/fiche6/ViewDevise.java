package com.helha.fiche6;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Olivier on 06-09-18.
 */

public class ViewDevise extends ConstraintLayout {

    public ViewDevise(Context context, int imageView, String libelleDevise) {
        super(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.vue_devise, this, true);

        ImageView imageViewDevise = (ImageView) findViewById(R.id.imageViewDevise);
        TextView textViewDevise = (TextView) findViewById(R.id.textViewDevise);

        Drawable image = context.getResources().getDrawable(imageView, null);
        imageViewDevise.setImageDrawable(image);
        textViewDevise.setText(libelleDevise);
    }
}
