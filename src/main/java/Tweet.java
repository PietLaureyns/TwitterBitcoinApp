import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tweet {

    public String name;
    public Date date;
    public String text;
    private String location;
    private String language;

    public Tweet(String name, Date date, String text, String location, String language){
        this.name = name;
        this.date = date;
        this.text = text;
        this.location = location;
        this.language = language;
    }

    public String getLocation(){
        return this.location == null ? "Geen Locatie" : this.location;
    }

    public String getLanguage(){
        return this.language == null ? "Geen Taal" : this.language;
    }

    public int getWordCount(){
        return this.text.toLowerCase().split(" ").length;
    }

    public String getWordPairs(){
        String[] words = this.text.split(" ");
        List<Pair<String, Integer>> pairs = new ArrayList<>();

        for(String word : words){
            Boolean found = true;
            int index = 0;
            Pair<String,Integer> newPair = null;

            for(Pair<String,Integer> pair : pairs){
                outerloop:
                if(pair.getValue0().equals(word.toLowerCase())){
                    int count = pair.getValue1().intValue();
                    System.out.println(pair.getValue1());

                    index = pairs.indexOf(pair);
                    newPair = new Pair(word.toLowerCase(),2);
                    System.out.println("DUPLICATE");
                    found = false;
                    break outerloop;
                }
            }

            if (found) {
                pairs.add(new Pair(word.toLowerCase(),1));
            }else{
                pairs.remove(index);
                pairs.add(newPair);
            }
        }

        StringBuilder sb = new StringBuilder();

        pairs.forEach(pair -> sb.append(pair.getValue0()+": "+pair.getValue1() +"\n"));

        return sb.toString();
    }
}
