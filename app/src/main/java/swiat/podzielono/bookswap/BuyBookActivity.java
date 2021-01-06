package swiat.podzielono.bookswap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import swiat.podzielono.bookswap.data.BookObject;
import swiat.podzielono.bookswap.ui.ChangeProfileDataActivity;

public class BuyBookActivity extends AppCompatActivity {

    private final String CHOSEN_BOOK_KEY = "hash";
    private String bookHash;
    private DatabaseReference mDatabaseReference;

    private TextView mTitle;
    private TextView mAuthor;
    private TextView mPublisher;
    private TextView mOwner;
    private TextView mCondition;
    private ImageView mMainPic;
    private TextView mPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookHash = getIntent().getStringExtra(CHOSEN_BOOK_KEY);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("books");

        mTitle = findViewById(R.id.book_title_text);
        mAuthor = findViewById(R.id.title_text);
        mMainPic = findViewById(R.id.imageView2);
        mOwner = findViewById(R.id.textView4);
        mPublisher = findViewById(R.id.publisher_text);
        mCondition = findViewById(R.id.condition_text);
        mPrice = findViewById(R.id.radioButton);
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
                mTitle.setText(chosenBook.getTitle());
                mAuthor.setText(chosenBook.getAuthor());
                mOwner.setText("Do you want to get this book from " + chosenBook.getOwner() + "?");
                mPublisher.setText(chosenBook.getPublisher());
                mCondition.setText(chosenBook.getCondition());
                mPrice.setText("Buy the book for $"+chosenBook.getPrice());

                String photoUrl1 = chosenBook.getPhoto1();
                if (photoUrl1 != null)
                    Picasso.get().load(photoUrl1).into(mMainPic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void openPersonalData(View view) {
        Intent intent = new Intent(this, PersonalInfoActivity.class);
        startActivity(intent);
        finish();
    }
}