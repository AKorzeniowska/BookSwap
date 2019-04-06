package swiat.podzielono.bookswap.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import swiat.podzielono.bookswap.R;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> list;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = new ArrayList<>();
        mRecyclerView = findViewById(R.id.chat_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        mDataBase = FirebaseDatabase.getInstance().getReference().child("owners")
                .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                .child("chats");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mRecyclerView.getAdapter() == null) {
                    List<String> list = new ArrayList<>((int)dataSnapshot.getChildrenCount());
                    for (DataSnapshot data: dataSnapshot.getChildren()) {
                        list.add(data.getKey());
                    }

                    ChatAdapter chatAdapter = new ChatAdapter(getApplicationContext(), list);
                    mRecyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDataBase.addValueEventListener(valueEventListener);

        mDataBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (mRecyclerView.getAdapter() != null) {
                    List<String> list = ((ChatAdapter) mRecyclerView.getAdapter()).getItemList();
                    list.add(dataSnapshot.getKey());
                    ChatAdapter chatAdapter = new ChatAdapter(getApplicationContext(), list);
                    mRecyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchUserActivity.class);
                startActivity(intent);
            }
        });
    }

}
