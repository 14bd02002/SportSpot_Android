package dias.newsapphttp;

/**
 * Created by 1 on 23.11.2017.
 */

public class News {

    private String title, desc, image, date;

    public News(){

    }

    public News(String title, String desc, String image, String date) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.date = date;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
