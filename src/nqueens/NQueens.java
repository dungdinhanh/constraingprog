package nqueens;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;


public class NQueens {

    private Model model;
    private int n;

    public NQueens(int n){
        this.n = n;
        setModel();
    }


    public void setModel()
    {
        this.model = new Model();
        int i;
        int j;
        IntVar[] queens = new IntVar[n];
        for (int q = 0;  q < n; q ++)
        {
            queens[q] = model.intVar("Q_"+q, 1, n);
        }

        for(i = 0; i < n-1; i++)
        {
            for (j = i+1; j < n; j++)
            {
                model.arithm(queens[i], "!=", queens[j]).post();
                model.arithm(queens[i], "!=", queens[j], "-", j - i).post();
                model.arithm(queens[i], "!=", queens[j], "+", j - i).post();
            }
        }
    }

    public Model getModel()
    {
        return model;
    }

    public static void main(String []args)
    {
        NQueens nQueens = new NQueens(8);
        Solution solution = nQueens.getModel().getSolver().findSolution();
        if(solution != null)
        {
            System.out.println(solution.toString());
        }
        else System.out.println("No solution");


    }
}
