package main.java.me.ryandw11.wordcloud.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import main.java.me.ryandw11.wordcloud.Main;
import main.java.me.ryandw11.wordcloud.Utils;
import main.java.me.ryandw11.wordcloud.WordContainer;
import main.java.me.ryandw11.wordcloud.Words;
import me.ryandw11.jdacommand.Command;
import me.ryandw11.jdacommand.JDACommand;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class WordCloud extends ListenerAdapter {
    private Main main;
    public WordCloud(Main main){
        this.main = main;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent evt){
        if(evt.getChannel().getType() == ChannelType.PRIVATE) return;
        String message = evt.getMessage().getContentStripped();
        if(!message.startsWith("!")) return;
        if(message.split(" ").length < 1) return;
        if(!message.split(" ")[0].equalsIgnoreCase("!wc") && !message.split(" ")[0].equalsIgnoreCase("!wordcloud")) return;

        if(main.getChannelCache() == null || !main.getChannelCache().containsKey(evt.getChannel().getIdLong())){
            loadData(new File(Utils.getLocalPath() + File.separator + evt.getChannel().getIdLong() + ".yml"), evt.getChannel().getIdLong());
        }

        if(main.getChannelCache() == null || !main.getChannelCache().containsKey(evt.getChannel().getIdLong())){
            evt.getChannel().sendMessage("There is no data for this channel!").queue();
            return;
        }


        List<Words> words = main.getChannelCache().get(evt.getMessage().getChannel().getIdLong())
                .stream().sorted().collect(Collectors.toList());
        if(words.size() < 1) return;
        int max = words.get(0).getUsageCount();
        int min = words.size() > 20 ? words.get(20).getUsageCount() : words.get(words.size() - 1).getUsageCount();

        BufferedImage bi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bi.createGraphics();
        graphics.setBackground(new Color(0, 0, 0, 1));
        int limit = 0;
        for(Words word : words){
            if(limit > 20) break;
            AffineTransform original = graphics.getTransform();
            graphics.setFont(new Font("Arial", Font.PLAIN, (int) Math.round(normalize(word.getUsageCount(), max, min) * 50)));
            float x = (float) Math.random() * 200;
            float y = (float) Math.random() * 200;
            graphics.rotate(Math.toRadians(Math.random() * 360), x,y);
            graphics.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1));
            graphics.drawString(word.getWord(), x, y);
            graphics.setTransform(original);
            limit++;
        }

        File tempFile = new File(Utils.getLocalPath() + File.separator + "tmp.png");
        try {
            ImageIO.write(bi, "png", tempFile);
        }
        catch(IOException ex){
             System.out.println("Error: Could not save temporary image file.");
             evt.getMessage().getChannel().sendMessage("A critical error has occurred! Check console for more information!").queue();
             graphics.dispose();
             return;
        }

        evt.getMessage().getChannel().sendFile(tempFile).queue();
        graphics.dispose();
    }

    private double normalize(int value, int max, int min){
        if(max == min) return 1;
        double val = (double) (value - min)/(max-min);
        return val == 0.0 ? 0.3 : val  == 1 ? val : val * 1.5;
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
}
