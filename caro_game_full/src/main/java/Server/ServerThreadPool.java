package Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThreadPool {
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void submit(Runnable task) {
        pool.submit(task);
    }

    public static void shutdown() {
        pool.shutdown();
    }
}
