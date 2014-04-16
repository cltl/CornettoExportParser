package vu.cornetto.cdb;

import org.xml.sax.helpers.DefaultHandler;
import vu.cornetto.util.FileProcessor;

import java.io.*;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 11-jul-2007
 * Time: 18:01:13
 * To change this template use File | Settings | File Templates.
 */
public class ExportSumoLabels extends DefaultHandler {

        static int getIndexForSumo (String value, ArrayList sumos) {
            for (int i=0; i<sumos.size();i++) {
                vu.cornetto.sumo.SumoRelation sumo = (vu.cornetto.sumo.SumoRelation) sumos.get(i);
                if (sumo.getArg2().equals(value)) {
                    return i;
                }
            }
            return -1;
        }

        static String synsetToOutput (CdbSynset synset, String pos) {
            String str = "";
            for (int i=0; i<synset.synonyms.size();i++) {
                Syn syn = (Syn) synset.synonyms.get(i);
                str = syn.previewtext+"\t" ;
            }
            str += pos+"\t";
            str += synset.eRelations.size()+"\t";
            TreeSet sorter = new TreeSet();
            ArrayList unsortedsumos = synset.getSRelations();
            for (int i=0; i<unsortedsumos.size();i++) {
                vu.cornetto.sumo.SumoRelation sumo = (vu.cornetto.sumo.SumoRelation) unsortedsumos.get(i);
                sorter.add(sumo.getArg2());
            }

            str += unsortedsumos.size()+"\t(";
            String complexPattern = "";
            Iterator iter = sorter.iterator();
            while (iter.hasNext()) { //  gtreeNode childNode = findFirstNodeByName(keys[i]);
                String arg2 = (String) iter.next();
                int i = getIndexForSumo(arg2, unsortedsumos);
                vu.cornetto.sumo.SumoRelation sumo = (vu.cornetto.sumo.SumoRelation) unsortedsumos.get(i);
                str += sumo.getRelation()+sumo.getArg2()+".";
                if ((!sumo.getRelation().equalsIgnoreCase("+")) &&
                    (!sumo.getRelation().equalsIgnoreCase("@")) &&
                    (!sumo.getRelation().equalsIgnoreCase("[")) &&
                    (!sumo.getRelation().equalsIgnoreCase("]")) &&
                    (!sumo.getRelation().equalsIgnoreCase("="))) {
                    complexPattern += sumo.getRelation()+"#"+sumo.getArg1()+"#"+sumo.getArg2()+"#"+sumo.getName()+"#"+sumo.getStatus()+"\t";
                }
            }
            str += ")\t";
            if (complexPattern.length()>0) {
                str += complexPattern+"\t";
            }
            str += "\n";
            return str;
        }
        ///* This function reads a synset file and stores CdbSynset objects in a hashmap
        ///
        public static void main(String[] args) throws IOException {
            // ENCODING = UTF8;
            try {

                String cdbSynsetFile = args[0];
                FileOutputStream importOutFile = new FileOutputStream(cdbSynsetFile+".ont.xls");
                FileOutputStream importOutFileallN = new FileOutputStream(cdbSynsetFile+".ontallNoun.csv");
                FileOutputStream importOutFileallV = new FileOutputStream(cdbSynsetFile+".ontallVerb.csv");
                FileOutputStream importOutFileallA = new FileOutputStream(cdbSynsetFile+".ontallAdjective.csv");
                FileOutputStream importOutFile2 = new FileOutputStream(cdbSynsetFile+".ont2.csv");
                FileOutputStream importOutFile3 = new FileOutputStream(cdbSynsetFile+".ont3.csv");
                FileOutputStream importOutFile4 = new FileOutputStream(cdbSynsetFile+".ont4plus.csv");
                OutputStreamWriter synsetOut = new OutputStreamWriter(importOutFile, "UTF-8");
                OutputStreamWriter synsetOutAllN = new OutputStreamWriter(importOutFileallN, "UTF-8");
                OutputStreamWriter synsetOutAllV = new OutputStreamWriter(importOutFileallV, "UTF-8");
                OutputStreamWriter synsetOutAllA = new OutputStreamWriter(importOutFileallA, "UTF-8");
                OutputStreamWriter synsetOut2 = new OutputStreamWriter(importOutFile2, "UTF-8");
                OutputStreamWriter synsetOut3 = new OutputStreamWriter(importOutFile3, "UTF-8");
                OutputStreamWriter synsetOut4 = new OutputStreamWriter(importOutFile4, "UTF-8");
                String str = "";
                String heading = "preview\tpos\tequiCount\tsumoCount\tsumoPattern\tnr of disjoints\taverage distance\tcomplexRelation\n";
                FileProcessor.storeResult(synsetOutAllN, heading);
                FileProcessor.storeResult(synsetOutAllV, heading);
                FileProcessor.storeResult(synsetOutAllA, heading);
                FileProcessor.storeResult(importOutFile2, heading);
                FileProcessor.storeResult(importOutFile3, heading);
                FileProcessor.storeResult(importOutFile4, heading);
                CdbSynsetParser cdbSynsetParser = new CdbSynsetParser();
                cdbSynsetParser.parseFile(cdbSynsetFile);
                //cdbSynsetParser.parseFileAsBytes(cdbSynsetFile);
                //cdbSynsetParser.parseFile(cdbSynsetFile, "UTF-8");
                Set keySet = cdbSynsetParser.synsetIdSynsetMap.keySet();
                Iterator keys = keySet.iterator();
                double totalScoreNouns2 = 0;
                double totalScoreNouns3 = 0;
                double totalScoreNouns4 = 0;
                double totalScoreVerbs2 = 0;
                double totalScoreVerbs3 = 0;
                double totalScoreVerbs4 = 0;
                double totalScoreAdjectives2 = 0;
                HashMap scoredMap2 = new HashMap();
                HashMap scoredMap3 = new HashMap();
                HashMap scoredMap4 = new HashMap();
                int nEqui0=0;
                int nEqui1=0;
                int nEqui2=0;
                int nEqui3=0;
                int nEqui4=0;
                int aEqui0=0;
                int aEqui1=0;
                int aEqui2=0;
                int aEqui3=0;
                int aEqui4=0;
                int vEqui0=0;
                int vEqui1=0;
                int vEqui2=0;
                int vEqui3=0;
                int vEqui4=0;
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    CdbSynset synset = (CdbSynset) cdbSynsetParser.synsetIdSynsetMap.get(key);
                    String pos = "";
                    int idx = synset.getPosSpecific().indexOf("_");
                    if (idx>-1) {
                        pos = synset.getPosSpecific().substring(0, idx);
                    }
                    else {
                        if (synset.getC_sy_id().indexOf("_n-")>-1) {
                            pos = "NOUN";
                        }
                        else if (synset.getC_sy_id().indexOf("_v-")>-1) {
                            pos = "VERB";
                        }
                        else if (synset.getC_sy_id().indexOf("_a-")>-1) {
                            pos = "ADJECTIVE";
                        }
                        else if (synset.getC_sy_id().indexOf("_r-")>-1) {
                            pos = "ADVERB";
                        }
                    }
                    if (synset.getSRelations().size()>3) {
                        str = synsetToOutput(synset, pos);
                        FileProcessor.storeResult(synsetOut4, str);
                        if (pos.startsWith("NOUN")) {
                            nEqui4++;
                            FileProcessor.storeResult(synsetOutAllN, str);
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui4++;
                            FileProcessor.storeResult(synsetOutAllA, str);
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui4++;
                            FileProcessor.storeResult(synsetOutAllV, str);
                        }
                    }
                    else if (synset.getSRelations().size()==0) {
                        //str = synsetToOutput(synset, pos);
                        if (pos.startsWith("NOUN")) {
                            nEqui0++;
                            //FileProcessor.storeResult(synsetOutAllN, str);
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui0++;
                            //FileProcessor.storeResult(synsetOutAllA, str);
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui0++;
                            //FileProcessor.storeResult(synsetOutAllV, str);
                        }
                        nEqui0++;
                    }
                    else if (synset.getSRelations().size()==1) {
                        str = synsetToOutput(synset, pos);
                        if (pos.startsWith("NOUN")) {
                            nEqui1++;
                            FileProcessor.storeResult(synsetOutAllN, str);
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui1++;
                            FileProcessor.storeResult(synsetOutAllA, str);
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui1++;
                            FileProcessor.storeResult(synsetOutAllV, str);
                        }
                        nEqui1++;
                    }
                    else if (synset.getSRelations().size()==2) {
                        str = synsetToOutput(synset, pos);
                        FileProcessor.storeResult(synsetOut2, str);
                        if (pos.startsWith("NOUN")) {
                            nEqui2++;
                            FileProcessor.storeResult(synsetOutAllN, str);
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui2++;
                            FileProcessor.storeResult(synsetOutAllA, str);
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui2++;
                            FileProcessor.storeResult(synsetOutAllV, str);
                        }
                    }
                    else if (synset.getSRelations().size()==3) {
                        str = synsetToOutput(synset, pos);
                        FileProcessor.storeResult(synsetOut3, str);
                        if (pos.startsWith("NOUN")) {
                            nEqui3++;
                            FileProcessor.storeResult(synsetOutAllN, str);
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui3++;
                            FileProcessor.storeResult(synsetOutAllA, str);
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui3++;
                            FileProcessor.storeResult(synsetOutAllV, str);
                        }
                    }
                }
                System.out.println("nEqui0 = " + nEqui0);
                System.out.println("nEqui1 = " + nEqui1);
                System.out.println("nEqui2 = " + nEqui2);
                System.out.println("nEqui3 = " + nEqui3);
                System.out.println("nEqui4 = " + nEqui4);
                String statsString = "NR OF SUMO RELATIONS\tzero\tone\ttwo\tthree\tfour or more\n";
                statsString += "NOUNS\t"+nEqui0+"\t"+nEqui1+"\t"+nEqui2+"\t"+nEqui3+"\t"+nEqui4+"\n";
                statsString += "VERBS\t"+vEqui0+"\t"+vEqui1+"\t"+vEqui2+"\t"+vEqui3+"\t"+vEqui4+"\n";
                statsString += "ADJECTIVES\t"+aEqui0+"\t"+aEqui1+"\t"+aEqui2+"\t"+aEqui3+"\t"+aEqui4+"\n";
                statsString += "\n";
                FileProcessor.storeResult(synsetOut, statsString);
                synsetOut.flush();
                synsetOut.close();
                synsetOutAllN.flush();
                synsetOutAllN.close();
                synsetOutAllV.flush();
                synsetOutAllV.close();
                synsetOutAllA.flush();
                synsetOutAllA.close();
                synsetOut2.flush();
                synsetOut2.close();
                synsetOut3.flush();
                synsetOut3.close();
                synsetOut4.flush();
                synsetOut4.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
}