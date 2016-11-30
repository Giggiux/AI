package ai.luigi.frunzio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vassilis on 15/09/15.
 */
public class Instance {

    List<Node> nodes = new ArrayList<>(70);
    private String filename;
    private int dimension;
    private long[][] distanceMap;

    int[][] nodeDistanceIndexMap;

    public Instance(String filename) {

        this.filename = filename;
    }

    public long[][] getDistanceMap() {
        return distanceMap;
    }

    public int size() {
        return dimension;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void read() throws Exception {
        try {
            BufferedReader reader =
                    Files.newBufferedReader(FileSystems.getDefault().getPath(filename),
                            Charset.defaultCharset());
            String line;
            State state = State.DESCRIPTION;
            while ((line = reader.readLine()) != null) {
                switch (state) {
                    case DESCRIPTION:
                        if (line.trim().equals("NODE_COORD_SECTION")) {
                            state = State.COORDS;
                        }
                        break;
                    case COORDS:
                        if (!line.trim().equals("EOF")) {
                            dimension++;
                            // Parse the coords
                            String tokens[] = line.split(" ");
                            if (tokens.length != 3) {
                                throw new Exception("Wrong number of tokens");
                            }
                            nodes.add(new Node(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));

                        }
                        break;
                }
            }
            Main.cities = dimension;
            createDistanceMap();
            Ants.init_ants();
            createNearestNeighborList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getDistance(int i, int j) {
        return distanceMap[i][j];
    }

    private long distance(Node n1, Node n2) {
        double dx = n1.x - n2.x;
        double dy = n1.y - n2.y;
        return Math.round(Math.sqrt(dx * dx + dy * dy));
    }

    private void createDistanceMap() {
        distanceMap = new long[dimension][dimension];
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                distanceMap[i][j] = distanceMap[j][i] = distance(nodes.get(i), nodes.get(j));
            }
        }

    }

    private void createNearestNeighborList() {
        long[] nodeDistanceMap = new long[dimension];
        int[] nodeDistanceIndexMap = new int[dimension];
        int numberOfNighbors = Ants.nn_ants;
        this.nodeDistanceIndexMap = new int[dimension][numberOfNighbors];

        for (int i = 0; i < dimension ; i++) { /* compute cnd-sets for all node */

            for (int j = 0; j < dimension; j++) { /* Copy distances from nodes to the others */
                nodeDistanceMap[j] = distanceMap[i][j];
                nodeDistanceIndexMap[j] = j;
            }
            nodeDistanceMap[i] = Long.MAX_VALUE; /* city is not nearest neighbour */
            sortArrays(nodeDistanceMap, nodeDistanceIndexMap, 0, dimension - 1);

            System.arraycopy(nodeDistanceIndexMap, 0, this.nodeDistanceIndexMap[i], 0, numberOfNighbors);

        }

    }

    static void swap2(long v[], int v2[], int i, int j)
    {
        long tmp;
        tmp = v[i];
        v[i] = v[j];
        v[j] = tmp;
        int tmp2 = v2[i];
        v2[i] = v2[j];
        v2[j] = tmp2;
    }

    static void sortArrays(long v[], int v2[], int left, int right) //Quicksort
    {
        int k, last;

        if (left >= right)
            return;
        swap2(v, v2, left, (left + right) / 2);
        last = left;
        for (k = left + 1; k <= right; k++)
            if (v[k] < v[left])
                swap2(v, v2, ++last, k);
        swap2(v, v2, left, last);
        sortArrays(v, v2, left, last);
        sortArrays(v, v2, last + 1, right);
    }

    enum State {
        DESCRIPTION, COORDS
    }

    private class Node {
        public double x;

        public double y;

        public Node(double x, double y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public String toString() {
            return "Node{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
