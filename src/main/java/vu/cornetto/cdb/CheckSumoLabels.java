package vu.cornetto.cdb;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 22-apr-2008
 * Time: 16:13:33
 * To change this template use File | Settings | File Templates.
 */
public class CheckSumoLabels {
    boolean DEBUG = false;

    HashMap sumoHierarchy;
    HashMap disjointMap;
    String scoredmappings;
    public CheckSumoLabels (String typePath, String disjointPath) {
          sumoHierarchy = readTree(typePath);
          disjointMap = readDisjoints(typePath);
    }

    ArrayList getMappedTypesFromString(String str) {
        ArrayList types = new ArrayList();
        //System.out.println("str = " + str);
        String [] typeArray = str.split("[.]");
        for (int i=0;i<typeArray.length;i++) {
            String type = typeArray[i];
            if (type.endsWith(".")) {
                if ((type.startsWith("+")) ||
                    (type.startsWith("@")) ||
                    (type.startsWith("[")) ||
                    (type.startsWith("]")) ||
                    (type.startsWith("="))){
                     type = type.substring(1, type.length()-1).trim();
                }
            }
            else {
                if (type.length()>1) {
                    type = type.substring(1).trim();
                }
            }
            //System.out.println("type = " + type);
            if (type.length()>0) {
                types.add(type);
            }
        }
        return types;
    }

    int countDisjoints (ArrayList mappings) {
        int nDisjoints = 0;
        for (int i=0;i<mappings.size();i++) {
            String mapping = (String) mappings.get(i);
            if (disjointMap.containsKey(mapping)) {
                String [] disjoints = (String[]) disjointMap.get(mapping);
                for (int d=0;d<disjoints.length;d++) {
                    String disjoint = disjoints[d];
                    if (!disjoint.equals(mapping)) {
                       if (mappings.contains(disjoint)) {
                           nDisjoints++;
                       }
                    }
                }
            }
        }
        return nDisjoints;
    }


    double scoreMappings (ArrayList mappings) {
        scoredmappings = "";
        double score = 0.0;
        int match = -1;
        int [][] steps = new int [mappings.size()][mappings.size()];
        ///init
        for (int s=0; s<mappings.size();s++) {
            steps[s][s]=-1;
        }
        //// WE ITERATE OVER THE MAPPINGS
        for (int i=0;i<mappings.size();i++) {
            String mapping = (String) mappings.get(i);
            //System.out.println("mapping = " + mapping);
            for (int j=0;j<mappings.size();j++) {
                //// WE ITERATE OVER ALL OTHER MAPPINGS
                if (j==i) {
                    // THE DISTANCE TO THE SAME MATRIX IS ZERO
                    steps [i][j]=0;
                }
                else {
                    String omapping = (String) mappings.get(j);
                    //System.out.println("\tomapping = " + omapping);
                    /// WE FIRST MATCH THE MAPPING WITH THE PARENTS OF THE OTHER MAPPING (OMAPPING)
                    if (sumoHierarchy.containsKey(omapping)) {
                        ArrayList oparents = (ArrayList) sumoHierarchy.get(omapping);
                        //printParentPath("omapping", oparents);
                        match = oparents.indexOf(mapping);
                        if (match>-1) {
                            /// IF THE MAPPING MATCHES ANY OF THE PARENTS OF THE OMAPPING
                            /// WE STORE THE PATH IN THE MATRIX AND BREAK THE LOOP
                            /// WE ADD A VALUE OF 1 BECAUSE THERE CANNOT BE A DIRECT MAP FROM MAPPING TO MAPPING
                            steps [i][j]=match+1;
/*
                            System.out.println("mapping-oparents.get(match) = " + oparents.get(match));
                            System.out.println("steps  [i][j]= " + steps [i][j]);
*/
                        }
                        /// AT THE END OF THE LOOP THE MATRIX IS STILL -1 OR SET
                    }
                    if (match ==-1) {
                        /// WHILE WE HAVE NOT FOUND A MATCH AND THERE ARE PARENTS FOR THE  MAPPING
                        /// TRY TO MATCH WITH A PARENT TO OTHER OMAPPING OR ONE OF ITS PARENTS
                        if (sumoHierarchy.containsKey(mapping)) {
                            ArrayList parents = (ArrayList) sumoHierarchy.get(mapping);
                            //printParentPath("mapping", parents);
                            match = parents.indexOf(omapping);
                            if (match>-1) {
                                steps [i][j]=match+1;
/*
                                System.out.println("parents-omapping.get(match) = " + parents.get(match));
                                System.out.println("steps  [i][j]= " + steps [i][j]);
*/
                            }
                            else {
                                ///WE GET THE PARENTS OF THE OMAPPING AND
                                // WE ITERATE OVER THE PARENTS OF THE MAPPING
                                if (sumoHierarchy.containsKey(omapping)) {
                                    ArrayList oparents = (ArrayList) sumoHierarchy.get(omapping);
                                    for (int p=0;p<parents.size();p++) {
                                        String parent = (String)parents.get(p);
                                        match = oparents.indexOf(parent);
                                        if (match>-1) {
                                            /// IF THE PARENT MATCHES ANY OF THE PARENTS OF THE OMAPPING
                                            /// WE STORE THE PATH IN THE MATRIX AND BREAK THE LOOP
                                            /// WE ADD A VALUE OF 1 BECAUSE THERE CANNOT BE A DIRECT MAP FROM MAPPING TO MAPPING
                                            steps [i][j]=match+p+1;
/*
                                            System.out.println("parent-oparents.get(match) = " + oparents.get(match));
                                            System.out.println("steps  [i][j]= " + steps [i][j]);
*/
                                            break;
                                        }
                                    }
                                    /// AT THE END OF THE LOOP THE MATRIX IS STILL -1
                                }
                                else {
                                    //// WITHOUT OMAPPING NO MATCH
                                }
                            }
                            /// AT THE END OF THE LOOP THE MATRIX IS STILL -1 OR SET
                        }
                        else {
                            //// WITHOUT MAPPING NO MATCH
                        }
                    }
                }
            }
        }
//        System.out.println("MATRIX");
        double mappingScore = 0;
        for (int m=0; m<mappings.size();m++) {
            mappingScore = 0;
            for (int s=0; s<mappings.size();s++) {
                mappingScore += (double) steps[m][s];
            }
            mappingScore = mappingScore/(double) mappings.size();
            scoredmappings += mappings.get(m)+":"+ vu.cornetto.util.Other.doubleStringFormat("##.##",mappingScore)+".";
            score +=mappingScore;
        }
        score = score/(double)mappings.size();
/*
        System.out.println("scoredmappings = " + scoredmappings);
        System.out.println("score = " + score);
*/
        return score;
    }

    void printParentPath (String pre, ArrayList parents) {
        String str = "";
        for (int i=0;i<parents.size();i++) {
            String parent = (String) parents.get(i);
            str +="#" + parent;
        }
        System.out.println("\t"+pre+" ParentPath=" + str);
    }


    void checkMappings (String filePath) {
        /*
        bondsstaat:1	NOUN	4	4	(+Declaring.+Organization.+PoliticalOrganization.+SocialInteraction.)

        (+Declaring.+Organization.+PoliticalOrganization.+SocialInteraction.)
        (+BaseballRun.+Maneuver.+Sport.+Walking.)
        (+Collection.+Music.@PositiveInteger.+Text.)
         */
        try {
                FileInputStream isLexicon = new FileInputStream (filePath);
                InputStreamReader isr = new InputStreamReader(isLexicon);
                FileOutputStream osLexicon = new FileOutputStream(filePath+".ont.ranked.xls");
                FileOutputStream osLexiconStats = new FileOutputStream(filePath+".ont.stats.xls");
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                HashMap NounScore = new HashMap();
                HashMap VerbScore = new HashMap();
                HashMap AdjectiveScore = new HashMap();
                int nNounSingleSumo = 0;
                double nNounSingleSumoDistance = 0;
                int nVerbSingleSumo = 0;
                int nAdjectiveSingleSumo = 0;            
                int nNounScores = 0;
                int nVerbScores = 0;
                int nAdjectiveScores = 0;
                double scoreNounTotal= 0;
                double scoreVerbTotal= 0;
                double scoreAdjectiveTotal= 0;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    int idx_s = inputLine.indexOf("(");
                    int idx_e = inputLine.indexOf(")");
                    if ((idx_e>idx_s) && (idx_s>-1)) {
                       String col_1_4 = inputLine.substring(0, idx_s);
                       String col_5 = inputLine.substring(idx_s+1, idx_e);
                       ArrayList mappings = getMappedTypesFromString (col_5);
                       double score = scoreMappings(mappings);
                       int nDis = countDisjoints(mappings);
                       if (mappings.size()>1) {
                           vu.cornetto.util.FileProcessor.storeResult(osLexicon, col_1_4);
                           vu.cornetto.util.FileProcessor.storeResult(osLexicon, scoredmappings+"\t");
                           vu.cornetto.util.FileProcessor.storeResult(osLexicon, nDis+"\t");
                           vu.cornetto.util.FileProcessor.storeResult(osLexicon, vu.cornetto.util.Other.doubleStringFormat("##.##", score)+"\t");
                           vu.cornetto.util.FileProcessor.storeResult(osLexicon, "("+col_5+")\n");
                       }
                       else {
                           
                       }
                       if (score>=0) {
                           if (col_1_4.indexOf("NOUN")!=-1) {
                               if (mappings.size()>1) {
                                   scoreNounTotal += score;
                                   //System.out.println("score = " + score);
                                   //System.out.println("scoreNounTotal = " + scoreNounTotal);
                                   nNounScores++;
                                   Integer scoreInt = new Integer((int) score);
                                   if (NounScore.containsKey(scoreInt)) {
                                       Integer nMappings = (Integer) (NounScore.get(scoreInt));
                                       nMappings = new Integer(nMappings.intValue()+1);
                                       NounScore.put(scoreInt, nMappings);
                                   }
                                   else {
                                       NounScore.put(scoreInt, new Integer(1));
                                   }
                               }
                               else {
                                   nNounSingleSumoDistance += score;
                                   nNounSingleSumo++;
                               }
                           }
                           if (col_1_4.indexOf("VERB")!=-1) {
                               if (mappings.size()>1) {
                                   scoreVerbTotal += score;
                                   nVerbScores++;
                                   Integer scoreInt = new Integer((int) score);
                                   if (VerbScore.containsKey(scoreInt)) {
                                       Integer nMappings = (Integer) (VerbScore.get(scoreInt));
                                       nMappings = new Integer(nMappings.intValue()+1);
                                       VerbScore.put(scoreInt, nMappings);
                                   }
                                   else {
                                       VerbScore.put(scoreInt, new Integer(1));
                                   }
                               }
                               else {
                                   nVerbSingleSumo++;
                               }
                           }
                           if (col_1_4.indexOf("ADJECTIVE")!=-1) {
                               if (mappings.size()>1) {
                                   scoreAdjectiveTotal += score;
                                   nAdjectiveScores++;
                                   Integer scoreInt = new Integer((int) score);
                                   if (AdjectiveScore.containsKey(scoreInt)) {
                                       Integer nMappings = (Integer) (AdjectiveScore.get(scoreInt));
                                       nMappings = new Integer(nMappings.intValue()+1);
                                       AdjectiveScore.put(scoreInt, nMappings);
                                   }
                                   else {
                                       AdjectiveScore.put(scoreInt, new Integer(1));
                                   }
                               }
                               else {
                                   nAdjectiveSingleSumo++;
                               }
                           }
                       }
                    }
                    else {
                        vu.cornetto.util.FileProcessor.storeResult(osLexicon, inputLine+"\n");
                    }
                }
            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "POS\tCUMULATED SCORE\tNR OF MEANINGS >1 SUMO MAPPINGS\tAVERAGE SCORE\tNR OF MEANINGS 1 SUMO MAPPING"+"\n");
            double average = (double) (scoreNounTotal/(double)nNounScores);
/*
            System.out.println("nNounScores = " + nNounScores);
            System.out.println("scoreNounTotal = " + scoreNounTotal);
            System.out.println("average = " + average);
*/
            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "NOUNS\t"+scoreNounTotal+"\t"+nNounScores+"\t"+ vu.cornetto.util.Other.doubleStringFormat("##,##",average)+"\t"+nNounSingleSumo+"\n");
            average =  (double) (scoreVerbTotal/(double)nVerbScores);
            //System.out.println("average = " + average);
            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "VERBS\t"+scoreVerbTotal+"\t"+nVerbScores+"\t"+ vu.cornetto.util.Other.doubleStringFormat("##,##",average)+"\t"+nVerbSingleSumo+"\n");
            average = (double) (scoreAdjectiveTotal/(double)nAdjectiveScores);
            //System.out.println("average = " + average);
            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "ADJECTIVES\t"+scoreAdjectiveTotal+"\t"+nAdjectiveScores+"\t"+ vu.cornetto.util.Other.doubleStringFormat("##,##", average)+"\t"+nAdjectiveSingleSumo+"\n");

            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "NOUNS\n");
            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "Average distance as integer\tNr of Meanings"+"\n");
            Set keySet = NounScore.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                Integer key = (Integer) keys.next();
                Integer cumulation = (Integer) NounScore.get(key);
                vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, key+"\t"+cumulation+"\n");
            }

            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "VERB\n");
            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "Average distance as integer\tNr of Meanings"+"\n");
            keySet = VerbScore.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                Integer key = (Integer) keys.next();
                Integer cumulation = (Integer) VerbScore.get(key);
                vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, key+"\t"+cumulation+"\n");
            }

            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "ADJECTIVE\n");
            vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, "Average distance as integer\tNr of Meanings"+"\n");
            keySet = AdjectiveScore.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                Integer key = (Integer) keys.next();
                Integer cumulation = (Integer) AdjectiveScore.get(key);
                vu.cornetto.util.FileProcessor.storeResult(osLexiconStats, key+"\t"+cumulation+"\n");
            }

            in.close();
            osLexicon.close();
            osLexiconStats.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    }

    ArrayList getParentsFromString(String str) {
        ArrayList types = new ArrayList();
        String [] typeArray = str.split("#");
        for (int i=0;i<typeArray.length;i++) {
            String type = typeArray[i];
            if (type.endsWith("#")) {
                type = type.substring(0, type.length()-1);
            }
            //System.out.println("type = " + type);
            types.add(type);
        }
        return types;
    }

    HashMap readTree (String filePath) {
        /*
SetOrClass:Abstract#Entity#
         */
        HashMap map = new HashMap();
        try {   FileInputStream isLexicon = new FileInputStream (filePath);
                InputStreamReader isr = new InputStreamReader(isLexicon);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                        int idx_1 = inputLine.indexOf(":");
                        if ((idx_1> -1)) {
                            String child = inputLine.substring(0, idx_1).trim();
                            String parentString = inputLine.substring(idx_1+1).trim();
                            ArrayList parents = getParentsFromString(parentString);
                            map.put(child, parents);
                        }
                }
                in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return map;
    }

    

    HashMap readDisjoints (String filePath) {
        /*
(disjoint AboveTheLine BelowTheLine)
(disjoint Alga Fern Fungus Moss)
(disjoint Amphibian Fish Reptile)
(disjoint AnaerobicExerciseDevice AerobicExerciseDevice)
(disjoint AnimalLanguage HumanLanguage ComputerLanguage)
(disjoint ApartmentBuilding SingleFamilyResidence)
         */
        HashMap map = new HashMap();
        try {   FileInputStream isLexicon = new FileInputStream (filePath);
                InputStreamReader isr = new InputStreamReader(isLexicon);
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                        int idx_1 = inputLine.indexOf(" ");
                        if ((idx_1> -1)) {
                            String str = inputLine.substring(idx_1+1, inputLine.length()-1).trim();
                            String [] disjoints = str.split(" ");
                            for (int i=0; i<disjoints.length;i++) {
                                String type = disjoints[i];
                                map.put(type, disjoints);
                            }
                        }
                }
                in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return map;
    }


    static public boolean balancedBrackets (String line) {
        int openBrackets = 0;
        int closedBrackets = 0;
        for (int i=0; i<line.length();i++) {
            char a = line.charAt(i);
            if (a=='(') openBrackets++;
            if (a==')') closedBrackets++;
        }
/*
        System.out.println("closedBrackets = " + closedBrackets);
        System.out.println("closedBrackets = " + closedBrackets);
*/
        return openBrackets==closedBrackets;
    }



    public static void main (String [] args) {
           String typeFile = args[0];
           String disjointFile = args[1];
           String mappingsFile = args[2];
           CheckSumoLabels check = new CheckSumoLabels(typeFile,disjointFile);
           check.checkMappings(mappingsFile);
    }
}
