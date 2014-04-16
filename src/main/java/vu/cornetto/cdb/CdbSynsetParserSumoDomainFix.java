package vu.cornetto.cdb;


import vu.cornetto.dwn.DwnDomainRelation;
import vu.cornetto.pwn.DomainSaxParser;
import vu.cornetto.sumo.SumoRelation;
import vu.cornetto.sumo.SumoSaxParser;
import vu.cornetto.util.FileProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 11-jul-2007
 * Time: 18:01:13
 * To change this template use File | Settings | File Templates.
 */
public class CdbSynsetParserSumoDomainFix {
    static final String SYSTEM_ENCODING = System.getProperty("file.encoding");
    String value = "";
    CdbSynset cdbSynset;
    CdbEquivalenceRelation equi;
    CdbInternalRelation inter;
    Authorship author;
    public HashMap data;
    int nSynsets;
    boolean INTER;
    boolean EQUI;
    boolean VLISDOMAIN;
    boolean WNDOMAIN;
    final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<cdb>";
    public static HashMap sumoMappings;
    public static HashMap domainMappings;

        ///* This function reads a synset file and stores CdbSynset objects in a hashmap
        ///
        public static void main(String[] args) throws IOException {
            // ENCODING = UTF8;
            String synsetFile = args[0];
            String sumoAll = args[1];
            String domAll_15 = args[2];
            String domAll_20 = args[3];
            SumoSaxParser sumoParser = new SumoSaxParser();
            sumoParser.initParser();
            sumoParser.parseFile(sumoAll, SYSTEM_ENCODING);
            System.out.println("sumoParser = " + sumoParser.pwn15Sumo.size());
            DomainSaxParser domParser = new DomainSaxParser();
            domParser.initParser();
            domParser.parseFile(domAll_15,SYSTEM_ENCODING);
            domParser.parseFile(domAll_20,SYSTEM_ENCODING);
            System.out.println("domParser = " + domParser.pwn15Domains.size());
            try {

                String cdbSynsetFile = args[0];
                FileOutputStream importOutFile = new FileOutputStream(cdbSynsetFile+".imp.xml");
                OutputStreamWriter synsetOut = new OutputStreamWriter(importOutFile, "UTF-8");
                String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<cdb_synsets>\n";
                FileProcessor.storeResult(synsetOut, str);
                CdbSynsetParser cdbSynsetParser = new CdbSynsetParser();
                cdbSynsetParser.parseFile(cdbSynsetFile);
                //cdbSynsetParser.parseFileAsBytes(cdbSynsetFile);
                //cdbSynsetParser.parseFile(cdbSynsetFile, "UTF-8");
                Set keySet = cdbSynsetParser.synsetIdSynsetMap.keySet();
                Iterator keys = keySet.iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();

                    CdbSynset synset = (CdbSynset) cdbSynsetParser.synsetIdSynsetMap.get(key);
                    if (!synset.manualEquivalence()) {
                        /// STORE THE ORIGINAL STRING FIRST
                        str = synset.toString();
                        if (synset.fixEquivalence()) {
                            System.out.println("\nTO BE FIXED");
                            System.out.println("key = " + key);
                            System.out.println("synset.getD_synset_id() = " + synset.getD_synset_id());
                            System.out.println("BEFORE");
                            System.out.println("synset.getDRelations().size() = " + synset.getDRelations().size());
                            System.out.println("synset.getSRelations.size() = " + synset.getSRelations().size());
                            if ((synset.getDRelations().size()>2) && !synset.manualDomain()) {
                                synset.setDRelations(getDomainRelations(domParser, synset.getERelations()));
                            }
                            if ((synset.getSRelations().size()>2) && !synset.manualOntology()) {
                                synset.setSRelations(getSumoRelations(sumoParser, synset.getERelations()));
                            }
                            System.out.println("AFTER");
                            System.out.println("synset.getDRelations().size() = " + synset.getDRelations().size());
                            System.out.println("synset.getSRelations.size() = " + synset.getSRelations().size());
                            str = synset.toString(); // ADAPT THE ORIGINAL STRING
                        }
                        FileProcessor.storeResult(synsetOut, str);
                    }
                    else {
                        //System.out.println("Manual synset.getD_synset_id() = " + synset.getD_synset_id());
                        str = synset.toString();
                        FileProcessor.storeResult(synsetOut, str);
                    }
                }
                str = "</cdb_synsets>\n";
                FileProcessor.storeResult(synsetOut, str);
                synsetOut.flush();
                synsetOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        static boolean hasDom (String term, ArrayList doms) {
            for (int d=0; d<doms.size();d++) {
                CdbDomainRelation dom = (CdbDomainRelation) doms.get(d);
                if (term.equals(dom.getTerm())) {
                    return true;
                }
            }
            return false;
        }

        static boolean hasSumo (String term, ArrayList sumos) {
            for (int d=0; d<sumos.size();d++) {
                SumoRelation sumo = (SumoRelation) sumos.get(d);
                if (term.equals(sumo.getArg2())) {
                    return true;
                }
            }
            return false;
        }

        static ArrayList getDomainRelations (
                DomainSaxParser domParser, ArrayList equiRelations) {
            ArrayList doms = new ArrayList();
            for (int e=0; e<equiRelations.size();e++) {
                CdbEquivalenceRelation equi = (CdbEquivalenceRelation) equiRelations.get(e);
                String id20 = equi.getTarget20();
                String id15 = equi.getTarget15();
                String id16 = equi.getTarget16();
                if (domParser.pwn20Domains.containsKey(id20)) {
                   ArrayList rels = (ArrayList) domParser.pwn20Domains.get(id20);
                   for (int r=0; r<rels.size();r++) {
                       DwnDomainRelation dom = (DwnDomainRelation) rels.get(r);
                       if (!hasDom(dom.getTerm(), doms)) {
                           CdbDomainRelation cDom = new CdbDomainRelation (dom);
                           doms.add(cDom);
                       }
                   }
                }
                else if (domParser.pwn15Domains.containsKey(id15)) {
                    ArrayList rels = (ArrayList) domParser.pwn15Domains.get(id15);
                    for (int r=0; r<rels.size();r++) {
                        DwnDomainRelation dom = (DwnDomainRelation) rels.get(r);
                        if (!hasDom(dom.getTerm(), doms)) {
                            CdbDomainRelation cDom = new CdbDomainRelation (dom);
                            System.out.println("cDom.getTerm() = " + cDom.getTerm());
                            doms.add(cDom);
                        }
                    }
                }
                else if (domParser.pwn16Domains.containsKey(id16)) {
                    ArrayList rels = (ArrayList) domParser.pwn16Domains.get(id16);
                    for (int r=0; r<rels.size();r++) {
                        DwnDomainRelation dom = (DwnDomainRelation) rels.get(r);
                        if (!hasDom(dom.getTerm(), doms)) {
                            CdbDomainRelation cDom = new CdbDomainRelation (dom);
                            System.out.println("cDom.getTerm() = " + cDom.getTerm());
                            doms.add(cDom);
                        }
                    }
                }
            }
            return doms;
        }

        static ArrayList getSumoRelations (SumoSaxParser sumoParser, ArrayList equiRelations) {
            ArrayList sumos = new ArrayList();
            for (int e=0; e<equiRelations.size();e++) {
                CdbEquivalenceRelation equi = (CdbEquivalenceRelation) equiRelations.get(e);
                //System.out.println("equi.getTarget20Previewtext() = " + equi.getTarget20Previewtext());
                String id20 = equi.getTarget20();
                String id15 = equi.getTarget15();
                String id16 = equi.getTarget16();
                if (sumoParser.pwn20Sumo.containsKey(id20)) {
                    //System.out.println("id20 = " + id20);
                    ArrayList rels = (ArrayList) sumoParser.pwn20Sumo.get(id20);
                    //System.out.println("rels.size() = " + rels.size());
                    for (int r=0; r<rels.size();r++){
                        SumoRelation sumo = (SumoRelation) rels.get(r);
                        if (!hasSumo(sumo.getArg2(), sumos)) {
                            //System.out.println("vu.cornetto.sumo.getArg2() = " + vu.cornetto.sumo.getArg2());
                            sumos.add(sumo);
                        }
                    }
                }
                else if (sumoParser.pwn15Sumo.containsKey(id15)) {
                    //System.out.println("id15 = " + id15);
                    ArrayList rels = (ArrayList) sumoParser.pwn15Sumo.get(id15);
                    //System.out.println("rels.size() = " + rels.size());
                    for (int r=0; r<rels.size();r++){
                        SumoRelation sumo = (SumoRelation) rels.get(r);
                        if (!hasSumo(sumo.getArg2(), sumos)) {
                            //System.out.println("vu.cornetto.sumo.getArg2() = " + vu.cornetto.sumo.getArg2());
                            sumos.add(sumo);
                        }
                    }
                }
                else if (sumoParser.pwn16Sumo.containsKey(id16)) {
                    //System.out.println("id16 = " + id16);
                    ArrayList rels = (ArrayList) sumoParser.pwn16Sumo.get(id16);
                    //System.out.println("rels.size() = " + rels.size());
                    for (int r=0; r<rels.size();r++){
                        SumoRelation sumo = (SumoRelation) rels.get(r);
                        if (!hasSumo(sumo.getArg2(), sumos)) {
                            //System.out.println("vu.cornetto.sumo.getArg2() = " + vu.cornetto.sumo.getArg2());
                            sumos.add(sumo);
                        }
                    }
                }
            }
            return sumos;
        }

        static boolean automaticData(CdbSynset synset) {
            if (synset.manualDomain() ||
                synset.manualEquivalence() ||
                synset.manualOntology()) {
                return false;
            }            
            return true;
        }
}