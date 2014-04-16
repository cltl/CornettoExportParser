CornettoExportParser
====================

Utilities to process the DebVisDic export of the Cornetto database


java -Xmx1824m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar"  vu.cornetto.conversions.StandardizedSynsetIdentifiers /Users/kyoto/Desktop/CDB/2013-MAY-18/cdblu-latest.xml /Users/kyoto/Desktop/CDB/2013-MAY-18/cdbsyn-latest.xml nld-21-

# CREATES SENSE GROUPS FROM THE CORNETTO DUMP
# it applies 3 different strategies:
# - word senses that have an explicit semantic relations
# - word senses that have the same hypernym
# = word senses that share at least one more synonym
# the program generates a separate output file for each.
# usage:
# arg1 = cornetto export synset file
# arg2 = cornetto export lu file

java -Xmx1824m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar"  conversions.ExtractSenseGroups "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml"  "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdblu-latest.xml"


# CREATES THE RESOURCES FOR UKB GRAPH AND LEXICON FROM THE CORNETTO DUMP
# arg1 = cornetto export synset file
# arg2 = domain hierarchy file
# arg3 = name prefix for output files
# arg4 (optional) if "eng" the lexicon is extended with equivalence mappings to English wordnet. Used for graph with English relations

#--cdb_syn
#"/Code/vu/kyotoproject/wsd/nl/cdb_syn_v191.xml"
#--domain-mapping
#"./domains/domain_parent"
#--name-key
#dwn191_nld
#--equivalence-mapping [OPT]
#--equivalence-proportion
#70
#--domain-hierarchy [OPT]

java -Xmx1024m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.conversions.SynsetsToWsdDomainGraphImport --cdb_syn "/Users/kyoto/Desktop/CDB/2013-MAY-18/cdbsyn-latest.xml" --domain-mapping "/Code/vu/Cornetto/domains/domain_parent" --name-key cdb2.0-nld --equivalence-mapping --equivalence-proportion 70 --domain-hierarchy

#### DERIVE OPEN DUTCH WORDNET
java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.conversions.DeriveOpenDwn --preempt --cdb-syn "/Users/piek/Desktop/CDB/test/cdb_syn.xml" --wn-lmf "/Tools/wordnet-tools.0.1/resources/wneng-30.lmf.xml"

# creates base concept data from synset file: 
# .stat file contains number of relations per synset
# .chain file contains hypernym relations
# EHU perl scripts can be used to get the BCs
java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" conversions.SynsetsToBcImport "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml"
cd BC
#		hyponymy-file: The hyponymy relations of the wordnet.	
#		output:  The name for the output files.  The program generates two files: output.list and output.rel	
#		frecuency: An integer equal or higher than 0. It sets the minimun number of synsets the BLC must represent.	
#		hypo|all: The relation type that is taken into account to select the BLC, with "all" all relations of the synsets are considerated, with "hypo" only the hyponymy relations.
#		relations-file: The number of relations of each synset. It is needed just in case of selecting "all" option.
# perl get-blc hyponymy-file ouput-file frequency hypo|all [relations-file]

# HYPO
perl get-blc "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.chain" "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.bc-10-hypo-ouput-file" 10 hypo
perl get-blc "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.chain" "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.bc-50-hypo-ouput-file" 50 hypo
perl get-blc "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.chain" "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.bc-100-hypo-ouput-file" 100 hypo
perl get-blc "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.chain" "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.bc-500-hypo-ouput-file" 500 hypo

# REL
perl get-blc "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.chain" "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.bc-10-all-ouput-file" 10 all "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.stat"
perl get-blc "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.chain" "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.bc-50-all-ouput-file" 50 all "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.stat"
perl get-blc "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.chain" "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.bc-100-all-ouput-file" 100 all "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.stat"
perl get-blc "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.chain" "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.bc-500-all-ouput-file" 500 all "/Users/kyoto/Desktop/CDB/2012-JUL-08/cdbsyn-latest.xml.bc.stat"


#java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.stats.StatsForSynsets "/Users/kyoto/Desktop/CDB/2013-NOV-05/cdb_syn.xml"
java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.stats.StatsForSynsetsPerPos "/Users/piek/Desktop/CDB/2014-JAN-29/cdb_syn.xml"
java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.stats.StatsForLexicalUnits "/Users/piek/Desktop/CDB/2014-JAN-29/cdb_lu.xml"


java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.check.CheckNotFoundLUsInSynsets "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_syn.xml"
# checks if "not_found:0" is given as a synonym or target in the synsets, also checks for duplicate LUs in the list of synonyms
java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.check.CheckForMissingTargets "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_syn.xml"
# lists of relations where the target cannot be found/resolved
java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.check.CheckOrphanSynsets "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_syn.xml"
# lists of synsets without hypernym: format = synset-id;synonyms;number of relations

java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.check.CheckSelfReferences "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_syn.xml"
# relations where target ID = synset ID
java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.check.CheckForLUsInMultipleSynsets "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_syn.xml"
# lists of LUs 
java -Xmx2000m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.check.CheckLuSynsetMapppings "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_syn.xml" "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_lu.xml"
# checks if the LUs in the synsets are actually present in the LU file, also checks for "not_found:0" in synonyms and targets and checks for duplicates in the synonyms
# this makes CheckNotFoundLUsInSynsets redundant

java -Xmx812m -cp "../lib/CornettoExportParser-1.0-jar-with-dependencies.jar" vu.cornetto.cdb.CdbSynsetParser --suppress "/Users/piek/Desktop/CDB/2014-MAR-28/cdb_syn.xml"


