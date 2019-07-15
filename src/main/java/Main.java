import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    private final static String CONSUMER_KEY = "Th1uGQV9i80IrYDhgpKamT5Q3";
    private final static String CONSUMER_KEY_SECRET = "No4urSpNsnkgfNYYuMGqrwm30s6MPZaLN7Dl2yiI5ywa4R0nK6";
    private final static String ACCESS_TOKEN = "1683608778-kzWasrwQKXXDW7H9pIQ2qCHucVBjjf6rl8MWkSr";
    private final static String ACCESS_TOKEN_SECRET = "L0hQTuc59UfQJPFG1Yn7aXvVYlpEDg2GnKrk1fwDtmevW";

    private App app;

    private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy__HH-mm-ss_");

    private BitcoinData bitcoinData;
    private TwitterSubscriber twitterSubscriber;

    public void start(App app) {
        this.app = app;
        bitcoinData = new BitcoinData(this.app);

        twitterSubscriber = new TwitterSubscriber(this.app);
        TwitterStreamClass twitterStreamClass = new TwitterStreamClass(twitterSubscriber);
        twitterStreamClass.twitterStream(twitterStreamClass.getTwitterStreamInstance());


        //https://www.rgagnon.com/javadetails/java-0085.html
        System.setProperty("http.proxyHost", "webcache.mydomain.com");
        System.setProperty("http.proxyPort", "8080");

        boolean start = false;
        while (!start) {
            if (new Date().getSeconds() == 0) {
                start = true;
            }
        }

        System.out.println("Start: " + dateFormat.format(new Date()));
        app.updateStartTime(new Date());
        twitterSubscriber.start();
        bitcoinData.start();

//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            public void run() {
//                System.out.println("Running Shutdown Hook");
//                bitcoinData.stop();
//                twitterSubscriber.stop();
//            }
//        });

        while (true) {
            new Thread(() -> {
                bitcoinData.resetOneMinute();
            }).start();

            try {
                Thread.sleep(59000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean start2 = false;
            while (!start2) {
                if (new Date().getSeconds() == 0) {
                    start2 = true;
                }
            }
            new Thread(() -> {
                twitterSubscriber.resetOneMinute();
            }).start();
        }
    }

    public void stop() {
        bitcoinData.stop();
        twitterSubscriber.stop();

        try {
            Thread.sleep(10000); //10 seconden buffer om alles tijd te geven om correct af te sluiten.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
