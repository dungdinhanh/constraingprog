package schedule;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Schedule {
    private int n;
    private int m;
    private int [][] T;
    private int []c;
    private int [][] Q;

    public Schedule(int n, int m, int [][] T, int []c, int [][]Q)
    {
        this.n = n;
        this.m = m;
        this.T = T;
        this.c = c;
        this.Q = Q;
        setup();
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int[][] getT() {
        return T;
    }

    public void setT(int[][] t) {
        T = t;
    }

    public int[] getC() {
        return c;
    }

    public void setC(int[] c) {
        this.c = c;
    }

    public int[][] getQ() {
        return Q;
    }

    public void setQ(int[][] q) {
        Q = q;
    }

    public void setup()
    {
        IntVar [][]A = new IntVar[m][n]; // 1 if teacher i is assigned with subject j
        IntVar [][]B = new IntVar[n][m];
        Model model = new Model("Scheduling");
        for(int i = 0; i < m; i++){
            for(int j =0; j < n; j++)
            {
                A[i][j] = model.intVar("Teacher " + j + " is assigned to subject " + i, 0, 1);
                model.arithm(A[i][j], "<=", T[j][i]).post();
                B[j][i] = A[i][j];
            }
        }

        for (int j =0; j < n; j++)
        {
            model.sum(B[j], "=", 1).post();

        }

        IntVar objective = model.intVar("Objective", 0, 99999999);
        IntVar []C = new IntVar[n];
        for (int i =0; i < n; i++)
        {
            C[i] = model.intVar("Tearcher " + i +" credits", 0, 99999999);

            model.scalar(A[i], c, "=", C[i]).post();
        }

        model.sum(C, "=", objective).post();


        model.setObjective(Model.MINIMIZE, objective);
        Solver solver = model.getSolver();

        while(solver.solve())
        {
            System.out.println("Objective " + objective.getValue());
        }

    }
}
