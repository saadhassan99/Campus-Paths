package marvel;

import com.opencsv.bean.CsvBindByName;

public class UserModel {

    @CsvBindByName
    private String hero;

    @CsvBindByName
    private String book;

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }
}
