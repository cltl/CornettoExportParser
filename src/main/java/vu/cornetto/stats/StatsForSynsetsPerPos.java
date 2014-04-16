package vu.cornetto.stats;

import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;
import vu.cornetto.cdb.Syn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 28-aug-2008
 * Time: 13:21:03
 * To change this template use File | Settings | File Templates.
 */
public class StatsForSynsetsPerPos {

    /**
     * @TODO
     * - synsets with double LUs
     * - synsets with LUs with wrong POS
     * -
     * @param synset
     * @return
     */

        static public String getPos (CdbSynset synset) {
            String pos = "n";
            String c_sy_id = synset.getC_sy_id();
            if (!c_sy_id.startsWith("c_"))  {
                //n_n-533391
                pos = c_sy_id.substring(2,3);
            }
            else {
                for (int i=0;i<synset.getSynonyms().size();i++) {
                    Syn syn = (Syn) synset.getSynonyms().get(i);
                    String ludId = syn.getC_lu_id();
                    //   LU:r_n-31445
                    pos = ludId.substring(2,3);
                    break;
                }
            }
            if ( (pos.equals("n")) ||
                 (pos.equals("v")) ||
                 (pos.equals("a"))) {
                return pos;
            }
            else return "n";
        }

        public static void main (String args[]) {
            if (args.length!=1) {
                System.out.println("USAGE:");
                System.out.println("\targ1 = cdb_synset export file");
            }
            String cdbSynFilePath = args[0];
            CdbSynsetParser parser = new CdbSynsetParser();
            parser.parseFile(cdbSynFilePath);
            try {
                HashMap<String, Integer> idNounType = new HashMap<String, Integer>();
                HashMap<String, Integer> idVerbType = new HashMap<String, Integer>();
                HashMap<String, Integer> idAdjectiveType = new HashMap<String, Integer>();
                HashMap<String, Integer> idOtherType = new HashMap<String, Integer>();
                int nounSynsets = 0;
                int verbSynsets = 0;
                int adjSynsets = 0;
                int otherSynsets = 0;
                int nounRels = 0;
                int verbRels = 0;
                int adjRels = 0;
                int nounEqui = 0;
                int verbEqui = 0;
                int adjEqui = 0;
                int nounDefs = 0;
                int verbDefs = 0;
                int adjDefs = 0;
                int nounSumo = 0;
                int verbSumo = 0;
                int adjSumo = 0;
                int nounDomain = 0;
                int verbDomain = 0;
                int adjDomain = 0;

                Set keySet = parser.synsetIdSynsetMap.keySet();
                Iterator keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                    String pos = getPos(synset);
                    if (pos.equals("n")) {
                        nounSynsets++;
                        if (!synset.getDefinition().isEmpty()) {
                            nounDefs++;
                        }
                        nounDomain += synset.getDRelations().size();
                        nounEqui += synset.getERelations().size();
                        nounRels += synset.getIRelations().size();
                        nounSumo += synset.getSRelations().size();
                        String idSubstring = synset.getC_sy_id().substring(0,3);
                        if (idNounType.containsKey(idSubstring)) {
                            Integer cnt = idNounType.get(idSubstring);
                            cnt++;
                            idNounType.put(idSubstring, cnt);
                        }
                        else {
                            idNounType.put(idSubstring, 1);
                        }
                    }
                    else if (pos.equals("v")) {
                        verbSynsets++;
                        if (!synset.getDefinition().isEmpty()) {
                            verbDefs++;
                        }
                        verbDomain += synset.getDRelations().size();
                        verbEqui += synset.getERelations().size();
                        verbRels += synset.getIRelations().size();
                        verbSumo += synset.getSRelations().size();
                        String idSubstring = synset.getC_sy_id().substring(0,3);
                        if (idVerbType.containsKey(idSubstring)) {
                            Integer cnt = idVerbType.get(idSubstring);
                            cnt++;
                            idVerbType.put(idSubstring, cnt);
                        }
                        else {
                            idVerbType.put(idSubstring, 1);
                        }
                    }
                    else if (pos.equals("a")) {
                        adjSynsets++;
                        if (!synset.getDefinition().isEmpty()) {
                            adjDefs++;
                        }
                        adjDomain += synset.getDRelations().size();
                        adjEqui += synset.getERelations().size();
                        adjRels += synset.getIRelations().size();
                        adjSumo += synset.getSRelations().size();
                        String idSubstring = synset.getC_sy_id().substring(0,3);
                        if (idAdjectiveType.containsKey(idSubstring)) {
                            Integer cnt = idAdjectiveType.get(idSubstring);
                            cnt++;
                            idAdjectiveType.put(idSubstring, cnt);
                        }
                        else {
                            idAdjectiveType.put(idSubstring, 1);
                        }
                    }
                    else {
                        otherSynsets++;
                        String idSubstring = synset.getC_sy_id().substring(0,3);
                        if (idOtherType.containsKey(idSubstring)) {
                            Integer cnt = idOtherType.get(idSubstring);
                            cnt++;
                            idOtherType.put(idSubstring, cnt);
                        }
                        else {
                            idOtherType.put(idSubstring, 1);
                        }
                    }
                }
                FileOutputStream fos = new FileOutputStream(cdbSynFilePath+".pos.stat.tex");
                String str ="";

                str += "File:"+ new File(cdbSynFilePath).getName()+"\n";
                str += "Nr. of synsets:"+parser.synsetIdSynsetMap.size()+"\n";
                str += "Nr. of other synsets:"+otherSynsets+"\n";
                str += "&&ALL&NOUNS&VERBS&ADJECTIVES"+"\\\\\\hline\n";
                str += "Nr. of synsets&"+(nounSynsets+verbSynsets+adjSynsets)+"&"+nounSynsets+"&"+verbSynsets+"&"+adjSynsets+"\\\\hline\n";
                str += "Definitions&"+(nounDefs+verbDefs+adjDefs)+"&"+nounDefs+"&"+verbDefs+"&"+adjDefs+"\\\\hline\n";
                str += "Semantic relations&"+(nounRels+verbRels+adjRels)+"&"+nounRels+"&"+verbRels+"&"+adjRels+"\\\\hline\n";
                str += "Equivalence relations&"+(nounEqui+verbEqui+adjEqui)+"&"+nounEqui+"&"+verbEqui+"&"+adjEqui+"\\\\hline\n";
                str += "Wordnet domain relations&"+(nounDomain+verbDomain+adjDomain)+"&"+nounDomain+"&"+verbDomain+"&"+adjDomain+"\\\\hline\n";
                str += "Sumo relations&"+(nounSumo+verbSumo+adjSumo)+"&"+nounSumo+"&"+verbSumo+"&"+adjSumo+"\\\\\n";
                fos.write(str.getBytes());
                fos.close();

                fos = new FileOutputStream(cdbSynFilePath+".pos.stat.xls");
                str ="";

                str += "File\t"+ new File(cdbSynFilePath).getName()+"\n";
                str += "Nr. of synsets\t"+parser.synsetIdSynsetMap.size()+"\n";
                str += "Nr. of other synsets:"+otherSynsets+"\n";
                str += "\tALL\tNOUNS\tVERBS\tADJECTIVES"+"\n";
                str += "Nr. of synsets\t"+(nounSynsets+verbSynsets+adjSynsets)+"\t"+nounSynsets+"\t"+verbSynsets+"\t"+adjSynsets+"\n";
                str += "Definitions\t"+(nounDefs+verbDefs+adjDefs)+"\t"+nounDefs+"\t"+verbDefs+"\t"+adjDefs+"\n";
                str += "Semantic relations\t"+(nounRels+verbRels+adjRels)+"\t"+nounRels+"\t"+verbRels+"\t"+adjRels+"\n";
                str += "Equivalence relations\t"+(nounEqui+verbEqui+adjEqui)+"\t"+nounEqui+"\t"+verbEqui+"\t"+adjEqui+"\n";
                str += "Wordnet domain relations\t"+(nounDomain+verbDomain+adjDomain)+"\t"+nounDomain+"\t"+verbDomain+"\t"+adjDomain+"\n";
                str += "Sumo relations\t"+(nounSumo+verbSumo+adjSumo)+"\t"+nounSumo+"\t"+verbSumo+"\t"+adjSumo+"\n";
                fos.write(str.getBytes());

                str = "\nNoun synset types\n";
                fos.write(str.getBytes());
                keySet = idNounType.keySet();
                keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Integer cnt = idNounType.get(key);
                    str = key+"\t"+cnt+"\n";
                    fos.write(str.getBytes());
                }

                str = "\nVerb synset types\n";
                fos.write(str.getBytes());
                keySet = idVerbType.keySet();
                keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Integer cnt = idVerbType.get(key);
                    str = key+"\t"+cnt+"\n";
                    fos.write(str.getBytes());
                }

                str = "\nAdjective synset types\n";
                fos.write(str.getBytes());
                keySet = idAdjectiveType.keySet();
                keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Integer cnt = idAdjectiveType.get(key);
                    str = key+"\t"+cnt+"\n";
                    fos.write(str.getBytes());
                }

                str = "\nOther synset types\n";
                fos.write(str.getBytes());
                keySet = idOtherType.keySet();
                keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Integer cnt = idOtherType.get(key);
                    str = key+"\t"+cnt+"\n";
                    fos.write(str.getBytes());
                }
                fos.close();

            }
            catch (FileNotFoundException e) {

            }
            catch (IOException ee) {
                
            }
        }
}
