package com.example.plantely;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

/**
 * Anzeige für die Detail-View der jeweiligen Pflanzen
 */

public class PlantDetailFragment extends Fragment {
    private Plant currentPlant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_detail, parent, false);

        TextView nameView        = view.findViewById(R.id.plantName);
        EditText intervalEdit    = view.findViewById(R.id.editWateringInterval);
        Button saveIntervalBtn   = view.findViewById(R.id.buttonSaveInterval);
        ImageView imageView      = view.findViewById(R.id.plantImage);
        TextView descView        = view.findViewById(R.id.plantDescription);
        TextView nextWatering    = view.findViewById(R.id.nextWatering);
        Switch reminderSwitch    = view.findViewById(R.id.switchReminder);
        Button deleteBtn         = view.findViewById(R.id.buttonDelete);

        // Argumente auslesen
        Bundle args = getArguments();
        if (args != null && args.containsKey("plantName")) {
            String plantName = args.getString("plantName");
            nameView.setText(plantName);
            currentPlant = PlantRepository.getInstance().getPlantByName(plantName);
        }

        if (currentPlant != null) {
            // Intervall und Bild
            intervalEdit.setText(String.valueOf(currentPlant.getWateringIntervalDays()));
            if (currentPlant.getPhotoUri() != null) {
                imageView.setImageURI(currentPlant.getPhotoUri());
            }
            // Beschreibung
            String desc = currentPlant.getDescription();
            descView.setText(!TextUtils.isEmpty(desc) ? desc : "Keine Beschreibung vorhanden.");
            // nächsten Gieß-Text
            nextWatering.setText("Gießen in " +
                    currentPlant.getWateringIntervalDays() + " Tagen");
            // Switch initial
            reminderSwitch.setChecked(currentPlant.isReminderActive());
        }

        saveIntervalBtn.setOnClickListener(v -> {
            String s = intervalEdit.getText().toString();
            if (!TextUtils.isEmpty(s) && currentPlant != null) {
                int days = Integer.parseInt(s);
                currentPlant.setWateringIntervalDays(days);
                nextWatering.setText("Gießen in " + days + " Tagen");
            }
        });

        // Sobald der Switch aktiviert wird, öffnet sich ein Bestätigungs-Popup
        reminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentPlant == null) return;

            currentPlant.setReminderActive(isChecked);
            int days = currentPlant.getWateringIntervalDays();
            if (isChecked) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Erinnerung aktiviert")
                        .setMessage("Du wirst jetzt alle " + days +
                                " Tage benachrichtigt,\n– " + currentPlant.getName() +
                                " zu gießen.")
                        .setPositiveButton("OK", null)
                        .show();
                // TODO: Hier WorkManager/AlarmManager zum Planen aufrufen
            } else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Erinnerung deaktiviert")
                        .setMessage("Die automatische Erinnerung für „" +
                                currentPlant.getName() + "“ wurde ausgeschaltet.")
                        .setPositiveButton("OK", null)
                        .show();
                // TODO: Erinnerungs-Task ggf. stornieren
            }
        });

        deleteBtn.setOnClickListener(v -> {
            if (currentPlant != null) {
                PlantRepository.getInstance().removePlant(currentPlant);
            }
            Navigation.findNavController(v).navigateUp();
        });

        return view;
    }
}
