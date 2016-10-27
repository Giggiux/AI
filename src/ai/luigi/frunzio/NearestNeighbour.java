package ai.luigi.frunzio;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by giggiux on 10/27/16.
 */
public class NearestNeighbour implements Solver {
    Instance instance;

    public NearestNeighbour(Instance instance) {

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
    public Solution getSolution() { return null; }

    public Solution getSolution(Integer startingNodeIndex) {
        Set<Integer> notYetVisited = IntStream.rangeClosed(0, instance.getDimension()-1).boxed().collect(Collectors.toSet());

        Solution ret = new Solution(instance);

        Integer staringNode = startingNodeIndex;

        Integer node = staringNode;
        ret.add(node);
        notYetVisited.remove(node);
        while (!notYetVisited.isEmpty()) {
            Integer nearestNeighbour = null;
            long nearestDistance = Long.MAX_VALUE;
            for (int j : notYetVisited) {
                if (nearestDistance > instance.getDistance(node, j)) {
                    nearestNeighbour = j;
                    nearestDistance = instance.getDistance(node, j);
                }
            }
            ret.add(nearestNeighbour);
            ret.addToCost(nearestDistance);
            notYetVisited.remove(nearestNeighbour);
            node = nearestNeighbour;
        }
        ret.addToCost(instance.getDistance(node,staringNode));
        return ret;
    }
}
