package swiat.podzielono.bookswap.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import swiat.podzielono.bookswap.R;
import swiat.podzielono.bookswap.data.Message;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<Message> messages;

    public ConversationAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_element, viewGroup, false);
        ConversationViewHolder myViewHolder = new ConversationViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder conversationViewHolder, int i) {
        Message message = messages.get(i);
        String displayMessage = message.getUser_name() + ": " + message.getMessage();
        conversationViewHolder.mMessage.setText(displayMessage);
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        private TextView mMessage;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.chat_list_element);
        }
    }
}
