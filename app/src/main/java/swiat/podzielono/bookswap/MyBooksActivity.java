package swiat.podzielono.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import swiat.podzielono.bookswap.data.BookAdapter;
import swiat.podzielono.bookswap.data.BookObject;

public class MyBooksActivity extends AppCompatActivity {

    private final String CHOSEN_BOOK_KEY = "hash";

    private DatabaseReference mDatabaseUsersReference;
    private DatabaseReference mDatabaseBooksReference;
    private ArrayList<BookObject> booksList;
    private ArrayList<String> hashesList;

    private BookAdapter bookAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        mDatabaseUsersReference = FirebaseDatabase.getInstance().getReference().child("owners");
        mDatabaseBooksReference = FirebaseDatabase.getInstance().getReference().child("books");
        booksList = new ArrayList<>();
        hashesList = new ArrayList<>();

        listView = findViewById(R.id.book_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveData();
    }

    protected void retrieveData() {
        booksList.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String username = user.getDisplayName();

        mDatabaseUsersReference.child(username).child("my books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bookHashcodes = new ArrayList<>();

                Iterable<DataSnapshot> books = dataSnapshot.getChildren();
                for (DataSnapshot book : books) {
                    bookHashcodes.add(book.getKey());
                }

                for (String hash : bookHashcodes) {
                    mDatabaseBooksReference.child(hash).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BookObject currentBook = dataSnapshot.getValue(BookObject.class);
                            booksList.add(currentBook);
                            hashesList.add(dataSnapshot.getKey());

                            bookAdapter = new BookAdapter(MyBooksActivity.this, booksList);
                            listView.setAdapter(bookAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(MyBooksActivity.this, ChosenBookActivity.class);
                                    intent.putExtra(CHOSEN_BOOK_KEY, hashesList.get(position));
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyBooksActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
