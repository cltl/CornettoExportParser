package vu.cornetto.sumo;

import vu.cornetto.util.FileProcessor;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 28-mrt-2008
 * Time: 6:32:35
 * To change this template use File | Settings | File Templates.
 */
public class KifParser {
    static public HashMap relations = new HashMap();

    //
    public static void processKifFolder (String folderPath) {
        try {
            relations = new HashMap();
            FileOutputStream os = new FileOutputStream (folderPath+"/kif.relations");
            String [] kifFiles = FileProcessor.makeRecursiveFileList(folderPath, ".kif.txt");
            if (kifFiles.length==0) {
                kifFiles = FileProcessor.makeRecursiveFileList(folderPath, ".kif");                
            }
            for (int i=0; i<kifFiles.length;i++) {
                String kifFile = kifFiles[i];
                System.out.println("kifFile = " + kifFile);
                processKifFile (kifFile);
            }
            Set keySet = relations.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList triplets = (ArrayList) relations.get(key);
                if (triplets.size()>1) {
                    for (int j=0; j<triplets.size();j++) {
                        String triplet = (String) triplets.get(j);
                        FileProcessor.storeResult(os, triplets.size()+"\t"+triplet+"\n");
                    }
                }
            }
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    public static void processKifFile (String inputPath) {
        try {  //D:\Projects\Cornetto\Exports\Nov 2007\cdb_syn2.xml.lex.out

                FileInputStream isLexicon = new FileInputStream (inputPath);
                InputStreamReader isr = new InputStreamReader(isLexicon, "UTF-8");
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                while (in.ready()&&(inputLine = in.readLine()) != null) {
                    if (inputLine.indexOf(";")==-1) {
                        int idx_1 = inputLine.indexOf("(");
                        int idx_2 = inputLine.indexOf(" ",idx_1+1);
                        if ((idx_2 > idx_1) && (idx_1> -1) && balancedBrackets(inputLine)) {
                            String key = inputLine.substring(idx_1+1, idx_2).trim();
                            String triplet = inputLine.substring(idx_1).trim();
                            if (isRelation(triplet)) {
                                if (relations.containsKey(key)) {
                                    ArrayList triplets = (ArrayList) relations.get(key);
                                    triplets.add(triplet);
                                    relations.put(key, triplets);
                                    //System.out.println("triplet = " + triplet);
                                }
                                else {
                                    ArrayList triplets = new ArrayList ();
                                    triplets.add(triplet);
                                    relations.put(key, triplets);
                                    //System.out.println("triplet = " + triplet);
                                }
                            }
                        }
                    }
                    else {
                        //System.out.println("inputLine = " + inputLine);
                    }
                }
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
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

    static boolean isRelation (String line) {
         String relation = "abcdefghijklmnopqrstuvwxyz";
         if ((line.startsWith("(")) && (line.endsWith(")"))) {
             if (relation.indexOf(line.charAt(1))!=-1) {
                 if (line.indexOf(" ")!=-1) {
                     return true;
                 }
                 else {
                     return false;
                 }
             }
             else {
                 return false;
             }
         }
         else {
             return false;
         }
    }
    
    static public void main (String [] args) {
        //D:\Projects\SUMO\KB
        processKifFolder(args[0]);
    }
}
