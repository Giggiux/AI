package ai.luigi.frunzio;

/**
 * Created by giggiux on 10/28/16.
 */
public class Timer {
    private static long startTime;

    static void start_timers()
    {
        startTime = System.currentTimeMillis();
    }

    static double elapsed_time()
    {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }
}