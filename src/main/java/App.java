import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {
    private JPanel mainPanel;
    private JLabel aantalTweetsInteger;
    private JLabel timeStartedLabel;
    private JList locationsList;
    private JButton stopButton;
    private JList languageList;
    private JLabel averageTweets;
    private JLabel aantal5minTweets;
    private JList twitter1minValues;
    private JList bitcoin1minValues;
    private JLabel aantal1minTweets;
    private JLabel aantal10minTweets;
    private JLabel aantal1HourTweets;
    private JList twitter10minValues;
    private JList bitcoin10minValues;
    private JList twitter1hourValues;
    private JList bitcoin1hourValues;
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM | HH:mm:ss");

    public static void main(String[] args){
        Main main = new Main();
        App app = new App();
        JFrame frame = new JFrame("App");
        frame.setContentPane(app.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1000,1000);

        frame.setVisible(true);

        app.timeStartedLabel.setText("Starttijd: "+format.format(new Date()));

        app.stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Stop Button Clicked");
                main.stop();
            }
        });

        main.start(app);
    }

    public void updateStartTime(Date startdate){
        timeStartedLabel.setText("Starttijd: "+format.format(startdate));
    }

    public void updateAantalTweets(int aantal){
        this.aantalTweetsInteger.setText("Aantal Tweets: "+aantal);
    }

//    public void updateLocationList(String[] locations){
//       this.locationsList.setListData(locations);
//    }
//
//    public void updateLanguageList(String[] languages){
//       this.languageList.setListData(languages);
//    }

    public void writeAverage(Double average){this.averageTweets.setText("Average Tweets per Minute: "+average);
    }

    public void writeTwitter1MinValues(TreeMap<Date, Integer> twitterValues) {
        List<String> values = new ArrayList<>();
        twitterValues.entrySet().forEach(item -> values.add(format.format(item.getKey())+"   -   "+item.getValue()));
        Collections.reverse(values);
        this.twitter1minValues.setListData(values.toArray());
    }
    public void writeTwitter10MinValues(TreeMap<Date, Integer> twitterValues) {
        List<String> values = new ArrayList<>();
        twitterValues.entrySet().forEach(item -> values.add(format.format(item.getKey())+"   -   "+item.getValue()));
        Collections.reverse(values);
        this.twitter10minValues.setListData(values.toArray());
    }
    public void writeTwitter1HourValues(TreeMap<Date, Integer> twitterValues) {
        List<String> values = new ArrayList<>();
        twitterValues.entrySet().forEach(item -> values.add(format.format(item.getKey())+"   -   "+item.getValue()));
        Collections.reverse(values);
        this.twitter1hourValues.setListData(values.toArray());
    }

    //private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void writeBitcoin1MinValues(TreeMap<Date, Double> bitcoinValues) {
        List<String> values = new ArrayList<>();
        bitcoinValues.entrySet().forEach(item -> values.add(format.format(item.getKey())+"   -   "+item.getValue()));
        Collections.reverse(values);
        this.bitcoin1minValues.setListData(values.toArray());
    }

    public void writeBitcoin10MinValues(TreeMap<Date, Double> bitcoinValues) {
        List<String> values = new ArrayList<>();
        bitcoinValues.entrySet().forEach(item -> values.add(format.format(item.getKey())+"   -   "+item.getValue()));
        Collections.reverse(values);
        this.bitcoin10minValues.setListData(values.toArray());
    }

    public void writeBitcoin1HourValues(TreeMap<Date, Double> bitcoinValues) {
        List<String> values = new ArrayList<>();
        bitcoinValues.entrySet().forEach(item -> values.add(format.format(item.getKey())+"   -   "+item.getValue()));
        Collections.reverse(values);
        this.bitcoin1hourValues.setListData(values.toArray());
    }

    public void updateAantal1minTweets(Integer aantal){
        this.aantal1minTweets.setText("Aantal 1min tweets: "+aantal);
    }

    public void updateAantal10minTweets(Integer aantal){this.aantal10minTweets.setText("Aantal 10min tweets: "+aantal);}

    public void updateAantal1HourTweets(Integer aantal){this.aantal1HourTweets.setText("Aantal 1 uur tweets: "+aantal);}

}

