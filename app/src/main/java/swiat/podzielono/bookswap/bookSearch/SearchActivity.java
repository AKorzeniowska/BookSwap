package swiat.podzielono.bookswap.bookSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import swiat.podzielono.bookswap.R;

public class SearchActivity extends AppCompatActivity {

    public static final String MAIN_CATEGORY = "main";
    public static final String SECONDARY_CATEGORY = "secondary";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String CITY = "city";

    private Spinner mMainCategory;
    private Spinner mSecondaryCategory;
    private EditText mAuthor;
    private EditText mTitle;
    private EditText mCity;

    private ArrayList<String> extraData = new ArrayList<>();

    private DatabaseReference mDatabaseCategoryReference;

    private ArrayList<String> dataForMainSpinner = new ArrayList<>();
    private ArrayList<String> dataForSecondarySpinner = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMainCategory = findViewById(R.id.book_main_category_list);
        mSecondaryCategory = findViewById(R.id.book_secondary_category_list);
        mAuthor = findViewById(R.id.book_author_text);
        mTitle = findViewById(R.id.book_title_text);
        mCity = findViewById(R.id.town_text);

        mDatabaseCategoryReference = FirebaseDatabase.getInstance().getReference().child("categories");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainSpinnerDataSetter();
        secondarySpinnerDataSetter();
    }



    private void mainSpinnerDataSetter() {
        mDatabaseCategoryReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> categories = dataSnapshot.getChildren();
                for (DataSnapshot category : categories) {
                    String main = category.getKey();
                    dataForMainSpinner.add(main);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        SearchActivity.this,
                        android.R.layout.simple_spinner_item,
                        dataForMainSpinner
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mMainCategory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void secondarySpinnerDataSetter(){
        mMainCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String x = mMainCategory.getSelectedItem().toString();
                mDatabaseCategoryReference.child(x).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataForSecondarySpinner.clear();
                        Iterable<DataSnapshot> secondaries = dataSnapshot.getChildren();
                        for (DataSnapshot secondary : secondaries){
                            String sec = secondary.getKey();
                            dataForSecondarySpinner.add(sec);

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    SearchActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    dataForSecondarySpinner
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mSecondaryCategory.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void startSearch(View view){
        Intent intent = new Intent(this, SearchEffectsActivity.class);
        intent.putExtra(MAIN_CATEGORY, mMainCategory.getSelectedItem().toString());
        intent.putExtra(SECONDARY_CATEGORY, mSecondaryCategory.getSelectedItem().toString());
        intent.putExtra(TITLE, mTitle.getText().toString().trim());
        intent.putExtra(AUTHOR, mAuthor.getText().toString().trim());
        intent.putExtra(CITY, mCity.getText().toString().trim());
        startActivity(intent);
    }
}
