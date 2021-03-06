package swiat.podzielono.bookswap.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import swiat.podzielono.bookswap.R;


public class BookAdapter extends ArrayAdapter<BookObject> {

    private Context mContext;
    private List<BookObject> bookList = new ArrayList<>();

    public BookAdapter(@NonNull Context context, ArrayList<BookObject> list) {
        super(context, 0 , list);
        mContext = context;
        bookList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.listview_book,parent,false);

        BookObject currentBook = bookList.get(position);


        TextView title = (TextView) listItem.findViewById(R.id.title_text);
        title.setText(currentBook.getTitle());

        TextView author = (TextView) listItem.findViewById(R.id.author_text);
        author.setText(currentBook.getAuthor());

        TextView category = (TextView) listItem.findViewById(R.id.category_txt);
        category.setText(currentBook.getCustom_category());

        TextView price = (TextView) listItem.findViewById(R.id.price_text);
        String price_display = "Price: $" + currentBook.getPrice();
        price.setText(price_display);

        ImageView photo = (ImageView) listItem.findViewById(R.id.book_image);
        String photoUrl = currentBook.getPhoto1();
        if (!photoUrl.equals(""))
            Picasso.get().load(photoUrl).into(photo);


        return listItem;
    }
}