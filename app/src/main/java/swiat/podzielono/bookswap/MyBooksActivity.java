package swiat.podzielono.bookswap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import swiat.podzielono.bookswap.ui.ProfileActivity;

public class MyBooksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabaseUsersReference;
    private DatabaseReference mDatabaseBooksReference;
    private ArrayList<BookObject> booksList;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private BookAdapter bookAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDatabaseUsersReference = FirebaseDatabase.getInstance().getReference().child("owners");
        mDatabaseBooksReference = FirebaseDatabase.getInstance().getReference().child("books");
        booksList = new ArrayList<>();

        listView = findViewById(R.id.book_list);
        mNavigationView = findViewById(R.id.nav_view);

        View view = mNavigationView.getHeaderView(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveData();
    }

    protected void retrieveData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String username = user.getDisplayName();

        mDatabaseUsersReference.child(username).addValueEventListener(new ValueEventListener() {
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
                            bookAdapter = new BookAdapter(MyBooksActivity.this, booksList);
                            listView.setAdapter(bookAdapter);
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