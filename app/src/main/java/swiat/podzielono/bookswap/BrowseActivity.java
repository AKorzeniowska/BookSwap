package swiat.podzielono.bookswap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import swiat.podzielono.bookswap.data.BookObject;

public class BrowseActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private ArrayList<BookObject> booksList;

    private TextView testTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        booksList = new ArrayList<>();

        testTextView = findViewById(R.id.test);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        testTextView.setText(currentUser.getDisplayName());
        retrieveData();

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

    public void addBookActivityStarter (View view){
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

}
