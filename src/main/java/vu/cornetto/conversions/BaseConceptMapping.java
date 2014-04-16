package vu.cornetto.conversions;

import vu.cornetto.cdb.CdbInternalRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;
import vu.cornetto.cdb.Syn;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: Nov 8, 2010
 * Time: 9:20:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class BaseConceptMapping {


    public static ArrayList<String> readBaseConcepts (String filePath) {
        /*
d_n-20089
d_n-20594
d_n-21525
d_n-21611
d_n-21983
d_n-22485
d_n-22494
d_n-22744
d_n-23431
d_n-23617
d_n-24294
d_n-24443
         */
        /// plain list wth selected BCs, e.g. using most common subsumers based on kaf-annotated tag file with synsets and tags
        ArrayList<String> baseConcepts = new ArrayList<String>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.trim().length()>0) {
                    baseConcepts.add(inputLine.trim());
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseConcepts;

    }

    static public int getHypernymChain ( ArrayList<String> chainList, CdbSynsetParser parser, String key) {
        CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
        if (synset!=null) {
            ArrayList<CdbInternalRelation> relations = synset.getIRelations();
            for (int i=0; i<relations.size();i++) {
                  CdbInternalRelation rel = relations.get(i);
                  if (rel.getRelation_name().equalsIgnoreCase("has_hyperonym")) {
                      if (rel.getTarget().length()>0) {
                          if (chainList.contains(rel.getTarget())) {
                              /////// duplicate
                          }
                          else {
                              chainList.add(rel.getTarget());
                              if (getHypernymChain(chainList, parser, rel.getTarget()) == -1) {
                                  String str = "";
                                  for (int j = 0; j < synset.getSynonyms().size(); j++) {
                                      Syn syn = (Syn) synset.getSynonyms().get(j);
                                      str += syn.getPreviewtext()+";";
                                  }
                                  str += "#"+rel.getRelation_name()+":"+rel.getTarget();
                                  System.out.println(str);
                              }
                          }
                      }
                  }
            }
        }
        else {
           // System.out.println("no synset for key = " + key);
            return -1;
        }
        return 0;
    }
    static public String getPosFromId (String id) {
        String pos = "";
        if (id.indexOf("_n-")>-1) {
            pos = "-n";
        }
        else if (id.indexOf("_v-")>-1) {
            pos = "-v";
        }
        else if (id.indexOf("_a-")>-1) {
            pos = "-a";
        }
        else if (id.indexOf("_r-")>-1) {
            pos = "-r";
        }
        return pos;
    }

    static public void main (String[] args) {
        /// Generates a mapping from a synset to a base concept by building the hypernym chain from the Cornetto synset export
        /// For each synset it creates the full hypernym chain. If the chain

        //arg[0] "/Projects/Cornetto/Results/DATA V132/cdb_syn1.3.2.xml"
        //arg[1] "/Users/kyoto/Desktop/srebrenica/historical_bcs.txt"
        try {
            String cdbSynFilePath = args[0];
            CdbSynsetParser parser = new CdbSynsetParser();
            parser.parseFile(cdbSynFilePath);
            String baseConceptFile = args[1];
            ArrayList<String> baseConcepts = readBaseConcepts(baseConceptFile);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            FileOutputStream fos = new FileOutputStream(baseConceptFile+".mapping.txt");
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset)parser.synsetIdSynsetMap.get(key);
                String pos = synset.getPos();
                if (pos.equalsIgnoreCase("noun")) {
                    pos = "-n";
                }
                else if (pos.equalsIgnoreCase("verb")) {
                    pos = "-v";
                }
                else if (pos.equalsIgnoreCase("adjective")) {
                    pos = "-v";
                }
                else if (pos.equalsIgnoreCase("adj")) {
                    pos = "-v";
                }
                else {
                    pos = getPosFromId(key);
                }
                ArrayList<String> chainList = new ArrayList<String>();
                getHypernymChain(chainList, parser, key);
                for (int i = 0; i < chainList.size(); i++) {
                    String s = chainList.get(i);
                    if (baseConcepts.contains(s)) {
                       if (!pos.startsWith("-")) {
                           pos = getPosFromId(s);
                       }
                       String str = key+pos+" "+s+pos+"\n";
                       fos.write(str.getBytes());
                    }
                }
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
}
