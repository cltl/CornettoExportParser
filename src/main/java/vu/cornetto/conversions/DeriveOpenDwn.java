package vu.cornetto.conversions;

import vu.cornetto.cdb.*;
import vu.cornetto.util.FileProcessor;
import vu.cornetto.wnlmf.SynsetData;
import vu.cornetto.wnlmf.WordnetLmfSaxParser;
import vu.cornetto.wnlmf.WordnetRelation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 9/3/12
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeriveOpenDwn {
    static final String HYPERONYM_STRING = "has_hyperonym";
    static final String cornettoversion = "cdb2.2";
    static HashMap<String, ArrayList<String>> idMapNewOld = new HashMap<String, ArrayList<String>>();
    static HashMap<String, ArrayList<String>> idMapOldNew = new HashMap<String, ArrayList<String>>();

    static public String getParentTarget (String key, HashMap<String, CdbSynset> map)  {
        if (map.containsKey(key)) {
            CdbSynset synset = map.get(key);
            if (synset.getERelations().size()>0) {
                for (int i = 0; i < synset.getERelations().size(); i++) {
                    CdbEquivalenceRelation cdbEquivalenceRelation = (CdbEquivalenceRelation) synset.getERelations().get(i);
                    if ((cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_synonym")) ||
                            (cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_near_synonym")))  {
                        String synsetId = cdbEquivalenceRelation.getTarget30();
                        if (synsetId.isEmpty()) synsetId = cdbEquivalenceRelation.getTarget20();
                        if (synsetId.isEmpty()) synsetId = cdbEquivalenceRelation.getTarget16();
                        if (synsetId.isEmpty()) synsetId = cdbEquivalenceRelation.getTarget15();
                        if (!synsetId.isEmpty()) {
                            //// takes the first id
                            return synsetId;
                        }
                    }
                }
            }
            for (int i = 0; i < synset.getIRelations().size(); i++) {
                CdbInternalRelation cdbInternalRelation = (CdbInternalRelation) synset.getIRelations().get(i);
                if (cdbInternalRelation.getRelation_name().equalsIgnoreCase(HYPERONYM_STRING)) {
                   String hyper = cdbInternalRelation.getTarget();
                   String parent = getParentTarget(hyper, map);
                    // we take the first success
                   return parent;
                }
            }

        }
        return "";
    }

    static public void getParents (WordnetLmfSaxParser wordnetLmfSaxParser, String pwnTargetSynsetId, ArrayList<String> parents) {
        if (wordnetLmfSaxParser.wordnetData.containsKey(pwnTargetSynsetId)) {
            if (!parents.contains(pwnTargetSynsetId)) {
                parents.add(pwnTargetSynsetId);
                SynsetData synsetData = wordnetLmfSaxParser.wordnetData.get(pwnTargetSynsetId);
                for (int i = 0; i < synsetData.getRelations().size(); i++) {
                    WordnetRelation wordnetRelation = synsetData.getRelations().get(i);
                    if (wordnetRelation.getRelation().equalsIgnoreCase("hypernym")) {
                        getParents(wordnetLmfSaxParser, wordnetRelation.getTarget(), parents);
                    }
                }
            }
        }
    }

    static public String getMostSpecificTarget (CdbSynset cdbSynset, WordnetLmfSaxParser wordnetLmfSaxParser) {
        String mostSpecific = "";
        ArrayList<ArrayList<String>> chains = new ArrayList<ArrayList<String>>();
        for (int j = 0; j < cdbSynset.getERelations().size(); j++) {
            CdbEquivalenceRelation cdbEquivalenceRelation   = (CdbEquivalenceRelation) cdbSynset.getERelations().get(j);
            String pwnTargetSynsetId = cdbEquivalenceRelation.getTarget30();
            ArrayList<String> chain = new ArrayList<String>();
            if (!pwnTargetSynsetId.isEmpty()) {
                getParents(wordnetLmfSaxParser, pwnTargetSynsetId, chain);
                chains.add(chain);
            }
        }
        return mostSpecific;
    }

    static public void addRelationsToRelationMap (String sourceId, CdbSynset synset, HashMap<String, ArrayList<CdbInternalRelation>> relationMap) {
          if (relationMap.containsKey(sourceId)) {
              ArrayList<CdbInternalRelation> rels = relationMap.get(sourceId);
              for (int i = 0; i < synset.getIRelations().size(); i++) {
                  CdbInternalRelation newRel =  (CdbInternalRelation) synset.getIRelations().get(i);
                  if (!newRel.getAuthor().getName().equalsIgnoreCase("paul")) {
                      rels.add(newRel);
                  }
              }
              relationMap.put(sourceId, rels);
          }
          else {
              ArrayList<CdbInternalRelation> rels = new ArrayList<CdbInternalRelation>();
              for (int i = 0; i < synset.getIRelations().size(); i++) {
                  CdbInternalRelation newRel =  (CdbInternalRelation) synset.getIRelations().get(i);
                  if (!newRel.getAuthor().getName().equalsIgnoreCase("paul")) {
                      rels.add(newRel);
                  }
              }
              relationMap.put(sourceId, rels);
          }
    }

    /**
     *
     WordnetLmfSaxParser wordnetLmfSaxParser,
     ArrayList<String> coveredSynsets,
     String target) {

     if (wordnetLmfSaxParser.wordnetData.containsKey(target)) {
     SynsetData synsetData = wordnetLmfSaxParser.wordnetData.get(target);
     CdbSynset pwnSynset = new CdbSynset();
     * @param args
     */
    static public void main (String[] args)  {
        String pathToCdbSyn = "";
        String pathToCdbLu = ""; /// we are not yet using the LUs.
        String pathToPwn = "";
        String pathToN_NIncludeList ="";
        ArrayList<String> includeN_NList = new ArrayList<String>();
        boolean NORIGIN = false;
        boolean SYNONYMPREEMPTION = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase("--cdb-syn") && args.length>(i+1)) {
                pathToCdbSyn = args[i+1];
            }
            else if (arg.equalsIgnoreCase("--cdb-lu") && args.length>(i+1)) {
                pathToCdbLu = args[i+1];
            }
            else if (arg.equalsIgnoreCase("--preempt")) {
                SYNONYMPREEMPTION = true;
            }
            else if (arg.equalsIgnoreCase("--n_n")) {
                NORIGIN = true;
            }
            else if (arg.equalsIgnoreCase("--n_n-include")) {
                includeN_NList = FileProcessor.ReadFileToArrayList(args[i+1]);
            }
            else if (arg.equalsIgnoreCase("--wn-lmf") && args.length>(i+1)) {
                pathToPwn = args[i+1];
            }
        }
        try {
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<cdbsyn>\n" +
                    "\n";
            String nopwn = ".nopwn.xml";
            String odwn = ".odwn.xml";
            String idNewOld = ".id-new-oldmap";
            String idOldNew = ".id-old-new-map";
            if (SYNONYMPREEMPTION) {
                nopwn += ".preempt"+nopwn;
                odwn += ".preempt"+odwn;
                idNewOld += ".preempt"+idNewOld;
                idOldNew += ".preempt"+idOldNew;
            }
            if (NORIGIN) {
                nopwn += ".n_n"+nopwn;
                odwn += ".n_n"+odwn;
                idNewOld += ".n_n"+idNewOld;
                idOldNew += ".n_n"+idOldNew;
            }
            FileOutputStream fosRelations = new FileOutputStream(pathToCdbSyn+".rel");
            FileOutputStream fosNoPwn = new FileOutputStream(pathToCdbSyn+nopwn);
            FileOutputStream fosOdwn = new FileOutputStream(pathToCdbSyn+odwn);
            FileOutputStream fosMapOldNew = new FileOutputStream(pathToCdbSyn+idOldNew);
            FileOutputStream fosMapNewOld = new FileOutputStream(pathToCdbSyn+idNewOld);
            fosOdwn.write(str.getBytes());
            fosNoPwn.write(str.getBytes());
            ArrayList<String> missedSynsets = new ArrayList<String>();
            ArrayList<String> coveredSynsets  = new ArrayList<String>();
            HashMap<String, ArrayList<CdbInternalRelation>> relationMap = new HashMap<String, ArrayList<CdbInternalRelation>>();
            HashMap<String, CdbSynset> openDWordnetMap = new HashMap<String, CdbSynset>();
            HashMap<String, CdbSynset> addWordnetMap = new HashMap<String, CdbSynset>();
            CdbSynsetParser cdbSynsetParser = new CdbSynsetParser();
            //CdbLuMapFastDomParser cdbLuMapFastDomParser = new CdbLuMapFastDomParser();
            WordnetLmfSaxParser wordnetLmfSaxParser = new WordnetLmfSaxParser();
            cdbSynsetParser.parseFile(pathToCdbSyn);
           // cdbLuMapFastDomParser.readLuFile(pathToCdbLu);
            wordnetLmfSaxParser.parseFile(pathToPwn);


            //// WE FIRST BUILD UP A ENGLISH TARGET SET FROM THE EQ_SYNONYM MAPPINGS.
            //   THIS CAN BE USED TO PREVENT MERGES OF NEAR_SYNONYM. WE ASSUME THAT IF
            //   THERE IS A EQ_SYNONYM MAPPING THAT THE TARGET IS TAKEN. ANY EQ_NEAR_SYNONYM
            //   MAPPINGS POINTING TO THE SAME TARGET ARE THEN INTERPRETED AS EQ_HYPERONYM MAPPINGS.
            //   THIS ONLY WORKS IF THE --preempt FLAG IS SET.

            ArrayList<String> eqSynonymTargets = new ArrayList<String>();
            System.out.println("wordnetLmfSaxParser.wordnetData.size() = " + wordnetLmfSaxParser.wordnetData.size());
            Set keySet = cdbSynsetParser.synsetIdSynsetMap.keySet();
            Iterator keys = keySet.iterator();
            if (SYNONYMPREEMPTION) {
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    ///// IF NORIGIN == false
                    ///// skipping synset ids with n_n unless explicitly admitted
                    if ((!NORIGIN) && key.startsWith("n_n"))  {
                        if (includeN_NList.size()>0) {
                            if (!includeN_NList.contains(key)) {
                                /// not admitted
                                continue;
                            }
                            else {
                                /// this id is in the n_n_include list and must be processed
                                System.out.println("admitted n_n key = " + key);
                            }
                        }
                        else {
                            /// no admitted synsets are given
                            continue;
                        }
                    }
                    // System.out.println("key = " + key);
                    CdbSynset cdbSynset = cdbSynsetParser.synsetIdSynsetMap.get(key);

                    /// skipping adjectives and others
                    if (cdbSynset.getPos().equalsIgnoreCase("adjective")) {
                        continue;
                    }
                    else if (cdbSynset.getPos().equalsIgnoreCase("other")) {
                        continue;
                    }

                    /// READY TO START

                    boolean WEFOUNDAPWNMATCH = false;
                    if (cdbSynset.getERelations().size()>0) {
                        //// we have equivalences and can insert the lexical units into the PWN wordnet if the relation is correct
                        //// if there is more than one appropriate target, we create a problem by inserting the same lexical unit into multiple targets
                        //// we fix this problem by creating subsenses for the lexical unit
                        int eqSynonymNearSynonymCount = 0; /// this counter is needed to determine the total amount of subsenses
                        int nSubsense = 0;
                        for (int j = 0; j < cdbSynset.getERelations().size(); j++) {
                            CdbEquivalenceRelation cdbEquivalenceRelation   = (CdbEquivalenceRelation) cdbSynset.getERelations().get(j);
                            if ((cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_synonym")))  {
                                String pwnTargetSynsetId = cdbEquivalenceRelation.getTarget30();
                                if (pwnTargetSynsetId.isEmpty()) pwnTargetSynsetId = cdbEquivalenceRelation.getTarget20();
                                if (pwnTargetSynsetId.isEmpty()) pwnTargetSynsetId = cdbEquivalenceRelation.getTarget16();
                                if (pwnTargetSynsetId.isEmpty()) pwnTargetSynsetId = cdbEquivalenceRelation.getTarget15();
                                if (!pwnTargetSynsetId.isEmpty()) {
                                    pwnTargetSynsetId = "eng-"+pwnTargetSynsetId.substring(3);
                                    eqSynonymTargets.add(pwnTargetSynsetId);
                                }
                            }
                        }
                    }
                }
                System.out.println("eqSynonymTargets = " + eqSynonymTargets.size());
            }
            int nPreEmptions = 0;
            String status = "";
            //keySet = cdbSynsetParser.synsetIdSynsetMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ///// IF NORIGIN == false
                ///// skipping synset ids with n_n unless explicitly admitted
                if ((!NORIGIN) && key.startsWith("n_n"))  {
                    if (includeN_NList.size()>0) {
                       if (!includeN_NList.contains(key)) {
                           /// not admitted
                            continue;
                        }
                        else {
                            /// this id is in the n_n_include list and must be processed
                            System.out.println("admitted n_n key = " + key);
                        }
                    }
                    else {
                        /// no admitted synsets are given
                        continue;
                    }
                }
                // System.out.println("key = " + key);
                CdbSynset cdbSynset = cdbSynsetParser.synsetIdSynsetMap.get(key);

                /// skipping adjectives and others
                if (cdbSynset.getPos().equalsIgnoreCase("adjective")) {
                    continue;
                }
                else if (cdbSynset.getPos().equalsIgnoreCase("other")) {
                    continue;
                }

                /////READY TO START
                boolean WEFOUNDAPWNMATCH = false;
                if (cdbSynset.getERelations().size()>0) {
                    //// we have equivalences and can insert the lexical units into the PWN wordnet if the relation is correct
                    //// if there is more than one appropriate target, we create a problem by inserting the same lexical unit into multiple targets
                    //// we fix this problem by creating subsenses for the lexical unit
                    int eqSynonymNearSynonymCount = 0; /// this counter is needed to determine the total amount of subsenses
                    int nSubsense = 0;
                    for (int j = 0; j < cdbSynset.getERelations().size(); j++) {
                        CdbEquivalenceRelation cdbEquivalenceRelation   = (CdbEquivalenceRelation) cdbSynset.getERelations().get(j);
                        if ((cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_synonym")) ||
                                (cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_near_synonym")))  {
                             eqSynonymNearSynonymCount++;
                        }
                    }
                    for (int j = 0; j < cdbSynset.getERelations().size(); j++) {
                        CdbEquivalenceRelation cdbEquivalenceRelation   = (CdbEquivalenceRelation) cdbSynset.getERelations().get(j);
                        String pwnTargetSynsetId = cdbEquivalenceRelation.getTarget30();
                        if (pwnTargetSynsetId.isEmpty()) pwnTargetSynsetId = cdbEquivalenceRelation.getTarget20();
                        if (pwnTargetSynsetId.isEmpty()) pwnTargetSynsetId = cdbEquivalenceRelation.getTarget16();
                        if (pwnTargetSynsetId.isEmpty()) pwnTargetSynsetId = cdbEquivalenceRelation.getTarget15();
                        if (!pwnTargetSynsetId.isEmpty()) {
                            pwnTargetSynsetId = "eng-"+pwnTargetSynsetId.substring(3);
                            if ((cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_synonym"))  ||
                                    /// eq_near is only accepted if eq_synonym does not exist
                                    ((cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_near_synonym")) &&
                                       !eqSynonymTargets.contains(pwnTargetSynsetId)
                                    )
                                    )  {
                                WEFOUNDAPWNMATCH = true;
                                status = cornettoversion+"_"+cdbEquivalenceRelation.getRelationName();
                                /// we trust that the relation is good enough to use the english synset to represent the Dutch synset
                                CdbSynset pwnSynset = new CdbSynset();
                                if (openDWordnetMap.containsKey(pwnTargetSynsetId)) {
                                    /// if the synsets is already in the oDWNmap we add the info from the current cdb synset

                                    pwnSynset = openDWordnetMap.get(pwnTargetSynsetId);
                                    pwnSynset.addComment(cdbEquivalenceRelation.getRelationName());
                                    pwnSynset.addPosSpecific(cdbSynset.getPos());

                                    /// Next statement just takes the current synonyms
                                    // pwnSynset.addSynonyms(cdbSynset.getSynonyms());
                                    /// Next statement is creates subsenses for each mapping in case there is more than one equivalence relation
                                    if (eqSynonymNearSynonymCount==1) {
                                        pwnSynset.addSynonyms(status, cdbSynset.getSynonyms());
                                    }
                                    else {
                                        nSubsense++;
                                        pwnSynset.addSynonyms(status,cdbSynset.getSynonymsAsSubSense(nSubsense));
                                    }
                                }
                                else {
                                    //// this pwn Synset was not added yet to oDWN so we need to fill it using the pwn data
                                    pwnSynset.setC_sy_id(pwnTargetSynsetId);
                                    pwnSynset.setComment(cdbEquivalenceRelation.getRelationName());
                                    pwnSynset.setPosSpecific(cdbSynset.getPos());
                                    /// Next statement just takes the current synonyms
                                    // pwnSynset.setSynonyms(cdbSynset.getSynonyms());
                                    /// Next statement is creates subsenses for each mapping in case there is more than one equivalence relation
                                    if (eqSynonymNearSynonymCount==1) {
                                        pwnSynset.setSynonyms(status, cdbSynset.getSynonyms());
                                    }
                                    else {
                                        nSubsense++;
                                        pwnSynset.setSynonyms(status, cdbSynset.getSynonymsAsSubSense(nSubsense));
                                    }
                                    if (wordnetLmfSaxParser.wordnetData.containsKey(pwnTargetSynsetId.toLowerCase())) {
                                        /// we copy all the relations and the gloss from PWN
                                        SynsetData synsetData = wordnetLmfSaxParser.wordnetData.get(pwnTargetSynsetId.toLowerCase());
                                        for (int k = 0; k < synsetData.getRelations().size(); k++) {
                                            WordnetRelation wordnetRelation = synsetData.getRelations().get(k);
                                            CdbInternalRelation rel = new CdbInternalRelation();
                                            rel.setTarget(wordnetRelation.getTarget());
                                            if (wordnetRelation.getRelation().equalsIgnoreCase("hypernym") || (wordnetRelation.getRelation().equalsIgnoreCase("hyperonym"))) {
                                                rel.setRelation_name(HYPERONYM_STRING);
                                            }
                                            else {
                                                rel.setRelation_name(wordnetRelation.getRelation());
                                            }
                                            pwnSynset.addIRelation(rel);
                                        }
                                        pwnSynset.setDefinition(synsetData.getGloss());
                                        coveredSynsets.add(pwnTargetSynsetId);
                                    }

                                }
                                openDWordnetMap.put(pwnTargetSynsetId, pwnSynset);
                                addRelationsToRelationMap(pwnTargetSynsetId, cdbSynset, relationMap);
                                addToIdMaps(cdbSynset.getC_sy_id(), pwnTargetSynsetId);
                            }
                            else if ((cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_near_synonym")) &&
                                    eqSynonymTargets.contains(pwnTargetSynsetId)
                            ) {
                                nPreEmptions++;
                                status = cornettoversion+"_"+cdbEquivalenceRelation.getRelationName();
                                /// the target is taken by a eq_synonym so we turn the eq_near into a hyponym relation
                                WEFOUNDAPWNMATCH = true;
                                // we represent this using the original identifier and link it to the pwn synset as a hyponym
                                CdbSynset pwnSynset = new CdbSynset();
                                pwnSynset.setPosSpecific(cdbSynset.getPos());
                                CdbInternalRelation rel = new CdbInternalRelation();
                                rel.setTarget(pwnTargetSynsetId);
                                rel.setRelation_name(HYPERONYM_STRING);
                                pwnSynset.addIRelation(rel);
                                pwnSynset.setComment("eq-synonym-preempt;" + cdbEquivalenceRelation.getRelationName());
                                /// we create a new identifier for this synset
                                String newId = getRandomIdentifier(cdbSynset.getC_sy_id(),cdbSynset.getPos() );
                                pwnSynset.setC_sy_id(newId);
                                if (eqSynonymNearSynonymCount==1) {
                                    pwnSynset.addSynonyms(status, cdbSynset.getSynonyms());
                                }
                                else {
                                    nSubsense++;
                                    pwnSynset.addSynonyms(status, cdbSynset.getSynonymsAsSubSense(nSubsense));
                                }
                                openDWordnetMap.put(newId, pwnSynset);
                                addRelationsToRelationMap(pwnTargetSynsetId, cdbSynset, relationMap);
                                addToIdMaps(cdbSynset.getC_sy_id(), pwnTargetSynsetId);
                            }
                            else if (cdbEquivalenceRelation.getRelationName().equalsIgnoreCase("eq_has_hyperonym"))  {
                                WEFOUNDAPWNMATCH = true;
                                // we represent this using the original identifier and link it to the pwn synset as a hyponym
                                status = cornettoversion+"_"+cdbEquivalenceRelation.getRelationName();
                                CdbSynset pwnSynset = new CdbSynset();
                                pwnSynset.setPosSpecific(cdbSynset.getPos());
                                CdbInternalRelation rel = new CdbInternalRelation();
                                rel.setTarget(pwnTargetSynsetId);
                                rel.setRelation_name(HYPERONYM_STRING);
                                pwnSynset.addIRelation(rel);
                                pwnSynset.setComment("eq-parent-match;" + cdbEquivalenceRelation.getRelationName());
                                /// we create a new identifier for this synset
                                String newId = getRandomIdentifier(cdbSynset.getC_sy_id(),cdbSynset.getPos() );
                                pwnSynset.setC_sy_id(newId);
                                pwnSynset.setSynonyms(status, cdbSynset.getSynonyms());
                                openDWordnetMap.put(newId, pwnSynset);
                                addRelationsToRelationMap(pwnTargetSynsetId, cdbSynset, relationMap);
                                addToIdMaps(cdbSynset.getC_sy_id(), pwnTargetSynsetId);
                            }
                            else {
                                //// there is another relation, but we cannot create a direct relation through these special types
                                //// EQ_HAS_HYPONYM
                                //// EQ_OTHER
                            }
                        }
                        else {
                            //// the target is empty and we cannot create a synset
                        }
                    } /// end for getErelations
                }
                if (!WEFOUNDAPWNMATCH) {
                    /// we need to find the first parent with a translation to PWN
                    String pwntargetId = getParentTarget(key, cdbSynsetParser.synsetIdSynsetMap);
                    if (pwntargetId.isEmpty())  {
                        /// there is no hyperonym target PWN synset not even through the parent relations in DWN
                        missedSynsets.add(key);
                    }
                    else {
                        /// we create a synset with original id but link it as a hyponym to the first parent equivalent
                        status = cornettoversion+"_"+"eq-parent-match";
                        pwntargetId = "eng-"+pwntargetId.substring(3);
                        CdbSynset pwnSynset = new CdbSynset();
                        pwnSynset.setPosSpecific(cdbSynset.getPos());
                        CdbInternalRelation rel = new CdbInternalRelation();
                        rel.setTarget(pwntargetId);
                        rel.setRelation_name(HYPERONYM_STRING);
                        pwnSynset.addIRelation(rel);
                        pwnSynset.setComment("eq-parent-match");
                        String newId = getRandomIdentifier(cdbSynset.getC_sy_id(), cdbSynset.getPos());
                        pwnSynset.setC_sy_id(newId);
                        pwnSynset.setSynonyms(status,cdbSynset.getSynonyms());
                        openDWordnetMap.put(newId, pwnSynset);
                        addRelationsToRelationMap(newId, cdbSynset, relationMap);

                    }
                }
            }
            // @TODO find a fix for synsets that got no match, not even through a parent link going up
            for (int i = 0; i < missedSynsets.size(); i++) {
                String key = missedSynsets.get(i);
                CdbSynset cdbSynset = cdbSynsetParser.synsetIdSynsetMap.get(key);
                ArrayList<CdbInternalRelation> newRelations = new ArrayList<CdbInternalRelation>();
                for (int j = 0; j < cdbSynset.getIRelations().size(); j++) {
                    CdbInternalRelation cdbInternalRelation = (CdbInternalRelation) cdbSynset.getIRelations().get(j);
                    String target = cdbInternalRelation.getTarget();
                    if (idMapOldNew.containsKey(target)) {
                        ArrayList<String> ids = idMapOldNew.get(target);
                        for (int k = 0; k < ids.size(); k++) {
                            String newTargetSynset = ids.get(k);
                            CdbInternalRelation newRelation = new CdbInternalRelation();
                            newRelation.setTarget(newTargetSynset);
                            newRelation.setRelation_name(cdbInternalRelation.getRelation_name());
                            newRelation.setAuthor(cdbInternalRelation.getAuthor());
                            newRelation.setCoordinative(cdbInternalRelation.getCoordinative());
                            newRelation.setDisjunctive(cdbInternalRelation.getDisjunctive());
                            newRelation.setFactive(cdbInternalRelation.getFactive());
                            newRelation.setNegative(cdbInternalRelation.getNegative());
                            newRelation.setReversed(cdbInternalRelation.getReversed());
                            newRelations.add(newRelation);
                        }
                    }
                    else {
                        newRelations.add(cdbInternalRelation);
                        //// recursive problem
                    }
                }
                cdbSynset.setIRelations(newRelations);
                fosNoPwn.write(cdbSynset.toStringPwn().getBytes());
            }



            /// next we add all pwn synsets that are targets and that are not yet in the OpenDWN structure
            keySet = openDWordnetMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset synset = openDWordnetMap.get(key);
                ArrayList<CdbInternalRelation> newRelations = new ArrayList<CdbInternalRelation>();
                for (int i = 0; i < synset.getIRelations().size(); i++) {
                    CdbInternalRelation cdbInternalRelation = (CdbInternalRelation) synset.getIRelations().get(i);
                    String target = cdbInternalRelation.getTarget();
                    if (idMapOldNew.containsKey(target)) {
                        ArrayList<String> ids = idMapOldNew.get(target);
                        for (int k = 0; k < ids.size(); k++) {
                            String newTargetSynset = ids.get(k);
                            CdbInternalRelation newRelation = new CdbInternalRelation();
                            newRelation.setTarget(newTargetSynset);
                            newRelation.setRelation_name(cdbInternalRelation.getRelation_name());
                            newRelation.setAuthor(cdbInternalRelation.getAuthor());
                            newRelation.setCoordinative(cdbInternalRelation.getCoordinative());
                            newRelation.setDisjunctive(cdbInternalRelation.getDisjunctive());
                            newRelation.setFactive(cdbInternalRelation.getFactive());
                            newRelation.setNegative(cdbInternalRelation.getNegative());
                            newRelation.setReversed(cdbInternalRelation.getReversed());
                            newRelations.add(newRelation);
                        }
                    }
                    else {
                        newRelations.add(cdbInternalRelation);
                        if (!coveredSynsets.contains(target)) {
                            //// this means that the target is not in our data set and the tree is broken
                            addTargetSynsetAsDummy(addWordnetMap, wordnetLmfSaxParser, coveredSynsets, target);
                        }
                    }
                }
                synset.setIRelations(newRelations);

            }
            System.out.println("nPreEmptions = " + nPreEmptions);


            /// REVISE THE RELATION MAP TO POINT TO THE NEW IDS
            keySet = relationMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                ArrayList<CdbInternalRelation> rels = relationMap.get(key);
                ArrayList<CdbInternalRelation> odwnrels = new ArrayList<CdbInternalRelation>();
                for (int i = 0; i < rels.size(); i++) {
                    CdbInternalRelation cdbInternalRelation = rels.get(i);
                    String target = cdbInternalRelation.getTarget();
                    if (idMapOldNew.containsKey(target)) {
                        ArrayList<String> cids = idMapOldNew.get(target);
                        for (int j = 0; j < cids.size(); j++) {
                            String cid = cids.get(j);
                            CdbInternalRelation odwnRel = new CdbInternalRelation(cdbInternalRelation);
                            odwnRel.setTarget(cid);
                            odwnrels.add(odwnRel);
                        }
                    }
                }
                if (odwnrels.size()>0) {
                    relationMap.put(key, odwnrels);
                }
            }


            /// Outputting ODWN
            keySet = openDWordnetMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset cdbSynset = openDWordnetMap.get(key);
                if (relationMap.containsKey(cdbSynset.getC_sy_id())) {
                    ArrayList<CdbInternalRelation> newRels = relationMap.get(cdbSynset.getC_sy_id());
                    for (int i = 0; i < newRels.size(); i++) {
                        CdbInternalRelation cdbInternalRelation = newRels.get(i);
                        cdbSynset.addIRelation(cdbInternalRelation);
                    }
                }
                fosOdwn.write(cdbSynset.toStringPwn().getBytes());
            }
            keySet = addWordnetMap.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                CdbSynset cdbSynset = addWordnetMap.get(key);
                fosOdwn.write(cdbSynset.toStringPwn().getBytes());
            }
            str = "</cdbsyn>\n";
            fosOdwn.write(str.getBytes());
            fosOdwn.flush();
            fosNoPwn.write(str.getBytes());
            fosNoPwn.close();
            fosOdwn.close();

            //// Outputting Ids
            keySet = idMapNewOld.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                str = key;
                ArrayList<String> cids = idMapNewOld.get(key);
                for (int i = 0; i < cids.size(); i++) {
                    String cid =  cids.get(i);
                    str += "\t"+cid;
                }
                str += "\n";
                fosMapNewOld.write(str.getBytes());
            }
            fosMapNewOld.close();
            keySet = idMapOldNew.keySet();
            keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                str = key;
                ArrayList<String> cids = idMapOldNew.get(key);
                for (int i = 0; i < cids.size(); i++) {
                    String cid =  cids.get(i);
                    str += "\t"+cid;
                }
                str += "\n";
                fosMapOldNew.write(str.getBytes());
            }
            fosMapOldNew.close();
            fosRelations.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    static void addToIdMaps (String oldId, String newId) {
        if (idMapOldNew.containsKey(oldId)) {
            ArrayList<String> ids = idMapOldNew.get(oldId);
            ids.add(newId);
            idMapOldNew.put(oldId, ids);
        }
        else {
            ArrayList<String> ids = new ArrayList<String>();
            ids.add(newId);
            idMapOldNew.put(oldId, ids);
        }
        if (idMapNewOld.containsKey(newId)) {
            ArrayList<String> ids = idMapNewOld.get(newId);
            ids.add(oldId);
            idMapNewOld.put(newId, ids);
        }
        else {
            ArrayList<String> ids = new ArrayList<String>();
            ids.add(oldId);
            idMapNewOld.put(newId, ids);
        }
    }

    static String getRandomIdentifier (String cId, String posSpecific) {
        Integer id = new Integer(0);
        String idString = "";
        String pos = "-n";
        if (posSpecific.toLowerCase().startsWith("noun")) {
            pos = "-n";
        }
        else if (posSpecific.toLowerCase().startsWith("verb")) {
            pos = "-v";
        }
        else if (posSpecific.toLowerCase().startsWith("adj")) {
            pos = "-a";
        }
        else if (posSpecific.toLowerCase().startsWith("adverb")) {
            pos = "-r";
        }

        id = new Integer((int) (100000000+10000000*Math.random()));
        idString = "odwn-10-"+id.toString()+pos;
        while (idMapNewOld.containsKey(idString)) {
            id = new Integer((int) (100000000+10000000*Math.random()));
            idString = "odwn-10-"+id.toString()+pos;
        }
        addToIdMaps(cId, idString);
        return idString;
    }


    /**
     * This recursive function creates a dummy synset with English LUs to keep the PWN hierarchy as much as possible in tact
     * @param fos
     * @param wordnetLmfSaxParser
     * @param coveredSynsets
     * @param target
     * @throws IOException
     */
    static public void addTargetSynsetAsDummy (FileOutputStream fos,
                                               WordnetLmfSaxParser wordnetLmfSaxParser,
                                               ArrayList<String> coveredSynsets,
                                               String target) throws IOException {

        if (wordnetLmfSaxParser.wordnetData.containsKey(target)) {
            SynsetData synsetData = wordnetLmfSaxParser.wordnetData.get(target);
            CdbSynset pwnSynset = new CdbSynset();
            pwnSynset.setComment("dummy");
            pwnSynset.setC_sy_id(target);
            pwnSynset.setDefinition(synsetData.getGloss());
            if (wordnetLmfSaxParser.synsetMap.containsKey(target)) {
                ArrayList<String> synonyms = wordnetLmfSaxParser.synsetMap.get(target);
                for (int j = 0; j < synonyms.size(); j++) {
                    String s = synonyms.get(j);
                    Syn syn = new Syn();
                    syn.setC_lu_id("eng_"+s);
                    int idx = s.lastIndexOf("_");
                    if (idx>-1) {
                        s = "eng_"+s.substring(0, idx)+":"+s.substring(idx+1);
                    }
                    syn.setPreviewtext(s);
                    pwnSynset.addSynonym(syn);
                }
            }
            else {
                //  System.out.println("No synonyms for target = " + target);
            }
            for (int k = 0; k < synsetData.getRelations().size(); k++) {
                WordnetRelation wordnetRelation = synsetData.getRelations().get(k);
                CdbInternalRelation rel = new CdbInternalRelation();
                rel.setTarget(wordnetRelation.getTarget());
                if (wordnetRelation.getRelation().equalsIgnoreCase("hypernym") || (wordnetRelation.getRelation().equalsIgnoreCase("hyperonym"))) {
                    rel.setRelation_name(HYPERONYM_STRING);
                }
                else {
                    rel.setRelation_name(wordnetRelation.getRelation());
                }
                pwnSynset.addIRelation(rel);
            }
            fos.write(pwnSynset.toStringPwn().getBytes());
            coveredSynsets.add(target);
            for (int k = 0; k < synsetData.getRelations().size(); k++) {
                WordnetRelation wordnetRelation = synsetData.getRelations().get(k);
                if (!coveredSynsets.contains(wordnetRelation.getTarget())) {
                    addTargetSynsetAsDummy(fos, wordnetLmfSaxParser, coveredSynsets, wordnetRelation.getTarget());
                }
                else {
                    /// the target is already covered, either as another dummy or as an open-dwn synset that was inserted from a dwn synset
                }
            }
        }
    }
    static public void addTargetSynsetAsDummy (HashMap<String, CdbSynset> odwnMap,
                                               WordnetLmfSaxParser wordnetLmfSaxParser,
                                               ArrayList<String> coveredSynsets,
                                               String target) {

        if (wordnetLmfSaxParser.wordnetData.containsKey(target)) {
            SynsetData synsetData = wordnetLmfSaxParser.wordnetData.get(target);
            CdbSynset pwnSynset = new CdbSynset();
            pwnSynset.setComment("dummy");
            pwnSynset.setC_sy_id(target);
            pwnSynset.setDefinition(synsetData.getGloss());
            if (wordnetLmfSaxParser.synsetMap.containsKey(target)) {
                ArrayList<String> synonyms = wordnetLmfSaxParser.synsetMap.get(target);
                for (int j = 0; j < synonyms.size(); j++) {
                    String s = synonyms.get(j);
                    Syn syn = new Syn();
                    syn.setC_lu_id("eng_"+s);
                    int idx = s.lastIndexOf("_");
                    if (idx>-1) {
                        s = "eng_"+s.substring(0, idx)+":"+s.substring(idx+1);
                    }
                    syn.setPreviewtext(s);
                    pwnSynset.addSynonym(syn);
                }
            }
            else {
                //  System.out.println("No synonyms for target = " + target);
            }
            for (int k = 0; k < synsetData.getRelations().size(); k++) {
                WordnetRelation wordnetRelation = synsetData.getRelations().get(k);
                CdbInternalRelation rel = new CdbInternalRelation();
                rel.setTarget(wordnetRelation.getTarget());
                if (wordnetRelation.getRelation().equalsIgnoreCase("hypernym") || (wordnetRelation.getRelation().equalsIgnoreCase("hyperonym"))) {
                    rel.setRelation_name(HYPERONYM_STRING);
                }
                else {
                    rel.setRelation_name(wordnetRelation.getRelation());
                }
                pwnSynset.addIRelation(rel);
            }
            odwnMap.put(target, pwnSynset);
            coveredSynsets.add(target);
            for (int k = 0; k < synsetData.getRelations().size(); k++) {
                WordnetRelation wordnetRelation = synsetData.getRelations().get(k);
                if (!coveredSynsets.contains(wordnetRelation.getTarget())) {
                    addTargetSynsetAsDummy(odwnMap, wordnetLmfSaxParser, coveredSynsets, wordnetRelation.getTarget());
                }
                else {
                    /// the target is already covered, either as another dummy or as an open-dwn synset that was inserted from a dwn synset
                }
            }
        }
    }

}
