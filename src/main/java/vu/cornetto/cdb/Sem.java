package vu.cornetto.cdb;

import vu.cornetto.dwn.InternalRelation;
import vu.cornetto.dwn.Synonym;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: piek
 * Date: 21-apr-2006
 * Time: 19:40:21
 * To change this template use Options | File Templates.
 */
public class Sem {

    public ArrayList dwnRels;
    public ArrayList dwnSynset;
    public ArrayList rbnRels;
    public ArrayList domRelations;

    public Sem() {
        dwnRels = new ArrayList();
        dwnSynset = new ArrayList();
        rbnRels = new ArrayList();
        domRelations = new ArrayList();
    }

    public String toString () {
        String str = "";
        for (int i=0;i<dwnRels.size();i++) {
            InternalRelation irl = (InternalRelation) dwnRels.get(i);
            str= "";
        }
        for (int i=0;i<dwnSynset.size();i++) {
            Synonym syn = (Synonym) dwnSynset.get(i);
        }
        for (int i=0;i<rbnRels.size();i++) {

        }
        for (int i=0;i<domRelations.size();i++) {

        }
        return str;
    }
}
