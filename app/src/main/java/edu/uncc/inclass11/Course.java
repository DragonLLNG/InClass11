package edu.uncc.inclass11;

public class Course {
    public String course_number, course_name, letter_grade;
    public double credit_hour;
    public Course(){

    }
    public Course(String course_number, String course_name, double credit_hour, String letter_grade){
        this.course_number = course_number;
        this.course_name = course_name;
        this.credit_hour = credit_hour;
        this.letter_grade = letter_grade;
    }

    public void setCourse_number(String course_number) {
        this.course_number = course_number;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCredit_hour(double credit_hour) {
        this.credit_hour = credit_hour;
    }

    public void setLetter_grade(String letter_grade) {
        this.letter_grade = letter_grade;
    }

    public String getCourse_number() {
        return course_number;
    }
    public String getCourse_name() {
        return course_name;
    }
    public double getCredit_hour() {
        return credit_hour;
    }
    public String getLetter_grade() {
        return letter_grade;
    }

    @Override
    public String toString() {
        return "Courses{" +
                "course_number='" + course_number + '\'' +
                ", course_name='" + course_name + '\'' +
                ", letter_grade='" + letter_grade + '\'' +
                ", credit_hour=" + credit_hour +
                '}';
    }
}
