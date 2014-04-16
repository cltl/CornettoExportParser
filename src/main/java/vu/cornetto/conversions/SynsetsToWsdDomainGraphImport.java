package vu.cornetto.conversions;

import vu.cornetto.cdb.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 22-aug-2008
 * Time: 7:50:42
 * To change this template use File | Settings | File Templates.
 *
 * This class reads the synset dump of Cornetto and a domainMapping File to create the UKB graph import file and the corresponding lexicon
 *
 */
public class SynsetsToWsdDomainGraphImport {


    static final String englishWordNetPrefix = "ENG-";
    /**
     *
     * @param filePath to the domain hierarchy file
     * doctrines
        archaeology
        astrology
        history
        history	heraldry
        etc...
        It creates a HashMap with for each domain the Array of parents
     * @return
     */
    public static HashMap<String, ArrayList<String>> makeDomainParentArrayMap (String filePath) {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.trim().length()>0) {
                   String [] fields = inputLine.split("\t");
                   if (fields.length>1) {
                       String key = fields[fields.length-1];
                       ArrayList<String> parents = new ArrayList<String>();
                       if (map.containsKey(key)) {
                           parents = map.get(key);
                           for (int i = 0; i < fields.length-1; i++) {
                               String field = fields[i].trim();
                               if (field.length()>0) {
                                   if (!parents.contains(field)){
                                       parents.add(field);
                                   }
                               }
                           }
                           map.put(key, parents);
                       }
                       else {
                           for (int i = 0; i < fields.length-1; i++) {
                               String field = fields[i].trim();
                               if (field.length()>0) {
                                   if (!parents.contains(field)){
                                       parents.add(field);
                                   }
                               }
                           }
                           map.put(key, parents);
                       }
                   }
                }
            }
            System.out.println("read mapppings = " + map.size());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }

    /**
     *
     * @param filePath to the domain hierarchy file
     * doctrines
        archaeology
        astrology
        history
        history	heraldry
        etc...
        It creates a HashMap with for each domain the parent
     * @return
     */
    public static HashMap<String, String> makeDomainParentMap (String filePath) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while (in.ready()&&(inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                if (inputLine.trim().length()>0) {
                   String [] fields = inputLine.split("\t");
                   if (fields.length>1) {
                       String key = fields[fields.length-1];
                       String parent = fields[fields.length-2];
                       if (map.containsKey(key)) {
                           /// skip, we already have
                       }
                       else {
                           map.put(key, parent);
                       }
                   }
                }
            }
            System.out.println("read mapppings = " + map.size());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }

    /**
     *
     * @param args
     *
     * name_key is used to mark the output files
     *
     * u:00023741-n v:02223033-a w:0.3 s:wn30

specifies a relation between "00023741-n" and "02223033-a" with weight
0.3. It also says that the relation is extracted from the "wn30" source.

You then compile the graph as usual via compile_kb, and then specify the
--with_weight switch when calling ukb_wsd
     *
     */
    public static void main (String args[]) {
        final String filter = "()"; /// illegal string in lexicon
        DecimalFormat df = new DecimalFormat("#.##");
        boolean domainhierarchy = false;
        boolean lexiconWithEquivalenceMapping = false;
        String cdbSynFilePath = "";
        String domainMappingFilePath = "";
        String nameKey = "";
        int equivalenceScoreProportion = 0;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ((arg.equalsIgnoreCase("--cdb_syn")) && args.length>(i+1)) {
                cdbSynFilePath = args[i+1];
            }
            else if ((arg.equalsIgnoreCase("--domain-mapping")) && args.length>(i+1)) {
                domainMappingFilePath = args[i+1];
            }
            else if ((arg.equalsIgnoreCase("--name-key")) && args.length>(i+1)) {
                nameKey = args[i+1];
            }
            else if ((arg.equalsIgnoreCase("--equivalence-proportion")) && args.length>(i+1)) {
                String proportion = args[i+1];
                try {
                    equivalenceScoreProportion = Integer.parseInt(proportion);
                } catch (NumberFormatException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    System.out.println("--equivalence-proportion should be an integer = " + proportion);
                    return;
                }

            }
            else if ((arg.equalsIgnoreCase("--equivalence-mapping"))) {
               lexiconWithEquivalenceMapping = true;
            }
            else if ((arg.equalsIgnoreCase("--domain-hierarchy"))) {
                domainhierarchy = true;
            }
        }
        if (cdbSynFilePath.isEmpty()) {
            System.out.println("--cdb_syn cdbSynFilePath = " + cdbSynFilePath);
            return;
        }
        if (domainMappingFilePath.isEmpty()) {
            System.out.println("--domain-mapping domainMappingFilePath = " + domainMappingFilePath);
            return;
        }
        HashMap<String, ArrayList<String>> parentArrayMap = makeDomainParentArrayMap(domainMappingFilePath);
        HashMap<String, String> parentMap = makeDomainParentMap(domainMappingFilePath);
        HashMap<String, ArrayList<String>> lexMap = new HashMap<String, ArrayList<String>>();
        String outputDefFile = nameKey+"synset.def";
        String outputExplicitFile = nameKey+"explicit.rel";
        String outputFile = nameKey+"synset_domain_graph.rel";
        String outputEng1RelFile = nameKey+"synset_eng_graph_all_equivalences.rel";
        String outputEng2RelFile = nameKey+"synset_eng_graph_scored_equivalences.rel";
        String outputLexFile = nameKey+"synset_domain_graph.lex";
        String outputLexEquiFile = nameKey+"synset_domain_graph_equi.lex";
        String outputPolysemyLexFile = nameKey+"synset_polysemy.lex";
        String outputSenseLexFile = nameKey+"synset_sense.lex";
        String outputDomainLexFile = nameKey+"synset_domain.lex";
        //String outputLexFile2 = nameKey+"synset_info.lex";
        String outputDefFilePath = new File(cdbSynFilePath).getParent()+"/"+outputDefFile;
        String outputFileExplicitRelPath = new File(cdbSynFilePath).getParent()+"/"+outputExplicitFile;
        String outputFilePath = new File(cdbSynFilePath).getParent()+"/"+outputFile;
        String outputFileEng1RelPath = new File(cdbSynFilePath).getParent()+"/"+outputEng1RelFile;
        String outputFileEng2RelPath = new File(cdbSynFilePath).getParent()+"/"+outputEng2RelFile;
        String outputLexFilePath = new File(cdbSynFilePath).getParent()+"/"+outputLexFile;
        String outputLexEquiFilePath = new File(cdbSynFilePath).getParent()+"/"+outputLexEquiFile;
        String outputPolysemyLexFilePath = new File(cdbSynFilePath).getParent()+"/"+outputPolysemyLexFile;
        String outputSenseLexFilePath = new File(cdbSynFilePath).getParent()+"/"+outputSenseLexFile;
        String outputDomainLexFilePath = new File(cdbSynFilePath).getParent()+"/"+outputDomainLexFile;
        CdbSynsetParser parser = new CdbSynsetParser();
        parser.parseFile(cdbSynFilePath);
        //parser.parseFileAsBytes(cdbSynFilePath);
        try {
            String str = "";
            FileOutputStream out = new FileOutputStream (outputFilePath);
            FileOutputStream outDef = new FileOutputStream (outputDefFilePath);
            FileOutputStream outExplicitRel = new FileOutputStream (outputFileExplicitRelPath);
            FileOutputStream outEngAllRel = new FileOutputStream (outputFileEng1RelPath);
            FileOutputStream outEngScoredRel = new FileOutputStream (outputFileEng2RelPath);
            FileOutputStream outLex = new FileOutputStream (outputLexFilePath);
            FileOutputStream outLexEqui = new FileOutputStream (outputLexEquiFilePath);
            FileOutputStream outSenseLex = new FileOutputStream (outputSenseLexFilePath);
            FileOutputStream outPolysemyLex = new FileOutputStream (outputPolysemyLexFilePath);
            FileOutputStream outDomainLex = new FileOutputStream (outputDomainLexFilePath);

            String relation_source = "";
            String relation_weight = "";
            //// make the relations
            if (domainhierarchy) {
            ///// domain hierarchy itself is added as relations
                relation_source = " s:domain";
                relation_weight = " w:1.0";
                Set keySet = parentMap.keySet();
                Iterator keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String parent = parentMap.get(key);
                    str = "u:"+key+" v:"+parent+relation_weight+relation_source+"\n";
                    out.write(str.getBytes());
                }
            }

/*
            String fixed = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<cids>\n";
            out.write(fixed.getBytes());
*/
            Set keySet = parser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            int counter = 0;
            while (keys.hasNext()) {
                counter++;
                String key = (String) keys.next();
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
                 if (c_sy_id.startsWith("u:c_")) {
                     if (
                             (!c_sy_id.endsWith("-n"))  &&
                             (!c_sy_id.endsWith("-v"))  &&
                             (!c_sy_id.endsWith("-a"))  &&
                             (!c_sy_id.endsWith("-b")))  {
                         c_sy_id += "-n";
                     }

                 }
                 //// make the lexicon
                String synsetId = c_sy_id.substring(2);  /// skip the "u:" which is not allowed in the lexicon

                //// Write the definitions....
                String def = synset.getDefinition();
                if (def.length()>0) {
                    str = synsetId +"\t"+def+"\n";
                    outDef.write(str.getBytes());
                }

                String synsetString = "";
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
                        String cleanStr = "";
                        lemma = lemma.replaceAll(" ", "_");
                        for (int c=0; c<lemma.length();c++) {
                            char cr = lemma.charAt(c);
                            if (filter.indexOf(cr)==-1) {
                                cleanStr += cr;
                            }
                        }
                        /// we output the lemma+sense+synset mapping for first sense heuristics
                        synsetString +=lemma+";";
                        str = lemma+"\t"+sense+"\t"+synsetId+"\n";
                        outSenseLex.write(str.getBytes());

                        /// we output the lemma+sense+synset+domains for the domain heuristics
                        str = lemma+"\t"+sense+"\t"+synsetId;
                        ArrayList<String> domList = new ArrayList<String>();
                        for (int j = 0; j < synset.getDRelations().size(); j++) {
                            CdbDomainRelation dom = synset.getDRelations().get(j);
                            String fields[] = dom.getTerm().split(" ");
                            for (int k = 0; k < fields.length; k++) {
                                String field = fields[k].trim();
                                if (field.length()>0) {
                                    if (!domList.contains(field)) {
                                        domList.add(field);
                                    }
                                }
                            }
                        }
                        for (int j = 0; j < domList.size(); j++) {
                            String s = domList.get(j);
                            str += "\t"+s;
                        }
                        str += "\n";
                        outDomainLex.write(str.getBytes());


                        ///// We build up a map with the equivalence mappings
                        ///// Apparently, we are putting all equis in this map, we may need to use the same scoring as for the relations here....
                        if (lexMap.containsKey(cleanStr)) {
                            ArrayList<String> synsets = lexMap.get(cleanStr);
                            if (!synsets.contains(synsetId)) {
                                synsets.add(synsetId);
                            }
                            if (lexiconWithEquivalenceMapping) {
                            /// ADD EQUIVALENCE MAPPINGS AS CONCEPTS
                                for (int j = 0; j < synset.getERelations().size(); j++) {
                                    CdbEquivalenceRelation o = (CdbEquivalenceRelation) synset.getERelations().get(j);
                                    String engId = o.getTarget30();
                                    if (engId.length()>0) {
                                        engId = englishWordNetPrefix+engId.substring(3);
                                        if (!synsets.contains(engId)) {
                                            synsets.add(engId);
                                        }
                                    }
                                }
                            }
                            lexMap.put(cleanStr, synsets);
                        }
                        else {
                            ArrayList<String> synsets = new ArrayList<String>();
                            synsets.add(synsetId);
                            if (lexiconWithEquivalenceMapping) {
                                /// ADD EQUIVALENCE MAPPINGS AS CONCEPTS
                                for (int j = 0; j < synset.getERelations().size(); j++) {
                                    CdbEquivalenceRelation o = (CdbEquivalenceRelation) synset.getERelations().get(j);
                                    String engId = o.getTarget30();
                                    if (engId.length()>0) {
                                        engId = englishWordNetPrefix+engId.substring(3);
                                        if (!synsets.contains(engId)) {
                                            synsets.add(engId);
                                        }
                                    }
                                }
                            }
                            lexMap.put(cleanStr, synsets);

                        }
                    }
                }

                //// make the relations

                ///// Cornetto internal relations
                relation_source = " s:cdb";
                relation_weight = " w:1.0";
                ArrayList<CdbInternalRelation> relations = synset.getIRelations();
                for (int i=0; i<relations.size();i++) {
                      CdbInternalRelation rel = relations.get(i);
                      if (rel.getTarget().length()>0) {
                          String targetId = " v:"+rel.getTarget();
                          if (targetId.indexOf("_n-")!=-1) {
                              targetId += "-n";
                          }
                          else if (targetId.indexOf("_v-")!=-1) {
                              targetId += "-v";
                          }
                          else if (targetId.indexOf("_a-")!=-1) {
                              targetId += "-a";
                          }
                          else if (targetId.indexOf("_b-")!=-1) {
                              targetId += "-b";
                          }
                          else if (targetId.indexOf("_r-")!=-1) {
                              targetId += "-b";
                          }
                          else if (targetId.startsWith("c_")) {
                              targetId += "-n";
                          }
                          str = c_sy_id + targetId+relation_weight+relation_source+"\n";
                          out.write(str.getBytes());
                          str = synsetString+"#"+c_sy_id +"#"+rel.getRelation_name()+"#"+ targetId.trim()+relation_weight+relation_source+"\n";
                          outExplicitRel.write(str.getBytes());

                      }
                }

                //// Creates a relation between the synset and the domains but also to the parent domains!
                relation_source = " s:domain";
                ArrayList<CdbDomainRelation> drelations = synset.getDRelations();
                ArrayList<String> parentsCovered = new ArrayList<String>();
                for (int i=0; i<drelations.size();i++) {
                      CdbDomainRelation rel = drelations.get(i);
                      //// domain relations are not scored yet, all values are zero
/*
                      double score = rel.getScore();
                      relation_weight = " w:"+score;
*/
                      relation_weight = " w:1.0";
                      String termString = rel.getTerm();
                      String [] tArray = termString.split(" ");
                      if (tArray.length<=3) {
                          for (int t=0; t<tArray.length;t++) {
                              String term = tArray[t];
                              if (term.length()>0) {
                                  if (!term.equalsIgnoreCase("factotum")) {
                                      parentsCovered.add(term);
                                      str = c_sy_id + " v:"+term+relation_weight+relation_source+"\n";
                                      out.write(str.getBytes());
                                      if (!domainhierarchy) {
                                          if (parentMap.containsKey(term)) {
                                              ArrayList<String> parents = parentArrayMap.get(term);
                                              for (int j = 0; j < parents.size(); j++) {
                                                  String s = parents.get(j);
                                                  if (!parentsCovered.contains(s)) {
                                                      str = c_sy_id + " v:"+s+relation_weight+relation_source+"\n";
                                                      out.write(str.getBytes());
                                                      parentsCovered.add(s);
                                                  }
                                              }
                                          }
                                      }
                                  }
                              }
                          }
                      }
                      else {
                          //// synsets with more than 3 domains are suspect, overgeneration through the equivalence relations
                          //// either ignore
                          //   or take the most frequent one.... to be implemented....
                          ////
                      }
                }

                relation_source = " s:equivalence";
                //// Can we trust synsets with many equivalences????
                /// we first determine the highest score
                double topScore = 0;
                for (int j = 0; j < synset.getERelations().size(); j++) {
                    CdbEquivalenceRelation o = (CdbEquivalenceRelation) synset.getERelations().get(j);
                    String engId = o.getTarget30();
                    String rel = o.getRelationName();
                    String authorName = o.getAuthor().getName();
                    double score = o.getAuthor().getScore();
                    if (score>topScore) {
                        topScore = score;
                    }
                }

                for (int j = 0; j < synset.getERelations().size(); j++) {
                    CdbEquivalenceRelation o = (CdbEquivalenceRelation) synset.getERelations().get(j);
                    String engId = o.getTarget30();
                    String rel = o.getRelationName();
                    String authorName = o.getAuthor().getName();
                    double score = o.getAuthor().getScore();
                    double proportion = (score/topScore);
                    String status = o.getAuthor().getStatus();

/*                    System.out.println("status = " + status);
                    System.out.println("authorName = " + authorName);
                    System.out.println("rel = " + rel);
                    System.out.println("score = " + score);
                    System.out.println("engId = " + engId);*/

                    if (engId.length()>0) {
                        engId = englishWordNetPrefix+engId.substring(3);

                        relation_weight = " w:"+df.format(proportion);
                        str = c_sy_id+" v:"+ engId+relation_weight+relation_source+"\n";
                        outEngAllRel.write(str.getBytes());
                        if (status.length()>0) {
                            //// there is a status field so this is manually created or confirmed
                            //// we give it the maximum score
                            relation_weight = " w:1.0";
                            str = c_sy_id+" v:"+ engId+relation_weight+relation_source+"\n";
                            outEngScoredRel.write(str.getBytes());
                        }
                        else if (authorName.equalsIgnoreCase("Paul")) {
                            if (100*proportion>=equivalenceScoreProportion) {
                                if (proportion==1.0) {
                                    relation_weight = " w:1.0";
                                }
                                else {
                                    relation_weight = " w:"+df.format(proportion);
                                }
                                str = c_sy_id+" v:"+ engId+relation_weight+relation_source+"\n";
                                outEngScoredRel.write(str.getBytes());
                            }
                        }
                        else if (authorName.equalsIgnoreCase("Irion Technologies")) {
                            if (100*proportion>=equivalenceScoreProportion) {
                                if (proportion==1.0) {
                                    relation_weight = " w:1.0";
                                }
                                else {
                                    relation_weight = " w:"+df.format(proportion);
                                }
                                str = c_sy_id+" v:"+ engId+relation_weight+relation_source+"\n";
                                outEngScoredRel.write(str.getBytes());
                            }
                        }
                        else {
                            //// Created in DebVisDic client and therefore manual
                            relation_weight = " w:1.0";
                            str = c_sy_id+" v:"+ engId+relation_weight+relation_source+"\n";
                            outEngScoredRel.write(str.getBytes());
                        }
                    }
                }
            }
            outExplicitRel.flush();
            outExplicitRel.close();
            outDef.flush();
            outDef.close();
            outSenseLex.flush();
            outSenseLex.close();
            outDomainLex.flush();
            outDomainLex.close();
            //fixed = "</cids>\n";
            //out.write(fixed.getBytes("UTF-8"));
            //out.write(fixed.getBytes());
            out.flush();
            out.close();
            outEngAllRel.flush();
            outEngAllRel.close();
            outEngScoredRel.flush();
            outEngScoredRel.close();
            keySet = lexMap.keySet();
            keys = keySet.iterator();
            String polyStr = "";
            String equiStr = "";
            while (keys.hasNext()) {
                String key = (String) keys.next();
                str = key+" ";
                equiStr = key+" ";
                polyStr = "";
                String pos = "n";
                int nN = 0;
                int nV = 0;
                int nA = 0;
                int nO= 0;
                ArrayList<String> synsets = lexMap.get(key);
                for (int i = 0; i < synsets.size(); i++) {
                    String s = synsets.get(i);
                    if (s.startsWith(englishWordNetPrefix)) {
                        equiStr += " "+s;
                    }
                    else {
                        pos = s.substring(s.length()-1);
                        if (pos.equals("n")) {
                            nN++;
                        }
                        else if (pos.equals("v")) {
                            nV++;
                        }
                        else if (pos.equals("a")) {
                            nA++;
                        }
                        else {
                            nO++;
                        }
                        equiStr += " "+s;
                        str += " "+s;
                    }
                }
                if (nN>0) {
                    polyStr +=key+"\t"+ "n"+"\t"+nN+"\n";
                }
                if (nV>0) {
                    polyStr += key+"\t"+"v"+"\t"+nV+"\n";
                }
                if (nA>0) {
                    polyStr += key+"\t"+"a"+"\t"+nA+"\n";
                }
                if (nO>0) {
                    polyStr += key+"\t"+"o"+"\t"+nO+"\n";
                }
                if (!polyStr.isEmpty()) {
                    outPolysemyLex.write(polyStr.getBytes());
                }
                str += "\n";
                equiStr += "\n";
                outLex.write(str.getBytes());
                outLexEqui.write(equiStr.getBytes());
            }
            outPolysemyLex.flush();
            outPolysemyLex.close();
            outLex.flush();
            outLex.close();
            outLexEqui.flush();
            outLexEqui.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}