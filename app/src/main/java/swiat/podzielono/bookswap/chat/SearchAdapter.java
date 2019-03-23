package swiat.podzielono.bookswap.chat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View.OnClickListener;
import swiat.podzielono.bookswap.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ChatViewHolder> {

    public static String EXTRA_USERNAME = "username";

    private List<String> mItemList;
    private Context context;

    public SearchAdapter(Context context, List<String> mItemList) {
        this.context = context;
        this.mItemList = mItemList;
    }

    @NonNull
    @Override
    public SearchAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_element, viewGroup, false);
        SearchAdapter.ChatViewHolder myViewHolder = new SearchAdapter.ChatViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.ChatViewHolder chatViewHolder, int i) {
        chatViewHolder.mUserNameTextView.setText(mItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return  mItemList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

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
            intent.putExtra(userName, EXTRA_USERNAME);
            context.startActivity(intent);
        }
    }
}
