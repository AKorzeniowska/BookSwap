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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import swiat.podzielono.bookswap.BrowseActivity;
import swiat.podzielono.bookswap.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserEmail;
    private EditText mPasswordField;
    private EditText mPasswordFieldCheck;
    private EditText mUsernameField;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserEmail = findViewById(R.id.user_email_field_register);
        mPasswordField = findViewById(R.id.user_password_field_register);
        mPasswordFieldCheck = findViewById(R.id.user_password_check_register);
        mUsernameField = findViewById(R.id.username_edit_text);
        mProgressBar = findViewById(R.id.progress_bar_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("owners");
    }

    public void signUp(View view) {
        final String email = mUserEmail.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();
        String passwordCheck = mPasswordFieldCheck.getText().toString().trim();
        username = mUsernameField.getText().toString().trim();

        if(email.contains("@") && password.equals(passwordCheck) && password.length() > 6 ) {

            DatabaseReference ownersReference = FirebaseDatabase.getInstance().getReference().child("owners");
            ownersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(username)){
                        Toast.makeText(RegisterActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mProgressBar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    addUserToDatabase();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Unsuccessfully created account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(RegisterActivity.this, "Failed connection with database", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(RegisterActivity.this, "Invalid passwords or email", Toast.LENGTH_SHORT).show();
        }
    }


    private void addUserToDatabase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
//                .setPhotoUri(URI)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDatabaseReference.child(username).setValue(0);
                mProgressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), "Successfully created account", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, BrowseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}