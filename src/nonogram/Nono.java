package nonogram;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.automata.FA.FiniteAutomaton;
import org.chocosolver.solver.constraints.nary.automata.FA.IAutomaton;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.util.tools.ArrayUtils;

public class Nono {
    private int M;
    private int N;
    private int [][][]blocks;

    public Nono(int M, int N, int [][][]blocks){
        this.M = M;
        this.N = N;
        this.blocks = blocks;
        setup();
    }

    private void setup()
    {
        Model model = new Model("Nonogram");
        BoolVar [][]cells = model.boolVarMatrix("c", N, M);

        for (int i = 0; i < M; i++) {
            dfa(cells[i], blocks[0][i], model);
        }
        for (int j = 0; j < N; j++) {
            dfa(ArrayUtils.getColumn(cells, j), blocks[1][j], model);
        }
        if(model.getSolver().solve()){
            for (int i = 0; i < cells.length; i++) {
                System.out.printf("\t");
                for (int j = 0; j < cells[i].length; j++) {
                    System.out.printf(cells[i][j].getValue() == 1 ? "1" : "0");
                }
                System.out.printf("\n");
            }
        }
    }


    private void dfa(BoolVar[] cells, int[] rest, Model model) {
        StringBuilder regexp = new StringBuilder("0*");
        int m = rest.length;
        for (int i = 0; i < m; i++) {
            regexp.append('1').append('{').append(rest[i]).append('}');
            regexp.append('0');
            regexp.append(i == m - 1 ? '*' : '+');
        }
        IAutomaton auto = new FiniteAutomaton(regexp.toString());
        model.regular(cells, auto).post();
    }



}
