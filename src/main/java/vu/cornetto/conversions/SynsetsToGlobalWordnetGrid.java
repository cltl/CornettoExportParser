package vu.cornetto.conversions;

import vu.cornetto.cdb.CdbEquivalenceRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;
import vu.cornetto.cdb.Syn;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kyoto
 * Date: 1/10/12
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class SynsetsToGlobalWordnetGrid {

 /*
 <SYNSET><ID>ENG20-00001740-n</ID><POS>n</POS>
 <SYNONYM><LITERAL>entity<SENSE>1</SENSE></LITERAL></SYNONYM>
 <DEF>that which is perceived or known or inferred to have its own distinct existence (living or nonliving)</DEF> </SYNSET>
  */
    public static void main (String args[]) {
        final String filter = "()"; /// illegal string in lexicon
        String synsetString = "";
        String synsetCdbString = "";
        String synsetSynonymString = "";
        String relation_weight = "";
        String relation = "";
        String pos = "";
        DecimalFormat df = new DecimalFormat("#.##");
        String cdbSynFilePath = args[0];
        int equivalenceScoreProportion = Integer.parseInt(args[1]);
        String cdbGwgFilePath = cdbSynFilePath+".gwg";
        CdbSynsetParser parser = new CdbSynsetParser();
        parser.parseFile(cdbSynFilePath);
        try {
            FileOutputStream fos = new FileOutputStream(cdbGwgFilePath);
            String str = "";
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            int counter = 0;
            while (keys.hasNext()) {
                synsetString = "";
                synsetCdbString = "";
                synsetSynonymString = "";
                pos = "";
                counter++;
                String key = (String) keys.next();
                CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                String c_sy_id = synset.getC_sy_id();
                if (synset.getPos().toLowerCase().equals("noun")) {
                    c_sy_id += "-n";
                    pos = "n";
                }
                else if (synset.getPos().toLowerCase().equals("verb")) {
                    c_sy_id += "-v";
                    pos = "v";
                }
                else if (synset.getPos().toLowerCase().equals("adjective")) {
                    c_sy_id += "-a";
                    pos = "a";
                }
                else if (synset.getPos().toLowerCase().equals("adj")) {
                    c_sy_id += "-a";
                    pos = "a";
                }
                else if (synset.getPos().toLowerCase().equals("adverb")) {
                    c_sy_id += "-b";
                    pos = "b";
                }
                else if (synset.getPos().toLowerCase().equals("adv")) {
                    c_sy_id += "-b";
                    pos = "b";
                }
                else if (synset.getPos().length()==0) {
                    c_sy_id += "-n";
                    pos = "n";
                }
                if (c_sy_id.startsWith("c_")) {
                    if (
                            (!c_sy_id.endsWith("-n"))  &&
                                    (!c_sy_id.endsWith("-v"))  &&
                                    (!c_sy_id.endsWith("-a"))  &&
                                    (!c_sy_id.endsWith("-b")))  {
                        c_sy_id += "-n";
                        pos = "n";
                    }

                }
                // <SYNSET><ID>ENG20-00001740-n</ID><POS>n</POS>
                synsetCdbString = "\t<SOURCE><ID>"+c_sy_id+"</ID><POS>"+pos+"</POS></SOURCE>\n";
                ArrayList<Syn> synonyms =  synset.getSynonyms();
                for (int i = 0; i < synonyms.size(); i++) {
                    Syn syn = synonyms.get(i);
                    String lemma = syn.getPreviewtext().trim();
                    String sense = "";
                    if (lemma.length()>0){
                        int idx = lemma.indexOf(":");
                        if (idx>-1) {
                            sense = lemma.substring(idx+1).trim();
                            lemma = lemma.substring(0, idx).trim();
                        }
                        //// we need to remove and clean some stuff for the UKB lexicon
/*
                        String cleanStr = "";
                        lemma = lemma.replaceAll(" ", "_");
                        for (int c=0; c<lemma.length();c++) {
                            char cr = lemma.charAt(c);
                            if (filter.indexOf(cr)==-1) {
                                cleanStr += cr;
                            }
                        }
*/

                        /// we output the lemma+sense+synset mapping for first sense heuristics
                        //<SYNONYM><LITERAL>entity<SENSE>1</SENSE></LITERAL></SYNONYM>

                        synsetSynonymString +="\t<SYNONYM><LITERAL>"+lemma+"<SENSE>"+sense+"</SENSE></LITERAL></SYNONYM>\n";
                    }
                }
                double topScore = 0;
                for (int j = 0; j < synset.getERelations().size(); j++) {
                    CdbEquivalenceRelation o = (CdbEquivalenceRelation) synset.getERelations().get(j);
                    double score = o.getAuthor().getScore();
                    if (score>topScore) {
                        topScore = score;
                    }
                }

                for (int j = 0; j < synset.getERelations().size(); j++) {
                    CdbEquivalenceRelation o = (CdbEquivalenceRelation) synset.getERelations().get(j);
                    String engId = o.getTarget30();
                    String engPos = "";
                    String rel = o.getRelationName();
                    String authorName = o.getAuthor().getName();
                    double score = o.getAuthor().getScore();
                    double proportion = (score/topScore);
                    String status = o.getAuthor().getStatus();
                    System.out.println("status = " + status);
                    System.out.println("authorName = " + authorName);
                    System.out.println("rel = " + rel);
                    System.out.println("score = " + score);

                    /*
                   <SYNSET><ID>ENG20-00001740-n</ID><POS>n</POS>
                   <SYNONYM><LITERAL>entity<SENSE>1</SENSE></LITERAL></SYNONYM>
                   <DEF>that which is perceived or known or inferred to have its own distinct existence (living or nonliving)</DEF> </SYNSET>
                    */

                    if (engId.length()>0) {
                        relation_weight = "";
                        engPos = engId.substring(engId.length()-1);
                        engId = "ENG-"+engId.substring(3);
                        synsetString = "<SYNSET>\n\t<ID>"+engId+"<ID><POS>"+engPos+"<POS>\n";
                        synsetString += "\t<EQREL>"+rel+"</EQREL>";
                        if (status.length()>0) {
                            //// there is a status field so this is manually created or confirmed
                            //// we give it the maximum score
                            relation_weight = "1.0";
                        }
                        else if (authorName.equalsIgnoreCase("Paul")) {
                            if (100*proportion>=equivalenceScoreProportion) {
                                if (proportion==1.0) {
                                    relation_weight = "1.0";
                                }
                                else {
                                    relation_weight = df.format(proportion);
                                }
                            }
                        }
                        else if (authorName.equalsIgnoreCase("Irion Technologies")) {
                            if (100*proportion>=equivalenceScoreProportion) {
                                if (proportion==1.0) {
                                    relation_weight = "1.0";
                                }
                                else {
                                    relation_weight = df.format(proportion);
                                }
                            }
                        }
                        else {
                            //// Created in DebVisDic client and therefore manual
                            relation_weight = "1.0";
                        }
                        if (relation_weight.isEmpty()) {
                            ///// mapping is ignored
                        }
                        else {
                            synsetString += "<EQSCORE>"+relation_weight+"</EQSCORE>\n";
                            str = synsetString+synsetSynonymString+synsetCdbString+"\t<DEF/>\n</SYNSET>\n";
                            fos.write(str.getBytes());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
