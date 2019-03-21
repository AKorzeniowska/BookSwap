package swiat.podzielono.bookswap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import swiat.podzielono.bookswap.data.BookObject;

public class AddBookActivity extends AppCompatActivity {

    private EditText mBookAuthor;
    private EditText mBookTitle;
    private EditText mBookPrice;
    private DatabaseReference mDatabaseReference;
    private String currentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mBookAuthor = findViewById(R.id.editText_BookAuthor);
        mBookTitle = findViewById(R.id.editText_BookTitle);
        mBookPrice = findViewById(R.id.editText_BookPrice);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("owners").child(currentUser);
    }

    public void addBookToDatabase (View view){
        String bookAuthor = mBookAuthor.getText().toString().trim();
        String bookTitle = mBookTitle.getText().toString().trim();
        String bookPrice = mBookPrice.getText().toString().trim();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        BookObject bookToAdd = new BookObject(bookTitle, bookPrice, "", bookAuthor, currentUser, currentDate);

        mDatabaseReference.push().setValue(bookToAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddBookActivity.this, "Book has been added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddBookActivity.this, MyBooksActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String getTodaysDate(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return dateFormat.format(date);
    }
}
