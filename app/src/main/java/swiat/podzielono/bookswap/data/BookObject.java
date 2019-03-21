package swiat.podzielono.bookswap.data;

public class BookObject {
    private String book_title, book_author, book_owner, book_price, book_image, add_date;

    public BookObject() {}

    public BookObject(String book_title, String book_price, String book_image, String book_author, String book_owner, String add_date){
        this.book_title = book_title;
        this.book_price = book_price;
        this.book_image = book_image;
        this.book_author = book_author;
        this.book_owner = book_owner;
        this.add_date = add_date;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getBook_owner() {
        return book_owner;
    }

    public void setBook_owner(String book_owner) {
        this.book_owner = book_owner;
    }

    public String getBook_price() {
        return book_price;
    }

    public void setBook_price(String book_price) {
        this.book_price = book_price;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }
}
