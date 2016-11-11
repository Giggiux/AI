package ai.luigi.frunzio;

import java.util.Collections;


class TwoHOpt implements Solver {
    private Instance instance;

    private final int dimension;

    TwoHOpt(Instance instance) {

        this.instance = instance;
        this.dimension = instance.getDimension();
    }

    /**
     *
     * @return Objective Value
     */
    @Override
    public Solution getSolution() {
        return null;
    }

    static void reverse(int[] solution, int start, int end) {
        while (end - start > 0) {
            int tmp = solution[start % Main.cities];
            solution[start % Main.cities] = solution[end % Main.cities];
            solution[end % Main.cities] = tmp;
            start++;
            end--;
        }
    }

    static void getSolution(int[] solution) {

        long best_gain = 1;

        while (best_gain != 0) {
            best_gain = 0;
            Integer best_i = null;
            Integer best_j = null;
            for (int i = 0; i < Main.cities; i++) {

                int actualIndex = solution[i % Main.cities];
                int actualIndexP1 = solution[(i + 1) % Main.cities];

                for (int j = (i + 1); j < Main.cities; j++) {
                    int indexToSwap = solution[j % Main.cities];
                    int indexToSwapP1 = solution[(j + 1) % Main.cities];

                    long gain = Main.instance.getDistance(actualIndex, indexToSwap) + Main.instance.getDistance(actualIndexP1, indexToSwapP1)
                            - Main.instance.getDistance(indexToSwap, indexToSwapP1) - Main.instance.getDistance(actualIndex, actualIndexP1);

                    if (gain < best_gain) {
                        best_gain = gain;
                        best_i = i;
                        best_j = j;
                    }
                }
            }
            if (best_i != null) {
                reverse(solution, best_i + 1, best_j);
            }

        }
    }
}

