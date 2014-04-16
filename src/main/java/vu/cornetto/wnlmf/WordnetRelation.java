package vu.cornetto.wnlmf;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 9/3/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class WordnetRelation {

    private String relation;
    private String target;

    public WordnetRelation(String relation, String target) {
        this.relation = relation;
        this.target = target;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
