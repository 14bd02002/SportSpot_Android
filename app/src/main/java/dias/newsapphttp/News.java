package dias.newsapphttp;

import java.util.Date;

/**
 * Created by 1 on 23.11.2017.
 */

public class News {

    private String title, desc, image, date, name;

    private int likes;

    public News(){

    }


    public News(String title, String desc, String image, String date, String name, int likes) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.date = date;
        this.name = name;
        this.likes = likes;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikes() { return likes; }

    public void setLikes(int likes) { this.likes = likes; }








}
