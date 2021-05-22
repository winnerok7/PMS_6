package ua.kpi.comsys.iv8107;

public class Book {
    private String ttl;
    private String sbttl;
    private String isbn13;
    private String price;
    private String image;
    private String authors;
    private String pblshr;
    private int pages;
    private int year;
    private int rate;
    private String desc;
    public Book(String ttl, String sbttl, String isbn13, String price, String image){
        this.ttl = ttl;
        this.sbttl = sbttl;
        this.isbn13 = isbn13;
        this.price = price;
        this.image = image;
    }
    public Book(String ttl, String sbttl, String authors, String pblshr, String isbn13,
                int pages, int year, int rate, String desc, String price, String image){
        this.ttl = ttl;
        this.sbttl = sbttl;
        this.authors = authors;
        this.pblshr = pblshr;
        this.isbn13 = isbn13;
        this.pages = pages;
        this.year = year;
        if (rate > 5){
            this.rate = 5;
        } else if (rate < 1){
            this.rate = 1;
        } else {
            this.rate = rate;
        }
        this.desc = desc;
        this.price = price;
        this.image = image;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getSbttl() {
        return sbttl;
    }

    public void setSbttl(String sbttl) {
        this.sbttl = sbttl;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPblshr() {
        return pblshr;
    }

    public void setPblshr(String pblshr) {
        this.pblshr = pblshr;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
