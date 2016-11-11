package ai.luigi.frunzio;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    static double maxtime;
    static int cities;
    static Instance instance;

    public static void usage() {
        System.out.println("java Main <file> [seed]");
    }

    public static void main(String[] args) {
        if (args.length > 2 || args.length < 1) {
            usage();
            System.exit(-1);
        }
        File f = new File(args[0]);
        if (!f.exists() || f.isDirectory()) {
            System.out.println("File " + args[0] + " does not exist!");
            usage();
            System.exit(-1);
        }


        ACo.seed = (args.length ==2 && args[1]!= null) ? Integer.parseInt(args[1]) : (int) System.currentTimeMillis();



        instance = new Instance(args[0]);
        try {
            instance.read();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }

            ACo.init();

            ACo.start();


        System.out.println("Tour Length: " + TspEvaluator.eval(Ants.best_ant.tour));

//        System.out.println("Tour cities: " + Ants.best_ant.tour.length);
//        System.out.println("Tour cities: " + Main.cities);

        int i;
        for ( i = 0; i < Ants.best_ant.tour.length; i++)
            System.out.printf("%d ", Ants.best_ant.tour[i]+1);
        System.out.println(ACo.seed + " " + Ants.best_ant.tour_length);

    }

}
