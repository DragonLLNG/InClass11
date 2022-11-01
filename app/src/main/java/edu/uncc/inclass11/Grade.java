package edu.uncc.inclass11;

public class Grade {
    double grade, credits;


    public Grade(){
    }

    public Grade(double grade, double credits){
        this.grade = grade;
        this.credits = credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public double getGrade() {
        return grade;
    }

    public double getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "grade=" + grade +
                ", credits=" + credits +
                '}';
    }
}
