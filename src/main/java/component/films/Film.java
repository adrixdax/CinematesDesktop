package component.films;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film {

    @JsonProperty("id_Film")
    private int id_Film;
    @JsonProperty("film_Title")
    private String film_Title;
    @JsonProperty("plot")
    private String plot;
    @JsonProperty("posterPath")
    private String posterPath;
    @JsonProperty("release_Date")
    private String release_Date;
    @JsonProperty("runtime")
    private int runtime;
    @JsonProperty("genres")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private String[] genres;
    @JsonProperty("cast")
    private List<Cast> cast;
    @JsonProperty("counter")
    private int counter;


    @JsonCreator
    public Film(@JsonProperty("id_Film") int id_Film,
                @JsonProperty("film_Title") String film_Title,
                @JsonProperty("plot") String plot,
                @JsonProperty("posterPath") String posterPath,
                @JsonProperty("release_Date") String release_Date,
                @JsonProperty("runtime") int runtime,
                @JsonProperty("genres") String[] genres,
                @JsonProperty("cast") List<Cast> cast,
                @JsonProperty("counter") int counter) {
        this.id_Film = id_Film;
        this.film_Title = film_Title;
        this.plot = plot;
        this.posterPath = posterPath;
        this.release_Date = release_Date;
        this.runtime = runtime;
        this.genres = genres;
        this.cast = cast;
        this.counter = counter;
    }

    public Film(String imageUrl, String title, String plot, String relaseDate, int duration, String[] category) {
        this.posterPath = imageUrl;
        this.film_Title = title;
        this.plot = plot;
        this.release_Date = relaseDate;
        this.runtime = duration;
        this.genres = category;
    }

    public Film(String imageUrl, String title) {
        this.posterPath = imageUrl;
        this.film_Title = title;
    }

    public Film(String imageUrl) {
        new Film(imageUrl, "");
    }

    public Film() {
    }

    public int getId_Film() {
        return id_Film;
    }

    @JsonSetter("id_Film")
    public void setId_Film(int id_Film) {
        this.id_Film = id_Film;
    }

    public String getFilm_Title() {
        return film_Title;
    }

    @JsonSetter("film_Title")
    public void setFilm_Title(String film_Title) {
        this.film_Title = film_Title;
    }

    public String getPlot() {
        return plot;
    }

    @JsonSetter("plot")
    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @JsonSetter("posterPath")
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getRelease_Date() {
        return release_Date;
    }

    @JsonSetter("release_Date")
    public void setRelease_Date(String relase_Date) {
        this.release_Date = relase_Date;
    }

    public int getRuntime() {
        return runtime;
    }

    @JsonSetter("runtime")
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String[] getGenres() {
        return genres;
    }

    @JsonSetter("genres")
    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public List<Cast> getCast() {
        return cast;
    }

    @JsonSetter("cast")
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public int getCounter() {
        return this.counter;
    }
}
