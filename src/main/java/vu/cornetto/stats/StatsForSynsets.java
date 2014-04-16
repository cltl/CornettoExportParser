package vu.cornetto.stats;


import vu.cornetto.cdb.CdbSynsetParser;
import vu.cornetto.cdb.CdbSynset;
import vu.cornetto.cdb.Syn;
import vu.cornetto.cdb.CdbInternalRelation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 28-aug-2008
 * Time: 13:21:03
 * To change this template use File | Settings | File Templates.
 */
public class StatsForSynsets {

        public static void main (String args[]) {
            if (args.length!=1) {
                System.out.println("USAGE:");
                System.out.println("\targ1 = cdb_synset export file");
            }
            String cdbSynFilePath = args[0];
            CdbSynsetParser parser = new CdbSynsetParser();

            //parser.parseFileAsBytes(cdbSynFilePath);
            parser.bigLog = true;
            parser.parseFile(cdbSynFilePath);
            try {
                FileOutputStream fosStats = new FileOutputStream(cdbSynFilePath+".stats.xls");
                FileOutputStream fos = new FileOutputStream(cdbSynFilePath+".data.log");
                FileOutputStream fosDupLu = new FileOutputStream(cdbSynFilePath+".duplu.log");
                FileOutputStream fosDupForm = new FileOutputStream(cdbSynFilePath+".dupform.log");
                FileOutputStream fosMisTarget = new FileOutputStream(cdbSynFilePath+".missingtarget.log");
                FileOutputStream fosInterRelations = new FileOutputStream(cdbSynFilePath+".nointerrelations.log");
                String str ="Synsets\t"+cdbSynFilePath+"\n\n";
                str += "Nr of synsets\t"+parser.synsetIdSynsetMap.size()+"\n";
                fosStats.write(str.getBytes());
                Set keySet = parser.synsetIdSynsetMap.keySet();
                Iterator keys = keySet.iterator();
                ArrayList synsets = new ArrayList();
                HashMap targets = new HashMap();
                int notFound = 0;
                int dupForm = 0;
                int emptyPreview = 0;
                int duplicateLu = 0;
                int noSynonyms = 0;
                int nInterRelations= 0;
                int noRelations = 0;
                int noInf = 0;
                boolean duplicateLU = false;
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    CdbSynset synset = (CdbSynset) parser.synsetIdSynsetMap.get(key);
                    String c_sy_id = synset.getC_sy_id();
                    synsets.add(c_sy_id);
                    fos.write(("SYNSET:"+c_sy_id+"\n").getBytes());
                    //System.out.println("c_sy_id = " + c_sy_id);
                    if ((synset.getSynonyms().size()==0) &&
                        (synset.getIRelations().size()==0) &&
                        (synset.getERelations().size()==0))
                        {
                        noInf++;
                        System.out.println("ERROR: NO INFORMATION. VOID SYNSET:"+c_sy_id);
                        continue;
                    }
                    ArrayList syns = synset.getSynonyms();
                    if (syns.size()==0) {
                        noSynonyms++;
                        System.out.println("ERROR: NO SYNONYMS:"+c_sy_id);
                    }
                    else {
                        ArrayList synLus = new ArrayList();
                        ArrayList synForms = new ArrayList();
                        for (int i=0;i<syns.size();i++) {
                           Syn syn = (Syn) syns.get(i);
                           fos.write(("LU:"+syn.getC_lu_id()+"\n").getBytes());
                           fos.write(("FORM:"+syn.getForm()+":"+syn.getSeqNr()+"\n").getBytes());
                           /// CHECKING DUPLCATE FORMS
                            if ((!syn.getForm().equals("not_found")) && (synForms.contains(syn.getForm()))) {
                                for (int j=0;j<syns.size();j++) {
                                    //// GETTING DUPLICATE FORMS
                                   Syn aSyn = (Syn) syns.get(j);
                                   if (i!=j) {
                                       if (aSyn.getForm().equals(syn.getForm())) {
                                           if (!syn.getC_lu_id().equals(aSyn.getC_lu_id())) {
                                               dupForm++;
                                               str="\nERROR: DUPLICATE FORM= " + c_sy_id+":"+syn.getPreviewtext()+"\n";
                                               //System.out.println("str = " + str);
                                               str +="\tsyn.getForm() = " + syn.getForm()+"\n";
                                               str +="\tsyn.getSeqNr() = " + syn.getSeqNr()+"\n";
                                               str +="\tsyn.getC_lu_id() = " + syn.getC_lu_id()+"\n";
                                               str +="\tsyn.getStatus() = " + syn.getStatus()+"\n";
                                               str +="\taSyn.getSeqNr() = " + aSyn.getSeqNr()+"\n";
                                               str +="\taSyn.getC_lu_id() = " + aSyn.getC_lu_id()+"\n";
                                               str +="\taSyn.getStatus() = " + aSyn.getStatus()+"\n";
                                               fosDupForm.write(str.getBytes());
                                               str = "";
                                           }
                                       }
                                   }
                                }
                            }
                            else {
                                synForms.add(syn.getForm());
                            }

                            /// CHECKING DUPLICATE LUs
                            if (synLus.contains(syn.getC_lu_id())){
                                for (int j=0;j<syns.size();j++) {
                                    //// GETTING DUPLICATE LUs
                                   Syn aSyn = (Syn) syns.get(j);
                                    if (i!=j) {
                                       if (aSyn.getC_lu_id().equals(syn.getC_lu_id())) {
                                           str="\nERROR: DUPLICATE LU IN SYNSET= " + c_sy_id+":"+syn.getPreviewtext()+"\n";
                                           //System.out.println("str = " + str);
                                           str+="\tsyn.getC_lu_id() = " + syn.getC_lu_id()+"\n";
                                           str+="\tsyn.getStatus() = " + syn.getStatus()+"\n";
                                           str+="\taSyn.getStatus() = " + aSyn.getStatus()+"\n";
                                           fosDupLu.write(str.getBytes());
                                           str = "";
                                           duplicateLu++;
                                       }
                                    }
                                }
                            }
                            else {
                                    synLus.add(syn.getC_lu_id()); 
                            }

                            /// CHECKING PREVIEW TEXT
                            int idx = syn.getPreviewtext().indexOf(":");
                            if (idx==-1) {
                                emptyPreview++;
                                System.out.println("ERROR: NO PREVIEW TEXT TO CHECK THIS FORM="+ c_sy_id);
                            }
                            else {
                                String form = syn.getPreviewtext().substring(0, idx);
                                if (form.equals("not_found")) {
                                    notFound++;
                                    System.out.println("ERROR: NOT_FOUND SYNONYM="+ c_sy_id);
                                }
                            }
                        }
                    }
                    
/*                    if ((synset.getIRelations().size()==0) &&
                        (synset.getERelations().size()==0)) {
                        noRelations++;
                        System.out.println("ERROR: NO RELATIONS="+c_sy_id+":"+synset.toSynsetString());
                    }*/
                    if (synset.getIRelations().size()==0) {
                            nInterRelations++;
                            str="ERROR: NO INTERNAL RELATIONS="+ c_sy_id+":"+synset.toSynsetString()+"\n";
                            fosInterRelations.write(str.getBytes());
                            str ="";
                    }
                    else {
                        ArrayList relations = synset.getIRelations();
                        for (int i=0; i< relations.size();i++) {
                            CdbInternalRelation rel = (CdbInternalRelation) relations.get(i);
                            String target = rel.getTarget();
                            if (targets.containsKey(target)) {
                                ArrayList targetData = (ArrayList) targets.get(target);
                                targetData.add(c_sy_id+":"+rel.getRelation_name()+":"+synset.toFlatSynonymString());
                                targets.put(target, targetData);
                            }
                            else {
                                ArrayList targetData = new ArrayList();
                                targetData.add(c_sy_id+":"+rel.getRelation_name()+":"+synset.toFlatSynonymString());
                                targets.put(target,targetData );
                            }
                        }
                   }
                }
                fos.close();
                fosDupForm.close();
                fosDupLu.close();
                fosInterRelations.close();
                long gtargets = 0;
                long badtargets = 0;
                keySet = targets.keySet();
                keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (synsets.contains(key)) {
                        gtargets++;
                    }
                    else {
                        ArrayList dataArray = (ArrayList) targets.get(key);
                        for (int t=0; t<dataArray.size(); t++) {
                            String data = (String) dataArray.get(t);
                            if (data.indexOf("HAS_HYPONYM")==-1) {
                                str="ERROR: MISSING TARGET:" + key+" IN:"+data+"\n";
                                fosMisTarget.write(str.getBytes());
                                badtargets++;
                            }
                        }
                    }
                }
                fosMisTarget.close();
                str = "##################################";
                str+= "void Synsets\t" + noInf+"\n";
                str += "no Synonyms\t" + noSynonyms+"\n";
                str += "no Relations\t" + noRelations+"\n";
                str += "no InterRelations\t" + nInterRelations+"\n";
                str += "duplicate LUs\t" + duplicateLu+"\n";
                str += "not Found Synonyms\t" + notFound+"\n";
                str += "Synonyms with empty Preview\t" + emptyPreview+"\n";
                str += "duplicate Forms in synonyms\t" + dupForm+"\n";
                str += "##################################"+"\n";
                str += "good targets\t" + gtargets+"\n";
                str += "bad targets\t" + badtargets+"\n";
                System.out.flush();
            }
            catch (FileNotFoundException e) {

            }
            catch (IOException ee) {
                
            }
        }
}
