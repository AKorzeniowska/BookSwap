package swiat.podzielono.bookswap.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import swiat.podzielono.bookswap.R;
import swiat.podzielono.bookswap.data.UserInfo;

public class ChangeProfileDataActivity extends AppCompatActivity {

    private static final int CHANGED_PROFILE_PICTURE = 0;

    private EditText mResidenceView;
    private EditText mStudyView;
    private EditText mStudyFieldView;
    private EditText mPhoneView;
    private TextView mUsernameField;
    private TextView mEmailField;
    private ImageView mProfileImage;

    private Uri newProfileUri;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseUser mUser;

    private String residence, study, studyField, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_data);

        mProfileImage = findViewById(R.id.profile_activity_image);
        mResidenceView = findViewById(R.id.user_residence_place);
        mStudyView = findViewById(R.id.user_university);
        mStudyFieldView = findViewById(R.id.user_study_field);
        mPhoneView = findViewById(R.id.user_phone_number);
        mUsernameField = findViewById(R.id.user_username);
        mEmailField = findViewById(R.id.user_email_field_profile_activity);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsernameField.setText(mUser.getEmail());
        mEmailField.setText(mUser.getDisplayName());


        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference()
                .child("owners")
                .child(mUser.getDisplayName())
                .child("info");
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentDataSetter();
    }

    public void currentDataSetter(){
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo info = dataSnapshot.getValue(UserInfo.class);
                residence = info.getResidence();
                study = info.getStudy();
                studyField = info.getField_of_study();
                phone = info.getPhone_number();

                mResidenceView.setText(residence);
                mStudyView.setText(study);
                mStudyFieldView.setText(studyField);
                mPhoneView.setText(phone);

                if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
                    Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).into(mProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changePersonalData(View view) {
        String residence = mResidenceView.getText().toString().trim();
        String study = mStudyView.getText().toString().trim();
        String studyField = mStudyFieldView.getText().toString().trim();
        String phone = mPhoneView.getText().toString().trim();
        UserInfo userInfo = new UserInfo();

        if (!residence.isEmpty()) {
            userInfo.setResidence(residence);
        }

        if (!study.isEmpty()) {
            userInfo.setStudy(study);
        }

        if (!studyField.isEmpty()) {
            userInfo.setField_of_study(studyField);
        }

        if(!phone.isEmpty()){
            userInfo.setPhone_number(phone);
        }
        mFirebaseDatabase.setValue(userInfo);

        if (newProfileUri != null) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile-pictures/" + UUID.randomUUID().toString());

            storageReference.putFile(newProfileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest
                                    .Builder()
                                    .setPhotoUri(uri)
                                    .build();
                            mUser.updateProfile(changeRequest);
                            Intent intent = new Intent(ChangeProfileDataActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
        else{
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void uploadImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHANGED_PROFILE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGED_PROFILE_PICTURE && resultCode == RESULT_OK
            && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                mProfileImage.setImageBitmap(bitmap);
                newProfileUri = data.getData();

            } catch (IOException e) {
                Toast.makeText(this, "Couldn't upload image", Toast.LENGTH_LONG).show();
            }
        }
    }
}
