package swiat.podzielono.bookswap.data;

public class BookObject {
    private String book_title, book_author, book_owner, book_price, book_image;

    public BookObject(String book_title, String book_price, String book_image, String book_author, String book_owner){
        this.book_title = book_title;
        this.book_price = book_price;
        this.book_image = book_image;
        this.book_author = book_author;
        this.book_owner = book_owner;
    }
}
