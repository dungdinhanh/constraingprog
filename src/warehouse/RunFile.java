package warehouse;

import org.chocosolver.solver.Solution;

public class RunFile {

    public static void main(String []args)
    {
        // number of warehouses
        int W = 5;
// number of stores
        int S = 10;
// maintenance cost
        int C = 30;
// capacity of each warehouse
        int[] K = new int[]{1, 4, 2, 1, 3};
// matrix of supply costs, store x warehouse
        int[][] P = new int[][]{
                {20, 24, 11, 25, 30},
                {28, 27, 82, 83, 74},
                {74, 97, 71, 96, 70},
                {2, 55, 73, 69, 61},
                {46, 96, 59, 83, 4},
                {42, 22, 29, 67, 59},
                {1, 5, 73, 59, 56},
                {10, 73, 13, 43, 96},
                {93, 35, 63, 85, 46},
                {47, 65, 55, 71, 95}};

        WareHouse wareHouse = new WareHouse(W, S, C, K, P);
        Solution solution = wareHouse.getSolution();
        System.out.println(solution.toString());
    }
}