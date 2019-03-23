package swiat.podzielono.bookswap.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import swiat.podzielono.bookswap.R;

public class ConversationActivity extends AppCompatActivity {

    private TextView mTestTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mTestTextView = findViewById(R.id.test_username);
        String username = getIntent().getStringExtra(SearchAdapter.EXTRA_USERNAME);
        mTestTextView.setText(username);
    }
}
