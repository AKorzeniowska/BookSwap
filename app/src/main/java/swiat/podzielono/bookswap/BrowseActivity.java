package swiat.podzielono.bookswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import swiat.podzielono.bookswap.bookSearch.SearchActivity;
import swiat.podzielono.bookswap.chat.ChatActivity;
import swiat.podzielono.bookswap.data.BookAdapter;
import swiat.podzielono.bookswap.data.BookObject;
import swiat.podzielono.bookswap.ui.ProfileActivity;

public class BrowseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String CHOSEN_BOOK_KEY = "hash";

    private ArrayList<BookObject> booksList;
    private ArrayList<String> hashesList;
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
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);


        booksList = new ArrayList<>();
        hashesList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.book_list);

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
            Picasso.get().load(mUser.getPhotoUrl().toString()).into(mProfilImage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        booksList.clear();
        retrieveData();

    }

    protected void retrieveData() {
        mDatabaseReference.child("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> books = dataSnapshot.getChildren();
                for (DataSnapshot book : books) {
                    BookObject currentBook = book.getValue(BookObject.class);
                    booksList.add(currentBook);
                    hashesList.add(book.getKey());
                }
                bookAdapter = new BookAdapter(BrowseActivity.this, booksList);
                listView.setAdapter(bookAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(BrowseActivity.this, ChosenBookActivity.class);
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


    public void openAddBookActivity(MenuItem item) {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    public void openChatActivity(MenuItem item) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void openSearchActivity(MenuItem item) {
        Intent intent = new Intent(this, SearchActivity.class);
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
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
