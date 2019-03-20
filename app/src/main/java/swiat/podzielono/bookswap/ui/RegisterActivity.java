package swiat.podzielono.bookswap.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import swiat.podzielono.bookswap.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUserEmail;
    private EditText mPasswordField;
    private EditText mPasswordFieldCheck;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserEmail = findViewById(R.id.user_email_field_register);
        mPasswordField = findViewById(R.id.user_password_field_register);
        mPasswordFieldCheck = findViewById(R.id.user_password_check_register);
        mProgressBar = findViewById(R.id.progress_bar_register);

        mAuth = FirebaseAuth.getInstance();
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
                        Toast.makeText(getApplicationContext(), "Successfully created account", Toast.LENGTH_LONG).show();
                        //FirebaseUser user = mAuth.getCurrentUser();
                        finish();
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