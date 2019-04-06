package swiat.podzielono.bookswap.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import swiat.podzielono.bookswap.R;
import swiat.podzielono.bookswap.data.UserInfo;

import java.io.IOException;

public class ChangeProfileDataActivity extends AppCompatActivity {

    private static final int CHANGED_PROFILE_PICTURE = 0;

    private EditText mResidenceView;
    private EditText mStudyView;
    private EditText mStudyFieldView;
    private EditText mPhoneView;
    private ImageView mProfileImage;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_data);

        mProfileImage = findViewById(R.id.profile_activity_image);
        mResidenceView = findViewById(R.id.user_residence_place);
        mStudyView = findViewById(R.id.user_university);
        mStudyFieldView = findViewById(R.id.user_study_field);
        mPhoneView = findViewById(R.id.user_phone_number);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference()
                .child("owners")
                .child(mUser.getDisplayName())
                .child("info");
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
        mFirebaseDatabase.setValue(userInfo);

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
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
            } catch (IOException e) {
                Toast.makeText(this, "Couldn't upload image", Toast.LENGTH_LONG).show();
            }
        }
    }
}
