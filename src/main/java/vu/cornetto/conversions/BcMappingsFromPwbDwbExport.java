package vu.cornetto.conversions;

import vu.cornetto.pwn.PwnSaxParser;
import vu.cornetto.pwn.PwnUtilities;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: 2/3/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class BcMappingsFromPwbDwbExport {

    public static HashMap<String, ArrayList<String>> readBaseConcepts (String filePath) {
        /*
eng-30-02927512-a sc_subclassOf PartAdjGeo
eng-30-00146210-a sc_subclassOf AdjConflict
eng-30-14010927-n sc_subclassOf ActionNomConflict
eng-30-14010927-n
         */
        HashMap<String, ArrayList<String>> baseConcepts = new HashMap<String, ArrayList<String>>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.trim().length()>0) {
                    String fields [] = inputLine.split(" ");
                    if (fields.length ==0) {
                        fields = inputLine.split("\t");
                    }
                    if (fields.length==3) {
                        String bc = fields[0].trim();
                        String rel = fields[1].trim();
                        String ont = fields[2].trim();
                        String relOInt = rel+" "+ont;
                        System.out.println("bc = " + bc);
                        if (baseConcepts.containsKey(bc)) {
                            ArrayList<String> onts = baseConcepts.get(bc);
                            if (!onts.contains(relOInt)) {
                                onts.add(relOInt);
                                baseConcepts.put(bc, onts);
                            }
                        }
                        else {
                            ArrayList<String> onts = new ArrayList<String>();
                            onts.add(relOInt);
                            baseConcepts.put(bc,onts);
                        }
                    }
                    else if (fields.length==2) {
                        String bc = fields[0].trim();
                        String ont = fields[1].trim();
                        System.out.println("bc = " + bc);

                        if (baseConcepts.containsKey(bc)) {
                            ArrayList<String> onts = baseConcepts.get(bc);
                            if (!onts.contains(ont)) {
                                onts.add(ont);
                                baseConcepts.put(bc, onts);
                            }
                        }
                        else {
                            ArrayList<String> onts = new ArrayList<String>();
                            onts.add(ont);
                            baseConcepts.put(bc,onts);
                        }
                    }
                    else {
                        System.out.println("inputLine = " + inputLine);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseConcepts;

    }



    static public void main (String [] args) {
        try {
            boolean DEBUG = true;
            String bcFilePath = args[0];
            String pwnFilePath = args[1];
            FileOutputStream fos1 = new FileOutputStream( bcFilePath+".bcmp.txt");
            FileOutputStream fos2 = new FileOutputStream( bcFilePath+".ontmp.txt");
            HashMap<String, ArrayList<String>> bcs = readBaseConcepts(bcFilePath);
            System.out.println("bcs.size() = " + bcs.size());
            PwnSaxParser parser = new PwnSaxParser();
            parser.parseFile(pwnFilePath);
            Set keySet = parser.hyperRelations.keySet();
            System.out.println("pwn keySet.size() = " + keySet.size());
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                //System.out.println("key = " + key);
                //>eng-30-00007846-n synset for person
                //eng-30-10622053-n synset for soldier

                if (key.equals("eng-30-00007846-n")) {
                  //  System.out.println("people key = " + key);
                  //  DEBUG = true;
                }
                else if (key.equals("eng-30-10622053-n")) {
                   // System.out.println("soldier:1 key = " + key);
                  //  DEBUG = true;
                }
                else if (key.equals("eng-30-15231263-n")) {
                    System.out.println(" key = " + key);
                    DEBUG = true;
                }
                else {
                    DEBUG = false;
                }

                ArrayList<String> hyperChain = new ArrayList<String>();
                PwnUtilities.getRelationChain(parser.hyperRelations, key, hyperChain);
                if (DEBUG) System.out.println("hyperChain = " + hyperChain);
                for (int i = 0; i < hyperChain.size(); i++) {
                    String h = hyperChain.get(i);
                    if (DEBUG) System.out.println("h = " + h);
                    if (bcs.containsKey(h)) {
                        String str = key+" "+h+"\n";
                        if (DEBUG) System.out.println("str = " + str);
                        fos1.write(str.getBytes());
                        ArrayList<String> onts = bcs.get(h);
                        for (int j = 0; j < onts.size(); j++) {
                            String relOInt = onts.get(j);
                            str = key+" "+ relOInt+"\n";
                            fos2.write(str.getBytes());
                        }
                       // break;
                    }
                }
            }
            /// we still need to add the bcs to the mapping
            keySet = bcs.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String bc = (String) keys.next();
                ArrayList<String> onts = bcs.get(bc);
                for (int j = 0; j < onts.size(); j++) {
                    String relOInt = onts.get(j);
                    String str = bc+" "+ relOInt+"\n";
                    fos2.write(str.getBytes());
                    //// next line adds the bcs to their own synset as bc. This
                    str = bc + " "+bc+"\n";
                    fos1.write(str.getBytes());
                }
            }
            fos1.close();
            fos2.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    
}
