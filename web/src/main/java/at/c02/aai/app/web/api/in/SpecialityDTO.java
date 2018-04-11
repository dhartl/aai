package at.c02.aai.app.web.api.in;

import java.util.List;

public class SpecialityDTO {
    private String name;
    private List<String> synonymes;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<String> getSynonymes() {
	return synonymes;
    }

    public void setSynonymes(List<String> synonymes) {
	this.synonymes = synonymes;
    }

}
