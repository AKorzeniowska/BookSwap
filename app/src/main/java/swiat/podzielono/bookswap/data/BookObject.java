package swiat.podzielono.bookswap.data;

public class BookObject {
    private String title, author, year, publisher, main_category;
    private String secondary_category, custom_category, owner;
    private String condition;
    private String price;
    private String photo1;
    private String photo2;
    private String photo3;
    private String description;
    private String add_date;
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    private String edition;

    public BookObject() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookObject(String title, String author, String year, String publisher, String main_category, String secondary_category, String custom_category, String owner, String condition, String price, String photo1, String photo2, String photo3, String description, String add_date, String city, String edition) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.main_category = main_category;
        this.secondary_category = secondary_category;
        this.custom_category = custom_category;
        this.owner = owner;
        this.condition = condition;
        this.price = price;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.description = description;
        this.add_date = add_date;
        this.city = city;
        this.edition = edition;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public String getSecondary_category() {
        return secondary_category;
    }

    public void setSecondary_category(String secondary_category) {
        this.secondary_category = secondary_category;
    }

    public String getCustom_category() {
        return custom_category;
    }

    public void setCustom_category(String custom_category) {
        this.custom_category = custom_category;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }
}
