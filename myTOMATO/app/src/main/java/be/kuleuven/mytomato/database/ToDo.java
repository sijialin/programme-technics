package be.kuleuven.mytomato.database;

import java.io.Serializable;

public class ToDo implements Serializable {
    private int imageID;
    private String name;
    String note;
    int color;
    String time;
    String date;
    boolean label1;
    boolean label2;
    boolean label3;
    boolean label4;
    int year,month,day,minute,hour;
    int isDone;
    int doneTimes,timesToDo;

    public ToDo() {
    }

    public ToDo(int imageID, String name, String note, int color, String time, String date,
                boolean label1, boolean label2, boolean label3, boolean label4,
                int year, int month, int day, int hour, int minute,
                int doneTimes,int timesToDo,int isDone) {
        this.imageID = imageID;
        this.name = name;
        this.note = note;
        this.color = color;
        this.time = time;
        this.date = date;
        this.label1 = label1;
        this.label2 = label2;
        this.label3 = label3;
        this.label4 = label4;
        this.year = year;
        this.month = month;
        this.day = day;
        this.minute = minute;
        this.hour = hour;
        this.doneTimes=doneTimes;
        this.timesToDo=timesToDo;
        this.isDone=isDone;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getNote() {
        return note;
    }

    public int getColor() {
        return color;
    }

    public boolean isLabel1() {
        return label1;
    }

    public boolean isLabel2() {
        return label2;
    }

    public boolean isLabel3() {
        return label3;
    }

    public boolean isLabel4() {
        return label4;
    }

    public int getImageID() {
        return imageID;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDate(){
        return date;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLabel1(boolean label1) {
        this.label1 = label1;
    }

    public void setLabel2(boolean label2) {
        this.label2 = label2;
    }

    public void setLabel3(boolean label3) {
        this.label3 = label3;
    }

    public void setLabel4(boolean label4) {
        this.label4 = label4;
    }

    public int isDone() {
        return isDone;
    }

    public void setDone(int done) {
        isDone = done;
    }

    public void setDoneTimes(int doneTimes) {
        this.doneTimes = doneTimes;
    }

    public void setTimesToDo(int timesToDo) {
        this.timesToDo = timesToDo;
    }

    public int getDoneTimes(){return doneTimes;}
    public int getTimesToDo(){return timesToDo;}
}