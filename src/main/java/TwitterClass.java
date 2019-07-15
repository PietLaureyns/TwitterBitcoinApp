import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TwitterClass {

    private final static String CONSUMER_KEY = "Th1uGQV9i80IrYDhgpKamT5Q3";
    private final static String CONSUMER_KEY_SECRET = "No4urSpNsnkgfNYYuMGqrwm30s6MPZaLN7Dl2yiI5ywa4R0nK6";
    private final static String ACCESS_TOKEN= "1683608778-kzWasrwQKXXDW7H9pIQ2qCHucVBjjf6rl8MWkSr";
    private final static String ACCESS_TOKEN_SECRET = "L0hQTuc59UfQJPFG1Yn7aXvVYlpEDg2GnKrk1fwDtmevW";

    public static Twitter getTwitterInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_KEY_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }


    public static void showHomeTimeline(Twitter twitter) {
        List<Status> statuses = null;
        try {
            statuses = twitter.getHomeTimeline();

            System.out.println("Showing home timeline.");

            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":" + status.getText());
                String url= "https://twitter.com/" + status.getUser().getScreenName() + "/status/"
                        + status.getId();
                System.out.println("Above tweet URL : " + url);
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public static void showTweetsOfLocation(Twitter twitter){
        try {
            Query query = new Query();
            query.geoCode(new GeoLocation(50.8503396,4.3517103),1000.0,"mi");
            QueryResult result;
            System.out.println("Searching...");
            int Count=0;

            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    if(tweet.getGeoLocation()!=null)
                        System.out.println(tweet.getGeoLocation() + tweet.getText());
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            while ((query = result.nextQuery()) != null);
            System.out.println(Count);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }

//    public static void test(Twitter twitter){
//        FilterQuery tweetFilterQuery = new FilterQuery(); // See
//        tweetFilterQuery.track(new String[]{"Bieber", "Teletubbies"}); // OR on keywords
//        tweetFilterQuery.locations(new double[][]{new double[]{-126.562500,30.448674},
//                new double[]{-61.171875,44.087585
//                }}); // See https://dev.twitter.com/docs/streaming-apis/parameters#locations for proper location doc.
////Note that not all tweets have location metadata set.
//        tweetFilterQuery.language(new String[]{"en"}); // Note that language does not work properly on Norwegian tweets
//    }
}
