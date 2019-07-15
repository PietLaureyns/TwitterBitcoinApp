//import java.util.Date;
//
//public class RunnableThread implements Runnable {
//
//    private Thread t;
//    TwitterSubscriber twitterSubscriber;
//    BitcoinData bitcoinData;
//    private int time;
//
//    public RunnableThread(TwitterSubscriber twitterSubscriber, BitcoinData bitcoinData, int time){
//        this.twitterSubscriber = twitterSubscriber;
//        this.bitcoinData = bitcoinData;
//        this.time = time;
//    }
//
//    @Override
//    public void run() {
//        if(time==60000){
//            while(true){
//                bitcoinData.oneMinute();
//                try {
//                    Thread.sleep(time-1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                boolean start = false;
//                while(!start){
//                    if(new Date().getSeconds() == 0){
//                        start = true;
//                    }
//                }
//                twitterSubscriber.oneMinuteReset();
//            }
//        }
//        if(time==300000){
//            while(true){
//                bitcoinData.fiveMinute();
//                try {
//                    Thread.sleep(time-1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                boolean start = false;
//                while(!start){
//                    if(new Date().getSeconds() == 0){
//                        start = true;
//                    }
//                }
//                twitterSubscriber.fiveMinuteReset();
//            }
//        }
//        if(time==600000){
//            while(true){
//                bitcoinData.tenMinute();
//                try {
//                    Thread.sleep(time-1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                boolean start = false;
//                while(!start){
//                    if(new Date().getSeconds() == 0){
//                        start = true;
//                    }
//                }
//                twitterSubscriber.tenMinuteReset();
//            }
//        }
//    }
//
//    public void start() {
//        System.out.println("Starting RunnableThread for "+time/1000 +" seconds.");
//        if (t == null) {
//            t = new Thread (this, "RunnableThread"+time);
//            t.start();
//        }
//    }
//}