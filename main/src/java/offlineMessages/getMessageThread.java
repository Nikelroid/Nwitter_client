package offlineMessages;

public class getMessageThread extends Thread{
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                new sendMessagesToServer();
            }catch (Exception ignored){
            }
        }
    }

}
