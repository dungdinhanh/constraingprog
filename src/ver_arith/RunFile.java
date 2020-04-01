package ver_arith;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;

public class RunFile {

    public static void main(String []args)
    {
        String []listString = new String[]{"SEND", "MORE"};
        VerbalArithmetic solverV = new VerbalArithmetic(listString, "MONEY");
        Model model = solverV.getModel();
        Solver solver = model.getSolver();
        solver.showStatistics();
        solver.showSolutions();
        solver.findSolution();
    }
}
