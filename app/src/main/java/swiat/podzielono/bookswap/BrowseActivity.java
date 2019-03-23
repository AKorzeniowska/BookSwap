package swiat.podzielono.bookswap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import swiat.podzielono.bookswap.chat.ChatActivity;
import swiat.podzielono.bookswap.data.*;

public class BrowseActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private ArrayList<BookObject> booksList;
    private BookAdapter bookAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        booksList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.book_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<BookObject> books = new ArrayList<>();
        books.add(new BookObject("Książka 1", "Autor", "wadziux", "10"));
        books.add(new BookObject("Książka 2", "Autor", "wadziux", "10"));
        books.add(new BookObject("Książka 3", "Autor", "wadziux", "10"));
        books.add(new BookObject("Książka 1", "Autor", "wadziux", "10"));
        books.add(new BookObject("Książka 2", "Autor", "wadziux", "10"));
        books.add(new BookObject("Książka 3", "Autor", "wadziux", "10"));
        books.add(new BookObject("Książka 1", "Autor", "wadziux", "10"));
        books.add(new BookObject("Książka 2", "Autor", "wadziux", "10"));
        books.add(new BookObject("Książka 3", "Autor", "wadziux", "10"));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new BookAdapter(books, recyclerView));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void retrieveData(){
        mDatabaseReference.child("owners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable <DataSnapshot> users = dataSnapshot.getChildren();
                for (DataSnapshot user : users){
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


    public void addBookActivityStarter (MenuItem item){
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

}
