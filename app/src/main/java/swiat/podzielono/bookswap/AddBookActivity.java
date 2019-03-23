package swiat.podzielono.bookswap;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import swiat.podzielono.bookswap.data.BookObject;

public class AddBookActivity extends AppCompatActivity {

    private EditText mBookAuthor;
    private EditText mBookTitle;
    private EditText mBookPrice;
    private DatabaseReference mDatabaseUserReference;
    private DatabaseReference mDatabaseBookReference;
    private String currentUser;

    private StorageReference mStorageReference;
    private final int PICK_FIRST_IMAGE_REQUEST = 0;
    private final int PICK_SECOND_IMAGE_REQUEST = 1;
    private final int PICK_THIRD_IMAGE_REQUEST = 2;
    private ImageView mImageView;
    private ImageView mImageView2;
    private ImageView mImageView3;
    List<Uri> imageUri = new ArrayList<>();
    List<Uri> uploadedImageUri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mBookAuthor = findViewById(R.id.book_author_text);
        mBookTitle = findViewById(R.id.book_title_text);
        mBookPrice = findViewById(R.id.book_price_text);

        mImageView = findViewById(R.id.book_image_first);
        mImageView2 = findViewById(R.id.book_image_second);
        mImageView3 = findViewById(R.id.book_image_third);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mDatabaseUserReference = FirebaseDatabase.getInstance().getReference().child("owners").child(currentUser).child("my books");
        mDatabaseBookReference = FirebaseDatabase.getInstance().getReference().child("books");

        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private void addBookToDatabase() {
        String bookAuthor = mBookAuthor.getText().toString().trim();
        String bookTitle = mBookTitle.getText().toString().trim();
        String bookPrice = mBookPrice.getText().toString().trim();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());

        final String hashcode = mDatabaseBookReference.push().getKey();

        String[] uri = {null, null, null};
        if (!uploadedImageUri.isEmpty()) {
            for (int i = 0; i < uploadedImageUri.size(); i++) {
                uri[i] = uploadedImageUri.get(i).toString();
            }
        }
        BookObject bookToAdd = new BookObject(bookTitle, bookAuthor, null, null, null, null, null, currentUser, null, bookPrice, uploadedImageUri.get(0).toString(), null, null, null, currentDate);

        mDatabaseBookReference.child(hashcode).setValue(bookToAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                mDatabaseUserReference.child(hashcode).setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddBookActivity.this, "Book has been added!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddBookActivity.this, MyBooksActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FIRST_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri.add(data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri.get(0));
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_SECOND_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri.add(data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri.get(1));
                mImageView2.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_THIRD_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri.add(data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri.get(2));
                mImageView3.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (imageUri.isEmpty()) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FIRST_IMAGE_REQUEST);
            return;
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), imageUri.size());

    }

    public void uploadImage(View view) {
        if (imageUri.isEmpty()) {
            addBookToDatabase();
            return;
        }


        final StorageReference ref = mStorageReference.child("images/" + UUID.randomUUID().toString());

        ref.putFile(imageUri.get(0))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadedImageUri.add(uri);
                                    addBookToDatabase();
                            }
                        });
                        Toast.makeText(AddBookActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
