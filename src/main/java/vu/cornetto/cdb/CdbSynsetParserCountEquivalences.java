package vu.cornetto.cdb;

import org.xml.sax.helpers.DefaultHandler;
import vu.cornetto.util.FileProcessor;

import java.io.*;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 11-jul-2007
 * Time: 18:01:13
 * To change this template use File | Settings | File Templates.
 */
public class CdbSynsetParserCountEquivalences extends DefaultHandler {

        static String synsetToOutput (CdbSynset synset, String pos) {
            String str = "";
            for (int i=0; i<synset.synonyms.size();i++) {
                Syn syn = (Syn) synset.synonyms.get(i);
                str = syn.previewtext+"\t"+pos+"\t"+syn.getC_cid_id()+"\t"+syn.c_lu_id+"\t"+synset.getC_sy_id()+"\t"
                        +synset.eRelations.size()+"\n";
            }
            return str;
        }
        ///* This function reads a synset file and stores CdbSynset objects in a hashmap
        ///
        public static void main(String[] args) throws IOException {
            // ENCODING = UTF8;
            try {

                String cdbSynsetFile = args[0];
                FileOutputStream importOutFile = new FileOutputStream(cdbSynsetFile+".counts.xls");
                OutputStreamWriter synsetOut = new OutputStreamWriter(importOutFile, "UTF-8");
                String str = "";
                str = "preview\tpos\tcid_id\tc_lu_id\tc_synset_id\tequiCount\n";
                CdbSynsetParser cdbSynsetParser = new CdbSynsetParser();
                cdbSynsetParser.parseFile(cdbSynsetFile);
                //cdbSynsetParser.parseFileAsBytes(cdbSynsetFile);
                //cdbSynsetParser.parseFile(cdbSynsetFile, "UTF-8");
                Set keySet = cdbSynsetParser.synsetIdSynsetMap.keySet();
                Iterator keys = keySet.iterator();
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
                    if (synset.eRelations.size()>3) {
                        if (pos.startsWith("NOUN")) {
                            nEqui4++;
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui4++;
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui4++;
                        }
                        str += synsetToOutput(synset, pos);
                    }
                    else if (synset.eRelations.size()==0) {
                        if (pos.startsWith("NOUN")) {
                            nEqui0++;
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui0++;
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui0++;
                        }
                        nEqui0++;
                    }
                    else if (synset.eRelations.size()==1) {
                        if (pos.startsWith("NOUN")) {
                            nEqui1++;
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui1++;
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui1++;
                        }
                        nEqui1++;
                    }
                    else if (synset.eRelations.size()==2) {
                        if (pos.startsWith("NOUN")) {
                            nEqui2++;
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui2++;
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui2++;
                        }
                    }
                    else if (synset.eRelations.size()==3) {
                        if (pos.startsWith("NOUN")) {
                            nEqui3++;
                        }
                        else if (pos.startsWith("AD")) {
                            aEqui3++;
                        }
                        else if (pos.startsWith("VERB")) {
                            vEqui3++;
                        }
                    }
                }
                System.out.println("nEqui0 = " + nEqui0);
                System.out.println("nEqui1 = " + nEqui1);
                System.out.println("nEqui2 = " + nEqui2);
                System.out.println("nEqui3 = " + nEqui3);
                System.out.println("nEqui4 = " + nEqui4);
                String statsString = "NR OF EQUIVALENCE RELATIONS\tzero\tone\ttwo\tthree\tfour or more\n";
                statsString += "NOUNS\t"+nEqui0+"\t"+nEqui1+"\t"+nEqui2+"\t"+nEqui3+"\t"+nEqui4+"\n";
                statsString += "VERBS\t"+vEqui0+"\t"+vEqui1+"\t"+vEqui2+"\t"+vEqui3+"\t"+vEqui4+"\n";
                statsString += "ADJECTIVES\t"+aEqui0+"\t"+aEqui1+"\t"+aEqui2+"\t"+aEqui3+"\t"+aEqui4+"\n";
                statsString += "\n";
                FileProcessor.storeResult(synsetOut, statsString);
                FileProcessor.storeResult(synsetOut, str);
                synsetOut.flush();
                synsetOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
}