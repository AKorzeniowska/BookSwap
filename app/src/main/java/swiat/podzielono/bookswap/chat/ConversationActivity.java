package swiat.podzielono.bookswap.chat;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import swiat.podzielono.bookswap.R;
import swiat.podzielono.bookswap.data.Message;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {

    private EditText mMessageTextView;
    private Button mSendButton;
    private RecyclerView mConversationRecycleView;

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private  String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mMessageTextView = findViewById(R.id.message_field);
        mSendButton = findViewById(R.id.send_message_button);
        mConversationRecycleView = findViewById(R.id.conversation_recycler_view);
        mConversationRecycleView.setHasFixedSize(true);
        mConversationRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        final String otherUser = getIntent().getStringExtra(SearchAdapter.EXTRA_USERNAME);
        username = otherUser;

        final String currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("owners").child(currentUserName).child("chats").child(otherUser);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String hashCode = dataSnapshot.getValue(String.class);
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("chats").child(hashCode);

                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (mConversationRecycleView.getAdapter() == null) {
                                List<Message> messages = new ArrayList<>((int) dataSnapshot.getChildrenCount());
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Message message = data.getValue(Message.class);
                                    messages.add(message);
                                }
                                ConversationAdapter adapter = new ConversationAdapter(messages);
                                mConversationRecycleView.setAdapter(adapter);
                                mConversationRecycleView.scrollToPosition(adapter.getItemCount() - 1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    database.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (mConversationRecycleView.getAdapter() != null) {
                                ConversationAdapter chatAdapter = (ConversationAdapter) mConversationRecycleView.getAdapter();
                                List<Message> list = chatAdapter.getMessages();
                                list.add(dataSnapshot.getValue(Message.class));
                                mConversationRecycleView.setAdapter(new ConversationAdapter(list));
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

                } else {
                    String hashCode = mDatabaseReference.push().getKey();
                    mDatabaseReference.setValue(hashCode);
                    DatabaseReference mData =  FirebaseDatabase.getInstance().getReference().child("owners").child(otherUser).child("chats").child(currentUserName);
                    mData.push();
                    mData.setValue(hashCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public void sendMessage(View view) {
        final String message = mMessageTextView.getText().toString().trim();
        if (!message.equals("")) {
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("owners").child(mUser.getDisplayName())
                    .child("chats").child(username);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String hashCode = dataSnapshot.getValue(String.class);
                    DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference().child("chats").child(hashCode);
                    Message dbMessage = new Message();
                    dbMessage.setMessage(message);
                    dbMessage.setUser_name(mUser.getDisplayName());
                    dbMessage.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
                    mDatabase.push().setValue(dbMessage);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        mMessageTextView.setText("");
    }
}
