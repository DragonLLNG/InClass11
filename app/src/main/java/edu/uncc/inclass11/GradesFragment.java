package edu.uncc.inclass11;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.inclass11.databinding.FragmentGradesBinding;
import edu.uncc.inclass11.databinding.GradeRowItemBinding;

public class GradesFragment extends Fragment {
    private String TAG = "demo";
    private double gpa;
    private double index;
    private double hours;

    private Menu menu;

    public GradesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addButton:
                mListener.addCourse();
        }

        return super.onOptionsItemSelected(item);
    }


    FragmentGradesBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGradesBinding.inflate(inflater, container, false);


        return binding.getRoot();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        binding.recycleViewCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        coursesAdapter = new CoursesAdapter();
        binding.recycleViewCourses.setAdapter(coursesAdapter);

        getActivity().setTitle("Grades");
        getCourse();



        binding.textViewGPA.setText("GPA: "+gpa);
        binding.textViewHours.setText("Hours "+hours);


    }

    void getCourse(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("courses").whereEqualTo("user_name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mCourses.clear();
                        for(QueryDocumentSnapshot forumDoc : value) {
                            Course course = forumDoc.toObject(Course.class);
                            mCourses.add(course);
                            Log.d(TAG, "onEvent: Course Name" + course.course_name + course.credit_hour);
                        }

                        coursesAdapter.notifyDataSetChanged();
                    }
                });
        updateGPA();
    }

    void updateGPA(){

            Log.d(TAG, "num courses: "+ mCourses.size());
            hours = 0;
            gpa = 0;
            index = 0;
        for (int i=0; i<mCourses.size()-1; i++){
            System.out.println(mCourses.get(i).credit_hour);
            hours +=  mCourses.get(i).credit_hour;

            Log.d(TAG, "credit hour " + mCourses.get(i).credit_hour );

            Log.d(TAG, "updateGPA: "+ hours);
            switch (mCourses.get(i).letter_grade) {
                case "A":
                    index += 4 * mCourses.get(i).credit_hour;
                    break;
                case "B":
                    index += 3 * mCourses.get(i).credit_hour;
                    break;
                case "C":
                    index += 2 * mCourses.get(i).credit_hour;
                    break;
                case "D":
                    index += 1 * mCourses.get(i).credit_hour;
                    break;
                case "F":
                    index += 0 * mCourses.get(i).credit_hour;
            }

        }
        gpa = index/hours;



        if (hours==0){
            gpa = 4.0;
        }

    }


    ArrayList<Course> mCourses = new ArrayList<>();
    CoursesAdapter coursesAdapter;

    public void deleteCourse(Course course) {

        Log.d(TAG, "deleteCourse: check id" + course.course_id);
        Log.d(TAG, "deleteCourse: " + course.course_name);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("courses")
                .document(course.course_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Course successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Error deleting course" + e);
                    }
                });
        mCourses.remove(course);
        coursesAdapter.notifyDataSetChanged();
    }


    public void addCourse(Course course) {
        mCourses.add(course);
        coursesAdapter.notifyDataSetChanged();
    }




    class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder>{

        @NonNull
        @Override
        public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GradeRowItemBinding binding = GradeRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new CoursesViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
            Course course = mCourses.get(position);
            holder.mCourse = course;
            holder.setupUI(course);

        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }

        class CoursesViewHolder extends RecyclerView.ViewHolder {
            GradeRowItemBinding mBinding;
            Course mCourse;
            public CoursesViewHolder(GradeRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Course course){


                mCourse = course;
                mBinding.textViewCourseNumber.setText(course.getCourse_number());
                mBinding.textViewCourseName.setText(course.getCourse_name());
                mBinding.textViewCourseHours.setText(course.getCredit_hour()+"");
                mBinding.textViewCourseLetterGrade.setText(course.getLetter_grade());

                Log.d(TAG, "setupUI2: "+ course.course_number);

                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCourse(course);
                    }
                });
            }
        }
    }



    GradesListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (GradesListener) context;
    }

    interface GradesListener{
        void login();
        void addCourse();
    }

}
