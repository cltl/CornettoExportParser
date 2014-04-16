package vu.cornetto.conversions;

import vu.cornetto.cdb.CdbEquivalenceRelation;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.CdbSynsetParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 8/1/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class DutchEnglishMappings {


       static public String getPos (CdbSynset synset) {
           String pos = "";
           if (synset.getPos().toLowerCase().equals("noun")) {
               pos = "n";
           }
           else if (synset.getPos().toLowerCase().equals("verb")) {
               pos = "v";
           }
           else if (synset.getPos().toLowerCase().equals("adjective")) {
               pos = "a";
           }
           else if (synset.getPos().toLowerCase().equals("adj")) {
               pos = "a";
           }
           else if (synset.getPos().toLowerCase().equals("adverb")) {
               pos = "b";
           }
           else if (synset.getPos().toLowerCase().equals("adv")) {
               pos = "b";
           }
           else if (synset.getPos().length()==0) {
               pos = "o";
           }
           return pos;
       }

       static public void main (String[] args) {
           try {
               String pathToCdbFile = args[0];
               String str = "";
               int wn3 = 0;
               int wn2 = 0;
               int wn16 = 0;
               int wn15 = 0;
               int synsetN = 0;
               int synsetV = 0;
               int synsetA = 0;
               int synsetB = 0;
               int synsetO = 0;
               int equiN = 0;
               int equiV = 0;
               int equiA = 0;
               int equiB = 0;
               int equiO = 0;
               HashMap<Integer, Integer> synonymsNouns = new HashMap<Integer, Integer>();
               HashMap<Integer, Integer> synonymsVerbs = new HashMap<Integer, Integer>();
               HashMap<Integer, Integer> synonymsAdjectives = new HashMap<Integer, Integer>();
               HashMap<Integer, Integer> deMappingRatio = new HashMap<Integer, Integer>();
               HashMap<Integer, Integer> edMappingRatio = new HashMap<Integer, Integer>();
               HashMap<String, ArrayList<String>> engDutchSynsetMapping = new HashMap<String, ArrayList<String>>();
               HashMap<String, ArrayList<String>> dutchEngSynsetMapping = new HashMap<String, ArrayList<String>>();
               HashMap<String, Integer> equiTypes = new HashMap<String, Integer>();
               HashMap<String, Integer> equiAuthors = new HashMap<String, Integer>();
               HashMap<String, Integer> equiStatus = new HashMap<String, Integer>();
               FileOutputStream fos = new FileOutputStream(pathToCdbFile+".equilog-d.xls");
               FileOutputStream fosn = new FileOutputStream(pathToCdbFile+".equilog-d-n.xls");
               FileOutputStream fosv = new FileOutputStream(pathToCdbFile+".equilog-d-v.xls");
               FileOutputStream fosa = new FileOutputStream(pathToCdbFile+".equilog-d-a.xls");
               FileOutputStream fose = new FileOutputStream(pathToCdbFile+".equilog-e.xls");
               FileOutputStream stats = new FileOutputStream(pathToCdbFile+".equistats.xls");
               str = "synsetId\tsynonyms\tnr. of synonyms\tnr. of equivalences\tautomatic-ewn\tautomatic-cornetto\tmanual\teq_syn\teq_near_syn\teq_other\teq_empty\n";
               fos.write(str.getBytes());
               fosn.write(str.getBytes());
               fosv.write(str.getBytes());
               fosa.write(str.getBytes());
               str = "synsetId\tnr. of equivalences\n";
               fose.write(str.getBytes());
               CdbSynsetParser parser = new CdbSynsetParser();
               parser.parseFile(pathToCdbFile);
               Set keySet = parser.synsetIdSynsetMap.keySet();
               Iterator keys = keySet.iterator();
               while (keys.hasNext()) {
                   String key = (String) keys.next();
                   CdbSynset synset = parser.synsetIdSynsetMap.get(key);

                   //// Can we trust synsets with many equivalences????
                   /// we first determine the highest score
                   double topScore = 0;
                   String pos = getPos(synset);
                   Integer nSyns = synset.getSynonyms().size();
                   if (pos.equals("n")) {
                       synsetN++;
                       if (synset.getERelations().size()>0) equiN++;
                       if (synonymsNouns.containsKey(nSyns)) {
                           Integer cnt = synonymsNouns.get(nSyns);
                           cnt++;
                           synonymsNouns.put(nSyns, cnt);
                       }
                       else {
                           synonymsNouns.put(nSyns, 1);
                       }
                   }
                   if (pos.equals("v")) {
                       synsetV++;
                       if (synset.getERelations().size()>0) equiV++;
                       if (synonymsVerbs.containsKey(nSyns)) {
                           Integer cnt = synonymsVerbs.get(nSyns);
                           cnt++;
                           synonymsVerbs.put(nSyns, cnt);
                       }
                       else {
                           synonymsVerbs.put(nSyns, 1);
                       }
                   }
                   if (pos.equals("a")) {
                       synsetA++;
                       if (synset.getERelations().size()>0) equiA++;
                       if (synonymsAdjectives.containsKey(nSyns)) {
                           Integer cnt = synonymsAdjectives.get(nSyns);
                           cnt++;
                           synonymsAdjectives.put(nSyns, cnt);
                       }
                       else {
                           synonymsAdjectives.put(nSyns, 1);
                       }
                   }
                   if (pos.equals("b")) {
                       synsetB++;
                       if (synset.getERelations().size()>0) equiB++;
                   }
                   if (pos.equals("o")) {
                       synsetO++;
                       if (synset.getERelations().size()>0) equiO++;
                   }

                   Integer deRatio = synset.getERelations().size();
                   if (deMappingRatio.containsKey(deRatio)) {
                       Integer cnt = deMappingRatio.get(deRatio);
                       cnt++;
                       deMappingRatio.put(deRatio,cnt);
                   }
                   else {
                       deMappingRatio.put(deRatio, 1);
                   }

                   ArrayList<String> eSynsets = new ArrayList<String>();
                   int automaticewn = 0;
                   int automaticirion = 0;
                   int manual = 0;
                   int syn = 0;
                   int nsyn = 0;
                   int osyn = 0;
                   int nosyn = 0;

                   for (int j = 0; j < synset.getERelations().size(); j++) {
                       CdbEquivalenceRelation o = (CdbEquivalenceRelation) synset.getERelations().get(j);
                       if (!o.getTarget30().isEmpty()) {
                           wn3++;
                       };
                       if (!o.getTarget20().isEmpty()) {
                           String target = o.getTarget20()+":"+o.getTarget20Previewtext();
                           if (engDutchSynsetMapping.containsKey(target)) {
                               ArrayList<String> dSynsets = engDutchSynsetMapping.get(target);
                               dSynsets.add(key);
                               engDutchSynsetMapping.put(target, dSynsets);
                           }
                           else {
                               ArrayList<String> dSynsets = new ArrayList<String>();
                               dSynsets.add(key);
                               engDutchSynsetMapping.put(target, dSynsets);
                           }
                           eSynsets.add(target);
                           wn2++;
                       };
                       if (!o.getTarget15().isEmpty()) {
                           if (o.getVersion().equals("pwn_1_5")) {
                               wn15++;
                           }
                           else {
                               wn16++;
                           }
                       };

                       String status = o.getAuthor().getStatus();
                       String rel = o.getRelationName();
                       String authorName = o.getAuthor().getName();
                       if (authorName.equalsIgnoreCase("Paul")) {
                           automaticewn++;
                       }
                       else if (authorName.equalsIgnoreCase("Irion Technologies")) {
                           automaticirion++;
                       }
                       else {
                           manual++;
                       }
                       if (rel.equalsIgnoreCase("EQ_SYNONYM")) {
                           syn++;
                       }
                       else if (rel.equalsIgnoreCase("EQ_NEAR_SYNONYM")) {
                           nsyn++;
                       }
                       else if (rel.isEmpty()) {
                          nosyn++;
                       }
                       else {
                          osyn++;
                       }
                       double score = o.getAuthor().getScore();
                       if (score>topScore) {
                           topScore = score;
                       }
                       if (equiTypes.containsKey(rel)) {
                           Integer cnt = equiTypes.get(rel);
                           cnt++;
                           equiTypes.put(rel, cnt);
                       }
                       else {
                           equiTypes.put(rel, 1);
                       }

                       if (equiAuthors.containsKey(authorName)) {
                           Integer cnt = equiAuthors.get(authorName);
                           cnt++;
                           equiAuthors.put(authorName, cnt);
                       }
                       else {
                           equiAuthors.put(authorName, 1);
                       }

                       if (equiStatus.containsKey(status)) {
                           Integer cnt = equiStatus.get(status);
                           cnt++;
                           equiStatus.put(status, cnt);
                       }
                       else {
                           equiStatus.put(status, 1);
                       }

                   }

                   str = key+"\t"+synset.toFlatSynonymLUString()+"\t"+synset.getSynonyms().size()+"\t"+synset.getERelations().size()+"\t"+automaticewn+"\t"+automaticirion+"\t"+manual+"\t"+syn+"\t"+nsyn+"\t"+osyn+"\t"+nosyn+"\n";
                   fos.write(str.getBytes());
                   if (pos.equals("n")) fosn.write(str.getBytes());
                   if (pos.equals("v")) fosv.write(str.getBytes());
                   if (pos.equals("a")) fosa.write(str.getBytes());
                   dutchEngSynsetMapping.put(key, eSynsets);
               }
               fos.close();
               fosn.close();
               fosv.close();
               fosa.close();

               /// vu.cornetto.stats
               str = "\tNr of synsets\tNr.equivalence mappings\n";
               str += "Nouns\t"+synsetN+"\t"+equiN+"\n";
               str += "Verbs\t"+synsetV+"\t"+equiV+"\n";
               str += "Adjectives\t"+synsetA+"\t"+equiA+"\n";
               str += "Adverbs\t"+synsetB+"\t"+equiB+"\n";
               str += "Others\t"+synsetO+"\t"+equiO+"\n\n";

               str += "WN30\t"+wn3+"\n";
               str += "WN20\t"+wn2+"\n";
               str += "WN16\t"+wn16+"\n";
               str += "WN15\t"+wn15+"\n\n";
               stats.write(str.getBytes());

               str = "Equivalent types+\n";
               stats.write(str.getBytes());
               keySet = equiTypes.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   String key = (String) keys.next();
                   Integer cnt = equiTypes.get(key);
                   str = "\t"+key+"\t"+cnt+"\n";
                   stats.write(str.getBytes());
               }
               str = "\nEquivalent authors+\n";
               stats.write(str.getBytes());
               keySet = equiAuthors.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   String key = (String) keys.next();
                   Integer cnt = equiAuthors.get(key);
                   str = "\t"+key+"\t"+cnt+"\n";
                   stats.write(str.getBytes());
               }
               str = "\nEquivalent status+\n";
               stats.write(str.getBytes());
               keySet = equiStatus.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   String key = (String) keys.next();
                   Integer cnt = equiStatus.get(key);
                   str = "\t"+key+"\t"+cnt+"\n";
                   stats.write(str.getBytes());
               }

               keySet = engDutchSynsetMapping.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   String key = (String) keys.next();
                   ArrayList<String> dSynsets = engDutchSynsetMapping.get(key);
                   Integer synsetSize = dSynsets.size();
                   if (synsetSize>4) {
                       //str = key+"\t"+synsetSize.toString()+"\t"+dSynsets.toString()+"\n";
                       str = key+"\t"+synsetSize.toString()+"\t[";
                       for (int i = 0; i < dSynsets.size(); i++) {
                           String s = dSynsets.get(i);
                           CdbSynset synset = parser.synsetIdSynsetMap.get(s);
                           String synonymString = synset.toFlatSynonymLUString();
                           str += s+":"+synonymString+",";
                       }
                       str += "]\n";
                   }
                   else {
                       str = key+"\t"+synsetSize.toString()+"\n";
                   }
                   fose.write(str.getBytes());
                   if (edMappingRatio.containsKey(synsetSize)) {
                       Integer cnt = edMappingRatio.get(synsetSize);
                       cnt++;
                       edMappingRatio.put(synsetSize, cnt);
                   }
                   else {
                       edMappingRatio.put(synsetSize, 1);
                   }
               }
               fose.close();
               str = "\nMapping ratio Dutch-English+\n";
               stats.write(str.getBytes());
               keySet = deMappingRatio.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   Integer key = (Integer) keys.next();
                   Integer cnt = deMappingRatio.get(key);
                   str = "\t"+key+"\t"+cnt+"\n";
                   stats.write(str.getBytes());
               }

               str = "\nMapping ratio English-Dutch+\n";
               stats.write(str.getBytes());
               keySet = edMappingRatio.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   Integer key = (Integer) keys.next();
                   Integer cnt = edMappingRatio.get(key);
                   str = "\t"+key+"\t"+cnt+"\n";
                   stats.write(str.getBytes());
               }

               str = "\nNr. synonyms Nouns+\n";
               stats.write(str.getBytes());
               keySet = synonymsNouns.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   Integer key = (Integer) keys.next();
                   Integer cnt = synonymsNouns.get(key);
                   str = "\t"+key+"\t"+cnt+"\n";
                   stats.write(str.getBytes());
               }

               str = "\nNr. synonyms Verbs+\n";
               stats.write(str.getBytes());
               keySet = synonymsVerbs.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   Integer key = (Integer) keys.next();
                   Integer cnt = synonymsVerbs.get(key);
                   str = "\t"+key+"\t"+cnt+"\n";
                   stats.write(str.getBytes());
               }

               str = "\nNr. synonyms Adjectives+\n";
               stats.write(str.getBytes());
               keySet = synonymsAdjectives.keySet();
               keys = keySet.iterator();
               while (keys.hasNext()) {
                   Integer key = (Integer) keys.next();
                   Integer cnt = synonymsAdjectives.get(key);
                   str = "\t"+key+"\t"+cnt+"\n";
                   stats.write(str.getBytes());
               }

               stats.close();
           } catch (IOException e) {
               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
           }

       }
}
