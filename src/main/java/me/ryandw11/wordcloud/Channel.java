package main.java.me.ryandw11.wordcloud;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private List<Words> words = new ArrayList();

    public List<Words> getWords() {
        return words;
    }

    public void setWords(List<Words> words){
        this.words = words;
    }

    public void addWord(Words word){
        this.words.add(word);
    }
}
