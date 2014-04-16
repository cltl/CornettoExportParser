package vu.cornetto.stats;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 28-aug-2008
 * Time: 8:30:36
 * To change this template use File | Settings | File Templates.
 */
public class LuElements {

    static public long arg = 0;
    static public long args = 0;
    static public long canonicalform = 0;
    static public long caseframe = 0;
    static public long caserole = 0;
    static public long category = 0;
    static public long cdb_lu = 0;
    static public long cdb_lus = 0;
    static public long example = 0;
    static public long examples = 0;
    static public long flex_conjugation = 0;
    static public long flex_conjugationtype = 0;
    static public long flex_mode = 0;
    static public long flex_number = 0;
    static public long flex_pastpart = 0;
    static public long flex_pasttense = 0;
    static public long flex_person = 0;
    static public long flex_tense = 0;
    static public long form = 0;
    static public long form_example = 0;
    static public long mor_base = 0;
    static public long mor_comparative = 0;
    static public long mor_comparis = 0;
    static public long mor_comparison = 0;
    static public long mor_declinability = 0;
    static public long mor_flectional_type = 0;
    static public long mor_superlative = 0;
    static public long morpho_plurform = 0;
    static public long morpho_plurforms = 0;
    static public long morpho_structure = 0;
    static public long morpho_type = 0;
    static public long morphology_adj = 0;
    static public long morphology_noun = 0;
    static public long morphology_verb = 0;
    static public long prag_chronology = 0;
    static public long prag_connotation = 0;
    static public long prag_domain = 0;
    static public long prag_frequency = 0;
    static public long prag_geography = 0;
    static public long prag_origin = 0;
    static public long prag_socGroup = 0;
    static public long prag_style = 0;
    static public long prag_subj_gen = 0;
    static public long pragmatics = 0;
    static public long selrestriction = 0;
    static public long selrestrole = 0;
    static public long sem_caseframe = 0;
    static public long sem_countability = 0;
    static public long sem_def = 0;
    static public long sem_def_noun = 0;
    static public long sem_defSource = 0;
    static public long sem_definition = 0;
    static public long sem_gc_compl = 0;
    static public long sem_gc_gramword = 0;
    static public long sem_genus = 0;
    static public long sem_hypernym = 0;
    static public long sem_hypernyms = 0;
    static public long sem_lc_collocator = 0;
    static public long sem_meaningdescription = 0;
    static public long sem_reference = 0;
    static public long sem_resume = 0;
    static public long sem_selrestriction = 0;
    static public long sem_selrestrictions = 0;
    static public long sem_shift = 0;
    static public long sem_spec_collocator = 0;
    static public long sem_specificae = 0;
    static public long sem_subclass = 0;
    static public long sem_subtype_argument = 0;
    static public long sem_synonym = 0;
    static public long sem_synonyms = 0;
    static public long sem_type = 0;
    static public long semantics_adj = 0;
    static public long semantics_example = 0;
    static public long semantics_noun = 0;
    static public long semantics_verb = 0;
    static public long sy_advusage = 0;
    static public long sy_article = 0;
    static public long sy_class = 0;
    static public long sy_combi = 0;
    static public long sy_combicat = 0;
    static public long sy_combipair = 0;
    static public long sy_combiword = 0;
    static public long sy_comp = 0;
    static public long sy_compl_text = 0;
    static public long sy_complementation = 0;
    static public long sy_gender = 0;
    static public long sy_number = 0;
    static public long sy_peraux = 0;
    static public long sy_position = 0;
    static public long sy_reflexiv = 0;
    static public long sy_separ = 0;
    static public long sy_subject = 0;
    static public long sy_subtype = 0;
    static public long sy_trans = 0;
    static public long sy_type = 0;
    static public long sy_valency = 0;
    static public long synset_list = 0;
    static public long syntax_adj = 0;
    static public long syntax_example = 0;
    static public long syntax_noun = 0;
    static public long syntax_verb = 0;
    static public long text_category = 0;
    static public long textualform = 0;

    static public void update (String qName) {
        if (qName.equals("arg")) {arg++;}
        if (qName.equals("args")) {args++;}
        if (qName.equals("canonicalform")) {canonicalform++;}
        if (qName.equals("caseframe")) {caseframe++;}
        if (qName.equals("caserole")) {caserole++;}
        if (qName.equals("category")) {category++;}
        if (qName.equals("cdb_lu")) {cdb_lu++;}
        if (qName.equals("cdb_lus")) {cdb_lus++;}
        if (qName.equals("example")) {example++;}
        if (qName.equals("examples")) {examples++;}
        if (qName.equals("flex-conjugation")) {flex_conjugation++;}
        if (qName.equals("flex-conjugationtype")) {flex_conjugationtype++;}
        if (qName.equals("flex-mode")) {flex_mode++;}
        if (qName.equals("flex-number")) {flex_number++;}
        if (qName.equals("flex-pastpart")) {flex_pastpart++;}
        if (qName.equals("flex-pasttense")) {flex_pasttense++;}
        if (qName.equals("flex-person")) {flex_person++;}
        if (qName.equals("flex-tense")) {flex_tense++;}
        if (qName.equals("form")) {form++;}
        if (qName.equals("form_example")) {form_example++;}
        if (qName.equals("mor-base")) {mor_base++;}
        if (qName.equals("mor-comparative")) {mor_comparative++;}
        if (qName.equals("mor-comparis")) {mor_comparis++;}
        if (qName.equals("mor-comparison")) {mor_comparison++;}
        if (qName.equals("mor-declinability")) {mor_declinability++;}
        if (qName.equals("mor-flectional-type")) {mor_flectional_type++;}
        if (qName.equals("mor-superlative")) {mor_superlative++;}
        if (qName.equals("morpho-plurform")) {morpho_plurform++;}
        if (qName.equals("morpho-plurforms")) {morpho_plurforms++;}
        if (qName.equals("morpho-structure")) {morpho_structure++;}
        if (qName.equals("morpho-type")) {morpho_type++;}
        if (qName.equals("morphology_adj")) {morphology_adj++;}
        if (qName.equals("morphology_noun")) {morphology_noun++;}
        if (qName.equals("morphology_verb")) {morphology_verb++;}
        if (qName.equals("prag-chronology")) {prag_chronology++;}
        if (qName.equals("prag-connotation")) {prag_connotation++;}
        if (qName.equals("prag-domain")) {prag_domain++;}
        if (qName.equals("prag-frequency")) {prag_frequency++;}
        if (qName.equals("prag-geography")) {prag_geography++;}
        if (qName.equals("prag-origin")) {prag_origin++;}
        if (qName.equals("prag-socGroup")) {prag_socGroup++;}
        if (qName.equals("prag-style")) {prag_style++;}
        if (qName.equals("prag-subj-gen")) {prag_subj_gen++;}
        if (qName.equals("pragmatics")) {pragmatics++;}
        if (qName.equals("selrestriction")) {selrestriction++;}
        if (qName.equals("selrestrole")) {selrestrole++;}
        if (qName.equals("sem-caseframe")) {sem_caseframe++;}
        if (qName.equals("sem-countability")) {sem_countability++;}
        if (qName.equals("sem-def")) {sem_def++;}
        if (qName.equals("sem-def-noun")) {sem_def_noun++;}
        if (qName.equals("sem-defSource")) {sem_defSource++;}
        if (qName.equals("sem-definition")) {sem_definition++;}
        if (qName.equals("sem-gc-compl")) {sem_gc_compl++;}
        if (qName.equals("sem-gc-gramword")) {sem_gc_gramword++;}
        if (qName.equals("sem-genus")) {sem_genus++;}
        if (qName.equals("sem-hypernym")) {sem_hypernym++;}
        if (qName.equals("sem-hypernyms")) {sem_hypernyms++;}
        if (qName.equals("sem-lc-collocator")) {sem_lc_collocator++;}
        if (qName.equals("sem-meaningdescription")) {sem_meaningdescription++;}
        if (qName.equals("sem-reference")) {sem_reference++;}
        if (qName.equals("sem-resume")) {sem_resume++;}
        if (qName.equals("sem-selrestriction")) {sem_selrestriction++;}
        if (qName.equals("sem-selrestrictions")) {sem_selrestrictions++;}
        if (qName.equals("sem-shift")) {sem_shift++;}
        if (qName.equals("sem-spec-collocator")) {sem_spec_collocator++;}
        if (qName.equals("sem-specificae")) {sem_specificae++;}
        if (qName.equals("sem-subclass")) {sem_subclass++;}
        if (qName.equals("sem-subtype-argument")) {sem_subtype_argument++;}
        if (qName.equals("sem-synonym")) {sem_synonym++;}
        if (qName.equals("sem-synonyms")) {sem_synonyms++;}
        if (qName.equals("sem-type")) {sem_type++;}
        if (qName.equals("semantics_adj")) {semantics_adj++;}
        if (qName.equals("semantics_example")) {semantics_example++;}
        if (qName.equals("semantics_noun")) {semantics_noun++;}
        if (qName.equals("semantics_verb")) {semantics_verb++;}
        if (qName.equals("sy-advusage")) {sy_advusage++;}
        if (qName.equals("sy-article")) {sy_article++;}
        if (qName.equals("sy-class")) {sy_class++;}
        if (qName.equals("sy-combi")) {sy_combi++;}
        if (qName.equals("sy-combicat")) {sy_combicat++;}
        if (qName.equals("sy-combipair")) {sy_combipair++;}
        if (qName.equals("sy-combiword")) {sy_combiword++;}
        if (qName.equals("sy-comp")) {sy_comp++;}
        if (qName.equals("sy-compl-text")) {sy_compl_text++;}
        if (qName.equals("sy-complementation")) {sy_complementation++;}
        if (qName.equals("sy-gender")) {sy_gender++;}
        if (qName.equals("sy-number")) {sy_number++;}
        if (qName.equals("sy-peraux")) {sy_peraux++;}
        if (qName.equals("sy-position")) {sy_position++;}
        if (qName.equals("sy-reflexiv")) {sy_reflexiv++;}
        if (qName.equals("sy-separ")) {sy_separ++;}
        if (qName.equals("sy-subject")) {sy_subject++;}
        if (qName.equals("sy-subtype")) {sy_subtype++;}
        if (qName.equals("sy-trans")) {sy_trans++;}
        if (qName.equals("sy-type")) {sy_type++;}
        if (qName.equals("sy-valency")) {sy_valency++;}
        if (qName.equals("synset_list")) {synset_list++;}
        if (qName.equals("syntax_adj")) {syntax_adj++;}
        if (qName.equals("syntax_example")) {syntax_example++;}
        if (qName.equals("syntax_noun")) {syntax_noun++;}
        if (qName.equals("syntax_verb")) {syntax_verb++;}
        if (qName.equals("text-category")) {text_category++;}
        if (qName.equals("textualform")) {textualform++;}
    }

    static public void printStats () {
        System.out.println("GENERAL");
        System.out.println("cdb_lu\t" + cdb_lu);
        System.out.println("cdb_lus\t" + cdb_lus);
        System.out.println("form\t" + form);

        System.out.println("\nSEM");
        System.out.println("arg\t" + arg);
        System.out.println("args\t" + args);
        System.out.println("caseframe\t" + caseframe);
        System.out.println("caserole\t" + caserole);
        System.out.println("selrestriction\t" + selrestriction);
        System.out.println("selrestrole\t" + selrestrole);
        System.out.println("sem_caseframe\t" + sem_caseframe);
        System.out.println("sem_countability\t" + sem_countability);
        System.out.println("sem_def\t" + sem_def);
        System.out.println("sem_defSource\t" + sem_defSource);
        System.out.println("sem_def_noun\t" + sem_def_noun);
        System.out.println("sem_definition\t" + sem_definition);
        System.out.println("sem_genus\t" + sem_genus);
        System.out.println("sem_hypernym\t" + sem_hypernym);
        System.out.println("sem_hypernyms\t" + sem_hypernyms);
        System.out.println("sem_reference\t" + sem_reference);
        System.out.println("sem_resume\t" + sem_resume);
        System.out.println("sem_selrestriction\t" + sem_selrestriction);
        System.out.println("sem_selrestrictions\t" + sem_selrestrictions);
        System.out.println("sem_shift\t" + sem_shift);
        System.out.println("sem_spec_collocator\t" + sem_spec_collocator);
        System.out.println("sem_specificae\t" + sem_specificae);
        System.out.println("sem_subclass\t" + sem_subclass);
        System.out.println("sem_synonym\t" + sem_synonym);
        System.out.println("sem_synonyms\t" + sem_synonyms);
        System.out.println("sem_type\t" + sem_type);
        System.out.println("semantics_adj\t" + semantics_adj);

        System.out.println("\nEXAMPLES");
        System.out.println("example\t" + example);
        System.out.println("examples\t" + examples);
        System.out.println("category\t" + category);
        System.out.println("form_example\t" + form_example);
        System.out.println("canonicalform\t" + canonicalform);
        System.out.println("text_category\t" + text_category);
        System.out.println("textualform\t" + textualform);
        System.out.println("semantics_example\t" + semantics_example);
        System.out.println("semantics_noun\t" + semantics_noun);
        System.out.println("semantics_verb\t" + semantics_verb);
        System.out.println("sem_gc_compl\t" + sem_gc_compl);
        System.out.println("sem_gc_gramword\t" + sem_gc_gramword);
        System.out.println("sem_lc_collocator\t" + sem_lc_collocator);
        System.out.println("sem_meaningdescription\t" + sem_meaningdescription);
        System.out.println("sem_subtype_argument\t" + sem_subtype_argument);
        System.out.println("synset_list\t" + synset_list);
        System.out.println("sy_combi\t" + sy_combi);
        System.out.println("sy_combicat\t" + sy_combicat);
        System.out.println("sy_combipair\t" + sy_combipair);
        System.out.println("sy_combiword\t" + sy_combiword);
        System.out.println("sy_subtype\t" + sy_subtype);
        System.out.println("sy_type\t" + sy_type);
        System.out.println("syntax_example\t" + syntax_example);

        System.out.println("\nMORPH");
        System.out.println("flex_conjugation\t" + flex_conjugation);
        System.out.println("flex_conjugationtype\t" + flex_conjugationtype);
        System.out.println("flex_mode\t" + flex_mode);
        System.out.println("flex_number\t" + flex_number);
        System.out.println("flex_pastpart\t" + flex_pastpart);
        System.out.println("flex_pasttense\t" + flex_pasttense);
        System.out.println("flex_person\t" + flex_person);
        System.out.println("flex_tense\t" + flex_tense);
        System.out.println("morphology_adj\t" + morphology_adj);
        System.out.println("morphology_noun\t" + morphology_noun);
        System.out.println("morphology_verb\t" + morphology_verb);
        System.out.println("mor_base\t" + mor_base);
        System.out.println("mor_comparative\t" + mor_comparative);
        System.out.println("mor_comparis\t" + mor_comparis);
        System.out.println("mor_comparison\t" + mor_comparison);
        System.out.println("mor_declinability\t" + mor_declinability);
        System.out.println("mor_flectional_type\t" + mor_flectional_type);
        System.out.println("mor_superlative\t" + mor_superlative);
        System.out.println("morpho_plurform\t" + morpho_plurform);
        System.out.println("morpho_plurforms\t" + morpho_plurforms);
        System.out.println("morpho_structure\t" + morpho_structure);
        System.out.println("morpho_type\t" + morpho_type);

        System.out.println("\nPRAG");
        System.out.println("pragmatics\t" + pragmatics);
        System.out.println("prag_chronology\t" + prag_chronology);
        System.out.println("prag_connotation\t" + prag_connotation);
        System.out.println("prag_domain\t" + prag_domain);
        System.out.println("prag_frequency\t" + prag_frequency);
        System.out.println("prag_geography\t" + prag_geography);
        System.out.println("prag_origin\t" + prag_origin);
        System.out.println("prag_socGroup\t" + prag_socGroup);
        System.out.println("prag_style\t" + prag_style);
        System.out.println("prag_subj_gen\t" + prag_subj_gen);

        System.out.println("\nSYNTAX");
        System.out.println("syntax_noun\t" + syntax_noun);
        System.out.println("syntax_verb\t" + syntax_verb);
        System.out.println("syntax_adj\t" + syntax_adj);
        System.out.println("sy_advusage\t" + sy_advusage);
        System.out.println("sy_article\t" + sy_article);
        System.out.println("sy_class\t" + sy_class);
        System.out.println("sy_comp\t" + sy_comp);
        System.out.println("sy_compl_text\t" + sy_compl_text);
        System.out.println("sy_complementation\t" + sy_complementation);
        System.out.println("sy_gender\t" + sy_gender);
        System.out.println("sy_number\t" + sy_number);
        System.out.println("sy_peraux\t" + sy_peraux);
        System.out.println("sy_position\t" + sy_position);
        System.out.println("sy_reflexiv\t" + sy_reflexiv);
        System.out.println("sy_separ\t" + sy_separ);
        System.out.println("sy_subject\t" + sy_subject);
        System.out.println("sy_trans\t" + sy_trans);
        System.out.println("sy_valency\t" + sy_valency);
    }
}
