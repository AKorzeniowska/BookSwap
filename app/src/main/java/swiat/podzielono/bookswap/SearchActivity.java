package swiat.podzielono.bookswap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity extends AppCompatActivity {

    private Spinner mMainCategory;
    private Spinner mSecondaryCategory;
    private EditText mAuthor;
    private EditText mTitle;
    private EditText mCity;

    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mMainCategory = findViewById(R.id.book_main_category_list);
        mSecondaryCategory = findViewById(R.id.book_secondary_category_list);
        mAuthor = findViewById(R.id.book_author_text);
        mTitle = findViewById(R.id.book_title_text);
        mCity = findViewById(R.id.town_text);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void startSearch(View view){

    }
}
