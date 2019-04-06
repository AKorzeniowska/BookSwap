package swiat.podzielono.bookswap.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
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
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if (mUser.getPhotoUrl() != null) {
            mProfileImage.setImageURI(mUser.getPhotoUrl());
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
