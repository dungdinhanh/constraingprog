package ver_arith;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;

public class VerbalArithmetic {

    private String []A;
    private String C;
    private int n;
    private int aLen;
    private int bLen;
    private int cLen;
    private ArrayList<Character> listChar;
    private int []dict = new int[256];
    private boolean [] start = new boolean[256];
    private Model model;
    private ArrayList<Integer> all;
    private ArrayList<Integer> eff;


    public VerbalArithmetic(String []A, String C)
    {
        this.A = A;
        this.C = C;
        listChar = new ArrayList<>();
        for(int i = 0; i < 256; i++)
        {
            dict[i] = -1;
        }
        n = 0;

        all = new ArrayList<>();
        eff = new ArrayList<>();

        for(int i = 0; i < A.length; i++)
        {
            setup(A[i], false);
        }
        setup(C, true);

        setupModel();
    }

    private void setup(String s, boolean minus)
    {
        int len = s.length() - 1;
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(dict[c] == -1) {

                listChar.add(c);
                dict[c] = n;
                n += 1;
            }
            if(i == 0)
            {
                start[c] = true;
            }
            all.add(dict[c]);
            if(minus)
                eff.add((int)(-Math.pow(10, len - i)));
            else
                eff.add((int)(Math.pow(10, len-i)));
        }
    }

    private int []convertIntegerList(ArrayList<Integer> intergers)
    {
        int []newList = new int[intergers.size()];

        for(int i =0; i < newList.length; i++)
        {
            newList[i] = intergers.get(i);
        }
        return newList;
    }

    private void setupModel()
    {
        model = new Model("verbal arithm");

        // Create variables
        IntVar[] characters = new IntVar[n];
        for(int i = 0; i < n ; i++)
        {
            char c = listChar.get(i);

            // Start of the words can not be zero
            int lowerbound=0;
            if(start[c])
                lowerbound = 1;
            characters[i] = model.intVar( String.valueOf((char)(listChar.get(i))), lowerbound, 9);
        }

        // no two letters are assigned the same digit

        for(int i = 0; i < n-1; i++)
        {
            for(int j = i+1; j < n; j++)
            {
                model.arithm(characters[i], "!=", characters[j]).post();
            }
        }


        // Operations

        IntVar [] allTerms = new IntVar[all.size()];

        for(int i = 0; i < allTerms.length; i++)
        {
            allTerms[i] = characters[all.get(i)];
        }

        int []coeff = convertIntegerList(eff);

        model.scalar(allTerms, coeff, "=", 0).post();


    }

    public Model getModel()
    {
        return model;
    }





}
