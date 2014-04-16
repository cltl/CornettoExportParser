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
public class LUsToClassifyDomain {

    public static void main (String args[]) {
        HashMap domainMap = new HashMap();
        String cdbSynFilePath = args[0];
        String luFilePath = args[1];
        CdbSynsetParser parser = new CdbSynsetParser();
        parser.parseFile(cdbSynFilePath);
        try {
            String str = "";
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                String trainData = "";
                for (int i = 0; i < synset.getSynonyms().size(); i++) {
                    Syn syn = (Syn) synset.getSynonyms().get(i);
                    trainData += " "+removeSenseTag(syn.getPreviewtext());
                }
                trainData += " "+synset.getDefinition();
                trainData += " "+synset.getDifferentiae();
                ArrayList relations = synset.getDRelations();
                for (int i=0; i<relations.size();i++) {
                      CdbDomainRelation rel = (CdbDomainRelation) relations.get(i);
                      String termString = rel.getTerm();
                      String [] tArray = termString.split(" ");
                      for (int t=0; t<tArray.length;t++) {
                          String term = tArray[t];
                          if (term.length()>0) {
                              if (domainMap.containsKey(term)) {
                                  ArrayList targets = (ArrayList) domainMap.get(term);
                                  if (!targets.contains(trainData)) {
                                      targets.add(trainData);
                                      domainMap.put(term, targets);
                                  }
                              }
                              else {
                                      ArrayList targets = new ArrayList();
                                      targets.add(trainData);
                                      domainMap.put(term, targets);
                              }
                          }
                      }
                }
            }
            System.out.println("domainMap = " + domainMap.size());
            keySet = domainMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                FileOutputStream out = new FileOutputStream (key+".txt");
                str = key;
                ArrayList targets = (ArrayList) domainMap.get(key);
                for (int t=0; t<targets.size();t++) {
                    String target = (String) targets.get(t);
                    str += target+" ";
                }
                str = str.trim()+"\n";
                out.write(str.getBytes());
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    static String removeSenseTag (String previewText) {
        String str = previewText;
        int idx = previewText.lastIndexOf(":");
        if (idx>-1) {
            str = previewText.substring(0, idx);
        }
        return str;
    }
}