package ai.luigi.frunzio;

/**
 * Created by vassilis on 16/09/15.
 */
public class TspEvaluator implements Evaluator<Long> {
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

    @Override
    public Long eval() {
        long value = 0;
        for (int i = 1; i < instance.size(); i++) {
            value += instance.getDistance(i - 1, i);
        }
        value += instance.getDistance(instance.size(), 0);
        return value;
    }
}
