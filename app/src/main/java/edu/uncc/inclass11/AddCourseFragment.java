package edu.uncc.inclass11;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import edu.uncc.inclass11.databinding.FragmentAddCourseBinding;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddCourseFragment extends Fragment {

    private final String TAG = "demo";

    Course course;
    public AddCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddCourseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCourseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Add Course");

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String courseNumber = binding.editTextCourseNumber.getText().toString();
                String courseName = binding.editTextCourseName.getText().toString();
                double courseHours = Double.parseDouble(binding.editTextCourseHours.getText().toString());
                int selectedId = binding.radioGroupGrades.getCheckedRadioButtonId();

                if(courseName.isEmpty() || courseNumber.isEmpty() || binding.editTextCourseHours.getText().toString().isEmpty()) {
                   Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else if(selectedId == -1){
                    Toast.makeText(getContext(), "Please select a letter grade !!", Toast.LENGTH_SHORT).show();
                } else {
                    String courseLetterGrade;
                    if(selectedId == R.id.radioButtonA) {
                        courseLetterGrade = "A";
                    } else if(selectedId == R.id.radioButtonB) {
                        courseLetterGrade = "B";
                    } else if(selectedId == R.id.radioButtonC) {
                        courseLetterGrade = "C";
                    } else if(selectedId == R.id.radioButtonD) {
                        courseLetterGrade = "D";
                    } else {
                        courseLetterGrade = "F";
                    }

                    course = new Course();
                    course.setCourse_number(courseNumber);
                    course.setCourse_name(courseName);
                    course.setCredit_hour(courseHours);
                    course.setLetter_grade(courseLetterGrade);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user != null) {
                        course.setUser_name(user.getDisplayName());
                    } else {
                        Log.d(TAG, "onClick: Error: No User Logged In");
                    }



                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("courses")
                            .add(course)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    course.setCourse_id(documentReference.getId());

                                    Log.d(TAG, "onClick: id" + documentReference.getId() );

                                    HashMap<String, Object> courseId = new HashMap<>();
                                    courseId.put("course_id", course.course_id);

                                    db.collection("posts")
                                            .document(course.course_id)
                                            .update(courseId);

                                    mListener.updateGrades(course);
                                }
                            });


                }
            }
        });

    }

    CreateCourseListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateCourseListener) context;
    }

    interface CreateCourseListener {
        void goBackToGrade();
        void updateGrades(Course course);
    }
}