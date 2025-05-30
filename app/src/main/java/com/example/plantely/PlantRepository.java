package com.example.plantely;

import java.util.ArrayList;
import java.util.List;

/**
 * Speicher für die Pflanzen
 */



public class PlantRepository {
    private static PlantRepository instance;
    private final List<Plant> plants = new ArrayList<>();

    private PlantRepository() {}

    public static synchronized PlantRepository getInstance() {
        if (instance == null) instance = new PlantRepository();
        return instance;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public Plant getPlantByName(String name) {
        for (Plant p : plants) {
            if (p.getName().equals(name)) return p;
        }
        return null;
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void removePlant(Plant plant) {
        plants.remove(plant);
    }
}
