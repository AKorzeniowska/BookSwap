package swiat.podzielono.bookswap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import swiat.podzielono.bookswap.BrowseActivity;
import swiat.podzielono.bookswap.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserEmail;
    private EditText mPasswordField;
    private EditText mPasswordFieldCheck;
    private EditText mNicknameField;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserEmail = findViewById(R.id.user_email_field_register);
        mPasswordField = findViewById(R.id.user_password_field_register);
        mPasswordFieldCheck = findViewById(R.id.user_password_check_register);
        mNicknameField = findViewById(R.id.username_edit_text);
        mProgressBar = findViewById(R.id.progress_bar_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("owners");
    }

    public void signUp(View view) {
        String email = mUserEmail.getText().toString();
        String password = mPasswordField.getText().toString();
        String passwordCheck = mPasswordFieldCheck.getText().toString();

        if(email.contains("@") && password.equals(passwordCheck) && password.length() > 6) {

            mProgressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressBar.setVisibility(View.INVISIBLE);

                    if(task.isSuccessful()) {
                        final String nick = mNicknameField.getText().toString().trim();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nick)
//                                .setPhotoUri(URI)
                                .build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mDatabaseReference.child(nick).setValue(0);

                                Toast.makeText(getApplicationContext(), "Successfully created account", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, BrowseActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } else {
                        Toast.makeText(RegisterActivity.this, "Unsuccessfully created account", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Toast.makeText(RegisterActivity.this, "Invalid passwords or email", Toast.LENGTH_LONG).show();
        }
    }
}