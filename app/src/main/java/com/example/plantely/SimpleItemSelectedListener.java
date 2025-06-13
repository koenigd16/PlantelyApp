package com.example.plantely;

import android.view.View;
import android.widget.AdapterView;

/**
 * Helfer-Listener für Spinner,
 * reduziert Boilerplate von OnItemSelectedListener.
 */
public abstract class SimpleItemSelectedListener
        implements AdapterView.OnItemSelectedListener {

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nicht verwendet
    }

    @Override
    public void onItemSelected(AdapterView<?> parent,
                               View view,
                               int position,
                               long id) {
        // delegiere nur die Position
        onItemSelected(position);
    }

    /**
     * Wird aufgerufen, wenn ein Spinner-Item ausgewählt wird.
     * @param position Index des ausgewählten Eintrags
     */
    public abstract void onItemSelected(int position);
}
