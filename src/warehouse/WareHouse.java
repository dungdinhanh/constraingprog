package warehouse;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMiddle;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.Smallest;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelectorWithTies;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

public class WareHouse {
    private int W;
    private int S;
    private int C;

    int []K;

    int [][] P;

    Model model;
    Solution solution;

    public WareHouse(int W, int S, int C, int []K, int [][]P)
    {
        this.W = W;
        this.S = S;
        this.C = C;
        this.K = K;
        this.P = P;
        setup();
    }


    private void setup()
    {
        model = new Model("Warehouse");
        IntVar [][]A = new IntVar[S][W];
        IntVar []wareHouse = new IntVar[W];

        for(int i = 0; i < S; i++)
        {
            for(int j = 0; j < W; j++)
            {
                A[i][j] = model.intVar("Warehouse " + j + " connected to " + i, 0, 1);
            }
        }

        // constraint by capacity

        for(int i = 0; i < W; i++){
            IntVar []stores = new IntVar[S];
            wareHouse[i] = model.intVar("Warehouse "+ i, 0, 1);
            for(int j = 0; j < S; j++)
            {
                stores[j] = A[j][i];
            }
            IntVar multiK = model.intVar("m" + i, 0, 10000);
            model.times(wareHouse[i], K[i], multiK).post();
            model.sum(stores, "<=", multiK).post();
        }

        // store is supplied by one
        for(int i = 0; i < S; i++)
        {
            model.sum(A[i], "=", 1).post();
        }

        // objective

        IntVar totalCost = model.intVar("C", 0, 999999);

        IntVar[] allVariables = new IntVar[W * S + W];
        int [] coeff = new int[W * S + W];
        int count = 0;
        for(int i = 0; i < S; i++)
        {
            for(int j = 0; j < W; j++)
            {
                allVariables[count] = A[i][j];
                coeff[count] = P[i][j];
                count++;
            }
        }

        for(int i = 0; i < W; i++)
        {
            allVariables[count] = wareHouse[i];
            coeff[count] = C;
            count++;
        }

        model.scalar(allVariables, coeff, "=", totalCost).post();

        Solver solver = model.getSolver();
        solver.setSearch(Search.intVarSearch(
                new VariableSelectorWithTies<>(
                        new FirstFail(model),
                        new Smallest()
                ),
                new IntDomainMiddle(false),
                ArrayUtils.append(allVariables)
        ));

        solution = solver.findOptimalSolution(totalCost, false);

    }

    public Solution getSolution()
    {
        return solution;
    }



}
