package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: Piek Vossen
 * Date: 3-jul-2008
 * Time: 10:53:34
 * To change this template use File | Settings | File Templates.
 */
public class CdbLexicalUnit {

    String c_seq_nr;
    String type;
    String c_lu_id;
    String morphologyString;
    String syntaxString;
    String semanticsString;
    String examplesString;
    CdbLuSemanticsNoun semNoun;
    CdbLuSemanticsVerb semVerb;
    CdbLuSemanticsAdjective adjNoun;

}
