package com.example.plantely;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import java.util.HashSet;
import java.util.Set;

/**
 * Anzeige für die Liste der Pflanzen in der Hauptanzeige
 */


public class PlantListFragment extends Fragment {
    private LinearLayout dynamicContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_list, parent, false);

        // Statische Klick-Handler
        view.findViewById(R.id.plant1).setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("plantName", "Monstera Deliciosa");
            Navigation.findNavController(v)
                    .navigate(R.id.action_list_to_detail, b);
        });
        view.findViewById(R.id.plant2).setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("plantName", "Ficus Lyrata");
            Navigation.findNavController(v)
                    .navigate(R.id.action_list_to_detail, b);
        });
        view.findViewById(R.id.plant3).setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("plantName", "Orchidee");
            Navigation.findNavController(v)
                    .navigate(R.id.action_list_to_detail, b);
        });

        dynamicContainer = view.findViewById(R.id.container_dynamic_plants);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDynamicPlants();
    }

    private void refreshDynamicPlants() {
        // alten Inhalt entfernen, um Duplikate zu vermeiden
        dynamicContainer.removeAllViews();

        for (Plant plant : PlantRepository.getInstance().getPlants()) {
            // Namens-Check: nur solche, die nicht statisch sind
            String name = plant.getName();
            if (name.equals("Monstera Deliciosa") ||
                    name.equals("Ficus Lyrata") ||
                    name.equals("Orchidee")) {
                continue;
            }

            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setText(name);
            tv.setTextSize(18f);
            tv.setPadding(0,16,0,16);
            tv.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.arrow, 0);
            tv.setCompoundDrawablePadding(8);
            tv.setClickable(true);

            tv.setOnClickListener(v -> {
                Bundle b = new Bundle();
                b.putString("plantName", name);
                Navigation.findNavController(v)
                        .navigate(R.id.action_list_to_detail, b);
            });

            dynamicContainer.addView(tv);
        }
    }
}
