package com.example.profiler.projecta;

public class DataModel {
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }


    public DataModel(String first,String sec,String uid) {
        this.first = first;
        this.sec=sec;
        this.uid=uid;
    }

    public DataModel(){}

    public String first;

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public  String sec;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public  String  uid;

}
