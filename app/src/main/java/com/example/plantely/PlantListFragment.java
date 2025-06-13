// java/com/example/plantely/PlantListFragment.java
package com.example.plantely;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Zeigt alle Pflanzen in einer nach Suchbegriff und Kategorie gefilterten Liste und navigiert zur Detailansicht.
 */

public class PlantListFragment extends Fragment {
    private Spinner spinnerFilter;
    private EditText searchEdit;
    private RecyclerView recyclerView;
    private PlantListAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_plant_list, parent, false);

        spinnerFilter = root.findViewById(R.id.spinnerFilterCategory);
        searchEdit    = root.findViewById(R.id.searchEditText);
        recyclerView  = root.findViewById(R.id.recyclerViewPlants);

        // Dynamisch aus CategoryRepository laden
        List<String> cats = CategoryRepository
                .getInstance(requireContext())
                .getCategories();
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                cats
        );
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(catAdapter);

        adapter = new PlantListAdapter(plant -> {
            Bundle b = new Bundle();
            b.putString("plantName", plant.getName());
            Navigation.findNavController(root)
                    .navigate(R.id.action_list_to_detail, b);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        spinnerFilter.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                filterAndShow();
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            @Override public void onTextChanged(CharSequence s,int a,int b,int c){}
            @Override public void afterTextChanged(Editable s) {
                filterAndShow();
            }
        });

        return root;
    }

    @Override public void onResume() {
        super.onResume();
        filterAndShow();
    }

    private void filterAndShow() {
        String q   = searchEdit.getText().toString().toLowerCase().trim();
        String cat = spinnerFilter.getSelectedItem().toString();
        List<Plant> all = PlantRepository
                .getInstance(requireContext())
                .getPlants();
        List<Plant> filtered = all.stream()
                .filter(p -> p.getName().toLowerCase().contains(q))
                .filter(p -> cat.equals("Alle") || p.getCategory().equals(cat))
                .collect(Collectors.toList());
        adapter.submitList(filtered);
    }
}
