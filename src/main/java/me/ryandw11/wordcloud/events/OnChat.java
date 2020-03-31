package main.java.me.ryandw11.wordcloud.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import main.java.me.ryandw11.wordcloud.Main;
import main.java.me.ryandw11.wordcloud.Utils;
import main.java.me.ryandw11.wordcloud.WordContainer;
import main.java.me.ryandw11.wordcloud.Words;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OnChat extends ListenerAdapter {
    private Main main;
    public OnChat(Main main){
        this.main = main;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent evt){
        if(evt.getChannelType() == ChannelType.PRIVATE) return;

        boolean created = false;
        File f = new File(Utils.getLocalPath() + File.separator + evt.getChannel().getId() + ".yml");
        if(!f.exists()) {
            try {
                f.createNewFile();
                created = true;
            } catch (IOException e) {
                System.out.println("Error: File creation failed!");
                return;
            }
        }

        String message = evt.getMessage().getContentStripped();
        if(message.startsWith("!")) return;

        if(!main.getChannelCache().containsKey(evt.getChannel().getIdLong()) && !created){
            loadData(f, evt.getChannel().getIdLong());
        }

        if(!main.getChannelCache().containsKey(evt.getChannel().getIdLong())){
            List<Words> words = new ArrayList<>();
            proccessWords(words, message);
            main.getChannelCache().put(evt.getChannel().getIdLong(), words);
        }
        else{
            List<Words> words = main.getChannelCache().get(evt.getChannel().getIdLong());
            // Hopefully this mutates the list.
            proccessWords(words, message);
        }

        main.setCacheChanged(true);
        // should be done!
    }

    private void loadData(File file, Long id){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        WordContainer container;
        try{
            container = mapper.readValue(file, WordContainer.class);
        }
        catch(IOException ex){
            System.out.println("Error: Cannot read file data.");
            return;
        }
        main.getChannelCache().put(id, container.getWords());
    }

    private void proccessWords(List<Words> words, String message){
        String[] strings = message.split(" ");
        for(String s : strings){
            if(s.equals("")) continue;
            Words word = new Words().init(s, 1);
            if(words.contains(word)){
                words.get(words.indexOf(word)).addUsage();
            }else{
                words.add(word);
            }
        }
    }
}
