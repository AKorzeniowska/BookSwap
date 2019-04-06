package swiat.podzielono.bookswap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import swiat.podzielono.bookswap.data.BookObject;
import swiat.podzielono.bookswap.data.UserInfo;
import swiat.podzielono.bookswap.exceptions.InvalidInputException;
import swiat.podzielono.bookswap.utilities.ImageResizer;

public class AddBookActivity extends AppCompatActivity {

    private EditText mBookAuthor;
    private EditText mBookTitle;
    private EditText mBookPrice;
    private EditText mDescription;
    private EditText mYear;
    private EditText mPublisher;
    private EditText mCustomCategory;
    private EditText mEdition;

    private ProgressBar mProgressBar;

    private ArrayAdapter<String> adapter;

    private DatabaseReference mDatabaseUserReference;
    private DatabaseReference mDatabaseBookReference;
    private DatabaseReference mDatabaseCategoryReference;
    private String currentUser;

    private StorageReference mStorageReference;
    private static final int PICK_FIRST_IMAGE_REQUEST = 0;
    private static final int PICK_SECOND_IMAGE_REQUEST = 1;
    private static final int PICK_THIRD_IMAGE_REQUEST = 2;
    private ImageView mImageView;
    private ImageView mImageView2;
    private ImageView mImageView3;

    private List<Uri> imageUri = new ArrayList<>();
    private List<Uri> uploadedImageUri = new ArrayList<>();
    private List<byte[]> bitmapList = new ArrayList<>();

    private Spinner mMainCategorySpinner;
    private Spinner mSecondaryCategorySpinner;
    private Spinner mConditionSpinner;
    private List<String> dataForMainSpinner = new ArrayList<>();
    private List<String> dataForSecondarySpinner = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBookAuthor = findViewById(R.id.book_author_text);
        mBookTitle = findViewById(R.id.book_title_text);
        mBookPrice = findViewById(R.id.book_price_text);
        mDescription = findViewById(R.id.book_description_text);
        mCustomCategory = findViewById(R.id.book_custom_category_textFill);
        mYear = findViewById(R.id.book_year_text);
        mPublisher = findViewById(R.id.book_publisher_text);
        mEdition = findViewById(R.id.book_edition_text);

        mImageView = findViewById(R.id.book_image_first);
        mImageView2 = findViewById(R.id.book_image_second);
        mImageView3 = findViewById(R.id.book_image_third);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mDatabaseUserReference = FirebaseDatabase.getInstance().getReference().child("owners").child(currentUser).child("my books");
        mDatabaseBookReference = FirebaseDatabase.getInstance().getReference().child("books");
        mDatabaseCategoryReference = FirebaseDatabase.getInstance().getReference().child("categories");

        mStorageReference = FirebaseStorage.getInstance().getReference();

        mMainCategorySpinner = findViewById(R.id.book_main_category_list);
        mSecondaryCategorySpinner = findViewById(R.id.book_secondary_category_list);
        mConditionSpinner = findViewById(R.id.spinner_BookCondition);

        mProgressBar = findViewById(R.id.progress_bar_add);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spinnerDataSetter();
        secondarySpinnerDataSetter();
        mProgressBar.setVisibility(View.INVISIBLE);
    }



    private void spinnerDataSetter() {
        String [] conditions = getResources().getStringArray(R.array.conditions);
        ArrayAdapter<String> adapterCond = new ArrayAdapter<>(
                AddBookActivity.this,
                android.R.layout.simple_spinner_item,
                conditions);
        adapterCond.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditionSpinner.setAdapter(adapterCond);

        mDatabaseCategoryReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> categories = dataSnapshot.getChildren();
                for (DataSnapshot category : categories) {
                    String main = category.getKey();
                    dataForMainSpinner.add(main);

              }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        AddBookActivity.this,
                        android.R.layout.simple_spinner_item,
                        dataForMainSpinner
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mMainCategorySpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddBookActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void secondarySpinnerDataSetter(){
        mMainCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String x = mMainCategorySpinner.getSelectedItem().toString();
                mDatabaseCategoryReference.child(x).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataForSecondarySpinner.clear();
                        Iterable<DataSnapshot> secondaries = dataSnapshot.getChildren();
                        for (DataSnapshot secondary : secondaries){
                            String sec = secondary.getKey();
                            dataForSecondarySpinner.add(sec);

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    AddBookActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    dataForSecondarySpinner
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mSecondaryCategorySpinner.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void addBookToDatabase() {

        String bookAuthor = mBookAuthor.getText().toString().trim();
        String bookTitle = mBookTitle.getText().toString().trim();
        String bookPrice = mBookPrice.getText().toString().trim();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        String year = mYear.getText().toString().trim();
        String mainCategory = mMainCategorySpinner.getSelectedItem().toString();
        String secondCategory = mSecondaryCategorySpinner.getSelectedItem().toString();
        String description = mDescription.getText().toString().trim();
        String publisher = mPublisher.getText().toString().trim();
        String customCategory = mCustomCategory.getText().toString().trim();
        String edition = mEdition.getText().toString().trim();
        String condition = mConditionSpinner.getSelectedItem().toString();

        final String hashcode = mDatabaseBookReference.push().getKey();

        String[] uri = {null, null, null};
        if (!uploadedImageUri.isEmpty()) {
            for (int i = 0; i < uploadedImageUri.size(); i++) {
                uri[i] = uploadedImageUri.get(i).toString();
            }
        }

        final String[] city = {null};
        FirebaseDatabase.getInstance().getReference().child("owners").child(currentUser).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserInfo info = dataSnapshot.getValue(UserInfo.class);
                    if (!info.getResidence().equals("Empty")) {
                        city[0] = info.getResidence();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BookObject bookToAdd = new BookObject(bookTitle, bookAuthor, year, publisher, mainCategory,
                secondCategory, customCategory, currentUser, condition, bookPrice,
                uploadedImageUri.get(0).toString(), uploadedImageUri.get(1).toString(), uploadedImageUri.get(2).toString(),
                description, currentDate, edition, city[0]);

        mDatabaseBookReference.child(hashcode).setValue(bookToAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                mDatabaseUserReference.child(hashcode).setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddBookActivity.this, "Book has been added!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddBookActivity.this, BrowseActivity.class);
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
                bitmap = ImageResizer.getSmallPicFromBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytedata = baos.toByteArray();
                bitmapList.add(bytedata);

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
                bitmap = ImageResizer.getSmallPicFromBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytedata = baos.toByteArray();
                bitmapList.add(bytedata);

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
                bitmap = ImageResizer.getSmallPicFromBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytedata = baos.toByteArray();
                bitmapList.add(bytedata);

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
        String bookAuthor = mBookAuthor.getText().toString().trim();
        String bookTitle = mBookTitle.getText().toString().trim();
        String bookPrice = mBookPrice.getText().toString().trim();

        try{
            if (bookAuthor.equals(""))  { throw new InvalidInputException(this, InvalidInputException.NO_AUTHOR_GIVEN); }
            if (bookTitle.equals(""))   { throw new InvalidInputException(this, InvalidInputException.NO_TITLE_GIVEN);  }
            if (bookPrice.equals(""))   { throw new InvalidInputException(this, InvalidInputException.NO_PRICE_GIVEN); }
            if (imageUri.isEmpty())     {throw new InvalidInputException(this, InvalidInputException.NO_PHOTO_UPLOADED); }
        }   catch (InvalidInputException e) { return; }


        mProgressBar.setVisibility(View.VISIBLE);

        for (int i=0; i<bitmapList.size(); i++) {

            final StorageReference ref = mStorageReference.child("images/" + UUID.randomUUID().toString());

            UploadTask uploadTask = ref.putBytes(bitmapList.get(i));
            final int finalI = i;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadedImageUri.add(uri);
                            if (finalI == bitmapList.size()-1) {
                                addBookToDatabase();
                            }
                        }
                    });
                }
            });
        }

    }
}
