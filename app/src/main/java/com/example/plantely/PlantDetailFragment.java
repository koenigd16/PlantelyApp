// java/com/example/plantely/PlantDetailFragment.java
package com.example.plantely;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.DateFormat;
import java.util.Date;

/**
 * Zeigt Detailinfos einer Pflanze an und erlaubt Bearbeitung von Kategorie, Alter,
 * Intervall, Erinnerung sowie Löschen. Lädt das persistierte Bild-URI erneut.
 */
public class PlantDetailFragment extends Fragment {
    private Plant currentPlant;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plant_detail, container, false);

        TextView nameView       = v.findViewById(R.id.plantName);
        Spinner categorySpinner = v.findViewById(R.id.spinnerCategory);
        EditText editAge        = v.findViewById(R.id.editPlantAge);
        ImageView imageView     = v.findViewById(R.id.plantImage);
        TextView descView       = v.findViewById(R.id.plantDescription);
        TextView nextWatering   = v.findViewById(R.id.nextWatering);
        EditText intervalEdit   = v.findViewById(R.id.editWateringInterval);
        Button saveIntervalBtn  = v.findViewById(R.id.buttonSaveInterval);
        Switch reminderSwitch   = v.findViewById(R.id.switchReminder);
        TextView dateAddedView  = v.findViewById(R.id.textDateAdded);
        Button deleteBtn        = v.findViewById(R.id.buttonDelete);

        // 1) Argument auslesen
        Bundle args = getArguments();
        if (args != null && args.containsKey("plantName")) {
            String name = args.getString("plantName");
            nameView.setText(name);
            currentPlant = PlantRepository
                    .getInstance(requireContext())
                    .getPlantByName(name);
        }

        // 2) Spinner füllen & Auswahl setzen
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.plant_categories,
                android.R.layout.simple_spinner_item
        );
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(catAdapter);

        // 3) Pflanze laden
        if (currentPlant != null) {
            // Kategorie
            int pos = catAdapter.getPosition(currentPlant.getCategory());
            if (pos >= 0) categorySpinner.setSelection(pos);

            // Alter
            editAge.setText(String.valueOf(currentPlant.getAgeYears()));

            // Bild erneut laden
            Uri photo = currentPlant.getPhotoUri();
            if (photo != null) {
                imageView.setImageURI(photo);
            }

            // Beschreibung
            String desc = currentPlant.getDescription();
            descView.setText(!TextUtils.isEmpty(desc) ? desc : "Keine Beschreibung.");

            // Intervall & Switch
            int days = currentPlant.getWateringIntervalDays();
            intervalEdit.setText(String.valueOf(days));
            nextWatering.setText("Gießen in " + days + " Tagen");
            reminderSwitch.setChecked(currentPlant.isReminderActive());

            // Erstellungsdatum
            String date = DateFormat.getDateInstance().format(
                    new Date(currentPlant.getCreatedAt()));
            dateAddedView.setText("Hinzugefügt am: " + date);
        }

        // 4) Kategorie ändern
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view,
                                                 int position, long id) {
                if (currentPlant == null) return;
                String c = parent.getItemAtPosition(position).toString();
                currentPlant.setCategory(c);
                PlantRepository.getInstance(requireContext()).saveChanges();
                Toast.makeText(getContext(),
                        "Kategorie geändert: " + c,
                        Toast.LENGTH_SHORT).show();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 5) Alter speichern
        editAge.setOnFocusChangeListener((v1, hasFocus) -> {
            if (!hasFocus && currentPlant != null) {
                String s = editAge.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    currentPlant.setAgeYears(Integer.parseInt(s));
                    PlantRepository.getInstance(requireContext()).saveChanges();
                }
            }
        });

        // 6) Intervall speichern
        saveIntervalBtn.setOnClickListener(btn -> {
            if (currentPlant == null) return;
            String s = intervalEdit.getText().toString();
            if (!TextUtils.isEmpty(s)) {
                currentPlant.setWateringIntervalDays(Integer.parseInt(s));
                nextWatering.setText("Gießen in " + s + " Tagen");
                PlantRepository.getInstance(requireContext()).saveChanges();
            }
        });

        // 7) Erinnerung-Switch
        reminderSwitch.setOnCheckedChangeListener((sw, checked) -> {
            if (currentPlant == null) return;
            currentPlant.setReminderActive(checked);
            PlantRepository.getInstance(requireContext()).saveChanges();
            Toast.makeText(getContext(),
                    checked ? "Erinnerung aktiviert" : "Erinnerung deaktiviert",
                    Toast.LENGTH_SHORT).show();
        });

        // 8) Löschen
        deleteBtn.setOnClickListener(btn -> {
            if (currentPlant != null) {
                PlantRepository.getInstance(requireContext())
                        .removePlant(currentPlant);
            }
            Navigation.findNavController(v).navigateUp();
        });

        return v;
    }
}
