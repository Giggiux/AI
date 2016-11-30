package ai.luigi.frunzio;

/**
 * Created by vassilis on 16/09/15.
 */
public class TspEvaluator {
    static public Long eval(int[] tour) {
        long sum2 = 0;
        for (int i = 0; i< tour.length -1; i++) {
            sum2 += Main.instance.getDistance(tour[i],tour[i+1]);
        }
        return sum2;
    }
}
