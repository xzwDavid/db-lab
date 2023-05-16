package org.example.dao;

public class SC {
    private Long Sid;
    private String Cid;
    private int grade;

    public SC(Long sid, String cid, int grade){
        this.setSid(sid);
        this.setCid(cid);
        this.setGrade(grade);
    }
    public void setSid(Long sid) {
        Sid = sid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Long getSid() {
        return Sid;
    }

    public String getCid() {
        return Cid;
    }

    public int getGrade() {
        return grade;
    }
}
