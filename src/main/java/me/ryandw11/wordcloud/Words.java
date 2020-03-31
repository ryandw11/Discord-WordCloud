package main.java.me.ryandw11.wordcloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class Words implements Comparable<Words>{

    @JsonProperty
    private String word;
    @JsonProperty
    private int usageCount;

    public Words init(String word, int usageCount){
        this.word = word;
        this.usageCount = usageCount;
        return this;
    }

    public String getWord(){
        return word;
    }

    public int getUsageCount(){
        return usageCount;
    }

    public void addUsage(){
        usageCount += 1;
    }

    @Override
    public int compareTo(@NotNull Words o) {
        return -getUsageCount() + o.getUsageCount();
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Words)) return false;
        return getWord().equals(((Words) o).getWord());
    }

    @Override
    public String toString(){
        return "{'" + getWord() + "', " + getUsageCount() + "}";
    }
}
