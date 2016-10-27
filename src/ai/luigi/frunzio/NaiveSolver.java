package ai.luigi.frunzio;

/**
 * Created by vassilis on 16/09/15.
 */
public class NaiveSolver implements Solver {
    Instance instance;

    public NaiveSolver(Instance instance) {

        this.instance = instance;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    /**
     *
     * @return Objective Value
     */
    @Override
    public Solution getSolution() {
        Solution ret = new Solution(instance);
        for (int i = 0; i < instance.getDimension(); i++) {
            ret.add(i);
        }
        return ret;
    }
}
