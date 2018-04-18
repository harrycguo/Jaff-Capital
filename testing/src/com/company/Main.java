package com.company;

import sun.rmi.server.InactiveGroupException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws FileNotFoundException{

        File currentDirFile = new File("");
        String csvFile = currentDirFile.getAbsolutePath() + "/testing/5_output_terms1.csv";
        String line = "";
        String cvsSplitBy = ",";

        ArrayList<String> compareList = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                compareList.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // get char sequence keys
        String keys = compareList.get(0);
        String[] keysSplit = keys.split(cvsSplitBy);

        compareList.remove(0);

        // init output csv file

        PrintWriter pw = new PrintWriter(new File("testing/output.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("ticker");
        sb.append(',');
        sb.append("Cosine Similarity");
        sb.append(',');
        sb.append("Jaccard Similarity");
        sb.append('\n');

        // split lines and calculate differences

        for (int k = 0 ; k < compareList.size();  k = k + 2) {
            String line1 = compareList.get(k);
            String line2 = compareList.get(k + 1);
            String[] split1 = line1.split(cvsSplitBy);
            String[] split2 = line2.split(cvsSplitBy);

            String ticker = split2[0];
            //ticker = ticker.charAt(5) == '_' ? ticker.substring(1, 5): ticker.substring(1, 4);

            Map<CharSequence, Integer> leftVector = new HashMap<>();
            Map<CharSequence, Integer> rightVector = new HashMap<>();

            ArrayList<Integer> leftJaccard = new ArrayList<>();
            ArrayList<Integer> rightJaccard = new ArrayList<>();

            for (int i = 1; i < split1.length - 1; i++) {
                leftVector.put(keysSplit[i], Integer.parseInt(split1[i]));
                rightVector.put(keysSplit[i], Integer.parseInt(split2[i]));

                if (Integer.parseInt(split1[i]) == 0){
                    leftJaccard.add(i);
                }

                if (Integer.parseInt(split2[i]) == 0){
                    rightJaccard.add(i);
                }
            }

            //Cosine Similarity
            CosineSimilarity cs = new CosineSimilarity();
            Double cosSim = cs.cosineSimilarity(leftVector, rightVector);

            //Jaccard Similatiyu
            JaccardSimilarity js = new JaccardSimilarity();
            Double jacSim = js.CalcJaccardSimilarity(leftJaccard, rightJaccard);

            // System.out.println(ticker + ", " + cosSim + ", " + jacSim);

            sb.append(ticker);
            sb.append(',');
            sb.append(cosSim);
            sb.append(',');
            sb.append(jacSim);
            sb.append('\n');

        }

        pw.write(sb.toString());
        pw.close();
        System.out.println("done!");

    }

}