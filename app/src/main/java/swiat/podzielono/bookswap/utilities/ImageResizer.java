package swiat.podzielono.bookswap.utilities;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageResizer {

    public static Bitmap getSmallPicFromUrl(String imageUrl) {
        Bitmap pic = getBitmapFromURL(imageUrl);
        if (pic == null) { return null; }
        if (pic.getHeight() > 480) {
            int rescaler;
            rescaler = pic.getHeight()/480;
            Bitmap resized = Bitmap.createScaledBitmap(pic, pic.getWidth() / rescaler, pic.getHeight() / rescaler, true);
            return resized;
        }
        return pic;
    }

    public static Bitmap getSmallPicFromBitmap(Bitmap pic){
        if (pic.getHeight() > 480) {
            int rescaler;
            rescaler = pic.getHeight()/480;
            Bitmap resized = Bitmap.createScaledBitmap(pic, pic.getWidth() / rescaler, pic.getHeight() / rescaler, true);
            return resized;
        }
        return pic;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, null, null);
        return Uri.parse(path);
    }



    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }


}
