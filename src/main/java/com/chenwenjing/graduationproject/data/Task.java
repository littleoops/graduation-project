package com.chenwenjing.graduationproject.data;

public class Task {

    private int id;

    private String species;

    private int stemCount;

    private int logCount;

    private String workTime;

    private String logAssort;

    private int minDBH;

    private int maxDBH;

    private int minLength;

    private int maxLength;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getLogAssort() {
        return logAssort;
    }

    public void setLogAssort(String logAssort) {
        this.logAssort = logAssort;
    }

    public int getStemCount() {
        return stemCount;
    }

    public void setStemCount(int stemCount) {
        this.stemCount = stemCount;
    }

    public int getLogCount() {
        return logCount;
    }

    public void setLogCount(int logCount) {
        this.logCount = logCount;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public int getMinDBH() {
        return minDBH;
    }

    public void setMinDBH(int minDBH) {
        this.minDBH = minDBH;
    }

    public int getMaxDBH() {
        return maxDBH;
    }

    public void setMaxDBH(int maxDBH) {
        this.maxDBH = maxDBH;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
