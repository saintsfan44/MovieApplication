package com.example.movieapplication.Model;

import java.util.List;

public class PersonResponseResults {
    private Double popularity;
    private Integer id;
    private String profile_path;
    private String name;
    private List<PersonResponseResultKnownFor> knownFor;
    private boolean adult;

    public PersonResponseResults() {
    }

    public PersonResponseResults(Double popularity, Integer id, String profile_path, String name, List<PersonResponseResultKnownFor> knownFor, boolean adult) {
        this.popularity = popularity;
        this.id = id;
        this.profile_path = profile_path;
        this.name = name;
        this.knownFor = knownFor;
        this.adult = adult;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PersonResponseResultKnownFor> getKnownFor() {
        return knownFor;
    }

    public void setKnownFor(List<PersonResponseResultKnownFor> knownFor) {
        this.knownFor = knownFor;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }
}
