package aircraft;

import org.chocosolver.solver.Solution;

public class RunFile {

    public static void main(String []args)
    {
        // number of planes
        int N = 10;
// Times per plane: {earliest landing time, target landing time, latest landing time}
        int[][] LT = {
                {129, 155, 559},
                {195, 258, 744},
                {89, 98, 510},
                {96, 106, 521},
                {110, 123, 555},
                {120, 135, 576},
                {124, 138, 577},
                {126, 140, 573},
                {135, 150, 591},
                {160, 180, 657}};
// penalty cost penalty cost per unit of time per plane: {for landing before target, after target}
        int[][] PC = {
                {10, 10},
                {10, 10},
                {30, 30},
                {30, 30},
                {30, 30},
                {30, 30},
                {30, 30},
                {30, 30},
                {30, 30},
                {30, 30}};

// Separation time required after i lands before j can land
        int[][] ST = {
                {99999, 3, 15, 15, 15, 15, 15, 15, 15, 15},
                {3, 99999, 15, 15, 15, 15, 15, 15, 15, 15},
                {15, 15, 99999, 8, 8, 8, 8, 8, 8, 8},
                {15, 15, 8, 99999, 8, 8, 8, 8, 8, 8},
                {15, 15, 8, 8, 99999, 8, 8, 8, 8, 8},
                {15, 15, 8, 8, 8, 99999, 8, 8, 8, 8},
                {15, 15, 8, 8, 8, 8, 99999, 8, 8, 8},
                {15, 15, 8, 8, 8, 8, 8, 99999, 8, 8},
                {15, 15, 8, 8, 8, 8, 8, 8,  99999, 8},
                {15, 15, 8, 8, 8, 8, 8, 8,  8, 99999}};

        Aircraft aircraft = new Aircraft(N, LT, PC, ST);
        Solution solution = aircraft.getSolution();
//        System.out.println(solution.toString());
    }
}
