package com.example.calendar.entity;


public class MyDate {

    private String day;//对应号数
    private String todo;//待办事项

    public MyDate(String day, String todo) {
        this.day = day;
        this.todo = todo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    @Override
    public String toString() {
        return "MyDate{" +
                "day='" + day + '\'' +
                ", todo='" + todo + '\'' +
                '}';
    }
}
