package swiat.podzielono.bookswap.chat;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import com.google.firebase.database.*;
import swiat.podzielono.bookswap.R;

import java.util.ArrayList;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity {

    private final static int MAX_USERS_ON_LIST_VIEW = 10;

    private SearchView mSearchView;
    private RecyclerView mUsersListView;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("owners");
        mUsersListView = findViewById(R.id.users_list_view);
        mUsersListView.setHasFixedSize(true);
        mUsersListView.setLayoutManager(new LinearLayoutManager(SearchUserActivity.this));
        mSearchView = findViewById(R.id.search_users);
        mSearchView.setQueryHint("Search users...");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> strings = new ArrayList<>();
                        for(DataSnapshot snap: dataSnapshot.getChildren()) {
                            if (snap.getKey().contains(query)) {
                                strings.add(snap.getKey());
                                if (strings.size() == MAX_USERS_ON_LIST_VIEW) {
                                    break;
                                }
                            }
                        }
                        SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(), strings);
                        mUsersListView.setAdapter(searchAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


}
