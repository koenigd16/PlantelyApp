package com.example.plantely;

import android.net.Uri;

public class Plant {
    private final String name;
    private final String description;
    private final Uri photoUri;
    private int wateringIntervalDays = 7;  // Standard: 7 Tage

    private boolean reminderActive;
    public boolean isReminderActive() { return reminderActive; }
    public void setReminderActive(boolean active) { reminderActive = active; }


    public Plant(String name, String description, Uri photoUri) {
        this.name = name;
        this.description = description;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public int getWateringIntervalDays() {
        return wateringIntervalDays;
    }

    public void setWateringIntervalDays(int days) {
        this.wateringIntervalDays = days;
    }
}
