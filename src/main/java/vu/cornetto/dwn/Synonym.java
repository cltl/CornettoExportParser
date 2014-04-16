package vu.cornetto.dwn;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 19-apr-2006
 * Time: 18:14:08
 * To change this template use Options | File Templates.
 */
public class Synonym {
    ////<SYNONYM><LITERAL>doorlaten<SENSE>1</SENSE></LITERAL></SYNONYM>
    private String LITERAL;
    private int SENSE;


    public Synonym(String LITERAL, int SENSE) {
        this.LITERAL = LITERAL;
        this.SENSE = SENSE;
    }

    public String getLITERAL() {
        return LITERAL;
    }

    public void setLITERAL(String LITERAL) {
        this.LITERAL = LITERAL;
    }

    public int getSENSE() {
        return SENSE;
    }

    public void setSENSE(int SENSE) {
        this.SENSE = SENSE;
    }

    public String toString() {
        String  str =  "\t<SYNONYM>\n";
                str += "\t\t<LITERAL>"+LITERAL+"</LITERAL>\n";
                str += "\t\t<SENSE>"+SENSE+"</SENSE>\n";
                str += "\t</SYNONYM>\n";
        return str;
    }
}
