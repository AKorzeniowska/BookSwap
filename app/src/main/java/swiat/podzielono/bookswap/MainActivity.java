package swiat.podzielono.bookswap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import swiat.podzielono.bookswap.ui.LoginActivity;
import swiat.podzielono.bookswap.ui.RegisterActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GOOGLE_SIGN_IN = 1;

    private Button mLoginButton;
    private Button mRegisterButton;
    private SignInButton mGoogleButton;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = findViewById(R.id.user_sign_in);
        mRegisterButton = findViewById(R.id.user_sign_up);
        mGoogleButton = findViewById(R.id.google_sing_in_register);
        mGoogleButton.setSize(SignInButton.SIZE_STANDARD);
        mGoogleButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("bookswap-9525c.firebaseapp.com")
                        .requestEmail()
                        .build();

        mGoogleClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startBrowseActivity();
        }
    }

    public void signIn(View view) {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    public void signUp(View view) {
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivity(registerActivity);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.google_sing_in_register) {
            googleSignIn();
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(R.id.main_layout), "Successfully signed in", Snackbar.LENGTH_LONG).show();
                            startBrowseActivity();
                        } else {
                            Snackbar.make(findViewById(R.id.main_layout), "Successfully signed in", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void startBrowseActivity() {
        Intent browseActivity = new Intent(this, BrowseActivity.class);
        startActivity(browseActivity);
        finish();
    }
}