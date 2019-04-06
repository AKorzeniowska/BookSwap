package swiat.podzielono.bookswap.exceptions;

import android.content.Context;
import android.widget.Toast;

public class InvalidInputException extends Exception {
    public static int NO_TITLE_GIVEN = 1;
    public static int NO_AUTHOR_GIVEN = 2;
    public static int NO_PRICE_GIVEN = 3;
    public static int NO_PHOTO_UPLOADED = 4;

    public InvalidInputException(Context context, int issue){
        switch (issue){
            case 1:
                Toast.makeText(context, "You have to fill the Title field!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(context, "You have to fill the Author field!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(context, "You have to fill the Price field!", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(context, "You have to upload at least one photo!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
