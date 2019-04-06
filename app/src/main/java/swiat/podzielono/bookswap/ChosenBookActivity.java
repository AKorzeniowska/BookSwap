package swiat.podzielono.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import swiat.podzielono.bookswap.chat.ConversationActivity;
import swiat.podzielono.bookswap.chat.SearchAdapter;
import swiat.podzielono.bookswap.data.BookObject;

public class ChosenBookActivity extends AppCompatActivity {

    private final String CHOSEN_BOOK_KEY = "hash";
    private String bookHash;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserDatabaseReference;

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
    private ImageView mSecondPic;
    private ImageView mThirdPic;
    private TextView mAddDate;
    private TextView mImagesText;

    private TextView mPrice;

    private String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookHash = getIntent().getStringExtra(CHOSEN_BOOK_KEY);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("books");
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("owners");

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
        mSecondPic = findViewById(R.id.price_text_f);
        mThirdPic = findViewById(R.id.photo3_image);
        mImagesText = findViewById(R.id.images_text);
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

                owner = chosenBook.getOwner();

                String categories = "Main: "+chosenBook.getMain_category() + "\nSecondary: " + chosenBook.getSecondary_category() + "\nCustom: " + chosenBook.getCustom_category();
                mCategories.setText(categories);

                String photoUrl1 = chosenBook.getPhoto1();
                if (photoUrl1 != null)
                    Picasso.get().load(photoUrl1).into(mMainPic);

                String photoUrl2 = chosenBook.getPhoto2();
                if (photoUrl2 != null) {
                    mSecondPic.setVisibility(View.VISIBLE);
                    Picasso.get().load(photoUrl2).into(mSecondPic);
                    mImagesText.setVisibility(View.VISIBLE);
                }
                else
                    mSecondPic.setVisibility(View.GONE);
                String photoUrl3 = chosenBook.getPhoto3();
                if (photoUrl3 != null) {
                    mThirdPic.setVisibility(View.VISIBLE);
                    Picasso.get().load(photoUrl3).into(mThirdPic);
                    mImagesText.setVisibility(View.VISIBLE);
                }
                else
                    mThirdPic.setVisibility(View.GONE);
                if (photoUrl2==null && photoUrl3==null)
                    mImagesText.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addToFavorites(View view) {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mUserDatabaseReference.child(currentUser).child("favorites").child(bookHash).setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChosenBookActivity.this, "Book has been added to favorites!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openChat(View view){
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(SearchAdapter.EXTRA_USERNAME, owner);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
