package com.example.zerowastehero.Auth.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zerowastehero.DataBinding.Model.UserModel;
import com.example.zerowastehero.Main.MainActivity;
import com.example.zerowastehero.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    TextInputEditText ETEmail, ETPassword, ETConfirmPassword, ETUsername;
    Button BtnRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar PBRegister;
    TextView TVLoginAccount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize view
        ETEmail = view.findViewById(R.id.ETEmail);
        ETPassword = view.findViewById(R.id.ETPassword);
        ETConfirmPassword = view.findViewById(R.id.ETConfirmPassword);
        ETUsername = view.findViewById(R.id.ETUsername);
        BtnRegister = view.findViewById(R.id.BtnRegister);
        PBRegister = view.findViewById(R.id.PBRegister);
        TVLoginAccount = view.findViewById(R.id.TVLoginAccount);

        TVLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.DestLogin);
            }
        });

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, confirmPassword, username;
                email = ETEmail.getText().toString().trim();
                password = ETPassword.getText().toString().trim();
                confirmPassword = ETConfirmPassword.getText().toString().trim();
                username = ETUsername.getText().toString().trim();

                boolean hasError = false;

                if (TextUtils.isEmpty(username)) {
                    ETUsername.setError("Username is required");
                    hasError = true;
                }
                if (TextUtils.isEmpty(email)) {
                    ETEmail.setError("Email is required");
                    hasError = true;
                }
                if (TextUtils.isEmpty(password)) {
                    ETPassword.setError("Password is required");
                    hasError = true;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    ETConfirmPassword.setError("Confirm Password is required");
                    hasError = true;
                }
                if (!TextUtils.isEmpty(password) && !password.equals(confirmPassword)) {
                    ETConfirmPassword.setError("Passwords do not match");
                    hasError = true;
                }
                if (password.length() < 6) {
                    ETPassword.setError("Password must be at least 6 characters");
                    hasError = true;
                }

                // If any error was found, stop execution
                if (hasError) return;
                PBRegister.setVisibility(View.VISIBLE);
                BtnRegister.setVisibility(View.GONE);

                db.collection("users")
                        .whereEqualTo("username", username)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (!task.getResult().isEmpty()) {
                                    PBRegister.setVisibility(View.GONE);
                                    BtnRegister.setVisibility(View.VISIBLE);
                                    // Username exists
                                    Log.d("CheckUsername", "Username is already taken");
                                    ETUsername.setError("Username is already taken");
                                    Toast.makeText(getContext(), "Username is already taken", Toast.LENGTH_SHORT).show();
                                } else {
                                    String joinDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    PBRegister.setVisibility(View.GONE);
                                                    if (task.isSuccessful()) {
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        UserModel userModel = new UserModel(username, "", email, joinDate);
                                                        db.collection("users").document(user.getUid()).set(userModel)
                                                                .addOnSuccessListener(aVoid -> {
                                                                    Toast.makeText(requireContext(), "Register Successful.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                                    startActivity(intent);
                                                                    getActivity().finish();
                                                                    // Navigate to another activity (e.g., MainActivity)
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Toast.makeText(getContext(), "Failed to save user info", Toast.LENGTH_SHORT).show();
                                                                });
                                                    } else {
                                                        Toast.makeText(getContext(), "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                PBRegister.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Failed to check username availability", Toast.LENGTH_SHORT).show();
                                Log.e("CheckUsername", "Error checking username", task.getException());
                            }
                        });

            }
        });
        return view;
    }
}