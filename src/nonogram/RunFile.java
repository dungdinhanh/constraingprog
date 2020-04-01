package nonogram;

public class RunFile {
    public static void main(String []args)
    {
        int N = 15;
        // number of rows
        int M = 15;
        // sequence of shaded blocks
        int[][][] BLOCKS =
                new int[][][]{{
                        {2},
                        {4, 2},
                        {1, 1, 4},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 2, 2, 1},
                        {1, 3, 1},
                        {2, 1},
                        {1, 1, 1, 2},
                        {2, 1, 1, 1},
                        {1, 2},
                        {1, 2, 1},
                }, {
                        {3},
                        {3},
                        {10},
                        {2},
                        {2},
                        {8, 2},
                        {2},
                        {1, 2, 1},
                        {2, 1},
                        {7},
                        {2},
                        {2},
                        {10},
                        {3},
                        {2}}};

        Nono nono = new Nono(M, N, BLOCKS);
    }
}
