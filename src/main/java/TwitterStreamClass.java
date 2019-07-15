import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.concurrent.SubmissionPublisher;

//https://www.coindesk.com/api -> Every minute

public class TwitterStreamClass {

    public static SubmissionPublisher publisher;

    private final static String CONSUMER_KEY = "Th1uGQV9i80IrYDhgpKamT5Q3";
    private final static String CONSUMER_KEY_SECRET = "No4urSpNsnkgfNYYuMGqrwm30s6MPZaLN7Dl2yiI5ywa4R0nK6";
    private final static String ACCESS_TOKEN= "1683608778-kzWasrwQKXXDW7H9pIQ2qCHucVBjjf6rl8MWkSr";
    private final static String ACCESS_TOKEN_SECRET = "L0hQTuc59UfQJPFG1Yn7aXvVYlpEDg2GnKrk1fwDtmevW";

    public TwitterStreamClass(TwitterSubscriber twitterSubscriber){
        this.publisher = new SubmissionPublisher();
        publisher.subscribe(twitterSubscriber);
    }

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

    public static TwitterStream getTwitterStreamInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_KEY_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

        return  new TwitterStreamFactory(cb.build()).getInstance();
    }

    public static void twitterStream(TwitterStream twitterStream){

        StatusListener listener = new StatusListener() {

            public void onStatus(Status status) {
                publisher.submit(new Tweet(status.getUser().getScreenName(),  status.getCreatedAt(), status.getText(), status.getUser().getLocation(), status.getLang()));
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            public void onStallWarning(StallWarning stallWarning) {

            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        double[][] location ={ { 40.714623d, -74.006605d },
                { 42.3583d, -71.0603d } };


        FilterQuery fq = new FilterQuery();
        String keywords[] = {"Bitcoin", "bitcoin", "BTC", "btc"};
        //fq.locations(location);
        fq.track(keywords);
        //fq.language("en");

        twitterStream.addListener(listener);
        twitterStream.filter(fq);
    }

}
