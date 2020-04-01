package golomb_rulers;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

public class GolombRuler {
    private int m;


    public GolombRuler(int m)
    {
        this.m = m;
        setup();
    }

    private void setup()
    {
        Model model = new Model("Golomb Ruler");
        IntVar []marks = new IntVar[m];
        for(int i = 0; i < m; i++)
        {
            marks[i] = model.intVar("mark " + i, 0, 99999);
        }

        IntVar []diffs = model.intVarArray("d", (m * (m-1))/2, 0, 999, false);


        model.arithm(marks[0], "=", 0).post();

        for (int i = 0, k = 0 ; i < m - 1; i++) {
            // // the mark variables are ordered
            model.arithm(marks[i + 1], ">", marks[i]).post();
            for (int j = i + 1; j < m; j++, k++) {
                // declare the distance constraint between two distinct marks
                model.scalar(new IntVar[]{marks[j], marks[i]}, new int[]{1, -1}, "=", diffs[k]).post();
                // redundant constraints on bounds of diffs[k]
                model.arithm(diffs[k], ">=", (j - i) * (j - i + 1) / 2).post();
                model.arithm(diffs[k], "<=", marks[m - 1], "-", ((m - 1 - j + i) * (m - j + i)) / 2).post();
            }
        }

        model.allDifferent(diffs, "BC").post();
        model.arithm(diffs[0], "<", diffs[diffs.length - 1]).post();

        Solver solver = model.getSolver();
        solver.setSearch(Search.inputOrderLBSearch(marks));
        solver.showShortStatistics();
        solver.findOptimalSolution(marks[m-1], false);
    }



}


