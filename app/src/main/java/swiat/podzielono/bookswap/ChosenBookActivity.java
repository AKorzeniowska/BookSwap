package swiat.podzielono.bookswap;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import swiat.podzielono.bookswap.data.BookObject;

public class ChosenBookActivity extends AppCompatActivity {

    private final String CHOSEN_BOOK_KEY = "hash";
    private String bookHash;
    private DatabaseReference mDatabaseReference;

    private TextView mTitle;
    private TextView mAuthor;
    private TextView mCategories;
    private TextView mEdition;
    private TextView mYear;
    private TextView mPublisher;
    private TextView mOwner;
    private TextView mCondition;
    private TextView mDescription;
    private ImageView mMainPic;
    private TextView mAddDate;

    private TextView mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_book);

        bookHash = getIntent().getStringExtra(CHOSEN_BOOK_KEY);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("books");

        mTitle = findViewById(R.id.book_title_text);
        mAuthor = findViewById(R.id.title_text);
        mCategories = findViewById(R.id.category_text);
        mMainPic = findViewById(R.id.imageView2);
        mDescription = findViewById(R.id.description_of_book_text);
        mYear = findViewById(R.id.year_text);
        mOwner = findViewById(R.id.owner_text);
        mPublisher = findViewById(R.id.publisher_text);
        mCondition = findViewById(R.id.condition_text);
        mAddDate = findViewById(R.id.add_year_text);
        mEdition = findViewById(R.id.edition_text);

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
                mDescription.setText(chosenBook.getDescription());
                mYear.setText(chosenBook.getYear());
                mOwner.setText(chosenBook.getOwner());
                mPublisher.setText(chosenBook.getPublisher());
                mCondition.setText(chosenBook.getCondition());
                mAddDate.setText(chosenBook.getAdd_date());
                mEdition.setText(chosenBook.getEdition());

                String categories = chosenBook.getMain_category() + ", \n" + chosenBook.getSecondary_category() + ", \n" + chosenBook.getCustom_category();
                mCategories.setText(categories);

                String photoUrl = chosenBook.getPhoto1();
                if (!photoUrl.equals(""))
                    Picasso.get().load(photoUrl).into(mMainPic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
