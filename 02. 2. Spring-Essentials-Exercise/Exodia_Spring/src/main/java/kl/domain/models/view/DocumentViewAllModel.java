package kl.domain.models.view;

public class DocumentViewAllModel {
    private String id;
    private String title;
    private String content;

    public DocumentViewAllModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if(this.title.length() > 12){
            return this.title.substring(0, 12) + "...";
        }

        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
