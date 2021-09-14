package component.films;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Cast {
    @JsonProperty("name")
    private String name;
    @JsonProperty("character")
    private String character;
    @JsonProperty("link")
    private String link;

    public Cast(@JsonProperty("name") String name, @JsonProperty("character") String character, @JsonProperty("link") String link) {
        this.name = name;
        this.character = character;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
