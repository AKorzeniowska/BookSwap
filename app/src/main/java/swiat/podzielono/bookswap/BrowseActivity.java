package swiat.podzielono.bookswap;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        booksList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retreiveData();

    }

    protected void retreiveData(){
        mDatabaseReference.child("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable <DataSnapshot> books = dataSnapshot.getChildren();
                for (DataSnapshot book : books){
                    BookObject currentBook = book.getValue(BookObject.class);
                    booksList.add(currentBook);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BrowseActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
