package aircraft;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMiddle;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.Smallest;
import org.chocosolver.solver.search.strategy.selectors.variables.VariableSelectorWithTies;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Aircraft {
    private int N;
    private int [][]LT;
    private int [][]PC;
    private int [][]ST;
    private Solution solution;

    public Aircraft(int N, int [][]LT, int [][]PC, int [][]ST)
    {
        this.N = N;
        this.LT = LT;
        this.PC = PC;
        this.ST = ST;
        setup();
    }

    private void setup()
    {
        Model model = new Model("Aircraft landing");

        IntVar [] landingTimes = new IntVar[N];
        IntVar [] orders = new IntVar[N];
        for(int i = 0; i < N; i++)
        {
            landingTimes[i] = model.intVar("aircraft " + (i+1), LT[i][0], LT[i][2]);
//            orders[i] = model.intVar((i + 1) +"th landing", 0, N-1);
        }

        // adding constraint for the order is different
//        model.allDifferent(orders).post();
        model.allDifferent(landingTimes).post();

        // adding constraint for the landing time and the order of landing (separation of landing time)
        for(int i = 0; i < N; i++)
        {
            for(int j = i+1; j < N; j++)
            {
//                model.arithm(landingTimes[orders[j].getValue()], "-", landingTimes[orders[i].getValue()], ">=", ST[orders[i].getValue()][orders[j].getValue()]).post();
                Constraint iBeforej = model.arithm(landingTimes[i], "<=", landingTimes[j], "-", ST[i][j]);
                Constraint jBeforei = model.arithm(landingTimes[j], "<=", landingTimes[i], "-", ST[j][i]);
                model.addClausesBoolNot(iBeforej.reify(), jBeforei.reify()); // no need to post
            }
        }

        // cost

        IntVar []LE = new IntVar[N*2];
        int []LTList = new int[N*2];
//        int j = 0;
//        for(int i = 0; i < N*2; i=i+2)
//        {
//            LE[i] = model.intVar((j+1) + " early",0 ,1);
//            LE[i+1] = model.intVar((j+1) + " late", 0 , 1);
//            LE[i] = model.arithm(landingTimes[j], "<", LT[j][1]).reify();
//            LE[i+1] = model.arithm(landingTimes[j], ">", LT[j][1]).reify();
//            LTList[i] = PC[j][0];
//            LTList[i+1] = PC[j][1];
//            j++;
//        }
        int j = 0;
        for (int i = 0; i < N*2; i = i + 2) {
            LE[i] = model.intVar((i+1) + " early", 0, LT[j][1] - LT[j][0]);

            LE[i+1] = model.intVar((i+2) + " late", 0, LT[j][2] - LT[j][1]);
            // maintain earliness
            LE[i].eq((landingTimes[j].neg().add(LT[j][1])).max(0)).post();
            // and tardiness
            LE[i+1].eq((landingTimes[j].sub(LT[j][1])).max(0)).post();


            LTList[i] = PC[j][0];
            LTList[i+1] = PC[j][1];
            j++;
        }

        IntVar cost = model.intVar("C", 0, 99999999);
        model.scalar(LE, LTList, "=", cost).post();


        Map <IntVar, Integer> map = IntStream.range(0, N).boxed().collect(Collectors.toMap(i-> landingTimes[i], i -> LT[i][1]));


        Solver solver = model.getSolver();
        solver.plugMonitor((IMonitorSolution) () -> {
            for (int i = 0; i < N; i++) {
                System.out.printf("%s lands at %d (%d)\n",
                        landingTimes[i].getName(),
                        landingTimes[i].getValue(),
                        landingTimes[i].getValue() - LT[i][1]);
            }
            System.out.printf("Deviation cost: %d\n", cost.getValue());
        });

        solver.setSearch(Search.intVarSearch(
                variables -> Arrays.stream(variables).filter(
                        v -> ! v.isInstantiated()).min((v1, v2) -> closest(v2, map) - closest(v1, map)).orElse(null),
                var -> closest(var, map),
                landingTimes
        ));
        solver.showShortStatistics();
        solver.findOptimalSolution(cost, false);

//        Solver solver = model.getSolver();
//        solver.setSearch(Search.intVarSearch(
//                new VariableSelectorWithTies<>(
//                        new FirstFail(model),
//                        new Smallest()
//                ),
//                new IntDomainMiddle(false),
//                ArrayUtils.append(landingTimes, orders,LE)
//        ));
//        solver.showShortStatistics();
//        solution = solver.findOptimalSolution(cost, false);
    }

    private static int closest(IntVar var, Map<IntVar, Integer> map){
        int target = map.get(var);
        if(var.contains(target)){
            return target;
        }
        else {
            int p = var.previousValue(target);
            int n = var.nextValue(target);
            return Math.abs(target - p) < Math.abs(n - target) ? p : n;
        }
    }


    public Solution getSolution()
    {
        return solution;
    }
}
