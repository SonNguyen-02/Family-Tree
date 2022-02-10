package com.example.treeview.model;

public class People {

    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int OTHER = 2;

    private String name;
    private String description;
    private int image;
    private int gender;
    private final boolean isOffspring;
    private final String fatherName;
    private final String motherName;

    public People(String name, String description, int image, int gender) {
        this(name, description, image, gender, true);
    }

    public People(String name, String description, int image, int gender, boolean isOffspring) {
        this(name, description, image, gender, isOffspring, null, null);
    }

    public People(String name, String description, int image, int gender, boolean isOffspring, String fatherName, String motherName) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.gender = gender;
        this.isOffspring = isOffspring;
        this.fatherName = fatherName;
        this.motherName = motherName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isOffspring() {
        return isOffspring;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherName() {
        return motherName;
    }
}
