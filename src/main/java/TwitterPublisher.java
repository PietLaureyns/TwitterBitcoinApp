import java.util.concurrent.Flow;

public class TwitterPublisher implements Flow.Publisher {

    public TwitterSubscriber tweetSubscriber;

    public void subscribe(Flow.Subscriber subscriber) {
        this.tweetSubscriber = (TwitterSubscriber) subscriber;
    }

//    public void emit(Tweet tweet){
//        this.tweetSubscriber.subscription.
//    }
}
