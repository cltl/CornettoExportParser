package vu.cornetto.conversions;

import vu.cornetto.cdb.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 22-aug-2008
 * Time: 7:50:42
 * To change this template use File | Settings | File Templates.
 */
public class SynsetsToWsdDomain {

    public static void main (String args[]) {
        HashMap domainMap = new HashMap();
        String cdbSynFilePath = args[0];
        String outputFilePath = cdbSynFilePath+".domain.txt";
        String outputFilePath2 = "dom_graph.txt";
        CdbSynsetParser parser = new CdbSynsetParser();
        parser.parseFile(cdbSynFilePath);
        try {
            String str = "";
            FileOutputStream out = new FileOutputStream (outputFilePath);
            FileOutputStream out2 = new FileOutputStream (outputFilePath2);
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                //// create the proper keys
                String c_sy_id = " v:"+synset.getC_sy_id();
                if (synset.getPos().toLowerCase().equals("noun")) {
                     c_sy_id += "-n";
                }
                else if (synset.getPos().toLowerCase().equals("verb")) {
                     c_sy_id += "-v";
                 }
                else if (synset.getPos().toLowerCase().equals("adjective")) {
                     c_sy_id += "-a";
                 }
                else if (synset.getPos().toLowerCase().equals("adj")) {
                     c_sy_id += "-a";
                 }
                else if (synset.getPos().toLowerCase().equals("adverb")) {
                     c_sy_id += "-b";
                 }
                else if (synset.getPos().toLowerCase().equals("adv")) {
                     c_sy_id += "-b";
                 }
                else if (synset.getPos().length()==0) {
                     c_sy_id += "-n";
                 }
                /// get the domain relations and add synset to the domain hashmap
                ArrayList relations = synset.getDRelations();
                for (int i=0; i<relations.size();i++) {
                      CdbDomainRelation rel = (CdbDomainRelation) relations.get(i);
                      String termString = rel.getTerm();
                      String [] tArray = termString.split(" ");
                      for (int t=0; t<tArray.length;t++) {
                          String term = tArray[t];
                          if (term.length()>0) {
                              if (!term.equalsIgnoreCase("factotum")) {
                                  if (domainMap.containsKey(term)) {
                                      ArrayList targets = (ArrayList) domainMap.get(term);
                                      if (!targets.contains(c_sy_id)) {
                                          targets.add(c_sy_id);
                                          domainMap.put(term, targets);
                                      }
                                  }
                                  else {
                                          ArrayList targets = new ArrayList();
                                          targets.add(c_sy_id);
                                          domainMap.put(term, targets);
                                  }
                              }
                          }
                      }
                }
            }
            /// write the domain + synset IDs to domain-synset file
            keySet = domainMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                str = key;
                ArrayList targets = (ArrayList) domainMap.get(key);
                for (int t=0; t<targets.size();t++) {
                    String target = (String) targets.get(t);
                    str += target+" ";
                }
                str = str.trim()+"\n";
                out.write(str.getBytes());                
            }
            out.close();
            System.out.println("domainMap = " + domainMap.size());
            //// convert the domain map to synset relation file which is the input for the  graph
            int count = 0;
            keySet = parser.synsetIdSynsetMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                count++;
                if (count%500==0) {
                    System.out.println("count = " + count);
                }
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                String c_sy_id = "u:"+synset.getC_sy_id();
                if (synset.getPos().toLowerCase().equals("noun")) {
                     c_sy_id += "-n";
                }
                else if (synset.getPos().toLowerCase().equals("verb")) {
                     c_sy_id += "-v";
                 }
                else if (synset.getPos().toLowerCase().equals("adjective")) {
                     c_sy_id += "-a";
                 }
                else if (synset.getPos().toLowerCase().equals("adj")) {
                     c_sy_id += "-a";
                 }
                else if (synset.getPos().toLowerCase().equals("adverb")) {
                     c_sy_id += "-b";
                 }
                else if (synset.getPos().toLowerCase().equals("adv")) {
                     c_sy_id += "-b";
                 }
                else if (synset.getPos().length()==0) {
                     c_sy_id += "-n";
                 }
                ArrayList relations = synset.getDRelations();
                for (int i=0; i<relations.size();i++) {
                      CdbDomainRelation rel = (CdbDomainRelation) relations.get(i);
                      String term = rel.getTerm();
                      if (term.length()>0) {
                          if (!term.equalsIgnoreCase("factotum")) {
                              if (domainMap.containsKey(term)) {
                                  ArrayList targets = (ArrayList) domainMap.get(term);
                                  for (int t=0; t<targets.size();t++) {
                                      String target = (String) targets.get(t);
                                      str = c_sy_id+target+"\n";
                                      out2.write(str.getBytes());
                                  }
                              }
                          }
                      }
                }
            }
            //fixed = "</cids>\n";
            //out.write(fixed.getBytes("UTF-8"));
            //out.write(fixed.getBytes());
            out2.flush();
            out2.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}