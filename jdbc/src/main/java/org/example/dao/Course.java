package org.example.dao;

public class Course {
    private String id;
    private String name;
    private int period;
    private double credit;
    private String teacher;
    public Course(String id, String name, int period, double credit, String teacher) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.credit = credit;
        this.teacher = teacher;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPeriod() {
        return period;
    }

    public double getCredit() {
        return credit;
    }

    public String getTeacher() {
        return teacher;
    }

    @Override
    public String toString(){
        return "The course name is "+this.getName();
    }
}
