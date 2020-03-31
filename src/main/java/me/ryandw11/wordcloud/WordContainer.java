package main.java.me.ryandw11.wordcloud;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WordContainer {
    @JsonProperty
    private List<Words> words;

    public WordContainer init(List<Words> words){
        this.words = words;
        return this;
    }

    public List<Words> getWords(){
        return this.words;
    }
}
