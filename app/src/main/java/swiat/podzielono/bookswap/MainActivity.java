package swiat.podzielono.bookswap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import swiat.podzielono.bookswap.ui.LoginActivity;
import swiat.podzielono.bookswap.ui.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private Button mLoginButton;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = findViewById(R.id.user_sign_in);
        mRegisterButton = findViewById(R.id.user_sign_up);
    }

    public void signIn(View view) {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }

    public void signUp(View view) {
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivity(registerActivity);
    }
}