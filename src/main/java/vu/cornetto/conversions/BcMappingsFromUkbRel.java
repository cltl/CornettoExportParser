package vu.cornetto.conversions;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: 2/3/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class BcMappingsFromUkbRel {
    static int level = 0;

    public static ArrayList<String> readBaseConcepts (String filePath) {
        /*
eng-30-02927512-a sc_subclassOf PartAdjGeo
eng-30-00146210-a sc_subclassOf AdjConflict
eng-30-14010927-n sc_subclassOf ActionNomConflict
eng-30-14010927-n
         */
        ArrayList<String> baseConcepts = new ArrayList<String>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.trim().length()>0) {
                    String fields [] = inputLine.split(" ");
                    if (fields.length>0) {
                        String bc = fields[0].trim();
                        if (!baseConcepts.contains(bc)) {
                            baseConcepts.add(bc);
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseConcepts;

    }


    public static HashMap<String, ArrayList<String>> readUkbRelaltions (String filePath) {
        /*
u:ENG-30-00001930-n v:ENG-30-00001740-n s:30 d:0
u:ENG-30-00002137-n v:ENG-30-00001740-n s:30 d:0
u:ENG-30-00002452-n v:ENG-30-00001930-n s:30 d:0
         */
        /// plain list wth selected BCs, e.g. using most common subsumers based on kaf-annotated tag file with synsets and tags
        HashMap<String, ArrayList<String>> relations = new HashMap<String, ArrayList<String>>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.trim().length()>0) {
                    String fields [] = inputLine.split(" ");
                    if (fields.length>=2) {
                        String source = fields[0].trim().substring(2).toLowerCase();
                        String target = fields[1].trim().substring(2).toLowerCase();
                        String sPos = source.substring(source.length()-1);
                        String tPos = target.substring(target.length()-1);
                        if (sPos.equals(tPos)) {
                            if (relations.containsKey(target)) {
                                ArrayList<String> children = relations.get(target);
                                children.add(source);
                                relations.put(target, children);
                            }
                            else {
                                ArrayList<String> children = new ArrayList<String>();
                                children.add(source);
                                relations.put(target, children);
                            }
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relations;

    }
    
    static void outputMappings (FileOutputStream fos, HashMap<String, ArrayList<String>> relations, String bc, String target) throws IOException {
        System.out.println("level = " + level);
        System.out.println("target = " + target);
        if (relations.containsKey(target)) {
            ArrayList<String> children = relations.get(target);
            for (int i = 0; i < children.size(); i++) {
                String c = children.get(i);
                String str = c+" "+bc+"\n";
                fos.write(str.getBytes());
                level++;
                outputMappings(fos, relations, bc, c);
                level--;
            }
        }
    }
    
    static public void main (String [] args) {
        try {
            String bcFilePath = args[0];
            String ukbFilePath = args[1];
            ArrayList<String> bcs = readBaseConcepts(bcFilePath);
            HashMap<String, ArrayList<String>> relations = readUkbRelaltions(ukbFilePath);
            FileOutputStream fos = new FileOutputStream( bcFilePath+".bcmp.txt");
            for (int i = 0; i < bcs.size(); i++) {
                String bc = bcs.get(i);
                System.out.println("bc = " + bc);
                level = 0;
                outputMappings(fos, relations, bc, bc);
             }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    
}
