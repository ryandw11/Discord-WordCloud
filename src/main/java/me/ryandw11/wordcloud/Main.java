package main.java.me.ryandw11.wordcloud;

import main.java.me.ryandw11.wordcloud.commands.WordCloud;
import main.java.me.ryandw11.wordcloud.events.OnChat;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class Main {

    private Map<Long, List<Words>> channelCache = new HashMap<>();
    private JDA jda;
    private boolean cacheChanged = false;

    public static void main(String[] args){
        // This needs to be your discord bot key
        String key = "key";

        Main main = new Main();

        JDA jda;
        try{
            jda = new JDABuilder(key).build();
        }catch(LoginException ex){
            System.out.println("Error: Cannot login to bot.");
            return;
        }

        main.jda = jda;

        jda.addEventListener(new WordCloud(main));
        jda.addEventListener(new OnChat(main));

        Timer timer = new Timer();
        timer.schedule(new SaveCache(main), 0, 5000);
    }

    public Map<Long, List<Words>> getChannelCache(){
        return channelCache;
    }

    public JDA getJDA(){
        return jda;
    }

    public boolean getCacheChanged(){
        return cacheChanged;
    }

    public void setCacheChanged(boolean value){
        this.cacheChanged = value;
    }
}