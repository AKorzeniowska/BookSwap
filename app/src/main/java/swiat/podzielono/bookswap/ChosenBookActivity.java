package swiat.podzielono.bookswap;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import swiat.podzielono.bookswap.data.BookObject;

public class ChosenBookActivity extends AppCompatActivity {

    private final String CHOSEN_BOOK_KEY = "hash";
    private String bookHash;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_book);

        bookHash = getIntent().getStringExtra(CHOSEN_BOOK_KEY);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("books");
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataGetter();
    }

    private void dataGetter(){
        mDatabaseReference.child(bookHash).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BookObject chosenBook = dataSnapshot.getValue(BookObject.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
