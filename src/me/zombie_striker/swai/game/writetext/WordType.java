package me.zombie_striker.swai.game.writetext;

public enum WordType {

    UNKNOWN (-1),
    GRAMMAR(0),
    PRONOUNS(1),
    NOUNS(2),
    ADJECTIVES(3),
    ADVERBS(4),
    VERBS(5),
    DETERMINER(6),
    CONJUNCTION(7),
    PREPOSITION(8),
    INTERJECTION(9),
    NAME(10),
    NUMERAL(11);

    private int id;

    WordType(int i ){
        this.id = i;
    }
    public int getId(){
        return id;
    }
}
