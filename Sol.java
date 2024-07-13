import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Sol {
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);
    public static final String END = "end"; 

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    queueA.put(generateText("abc", 100_000));
                    queueB.put(generateText("abc", 100_000));
                    queueC.put(generateText("abc", 100_000));
                } catch (InterruptedException e) {
                    return;
                }
            }
            try {
                queueA.put(END);
                queueB.put(END);
                queueC.put(END);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Текст с максимальным количеством букв a: " + get(queueA, 'a'));
                //get(queueA, 'a');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Текст с максимальным количеством букв b: " + get(queueB, 'b'));
                //get(queueB, 'b');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Текст с максимальным количеством букв c: " + get(queueC, 'c'));
                //get(queueC, 'c');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static String get(BlockingQueue<String> queue, char targetChar) throws InterruptedException {
        int maxCount = 0;
        String maxWord = "";
        while (true) {
            String word = queue.take();
            if (word.equals(END)) {
                queue.put(END);
                break;
            }
            int count = 0;
            for (char c : word.toCharArray()) {
                if (c == targetChar) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxWord = word;
            }
        }
        return maxWord;
    }
}