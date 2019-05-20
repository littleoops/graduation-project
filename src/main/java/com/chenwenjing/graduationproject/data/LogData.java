package com.chenwenjing.graduationproject.data;

public class LogData {

    private int stemId;

    private int species;

    private int dbh;

    private int type;

    private int logId;

    private int assort;

    private int diam;

    private int length;

    private String workTime;

    public int getStemId() {
        return stemId;
    }

    public void setStemId(int stemId) {
        this.stemId = stemId;
    }

    public int getSpecies() {
        return species;
    }

    public void setSpecies(int species) {
        this.species = species;
    }

    public int getDbh() {
        return dbh;
    }

    public void setDbh(int dbh) {
        this.dbh = dbh;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getAssort() {
        return assort;
    }

    public void setAssort(int assort) {
        this.assort = assort;
    }

    public int getDiam() {
        return diam;
    }

    public void setDiam(int diam) {
        this.diam = diam;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    @Override
    public String toString() {
        return "LogData{" + "stemId=" + stemId + ", species=" + species + ", dbh=" + dbh + ", type=" + type +
                ", logId=" + logId + ", assort=" + assort + ", diam=" + diam + ", length=" + length + ", workTime='" +
                workTime + '\'' + '}';
    }
}
