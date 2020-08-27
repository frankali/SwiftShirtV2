package modalclass;

public class Profile1_ModalClass {

    String like;
    String comment;
    String time;

    public Profile1_ModalClass(){

    }

    public Profile1_ModalClass(String like, String comment, String time) {
        this.like = like;
        this.comment = comment;
        this.time = time;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
