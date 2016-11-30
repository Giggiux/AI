package ai.luigi.frunzio;

/**
 * Created by giggiux on 10/28/16.
 */
public class TwoOpt {

    static int h1=0;
    static int h2=0;
    static int h3=0;
    static int h4=0;
    static int[] pos = new int[Main.cities];
    static boolean[] dlb = new boolean[Main.cities];
    static int c2;

    static int[] generate_random_permutation(int n)
        {
            int tot_assigned = 0;
            int[] r;
            r = new int[n];
            for (int i = 0; i < n; i++)
                r[i] = i;
            for (int i = 0; i < n; i++) {
                double rnd = Ants.random();
                int node = (int) (rnd * (n - tot_assigned));
                int tmp = r[i];
                r[i] = r[i + node];
                r[i + node] = tmp;
                tot_assigned++;
            }
            return r;
        }

    private static boolean search_exchange_successor(int[] tour, int c1) {
        int pos_c1 = pos[c1];
        int s_c1 = tour[pos_c1 + 1];
        long radius = Main.instance.getDistance(c1,s_c1);
        for (int h = 0; h < Ants.nn_ants; h++) {
            c2 = Main.instance.nodeDistanceIndexMap[c1][h];
            if ( radius > Main.instance.getDistance(c1,c2)) {
                int s_c2 = tour[pos[c2] + 1];
                long gain = -radius + Main.instance.getDistance(c1,c2) + Main.instance.getDistance(s_c1,s_c2)
                        - Main.instance.getDistance(c2,s_c2);
                if (gain < 0) {
                    h1 = c1;
                    h2 = s_c1;
                    h3 = c2;
                    h4 = s_c2;
                    // dunno, but it's more precise the two-opt with this.
                    search_exchange_predecessor(tour, c1);
                    return true;
                }
            } else
                return false;
        }
        return false;
    }
    private static void search_exchange_predecessor(int[] tour, int c1) {
        int pos_c1 = pos[c1];
        int p_c1;
        if (pos_c1 > 0)
            p_c1 = tour[pos_c1 - 1];
        else
            p_c1 = tour[Main.cities - 1];
        long radius = Main.instance.getDistance(p_c1,c1);
        for (int h = 0; h < Ants.nn_ants; h++) {
            c2 = Main.instance.nodeDistanceIndexMap[c1][h];
            if (radius > Main.instance.getDistance(c1,c2)) {
               int pos_c2 = pos[c2];
                int p_c2;
                if (pos_c2 > 0)
                    p_c2 = tour[pos_c2 - 1];
                else
                    p_c2 = tour[Main.cities - 1];
                if (p_c2 == c1)
                    continue;
                if (p_c1 == c2)
                    continue;
                long gain = -radius + Main.instance.getDistance(c1,c2) + Main.instance.getDistance(p_c1,p_c2)
                        - Main.instance.getDistance(p_c2,c2);
                if (gain < 0) {
                    h1 = p_c1;
                    h2 = c1;
                    h3 = p_c2;
                    h4 = c2;
                    return;
                }
            } else
                return;
        }
    }

    static void two_opt(int[] tour){
        boolean exchange;

        int c1;
        int i, j, h, l;
        int help;
        boolean improvement_flag;
        int[] random_vector;
        for (i = 0; i < Main.cities; i++) {
            pos[tour[i]] = i;
            dlb[i] = false;
        }

        improvement_flag = true;
        random_vector = generate_random_permutation(Main.cities);

        while (improvement_flag) {
            improvement_flag = false;
            for (l = 0; l < Main.cities; l++) {
                c1 = random_vector[l];
                if (dlb[c1])
                    continue;
                exchange = search_exchange_successor(tour, c1);
                if (!exchange) {
                    dlb[c1] = true;
                } else {
                    improvement_flag = true;
                    dlb[h1] = false;
                    dlb[h2] = false;
                    dlb[h3] = false;
                    dlb[h4] = false;
		    /* Now perform move */
                    if (pos[h3] < pos[h1]) {
                        help = h1;
                        h1 = h3;
                        h3 = help;
                        help = h2;
                        h2 = h4;
                        h4 = help;
                    }
                    if (pos[h3] - pos[h2] < Main.cities / 2 + 1) {
			// If the inner distance is less than the outer, swap inside (:
                        i = pos[h2];
                        j = pos[h3];
                        while (i < j) {
                            c1 = tour[i];
                            c2 = tour[j];
                            tour[i] = c2;
                            tour[j] = c1;
                            pos[c1] = j;
                            pos[c2] = i;
                            i++;
                            j--;
                        }
                    } else {
			// If the outer distance is less than the inner, swap outside :)
                        i = pos[h1];
                        j = pos[h4];
                        if (j > i)
                            help = Main.cities - (j - i) + 1;
                        else
                            help = (i - j) + 1;
                        help = help / 2;
                        for (h = 0; h < help; h++) {
                            c1 = tour[i];
                            c2 = tour[j];
                            tour[i] = c2;
                            tour[j] = c1;
                            pos[c1] = j;
                            pos[c2] = i;
                            i--;
                            j++;
                            if (i < 0)
                                i = Main.cities - 1;
                            if (j >= Main.cities)
                                j = 0;
                        }
                        // Remember to change the last city since, since was outer swap :)
                        tour[Main.cities] = tour[0];
                    }
                }
            }
        }
    }
}
