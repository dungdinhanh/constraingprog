package schedule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Input {
    private int n;
    private int m;
    private int[][] T;
    private int []c;
    private int [][]Q;

    public Input(){

    }


    public void readFile(String filePath)
    {
        File file = new File(filePath);
        try (Scanner sc = new Scanner(file)) {
            this.m = sc.nextInt();
            this.n = sc.nextInt();
            T = new int[n][m];
//            Q = new int[n][2];

            sc.next();
            for(int i = 0; i < m; i++)
            {
                String buffer = sc.nextLine();
                StringTokenizer strTok = new StringTokenizer(buffer);
                String subjectS = strTok.nextToken();
                int subject = Integer.valueOf(subjectS);
                while(strTok.hasMoreTokens())
                {
                    String teacherS = strTok.nextToken();
                    int teacher = Integer.valueOf(teacherS);
                    T[subject][teacher] = 1;
                }
            }

            String buffer = sc.nextLine();
            int nQ = Integer.valueOf(buffer);
            for(int i = 0; i < nQ; i++)
            {
                
            }


//            while (sc.hasNextLine()){
////                System.out.println(sc.nextLine());
//                String buffer = sc.nextLine();
//                StringTokenizer stringTok = new StringTokenizer(buffer);
//                while(stringTok.hasMoreTokens())
//                {
//
//                }
//            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
