package swiat.podzielono.bookswap.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import swiat.podzielono.bookswap.R;
import swiat.podzielono.bookswap.data.BookObject;

import java.util.ArrayList;
import java.util.List;

import static swiat.podzielono.bookswap.chat.SearchAdapter.EXTRA_USERNAME;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private List<String> mItemList;

    public ChatAdapter(Context context, List<String> mItemList) {
        this.context = context;
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = mFirebaseUser.getDisplayName();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("owners").child(username);

        this.mItemList = mItemList;
    }

    public List<String> getItemList() {
        return mItemList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_element, viewGroup, false);
        ChatViewHolder myViewHolder = new ChatViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder chatViewHolder, int i) {
        chatViewHolder.mUserNameTextView.setText(mItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return  mItemList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView mUserNameTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserNameTextView = itemView.findViewById(R.id.chat_list_element);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String userName = mUserNameTextView.getText().toString();
            Intent intent = new Intent(context, ConversationActivity.class);
            intent.putExtra(EXTRA_USERNAME, userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
