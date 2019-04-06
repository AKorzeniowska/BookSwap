package swiat.podzielono.bookswap.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import swiat.podzielono.bookswap.R;
import swiat.podzielono.bookswap.data.UserInfo;

public class ProfileActivity extends AppCompatActivity {

    private TextView mUsernameView;
    private TextView mUserEmailView;
    private TextView mResidenceView;
    private TextView mStudyView;
    private TextView mStudyFieldView;
    private TextView mPhoneView;
    private ImageView mProfileImage;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsernameView = findViewById(R.id.user_username);
        mUserEmailView = findViewById(R.id.user_email_field_profile_activity);
        mResidenceView = findViewById(R.id.user_residence_place);
        mStudyView = findViewById(R.id.user_university);
        mStudyFieldView = findViewById(R.id.user_study_field);
        mPhoneView = findViewById(R.id.user_phone_number);
        mProfileImage = findViewById(R.id.profile_activity_image);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsernameView.setText(mUser.getDisplayName());
        mUserEmailView.setText(mUser.getEmail());

        FirebaseDatabase.getInstance().getReference().child("owners")
                .child(mUser.getDisplayName())
                .child("info")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                        if (userInfo != null) {
                            if (userInfo.getField_of_study() != null) {
                                mStudyFieldView.setText(userInfo.getField_of_study());
                            }

                            if (userInfo.getResidence() != null) {
                                mResidenceView.setText(userInfo.getResidence());
                            }

                            if (userInfo.getStudy() != null) {
                                mStudyView.setText(userInfo.getStudy());
                            }

                            if (userInfo.getPhone_number() != null) {
                                mPhoneView.setText(userInfo.getPhone_number());
                            }
                        }

                        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null){
                            Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()).resize(250,250).into(mProfileImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if (mUser.getPhotoUrl() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(mUser.getPhotoUrl().toString()));
                mProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Couldn't upload file", Toast.LENGTH_LONG);
            }
        }

        if (mUser.getPhoneNumber() != null) {
            mPhoneView.setText(mUser.getPhoneNumber());
        }
    }

    public void changePersonalData(View view) {
        Intent intent = new Intent(this, ChangeProfileDataActivity.class);
        startActivity(intent);
        finish();
    }
}
