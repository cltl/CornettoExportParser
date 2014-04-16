package vu.cornetto.conversions;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: Nov 8, 2010
 * Time: 9:20:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class BaseConceptOntologyMapping {


    public static HashMap<String,ArrayList<String>> readBaseConceptOntology (String filePath) {
        /*
        d_n-18658-n sc_subClassOf ParticipantInfoSource
d_n-19123-n sc_subClassOf ActionHistoricalNominalization
d_n-19123-n sc_subClassOf ObjectHistorical
         */
        HashMap<String,ArrayList<String>> baseConcepts = new HashMap<String,ArrayList<String>>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.trim().length()>0) {
                   String [] fields = inputLine.split(" ");
                    if (fields.length==3) {
                       if (baseConcepts.containsKey(fields[0])) {
                          ArrayList<String> onts = baseConcepts.get(fields[0]);
                          if (!onts.contains(fields[2])) {
                              onts.add(fields[2]);
                              baseConcepts.put(fields[0], onts);
                          }
                       }
                       else {
                           ArrayList<String> onts= new ArrayList<String>();
                           onts.add(fields[2]);
                           baseConcepts.put(fields[0], onts);
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


    static public void main (String[] args) {
        try {
            // reads a file where bcs are mapped to ontology classes and a file where synsets are mapped to BCs
            // creates a file where all synsets are mapped to the ontology
            // args[0] "/Users/kyoto/Desktop/srebrenica/historical_bcs.onto.txt"
            //args[1]"/Users/kyoto/Desktop/srebrenica/historical_bcs.txt.mapping.txt"
            String bcOntologyFile = args[0];
            String synsetBcFile = args[1];
            HashMap<String,ArrayList<String>> baseConcepts = readBaseConceptOntology(bcOntologyFile);
            FileOutputStream fos = new FileOutputStream(synsetBcFile+".ont.txt");
            FileInputStream fis = new FileInputStream(synsetBcFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.trim().length()>0) {
                   String [] fields = inputLine.split(" ");
                    if (fields.length==2) {
                       if (baseConcepts.containsKey(fields[1])) {
                          ArrayList<String> onts = baseConcepts.get(fields[1]);
                           for (int i = 0; i < onts.size(); i++) {
                               String o = onts.get(i);
                               String str = fields[0]+" sc_subClassOf "+o+"\n";
                               fos.write(str.getBytes());
                           }
                       }
                    }
                }
            }
            in.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}