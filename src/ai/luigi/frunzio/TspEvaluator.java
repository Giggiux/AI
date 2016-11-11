package ai.luigi.frunzio;

/**
 * Created by vassilis on 16/09/15.
 */
public class TspEvaluator {
    Instance instance;
    Solution solution;

    public TspEvaluator(Instance instance, Solution solution) {
        this.instance = instance;
        this.solution = solution;
    }

    public TspEvaluator(Instance instance) {
        this.instance = instance;
    }

    public Solution getSolution() {

        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public Instance getInstance() {

        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    static public Long eval(int[] tour) {
        long sum2 = 0;
        for (int i = 0; i< tour.length -1; i++) {
            sum2 += Main.instance.getDistance(tour[i],tour[i+1]);
        }
        return sum2;
    }
}
