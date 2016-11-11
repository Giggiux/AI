package ai.luigi.frunzio;

import java.util.*;

/**
 * Created by vassilis on 16/09/15.
 */
public class Solution implements Iterable {
    List<Integer> indices;
    long cost;
    Instance inst;

    public Solution() {
        indices = new ArrayList<>();
    }

    public Solution(Instance inst) {
        this.inst = inst;
        indices = new ArrayList<>(inst.getDimension());
        ((ArrayList) (indices)).ensureCapacity(inst.getDimension());
    }

    public void add(Integer integer) {
        indices.add(integer);
    }

    @Override
    public Iterator iterator() {
        return indices.iterator();
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public void addToCost(long value) { this.cost += value; }

    public Instance getInst() {
        return inst;
    }

    public void setInst(Instance inst) {
        this.inst = inst;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Solution solution = (Solution) o;

        if (!getIndices().equals(solution.getIndices())) return false;
        return getInst().equals(solution.getInst());

    }

    @Override
    public int hashCode() {
        int result = getIndices().hashCode();
        result = 31 * result + getInst().hashCode();
        return result;
    }


    @Override
    public String toString() {
        String sol = "";
        String delim = "";
        for (int index : indices) {
            sol = sol + delim + String.valueOf(index+1);
            delim = " ";
        }
        return sol;
    }

    public void set(int index, Integer val) {
        indices.set(index, val);
    }
}
