package vu.cornetto.cdb;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 21-apr-2006
 * Time: 19:39:06
 * To change this template use Options | File Templates.
 */
public class LexicalUnit {

    public Form aForm;
    public Mor aMor;
    public Ont aOnt;
    public Sem aSem;
    public Prag aPrag;
    public Syn aSyn;
    public Ili aIli;


    public LexicalUnit () {
        aForm = new Form("", "", "");
        aMor = new Mor();
        aOnt = new Ont();
        aSyn = new Syn();
        aSem = new Sem();
        aPrag = new Prag();
        aIli = new Ili();
    }

    public String toString () {
        String str = "";
        str = "<lexicalEntry>\n";
        str = "\t"+"<form"+aForm.toString()+">\n";
        str = "\t"+"<morphology\\>"+"\n";
        str = "\t"+"<syntax\\>"+"\n";
        str = "\t"+"<ont\\>"+"\n";
        str = "\t"+"<semantics\\>"+"\n";
        str = "\t"+"<ili\\>"+"\n";
        str += "</lexicalEntry>\n";
        return str;
    }
}
