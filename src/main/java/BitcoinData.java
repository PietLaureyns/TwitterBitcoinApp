import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BitcoinData {

    private String fileName;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy__HH-mm-ss_");
    private App app;
    private TreeMap<Date, Double> tenMinuteValues = new TreeMap<>();
    private TreeMap<Date, Double> oneMinuteValues = new TreeMap<>();
    private TreeMap<Date, Double> oneHourValues = new TreeMap<>();
    private TreeMap<Date, Double> oneMinuteValuesTemp = new TreeMap<>();
    private TreeMap<Date, Double> tenMinuteValuesTemp = new TreeMap<>();
    private TreeMap<Date, Double> oneHourValuesTemp = new TreeMap<>();
    private int teller10 = 10;
    private int hourTeller = 6;

    public BitcoinData(App app) {
        this.app = app;
    }

    public void stop() {
        tenMinuteValues.pollLastEntry();
        oneMinuteValues.pollLastEntry();
        oneHourValues.pollLastEntry();
        this.writeToCsv(tenMinuteValues, "_10min");
        this.writeToCsv(oneMinuteValues, "_1min");
        this.writeToCsv(oneHourValues, "_1uur");
    }

    public void writeToCsvOneHour() {
        this.writeToCsv(oneMinuteValuesTemp, "_1min2");
        this.writeToCsv(tenMinuteValuesTemp, "_10min2");
        this.writeToCsv(oneHourValuesTemp, "_1uur2");

        oneMinuteValuesTemp = new TreeMap<>();
        tenMinuteValuesTemp = new TreeMap<>();
        oneHourValuesTemp = new TreeMap<>();
    }

    public void start() {
        String startTime = dateFormat2.format(new Date());
        this.fileName = startTime+"Bitcoin";
    }

    public JsonObject getJsonArrayFromUrl(String url){
        String json = readUrl(url);
        JsonParser parser = new JsonParser();
        try{
            return (JsonObject) parser.parse(json);
        } catch(Exception e){
            return null;
        }
    }

    private String readUrl(String urlString){
        BufferedReader reader = null;
        try {
            InputStream inputStream = new URL(urlString).openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuffer buffer = new StringBuffer();

            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            inputStream.close();

            return buffer.toString();
        } catch (NullPointerException e) {
            System.out.println("Nullpointer Exception");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getBitcoinValue() {
        JsonObject item = getJsonArrayFromUrl("https://api.coindesk.com/v1/bpi/currentprice.json");
        Double bitcoinValue;
        if (item != null) {
            bitcoinValue = item.get("bpi").getAsJsonObject().get("EUR").getAsJsonObject().get("rate_float").getAsDouble();
        } else {
            System.out.println(dateFormat.format(new Date())+"JsonObject was null");
            bitcoinValue = this.oneMinuteValues.lastEntry().getValue();
        }

        return bitcoinValue;
    }

    public void resetOneMinute() {
        Double bitcoinValue = getBitcoinValue();
        Date date = new Date();
        teller10++;
        oneMinuteValues.put(date, bitcoinValue);
        oneMinuteValuesTemp.put(date, bitcoinValue);

        if(teller10 >= 10){
            hourTeller++;
            tenMinuteValues.put(date, bitcoinValue);
            tenMinuteValuesTemp.put(date, bitcoinValue);
            app.writeBitcoin10MinValues(tenMinuteValues);
            teller10 = 0;

            if(hourTeller >= 6){
                oneHourValues.put(date, bitcoinValue);
                oneHourValuesTemp.put(date, bitcoinValue);
                app.writeBitcoin1HourValues(oneHourValues);
                hourTeller = 0;
                writeToCsvOneHour();
            }
        }

        app.writeBitcoin1MinValues(oneMinuteValues);
    }

    private void writeToCsv(TreeMap<Date, Double> values, String name) {
        if (!values.isEmpty()) {
            FileWriter fw;
            try {
                fw = new FileWriter(fileName + name + ".txt", true);
                for (Map.Entry<Date, Double> entry : values.entrySet()) {
                    fw.write(dateFormat.format(entry.getKey()) + ";" + entry.getValue() + "\n");
                }
                fw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("File Not Found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No Data to write.");
        }
    }
}
