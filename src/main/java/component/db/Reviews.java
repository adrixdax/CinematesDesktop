package component.db;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Reviews {

    @JsonProperty("id_reviews")
    private int id_reviews;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("val")
    private double val;
    @JsonProperty("idRecordRef")
    private int idRecordRef;
    @JsonProperty("iduser")
    private String iduser;
    @JsonProperty("typeOfReview")
    private String typeOfReview;
    @JsonProperty("obscured")
    private boolean obscured;

    @JsonCreator
    public Reviews(@JsonProperty("id_review") int id_review,
                   @JsonProperty("title") String title,
                   @JsonProperty("description") String description,
                   @JsonProperty("val") double val,
                   @JsonProperty("idRecordRef") int idRecordRef,
                   @JsonProperty("iduser") String iduser,
                   @JsonProperty("typeOfReview") String typeOfReview,
                   @JsonProperty("obscured") boolean obscured) {
        this.id_reviews = id_review;
        this.title = title;
        this.description = description;
        this.val = val;
        this.idRecordRef = idRecordRef;
        this.iduser = iduser;
        this.typeOfReview = typeOfReview;
        this.obscured = obscured;
    }

    public int getId_reviews() {
        return id_reviews;
    }

    @JsonSetter("id_reviews")
    public void setId_reviews(int id_reviews) {
        this.id_reviews = id_reviews;
    }

    public String getTitle() {
        return title;
    }

    @JsonSetter("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }


    public double getVal() {
        return val;
    }

    @JsonSetter("val")
    public void setVal(double val) {
        this.val = val;
    }

    public int getIdRecordRef() {
        return idRecordRef;
    }

    @JsonSetter("idRecordRef")
    public void setIdRecordRef(int idRecordRef) {
        this.idRecordRef = idRecordRef;
    }

    public String getIduser() {
        return iduser;
    }

    @JsonSetter("iduser")
    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getTypeOfReview() {
        return typeOfReview;
    }

    @JsonSetter("typeOfReview")
    public void setTypeOfReview(String typeOfReview) {
        this.typeOfReview = typeOfReview;
    }

    public boolean isObscured() {
        return obscured;
    }

    @JsonSetter("obscured")
    public void setObscured(boolean obscured) {
        this.obscured = obscured;
    }
}
