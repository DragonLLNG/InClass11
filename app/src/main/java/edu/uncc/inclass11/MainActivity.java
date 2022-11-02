package edu.uncc.inclass11;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener, GradesFragment.GradesListener, AddCourseFragment.CreateCourseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .commit();
    }

    @Override
    public void login() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addCourse() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AddCourseFragment())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void gotoGrade() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new GradesFragment(), "gradeFragment")
                .commit();
    }

    @Override
    public void goBackToGrade() {

        GradesFragment gradesFragment = (GradesFragment) getSupportFragmentManager().findFragmentByTag("gradeFragment");
        if(gradesFragment != null) {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    public void updateGrades(Course course) {
        GradesFragment gradesFragment = (GradesFragment) getSupportFragmentManager().findFragmentByTag("gradeFragment");
        if(gradesFragment != null) {
            gradesFragment.addCourse(course);
            getSupportFragmentManager().popBackStack();
        }
    }
}