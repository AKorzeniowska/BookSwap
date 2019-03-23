package swiat.podzielono.bookswap.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swiat.podzielono.bookswap.R;



public class BookAdapter extends RecyclerView.Adapter {

    private RecyclerView mRecyclerView;
    private List<BookObject> bookObjectList = new ArrayList<>();


    // implementacja wzorca ViewHolder
    // każdy obiekt tej klasy przechowuje odniesienie do layoutu elementu listy
    // dzięki temu wywołujemy findViewById() tylko raz dla każdego elementu
    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mAuthor;
        public TextView mOwner;
        public TextView mPrice;

        public MyViewHolder(View pItem) {
            super(pItem);
            mTitle = (TextView) pItem.findViewById(R.id.book_title_text);
            mAuthor = (TextView) pItem.findViewById(R.id.book_author_text);
            mOwner = (TextView) pItem.findViewById(R.id.username_text);
            mPrice = (TextView) pItem.findViewById(R.id.book_price_text);

        }
    }

    // konstruktor adaptera
    public BookAdapter(ArrayList<BookObject> books, RecyclerView pRecyclerView){
        this.bookObjectList=books;
        mRecyclerView = pRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        // tworzymy layout ksiażki oraz obiekt ViewHoldera
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_book, viewGroup, false);

        // dla elementu listy ustawiamy obiekt OnClickListener,
        // który usunie element z listy po kliknięciu na niego
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // odnajdujemy indeks klikniętego elementu
                int positionToDelete = mRecyclerView.getChildAdapterPosition(v);
                // usuwamy element ze źródła danych
                bookObjectList.remove(positionToDelete);
                // poniższa metoda w animowany sposób usunie element z listy
                notifyItemRemoved(positionToDelete);
            }
        });

        // tworzymy i zwracamy obiekt ViewHolder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        // uzupełniamy layout artykułu
        BookObject bookObject = bookObjectList.get(i);
        ((MyViewHolder) viewHolder).mTitle.setText(bookObject.getTitle());
        ((MyViewHolder) viewHolder).mAuthor.setText(bookObject.getAuthor());
        ((MyViewHolder) viewHolder).mOwner.setText(bookObject.getOwner());
        ((MyViewHolder) viewHolder).mPrice.setText(bookObject.getPrice());
    }

    @Override
    public int getItemCount() {
        return bookObjectList.size();
    }
}