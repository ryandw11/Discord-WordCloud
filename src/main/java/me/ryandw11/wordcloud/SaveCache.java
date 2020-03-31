package main.java.me.ryandw11.wordcloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class SaveCache extends TimerTask {
    private Main main;
    public SaveCache(Main main){
        this.main = main;
    }
    @Override
    public void run() {
        if(!main.getCacheChanged()) return;
        if(main.getChannelCache().size() < 1) return;

        for (Map.Entry<Long, List<Words>> pair : main.getChannelCache().entrySet()) {
            Long id = pair.getKey();
            List<Words> infoToSave = pair.getValue();

            File f = new File(Utils.getLocalPath() + File.separator + id + ".yml");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    System.out.println("Error: Cannot save file from cache!");
                    return;
                }
            }


            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                mapper.writeValue(f, new WordContainer().init(infoToSave));
            } catch (IOException e) {
                System.out.println("Cannot write to data file!");
            }
        }
    }
}
