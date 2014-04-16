package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 21-apr-2006
 * Time: 19:39:44
 * To change this template use Options | File Templates.
 */
public class Form {
    private String orth;
    private String pron;
    private String infl;

    public Form(String orth, String pron, String inflection) {
        this.orth = orth;
        this.pron = pron;
        this.infl = inflection;
    }

    public String toString () {
        String str = " orth=\""+orth+ "\" pron=\""+pron+"\" inflection=\""+infl+"\"";
        return str;
    }
}
