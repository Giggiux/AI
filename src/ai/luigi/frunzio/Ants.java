package ai.luigi.frunzio;

import java.util.Random;

/**
 * Created by giggiux on 10/28/16.
 */
public class Ants {


    static int n_ants = 10;
    static int nn_ants = 20;
    static double alpha = 1.0;
    static double beta = 2.0;
    static double rho = 0.1;
    static double q_0 = 0.90  ;

    static int u_gb = Integer.MAX_VALUE;


    static ant ants[];
    static ant best_ant;

    static double pheromone[][];
    static double total[][];

    static double probability[];

    static double initial_pheromone;

    static Random random;

    static double HEURISTIC(int m, int n) {
        return (1.0 / ((double) Main.instance.getDistance(m,n) + 0.1));
    }


    static class ant {
        int[] tour;
        boolean[] visited;
        long tour_length;
    }

    static void init_ants()
    {
        int i;

        ants = new ant[n_ants];

        for (i = 0; i < n_ants; i++) {
            ants[i] = new ant();
            ants[i].tour = new int[Main.cities + 1];
            ants[i].visited = new boolean[Main.cities];
        }
        best_ant = new ant();

        best_ant.tour = new int[Main.cities + 1];
        best_ant.visited = new boolean[Main.cities];
        best_ant.tour_length = Long.MAX_VALUE;

        probability = new double[nn_ants + 1];
        for (i = 0; i < nn_ants + 1; i++) {
            probability[i] = Double.POSITIVE_INFINITY;
        }
    }

    static void init_pheromone() {
        initial_pheromone = 1. / ((double) Main.cities * (double) nn());

        for (int i = 0; i < Main.cities; i++) {
            for (int j = 0; j <= i; j++) {
                pheromone[i][j] = pheromone[j][i] = initial_pheromone;
                total[i][j] = total[i][j] = Math.pow(pheromone[i][j], alpha) * Math.pow(HEURISTIC(i, j), beta);
            }
        }
    }

    static void local_pheromone_update (ant ant, int actualCity){
        int j = ant.tour[actualCity];
        int h = ant.tour[actualCity - 1];
        pheromone[h][j] = pheromone[j][h] = (1. - 0.1) * pheromone[h][j] + 0.1 * initial_pheromone;
        total[h][j] = total[j][h] = Math.pow(pheromone[h][j], alpha) * Math.pow(HEURISTIC(h, j), beta);
    }

    static void global_pheromone_update (ant ant) {
        double d_tau = 1.0 / (double) ant.tour_length;

        for (int i = 0; i < Main.cities; i++) {
            int j = ant.tour[i];
            int h = ant.tour[i + 1];
            pheromone[j][h] = pheromone[h][j] = (1. - rho) * pheromone[j][h] + rho * d_tau;
            total[h][j] = total[j][h] = Math.pow(pheromone[h][j], alpha) * Math.pow(HEURISTIC(h, j), beta);
        }
    }

    static int find_best() {
        long min = ants[0].tour_length;
        int k_min = 0;
        for (int k = 1; k < n_ants; k++) {
            if (ants[k].tour_length < min) {
                min = ants[k].tour_length;
                k_min = k;
            }
        }
        return k_min;
    }

    static long nn()
       {

        empty_ant_memory(ants[0]);

        int actualCity = 0;
        place_ant(ants[0], actualCity);

        while (actualCity < Main.cities - 1) {
            actualCity++;
            add_closest_city_to_tour(ants[0], actualCity);
        }
        ants[0].tour[Main.cities] = ants[0].tour[0];

           TwoOpt.two_opt(ants[0].tour);

        ants[0].tour_length = TspEvaluator.eval(ants[0].tour);

        long help = ants[0].tour_length;
           empty_ant_memory(ants[0]);
        return help;
    }

    static void empty_ant_memory(ant ant) {
        for (int i = 0; i < Main.cities; i++) {
            ant.visited[i] = false;
        }
    }

    static void place_ant(ant ant, int actualCity) {
        int rnd = (int) (random() * (double) Main.cities); /* random number between 0 .. n-1 */
        ant.tour[actualCity] = rnd;
        ant.visited[rnd] = true;
    }

    static void add_closest_city_to_tour(ant ant, int actualCity) {
        int nearestCity = Main.cities;
        int current_city = ant.tour[actualCity - 1];
        long bestDistance = Long.MAX_VALUE;

        for (int i = 0; i < Main.cities; i++) {
            if (!ant.visited[i]) {
                long distance = Main.instance.getDistance(current_city,i);
                if ( distance < bestDistance) {
                    nearestCity = i;
                    bestDistance = distance;
                }
            }
        }
        ant.tour[actualCity] = nearestCity;
        ant.visited[nearestCity] = true;
    }

    static double random()
    {
        if (random == null) {
            random = new Random(ACo.seed);
//            System.out.println("Initializing random with seed: " + ACo.seed);
        }
        return random.nextDouble();
    }

    private static void choose_best_next_in_neighbour(ant ant, int actualCity) {

        int bestCity = Main.cities;
        int current_city = ant.tour[actualCity - 1];
        double bestPheromone = -1.;
        for (int i = 0; i < nn_ants; i++) {
            int help_city = Main.instance.nodeDistanceIndexMap[current_city][i];
            if (!ant.visited[help_city]) {
                double help = total[current_city][help_city];
                if (help > bestPheromone) {
                    bestPheromone = help;
                    bestCity = help_city;
                }
            }
        }
        if (bestCity == Main.cities)
            choose_best_next(ant, actualCity);
        else {
            ant.tour[actualCity] = bestCity;
            ant.visited[bestCity] = true;
        }
    }

    private static void choose_best_next(ant ant, int actualCity){

            int next_city = Main.cities;
            int current_city = ant.tour[actualCity - 1];
            double value_best = -1.;
            for (int city = 0; city < Main.cities; city++) {
                if (!ant.visited[city])  {
                    if (total[current_city][city] > value_best) {
                        next_city = city;
                        value_best = total[current_city][city];
                    }
                }
            }
            ant.tour[actualCity] = next_city;
            ant.visited[next_city] = true;
    }

    static void chooseNext(ant ant, int actualCity) {


        double rnd, partial_sum, sum_prob = 0.0;

        if ((q_0 > 0.0) && (random() < q_0)) {
            choose_best_next_in_neighbour(ant, actualCity);
            return;
        }

        double prob[] = probability;

        int current_city = ant.tour[actualCity - 1];
        for (int i = 0; i < nn_ants; i++) {
            if (ant.visited[Main.instance.nodeDistanceIndexMap[current_city][i]])
                prob[i] = 0.0;
            else {
                prob[i] = total[current_city][Main.instance.nodeDistanceIndexMap[current_city][i]];
                sum_prob += prob[i];
            }
        }

        if (sum_prob <= 0.0) {
            choose_best_next(ant, actualCity);

        } else {
            rnd = random();
            rnd *= sum_prob;
            int i = 0;
            partial_sum = prob[i];
            while (partial_sum <= rnd) {
                i++;
                partial_sum += prob[i];
            }

            if (i == nn_ants) {
                choose_best_next_in_neighbour(ant, actualCity);
                return;
            }

            int nearestCity = Main.instance.nodeDistanceIndexMap[current_city][i];
            ant.tour[actualCity] = nearestCity;
            ant.visited[nearestCity] = true;
        }
    }
}
