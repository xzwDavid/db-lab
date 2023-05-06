package org.example.dao;

public class Student {
    private Long id;
    private String name;
    private String sex;
    private String bdate;
    private double height;
    private String dorm;
    public Student(Long id, String name, String sex, String bdate, double height, String dorm) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.bdate = bdate;
        this.height = height;
        this.dorm = dorm;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getBdate() {
        return bdate;
    }

    public double getHeight() {
        return height;
    }

    public String getDorm() {
        return dorm;
    }

}
