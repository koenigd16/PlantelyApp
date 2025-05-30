package com.example.plantely;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class PlantListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_list, container, false);
        view.findViewById(R.id.plant1).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_list_to_detail)
        );
        view.findViewById(R.id.plant2).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_list_to_detail)
        );
        view.findViewById(R.id.plant3).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_list_to_detail)
        );
        return view;
    }
}