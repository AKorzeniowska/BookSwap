package swiat.podzielono.bookswap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import swiat.podzielono.bookswap.data.UserInfo;
import swiat.podzielono.bookswap.ui.ChangeProfileDataActivity;

public class PersonalInfoActivity extends AppCompatActivity {

    private TextView mResidenceView;
    private TextView mPhoneView;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mResidenceView = findViewById(R.id.book_year_text);
        mPhoneView = findViewById(R.id.book_publisher_text);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference().child("owners")
                .child(mUser.getDisplayName())
                .child("info")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                        if (userInfo != null) {
                            if (userInfo.getResidence() != null) {
                                mResidenceView.setText(userInfo.getResidence());
                            }

                            if (userInfo.getPhone_number() != null) {
                                mPhoneView.setText(userInfo.getPhone_number());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        if (mUser.getPhoneNumber() != null) {
            mPhoneView.setText(mUser.getPhoneNumber());
        }
    }
}