// java/com/example/plantely/SettingsFragment.java
package com.example.plantely;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;

/**
 * Bietet UI zur Konfiguration von Benachrichtigungen, Theme und Hinzufügen/Löschen von Kategorien.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private CategoryRepository categoryRepo;
    private PreferenceCategory catListPref;
    private Preference addCatPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        categoryRepo = CategoryRepository.getInstance(getContext());
        catListPref  = findPreference("pref_category_list");
        addCatPref   = findPreference("pref_add_category");

        // Klick auf “Kategorie hinzufügen”
        addCatPref.setOnPreferenceClickListener(preference -> {
            showAddCategoryDialog();
            return true;
        });

        populateCategoryList();
    }

    private void populateCategoryList() {
        catListPref.removeAll();

        List<String> cats = categoryRepo.getCategories();
        for (String cat : cats) {
            Preference p = new Preference(getContext());
            p.setKey("cat_" + cat);
            p.setTitle(cat);
            p.setSummary("Tippe zum Löschen");
            p.setOnPreferenceClickListener(pref -> {
                confirmDeleteCategory(cat);
                return true;
            });
            catListPref.addPreference(p);
        }
    }

    private void showAddCategoryDialog() {
        EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(getContext())
                .setTitle("Neue Kategorie")
                .setView(input)
                .setPositiveButton("Hinzufügen", (dialog, which) -> {
                    String cat = input.getText().toString().trim();
                    if (!cat.isEmpty()) {
                        categoryRepo.addCategory(cat);
                        Toast.makeText(getContext(),
                                "Kategorie hinzugefügt: " + cat,
                                Toast.LENGTH_SHORT).show();
                        populateCategoryList();
                    }
                })
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    private void confirmDeleteCategory(String cat) {
        new AlertDialog.Builder(getContext())
                .setTitle("Löschen bestätigen")
                .setMessage("Kategorie \"" + cat + "\" wirklich löschen?")
                .setPositiveButton("Ja", (dialog, which) -> {
                    categoryRepo.removeCategory(cat);
                    Toast.makeText(getContext(),
                            "Kategorie gelöscht: " + cat,
                            Toast.LENGTH_SHORT).show();
                    populateCategoryList();
                })
                .setNegativeButton("Abbrechen", null)
                .show();
    }
}
