package swiat.podzielono.bookswap;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import swiat.podzielono.bookswap.chat.ChatActivity;
import swiat.podzielono.bookswap.data.*;
import swiat.podzielono.bookswap.ui.ProfileActivity;

public class BrowseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<BookObject> booksList;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;

    private BookAdapter bookAdapter;
    private ListView listView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView mUsernameTextView;
    private TextView mEmailTextView;
    private ImageView mProfilImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.book_list);
        ArrayList<BookObject> books = new ArrayList<>();
        books.add(new BookObject("Książka 1", "Autor1", "wadziux", "10", "https://firebasestorage.googleapis.com/v0/b/bookswap-9525c.appspot.com/o/images%2Fcb9ac3b8-54b2-4f6d-9572-8e177b5fd995?alt=media&token=70666a68-a01e-45e9-96fd-b8ba4d3eab47"));

        bookAdapter = new BookAdapter(this, books);
        listView.setAdapter(bookAdapter);

        mUsernameTextView = findViewById(R.id.profile_username);
        mEmailTextView = findViewById(R.id.profile_email);
        mProfilImage = findViewById(R.id.user_profile);

        View view = mNavigationView.getHeaderView(0);
        mUsernameTextView = (TextView) view.findViewById(R.id.profile_username);
        mUsernameTextView.setText(mUser.getDisplayName());
        mEmailTextView = (TextView) view.findViewById(R.id.profile_email);
        mEmailTextView.setText(mUser.getEmail());
        mProfilImage = (ImageView) view.findViewById(R.id.user_profile);
        if (mUser.getPhotoUrl() != null) {
            mProfilImage.setImageURI(mUser.getPhotoUrl());
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void retrieveData() {
        mDatabaseReference.child("owners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                for (DataSnapshot user : users) {
                    Iterable<DataSnapshot> books = user.getChildren();
                    for (DataSnapshot book : books) {
                        BookObject currentBook = book.getValue(BookObject.class);
                        booksList.add(currentBook);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BrowseActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void addBookActivityStarter(MenuItem item) {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    public void openChatActivity(MenuItem item) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_sign_out) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            FirebaseAuth.getInstance().signOut();
            finish();
        } else if (id == R.id.nav_my_books) {
            Intent intent = new Intent(this, MyBooksActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_favourite_books) {
            //in future
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
