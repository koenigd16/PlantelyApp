// java/com/example/plantely/Plant.java
package com.example.plantely;

import android.net.Uri;
import java.util.Date;
/**
 * Model-Klasse, die alle Eigenschaften einer Pflanze (Name, Kategorie, Foto, Intervall, Alter, Erstellungsdatum) kapselt.
 */
public class Plant {
    private final String name;
    private final String description;
    private final Uri photoUri;
    private String category;
    private int wateringIntervalDays = 7;
    private boolean reminderActive;
    private final long createdAt;      // Unix-Timestamp
    private int ageYears = 0;          // Alter in Jahren

    public Plant(String name, String description, Uri photoUri, String category) {
        this.name        = name;
        this.description = description;
        this.photoUri    = photoUri;
        this.category    = category;
        this.createdAt   = System.currentTimeMillis();
    }

    // Für JSON-Persistenz (hinter den Kulissen)
    public long getCreatedAt() { return createdAt; }
    public int  getAgeYears() { return ageYears; }
    public void setAgeYears(int age) { this.ageYears = age; }

    public String getName()                 { return name; }
    public String getDescription()          { return description; }
    public Uri    getPhotoUri()             { return photoUri; }
    public String getCategory()             { return category; }
    public void   setCategory(String cat)   { this.category = cat; }
    public int    getWateringIntervalDays() { return wateringIntervalDays; }
    public void   setWateringIntervalDays(int days) { this.wateringIntervalDays = days; }
    public boolean isReminderActive()       { return reminderActive; }
    public void   setReminderActive(boolean b)    { this.reminderActive = b; }
}
