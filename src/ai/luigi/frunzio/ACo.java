package ai.luigi.frunzio;

/**
 * Created by giggiux on 10/28/16.
 */
public class ACo {
     /* use fixed radius search in the 20 nearest neighbours */

    static int seed;
    static double max_time = 180.0;

    static void init() {
        Ants.pheromone = new double[Main.cities][Main.cities];
        Ants.total = new double[Main.cities][Main.cities];

        Ants.init_pheromone();
    }

    static void start() {
        Timer.start_timers();


        while (!(Timer.elapsed_time() >= max_time)) {
            for (int k = 0; k < Ants.n_ants; k++) {
                Ants.empty_ant_memory(Ants.ants[k]);
            }

            int actualCity = 0;

            for (int k = 0; k < Ants.n_ants; k++)
                Ants.place_ant(Ants.ants[k], actualCity);

            while (actualCity < Main.cities - 1) {
                actualCity++;
                for (int k = 0; k < Ants.n_ants; k++) {
                    Ants.chooseNext(Ants.ants[k], actualCity);
                    Ants.local_pheromone_update(Ants.ants[k], actualCity);
                }
            }

            for (int k = 0; k < Ants.n_ants; k++) {
                Ants.ants[k].tour[Main.cities] = Ants.ants[k].tour[0];
                Ants.ants[k].tour_length = TspEvaluator.eval(Ants.ants[k].tour);
                Ants.local_pheromone_update(Ants.ants[k], Main.cities);
            }

            for (int k = 0; k < Ants.n_ants; k++) {
                TwoOpt.two_opt(Ants.ants[k].tour);
                Ants.ants[k].tour_length = TspEvaluator.eval(Ants.ants[k].tour);

            }

            int iteration_best_ant = Ants.find_best(); /* iteration_best_ant is a global variable */

            if (Ants.ants[iteration_best_ant].tour_length < Ants.best_ant.tour_length) {
                System.arraycopy(Ants.ants[iteration_best_ant].tour, 0, Ants.best_ant.tour, 0, Ants.best_ant.tour.length);
                Ants.best_ant.tour_length = Ants.ants[iteration_best_ant].tour_length;
            }

            Ants.global_pheromone_update(Ants.best_ant);
        }
//        System.out.println(ACo.seed + " " + Ants.best_ant.tour_length);

    }

}
