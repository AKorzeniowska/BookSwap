package swiat.podzielono.bookswap.bookSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import swiat.podzielono.bookswap.ChosenBookActivity;
import swiat.podzielono.bookswap.R;
import swiat.podzielono.bookswap.data.BookAdapter;
import swiat.podzielono.bookswap.data.BookObject;

public class SearchEffectsActivity extends AppCompatActivity {

    private final String CHOSEN_BOOK_KEY = "hash";

    private DatabaseReference mDatabaseReference;
    private String mainCategory, secondaryCategory, title, author, city;

    private ArrayList<BookObject> bookList = new ArrayList<>();
    private ArrayList<String> bookHashCodes = new ArrayList<>();

    private BookAdapter bookAdapter;
    private ListView listView;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("books");

        mainCategory = getIntent().getStringExtra(SearchActivity.MAIN_CATEGORY);
        secondaryCategory = getIntent().getStringExtra(SearchActivity.SECONDARY_CATEGORY);
        title = getIntent().getStringExtra(SearchActivity.TITLE);
        author = getIntent().getStringExtra(SearchActivity.AUTHOR);
        city = getIntent().getStringExtra(SearchActivity.CITY);

        listView = findViewById(R.id.book_list);
        header = findViewById(R.id.your_books_text);
        header.setText("Found books");
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryExecutor();

    }

    private void queryExecutor(){
        Query query = mDatabaseReference.orderByChild("main_category").equalTo(mainCategory);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> afterMainCategory = dataSnapshot.getChildren();
                for (DataSnapshot book : afterMainCategory){
                    BookObject currentBook = book.getValue(BookObject.class);
                    if (currentBook.getSecondary_category().equals(secondaryCategory) &&
                            currentBook.getAuthor().toLowerCase().matches(author.toLowerCase()) &&
                            currentBook.getTitle().toLowerCase().matches(title.toLowerCase())){
                        bookList.add(currentBook);
                        bookHashCodes.add(book.getKey());
                    }
                }

                bookAdapter = new BookAdapter(SearchEffectsActivity.this, bookList);
                listView.setAdapter(bookAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SearchEffectsActivity.this, ChosenBookActivity.class);
                        intent.putExtra(CHOSEN_BOOK_KEY, bookHashCodes.get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
