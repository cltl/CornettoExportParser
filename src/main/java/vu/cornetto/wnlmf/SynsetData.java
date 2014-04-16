package vu.cornetto.wnlmf;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kyoto
 * Date: 9/3/12
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SynsetData {

    private String gloss;
    private ArrayList<String> usages;
    private ArrayList<WordnetRelation> relations = new ArrayList<WordnetRelation>();
    private ArrayList<String> synonyms;


    public SynsetData() {
        init();
    }

    public void init () {
        usages = new ArrayList<String>();
        gloss = "";
        relations = new ArrayList<WordnetRelation>();
        synonyms = new ArrayList<String>();
    }

    public ArrayList<WordnetRelation> getRelations() {
        return relations;
    }

    public void setHyperRelations(ArrayList<WordnetRelation> relations) {
        this.relations = relations;
    }

    public void addRelation(WordnetRelation targetRelation) {
        this.relations.add(targetRelation);
    }

    public String getGloss() {
        return gloss;
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }

    public void addSynonyms(String synonym) {
        this.synonyms.add(synonym);
    }

    public ArrayList<String> getUsages() {
        return usages;
    }

    public void setUsages(ArrayList<String> usages) {
        this.usages = usages;
    }
}
