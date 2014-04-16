package vu.cornetto.conversions;

import vu.cornetto.cdb.CdbInternalRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;
import vu.cornetto.cdb.Syn;

import java.io.FileOutputStream;
import java.io.IOException;
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
public class ExportLemmaDepthScore {



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
            pos = "n";
        }
        else if (id.indexOf("_v-")>-1) {
            pos = "v";
        }
        else if (id.indexOf("_a-")>-1) {
            pos = "a";
        }
        else if (id.indexOf("_r-")>-1) {
            pos = "r";
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
            FileOutputStream fos = new FileOutputStream(cdbSynFilePath+".depth.xml");
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
                   str += "<lexicon>\n";
            fos.write(str.getBytes());

            int maxDepthNouns = 0;
            int maxDepthVerbs = 0;
            int maxDepthAdjectives = 0;
            int avgDepthNouns = 0;
            int avgDepthVerbs = 0;
            int avgDepthAdjectives = 0;
            int nNouns = 0;
            int nVerbs = 0;
            int nAdjectives = 0;
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset)parser.synsetIdSynsetMap.get(key);
                String pos = synset.getPos();
                ArrayList<String> chainList = new ArrayList<String>();
                getHypernymChain(chainList, parser, key);
                if (pos.equalsIgnoreCase("noun")) {
                    nNouns++;
                    avgDepthNouns += chainList.size();
                    if (chainList.size()>maxDepthNouns) {
                        maxDepthNouns = chainList.size();
                    }
                }
                else if (pos.equalsIgnoreCase("verb")) {
                    nVerbs++;
                    avgDepthVerbs += chainList.size();
                    if (chainList.size()>maxDepthVerbs) {
                        maxDepthVerbs = chainList.size();
                    }
                }
                else if (pos.equalsIgnoreCase("adjective")) {
                    avgDepthAdjectives+= chainList.size();
                    nAdjectives++;
                    if (chainList.size()>maxDepthAdjectives) {
                        maxDepthAdjectives = chainList.size();
                    }
                }
                else if (pos.equalsIgnoreCase("adj")) {
                    avgDepthAdjectives+= chainList.size();
                    nAdjectives++;
                    if (chainList.size()>maxDepthAdjectives) {
                        maxDepthAdjectives = chainList.size();
                    }
                }
            }
            avgDepthNouns = avgDepthNouns/nNouns;
            avgDepthVerbs = avgDepthVerbs/nVerbs;
            avgDepthAdjectives = avgDepthAdjectives/nAdjectives;

            str ="<maxdepth noun=\""+maxDepthNouns+"\" verb=\""+maxDepthVerbs+"\" adjective=\""+maxDepthAdjectives+"\"/>\n";
            str +="<averagedepth noun=\""+avgDepthNouns+"\" verb=\""+avgDepthVerbs+"\" adjective=\""+avgDepthAdjectives+"\"/>\n";
            fos.write(str.getBytes());
            keySet = parser.synsetIdSynsetMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset)parser.synsetIdSynsetMap.get(key);
                String pos = synset.getPos();
                if (pos.equalsIgnoreCase("noun")) {
                    pos = "n";
                }
                else if (pos.equalsIgnoreCase("verb")) {
                    pos = "v";
                }
                else if (pos.equalsIgnoreCase("adjective")) {
                    pos = "a";
                }
                else if (pos.equalsIgnoreCase("adj")) {
                    pos = "a";
                }
                else {
                    pos = getPosFromId(key);
                }
                ArrayList<String> chainList = new ArrayList<String>();
                getHypernymChain(chainList, parser, key);
                Double maxscore = new Double(0);
                Double avgscore = new Double(0);
                if (chainList.size()>0) {
                   // score = (1.0-(1.0/chainList.size()));
                    if (pos.equals("n")) {
                        maxscore = 1+ java.lang.Math.log10(((double) chainList.size() / (double) maxDepthNouns));
                        avgscore = ((double)(chainList.size()+1)/(double)((2*avgDepthNouns)+1));
                    }
                    else if (pos.equals("v")) {
                        maxscore =  1+ java.lang.Math.log10(((double)chainList.size()/(double)maxDepthVerbs));
                        avgscore = ((double)(chainList.size()+1)/(double)((2*avgDepthVerbs)+1));
                    }
                    else if (pos.equals("a")) {
                        maxscore =  1+ java.lang.Math.log10(((double)chainList.size()/(double)maxDepthAdjectives));
                        avgscore = ((double)(chainList.size()+1)/(double)((2*avgDepthAdjectives)+1));
                    }
                }
                for (int i = 0; i < synset.getSynonyms().size(); i++) {
                    Syn synonym = (Syn) synset.getSynonyms().get(i);
                    str = "<entry lemma=\""+synonym.getForm()+"\" pos=\""+pos+"\" depth=\""+chainList.size()
                            +"\" max_depth_proportion=\""+maxscore
                            +"\" avg_depth_proportion=\""+avgscore+"\"/>\n";
                    fos.write(str.getBytes());
                }
            }
            str = "</lexicon>\n";
            fos.write(str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
}
