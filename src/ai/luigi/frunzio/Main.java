package ai.luigi.frunzio;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void usage() {
        System.out.println("java Main <file>");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
            System.exit(-1);
        }
        File f = new File(args[0]);
        if (!f.exists() || f.isDirectory()) {
            System.out.println("File " + args[0] + " does not exist!");
            usage();
            System.exit(-1);
        }

        Instance instance = new Instance(args[0]);
        try {
            instance.read();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
        long bestCost = Long.MAX_VALUE;
        Solution bestSolution = null;
        NearestNeighbour solver = new NearestNeighbour(instance);
        for (Integer i : IntStream.rangeClosed(0, instance.getDimension()-1).toArray()) {
            Solution s = solver.getSolution(i);
            if (s.getCost() < bestCost) {
                bestCost = s.getCost();
                bestSolution = s;
            }
        }
        System.out.println(bestSolution);
    }

}
