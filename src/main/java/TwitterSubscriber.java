import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Flow;

import static java.util.stream.Collectors.toMap;

public class TwitterSubscriber implements Flow.Subscriber<Tweet> {

    public Flow.Subscription subscription;
    private String fileName;
    private Date startDate = new Date();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy__HH-mm-ss_");
    private App app;

    private int tenMinuteTeller = 0;
    private int oneMinuteTeller = 0;
    private int oneHourTeller = 0;

    private int totalCounter = 0;
    private int tenMinCounter = 0;
    private int hourCounter = 0;

//    private Date tenMinuteDate;
//    private Date oneHourDate;

    private HashMap<String, Integer> hmapLocation = new HashMap<>();
    private HashMap<String, Integer> hmapLanguage = new HashMap<>();
    private TreeMap<Date, Integer> oneMinuteValues = new TreeMap<>();
    private TreeMap<Date, Integer> tenMinuteValues = new TreeMap<>();
    private TreeMap<Date, Integer> oneHourValues = new TreeMap<>();
    private TreeMap<Date, Integer> oneMinuteValuesTemp = new TreeMap<>();
    private TreeMap<Date, Integer> tenMinuteValuesTemp = new TreeMap<>();
    private TreeMap<Date, Integer> oneHourValuesTemp = new TreeMap<>();


    public TwitterSubscriber(App app) {
        this.app = app;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Tweet tweet) {
        this.addLocation(tweet.getLocation());
        this.addLanguage(tweet.getLanguage());
        this.oneMinuteTeller++;
        this.totalCounter++;
        app.updateAantalTweets(this.totalCounter);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Done");
    }

    private void addLocation(String location) {
        if (hmapLocation.containsKey(location)) {
            hmapLocation.put(location, hmapLocation.get(location) + 1);
        } else {
            hmapLocation.put(location, 1);
        }
    }

    private void addLanguage(String language) {
        if (hmapLanguage.containsKey(language)) {
            hmapLanguage.put(language, hmapLanguage.get(language) + 1);
        } else {
            hmapLanguage.put(language, 1);
        }
    }

    private void printLocation() {
        Map<String, Integer> sorted = hmapLocation
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                        LinkedHashMap::new));

        int teller = 1;
        List<String> locationList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            locationList.add(teller + ". " + entry.getKey() + " (" + entry.getValue() + ")");
            teller++;
        }
        //this.app.updateLocationList(locationList.toArray(new String[0]));
    }

    private void printLanguage() {
        Map<String, Integer> sorted = hmapLanguage
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                        LinkedHashMap::new));

        int teller = 1;
        List<String> languageList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            languageList.add(teller + ". " + entry.getKey() + " (" + entry.getValue() + ")");
            teller++;
        }
        //this.app.updateLanguageList(languageList.toArray(new String[0]));
    }

    public void resetOneMinute() {
        tenMinCounter++;
        tenMinuteTeller += oneMinuteTeller;

        Date date = new Date(System.currentTimeMillis() - 60000);

        oneMinuteValues.put(date, oneMinuteTeller);
        oneMinuteValuesTemp.put(date, oneMinuteTeller);
        oneMinuteTeller = 0;

        if (tenMinCounter >= 10) {
            Date date2 = new Date(System.currentTimeMillis() - 600000);
            oneHourTeller += tenMinuteTeller;
            hourCounter++;
            if(hourCounter >= 6){
                oneHour();
            }
            tenMinuteValues.put(date2, tenMinuteTeller);
            tenMinuteValuesTemp.put(date2, tenMinuteTeller);
            app.updateAantal10minTweets(tenMinuteValues.size());
            app.writeTwitter10MinValues(tenMinuteValues);
            tenMinCounter = 0;
            tenMinuteTeller = 0;
        }

        app.updateAantal1minTweets(oneMinuteValues.size());
        app.writeTwitter1MinValues(oneMinuteValues);
        app.writeAverage(oneMinuteValues.values().stream().mapToDouble(a -> a).average().getAsDouble());
//        printLocation();
//        printLanguage();
    }

    private void oneHour(){
        Date date = new Date(System.currentTimeMillis() - 3600000);

        oneHourValues.put(date, oneHourTeller);
        oneHourValuesTemp.put(date, oneHourTeller);
        app.updateAantal1HourTweets(oneHourValues.size());
        hourCounter = 0;
        oneHourTeller = 0;
        app.writeTwitter1HourValues(oneHourValues);
        writeToCsvOneHour();
    }

    public void start() {
        this.startDate = new Date();
        String startTime = dateFormat2.format(startDate);
        this.fileName = startTime + "Twitter";
        this.hmapLocation = new HashMap<>();
        this.hmapLanguage = new HashMap<>();
        this.totalCounter = 0;
        this.tenMinuteTeller = 0;
        this.oneMinuteTeller = 0;
        this.oneHourTeller = 0;
//        this.tenMinuteDate = startDate;
//        this.oneHourDate = startDate;
    }

    public void writeToCsvOneHour(){
        writeToCsv(oneMinuteValuesTemp, "_1min2");
        writeToCsv(tenMinuteValuesTemp, "_10min2");
        writeToCsv(oneHourValuesTemp, "_1uur2");
        oneMinuteValuesTemp = new TreeMap<>();
        tenMinuteValuesTemp = new TreeMap<>();
        oneHourValuesTemp = new TreeMap<>();
    }

    public void stop() {
        writeToCsv(tenMinuteValues, "_10min");
        writeToCsv(oneMinuteValues, "_1min");
        writeToCsv(oneHourValues, "_1uur");
        writeHmapToCsv(hmapLocation, "_Locations");
        writeHmapToCsv(hmapLanguage, "_Languages");
        writeSummaryToCsv("_Summary");
    }

    public void writeToCsv(TreeMap<Date, Integer> values, String name) {
        if (!values.isEmpty()) {
            FileWriter fw;
            try {
                fw = new FileWriter(fileName + name + ".txt", true);
                for (Map.Entry<Date, Integer> entry : values.entrySet()) {
                    fw.write(dateFormat.format(entry.getKey()) + ";" + entry.getValue() + "\n");
                }
                fw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(name + ": File Not Found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(name + ": No Data to write.");
        }
    }

    public void writeSummaryToCsv(String name) {
        FileWriter fw;
        try {
            fw = new FileWriter(fileName + name + ".txt", true);
            fw.write("Starttijd: " + dateFormat.format(startDate) + "\n");
            fw.write("Eindtijd: " + dateFormat.format(new Date()) + "\n");
            long diffInMillieseconds = new Date().getTime() - startDate.getTime();
            double seconds = (diffInMillieseconds / 1000);
            double minutes = seconds / 60;
            double hours = minutes / 60;

            DecimalFormat df = new DecimalFormat("0.000");

            fw.write("Totaal RunTime: " +  df.format(hours) + " uur of " + df.format(minutes) + " minuten of " + df.format(seconds) + " seconden \n\n");
            fw.write("Totaal aantal tweets: " + this.totalCounter + "\n\n");

            fw.write("Aantal 1 minuut waarden: " + this.oneMinuteValues.size() + "\n");
            fw.write("Aantal 10 minuten waarden: " + this.tenMinuteValues.size() + "\n");
            fw.write("Aantal 60 minuten waarden: " + this.oneHourValues.size() + "\n\n");

            fw.write("Gemiddelde 1 minuut waarde: " + df.format(this.oneMinuteValues.values().stream().mapToDouble(a -> a).average().getAsDouble()) + "\n");
            fw.write("Gemiddelde 10 minuut waarde: " + df.format(this.tenMinuteValues.values().stream().mapToDouble(a -> a).average().getAsDouble()) + "\n");
            fw.write("Gemiddelde 60 minuut waarde: " + df.format(this.oneHourValues.values().stream().mapToDouble(a -> a).average().getAsDouble()) + "\n\n");

            fw.write("Aantal verschillende locaties: " + this.hmapLocation.size() + "\n");
            fw.write("Aantal verschillende Talen: " + this.hmapLanguage.size() + "\n");

            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(name + ": File Not Found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHmapToCsv(HashMap<String, Integer> hmap, String name) {
        if (!hmap.isEmpty()) {
            Map<String, Integer> sorted = hmap
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                            LinkedHashMap::new));
            FileWriter fw;
            try {
                fw = new FileWriter(fileName + name + ".txt", true);
                int amount = 0;
                for(int number : hmap.values()){
                    amount += number;
                }
                fw.write("Total Amount: "+amount +"\n");
                for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
                    fw.write(entry.getValue() + ";" + entry.getKey() + "\n");
                }
                fw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println(name + ": File Not Found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(name + ": No Data to write.");
        }
    }
}
