package me.zombie_striker.swai.game.writetext;


import javax.naming.Name;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static me.zombie_striker.swai.game.writetext.WordType.*;

public class WordBank {

    private static List<String> nouns = new LinkedList<>();
    private static List<String> verbs = new LinkedList<>();
    private static List<String> adverbs = new LinkedList<>();
    private static List<String> adjectives = new LinkedList<>();
    private static List<String> pronouns = new LinkedList<>();
    private static List<String> determiners = new LinkedList<>();
    private static List<String> conjunctions = new LinkedList<>();
    private static List<String> names = new LinkedList<>();
    private static List<String> preposition = new LinkedList<>();
    private static List<String> interjections = new LinkedList<>();
    private static List<String> grammar = new LinkedList<>();
    private static List<String> numeral = new LinkedList<>();

    public static void registerWord(String word, WordType type) {
        if (type == PRONOUNS) {
            pronouns.add(word);
            return;
        }
        if (type == NAME) {
            names.add(word);
            return;
        }
        if (type == NOUNS) {
            nouns.add(word);
            return;
        }
        if (type == NUMERAL) {
            numeral.add(word);
            return;
        }
        if (type == ADJECTIVES) {
            adjectives.add(word);
            return;
        }
        if (type == ADVERBS) {
            adverbs.add(word);
            return;
        }
        if (type == WordType.GRAMMAR) {
            grammar.add(word);
            return;
        }
        if (type == DETERMINER) {
            determiners.add(word);
            return;
        }
        if (type == VERBS) {
            verbs.add(word);
            return;
        }
        if (type == CONJUNCTION) {
            conjunctions.add(word);
            return;
        }
        if (type == PREPOSITION) {
            preposition.add(word);
            return;
        }
        if (type == INTERJECTION) {
            interjections.add(word);
            return;
        }
    }

    public static void initWords() {
        registerWord("he", PRONOUNS);
        registerWord("she", PRONOUNS);
        registerWord("his", DETERMINER);
        registerWord("her", DETERMINER);
        registerWord("hers", DETERMINER);
        registerWord("they", PRONOUNS);
        registerWord("them", DETERMINER);
        registerWord("their", DETERMINER);
        registerWord("the", DETERMINER);
        registerWord("days", NOUNS);
        registerWord("summer", NOUNS);
        registerWord("happy", ADJECTIVES);
        registerWord("and", DETERMINER);
        registerWord("own", ADJECTIVES);
        registerWord("life", NOUNS);
        registerWord("child", NOUNS);
        registerWord("remembering", VERBS);
        registerWord("joys", NOUNS);
        registerWord("all", DETERMINER);
        registerWord("a", DETERMINER);
        registerWord("in", PREPOSITION);
        registerWord("pleasure", NOUNS);
        registerWord("find", VERBS);
        registerWord("with", PREPOSITION);
        registerWord("long", ADJECTIVES);
        registerWord("ago", ADVERBS);
        registerWord("simple", ADJECTIVES);
        registerWord("of", PREPOSITION);
        registerWord("dream", NOUNS);
        registerWord("wonderland", NOUNS);
        registerWord("how", ADVERBS);
        registerWord("perhaps", ADVERBS);
        registerWord("can", VERBS);
        registerWord("pigeon", NOUNS);
        registerWord("said", ADJECTIVES);
        registerWord("say", VERBS);
        registerWord("see", VERBS);
        registerWord("something", PRONOUNS);
        registerWord("be", VERBS);
        registerWord("free", ADVERBS);
        registerWord("come", PREPOSITION);
        registerWord("must", VERBS);
        registerWord("needs", ADVERBS);
        registerWord("in", PREPOSITION);
        registerWord("to", PREPOSITION);
        registerWord("from", PREPOSITION);
        registerWord("its", PRONOUNS);
        registerWord("it", NOUNS);
        registerWord("even", ADVERBS);
        registerWord("hatter", NOUNS);
        registerWord("alice", NAME);
        registerWord("you", PRONOUNS);
        registerWord("i", PRONOUNS);
        registerWord("about", ADVERBS);
        registerWord("know", VERBS);
        registerWord("as", CONJUNCTION);
        registerWord("do", VERBS);
        registerWord("elizabeth", NAME);
        registerWord("up", PREPOSITION);
        registerWord("wrapping", VERBS);
        registerWord("very", ADVERBS);
        registerWord("itself", PRONOUNS);
        registerWord("began", VERBS);
        registerWord("old", ADJECTIVES);
        registerWord("one", PRONOUNS);
        registerWord("off", PREPOSITION);
        registerWord("at", PREPOSITION);
        registerWord("birds", NOUNS);
        registerWord("bird", NOUNS);
        registerWord("speech", NOUNS);
        registerWord("this", DETERMINER);
        registerWord("why", ADVERBS);
        registerWord("oh", INTERJECTION);
        registerWord("could", VERBS);
        registerWord("couldnt", VERBS);
        registerWord("would", VERBS);
        registerWord("wouldnt", VERBS);
        registerWord("should", VERBS);
        registerWord("shouldnt", VERBS);
        registerWord("mice", NOUNS);
        registerWord("mouse", NOUNS);
        registerWord("cat", NOUNS);
        registerWord("dog", NOUNS);
        registerWord("wolf", NOUNS);
        registerWord("such", DETERMINER);
        registerWord("funny", ADJECTIVES);
        registerWord("shoulder", NOUNS);
        registerWord("tell", VERBS);
        registerWord("tells", VERBS);
        registerWord("does", VERBS);
        registerWord("doesnt", VERBS);
        registerWord("eyes", NOUNS);
        registerWord("eye", NOUNS);
        registerWord("children", NOUNS);
        registerWord("little", ADJECTIVES);
        registerWord("make", VERBS);
        registerWord("many", DETERMINER);
        registerWord("strange", ADJECTIVES);
        registerWord("bright", ADJECTIVES);
        registerWord("feel", VERBS);
        registerWord("feels", VERBS);
        registerWord("sorrow", NOUNS);
        registerWord("sorrows", NOUNS);
        registerWord("herself", PRONOUNS);
        registerWord("time", NOUNS);
        registerWord("sister", NOUNS);
        registerWord("sisters", NOUNS);
        registerWord("brother", NOUNS);
        registerWord("brothers", NOUNS);
        registerWord("after", CONJUNCTION);
        registerWord("year", NOUNS);
        registerWord("years", NOUNS);
        registerWord("heart", NOUNS);
        registerWord("hearts", NOUNS);
        registerWord("love", NOUNS);
        registerWord("loving", NOUNS);
        registerWord("riper", ADJECTIVES);
        registerWord("woman", NOUNS);
        registerWord("women", NOUNS);
        registerWord("man", NOUNS);
        registerWord("men", NOUNS);
        registerWord("so", CONJUNCTION);
        registerWord("had", VERBS);
        registerWord("thought", VERBS);
        registerWord("thoughts", NOUNS);
        registerWord("if", CONJUNCTION);
        registerWord("that", NOUNS);
        registerWord("face", VERBS);
        registerWord("faces", VERBS);
        registerWord("what", ADVERBS);
        registerWord("where", ADVERBS);
        registerWord("when", ADVERBS);
        registerWord("why", ADVERBS);
        registerWord("which", DETERMINER);
        registerWord("besides", PREPOSITION);
        registerWord("having", VERBS);
        registerWord("use", VERBS);
        registerWord("using", VERBS);
        registerWord("used", ADJECTIVES);
        registerWord("ever", ADVERBS);
        registerWord("and", CONJUNCTION);
        registerWord("rule", VERBS);
        registerWord("heard", VERBS);
        registerWord("thinking", VERBS);
        registerWord("think", VERBS);
        registerWord("dreaming", VERBS);
        registerWord("dreamed", VERBS);
        registerWord("fashion", NOUNS);
        registerWord("first", ADJECTIVES);
        registerWord("second", ADJECTIVES);
        registerWord("third", ADJECTIVES);
        registerWord("till", CONJUNCTION);
        registerWord("too", ADVERBS);
        registerWord("once", ADVERBS);
        registerWord("sun", NOUNS);
        registerWord("adventure", NOUNS);
        registerWord("adventures", NOUNS);
        registerWord("trip", NOUNS);
        registerWord("tripped", VERBS);
        registerWord("tripping", VERBS);
        registerWord("hand", NOUNS);
        registerWord("sat", VERBS);
        registerWord("sit", VERBS);
        registerWord("chair", NOUNS);
        registerWord("table", NOUNS);
        registerWord("chairs", NOUNS);
        registerWord("tea", NOUNS);
        registerWord("late", ADVERBS);
        registerWord("getting", VERBS);
        registerWord("get", VERBS);
        registerWord("got", VERBS);
        registerWord("ran", VERBS);
        registerWord("run", VERBS);
        registerWord("running", VERBS);
        registerWord("kissed", VERBS);
        registerWord("read", VERBS);
        registerWord("reading", VERBS);
        registerWord("finished", VERBS);
        registerWord("just", ADJECTIVES);
        registerWord("have", VERBS);
        registerWord("been", VERBS);
        registerWord("told", VERBS);
        registerWord("remember", VERBS);
        registerWord("these", DETERMINER);
        registerWord("those", DETERMINER);
        registerWord("youve", VERBS);
        registerWord("sleep", VERBS);
        registerWord("sleeping", VERBS);
        registerWord("slept", VERBS);
        registerWord("dear", ADJECTIVES);
        registerWord("fluttered", ADJECTIVES);
        registerWord("leaves", ADJECTIVES);
        registerWord("leaf", NOUNS);
        registerWord("left", VERBS);
        registerWord("right", ADJECTIVES);
        registerWord("some", ADVERBS);
        registerWord("away", ADVERBS);
        registerWord("lying", VERBS);
        registerWord("on", ADVERBS);
        registerWord("lap", ADVERBS);
        registerWord("who", PRONOUNS);
        registerWord("was", VERBS);
        registerWord("bank", NOUNS);
        registerWord("head", NOUNS);
        registerWord("gently", ADJECTIVES);
        registerWord("brush", VERBS);
        registerWord("brushing", VERBS);
        registerWord("tears", NOUNS);
        registerWord("poor", ADJECTIVES);
        registerWord("shall", VERBS);
        registerWord("go", VERBS);
        registerWord("going", VERBS);
        registerWord("gone", VERBS);
        registerWord("went", VERBS);
        registerWord("play", VERBS);
        registerWord("playing", VERBS);
        registerWord("played", VERBS);
        registerWord("no", ADVERBS);
        registerWord("not", ADVERBS);
        registerWord("yes", ADVERBS);
        registerWord("house", NOUNS);
        registerWord("houses", NOUNS);
        registerWord("home", NOUNS);
        registerWord("toy", NOUNS);
        registerWord("toys", NOUNS);
        registerWord("lesson", NOUNS);
        registerWord("teach", VERBS);
        registerWord("teaching", VERBS);
        registerWord("taught", VERBS);
        registerWord("next", ADJECTIVES);
        registerWord("else", CONJUNCTION);
        registerWord("cried", VERBS);
        registerWord("sudden", ADJECTIVES);
        registerWord("but", PREPOSITION);
        registerWord("looked", VERBS);
        registerWord("look", VERBS);
        registerWord("put", VERBS);
        registerWord("putting", VERBS);
        registerWord("puts", VERBS);
        registerWord("rabbit", NOUNS);
        registerWord("rabbits", NOUNS);
        registerWord("white", ADJECTIVES);
        registerWord("black", ADJECTIVES);
        registerWord("red", ADJECTIVES);
        registerWord("blue", ADJECTIVES);
        registerWord("green", ADJECTIVES);
        registerWord("orange", ADJECTIVES);
        registerWord("striped", ADJECTIVES);
        registerWord("purple", ADJECTIVES);
        registerWord("teal", ADJECTIVES);
        registerWord("gray", ADJECTIVES);
        registerWord("kid", NOUNS);
        registerWord("locked", VERBS);
        registerWord("lock", NOUNS);
        registerWord("soon", ADJECTIVES);
        registerWord("shrinking", VERBS);
        registerWord("fan", NOUNS);
        registerWord("dropped", VERBS);
        registerWord("avoid", VERBS);
        registerWord("hastily", ADJECTIVES);
        registerWord("going", VERBS);
        registerWord("rapidly", ADJECTIVES);
        registerWord("now", ADJECTIVES);
        registerWord("garden", NOUNS);
        registerWord("alas", INTERJECTION);
        registerWord("door", NOUNS);
        registerWord("doors", NOUNS);
        registerWord("still", ADJECTIVES);
        registerWord("making", VERBS);
        registerWord("am", VERBS);
        registerWord("again", ADVERBS);
        registerWord("me", PRONOUNS);
        registerWord("chance", NOUNS);
        registerWord("thatll", VERBS);
        registerWord("thats", VERBS);
        registerWord("that", DETERMINER);
        registerWord("added", VERBS);
        registerWord("add", VERBS);
        registerWord("subtract", VERBS);
        registerWord("remove", VERBS);
        registerWord("removed", VERBS);
        registerWord("felt", VERBS);
        registerWord("much", DETERMINER);
        registerWord("by", ADVERBS);
        registerWord("feet", NOUNS);
        registerWord("upon", PREPOSITION);
        registerWord("whether", CONJUNCTION);
        registerWord("waited", VERBS);
        registerWord("under", PREPOSITION);
        registerWord("sure", INTERJECTION);
        registerWord("sort", VERBS);
        registerWord("seemed", VERBS);
        registerWord("sea", NOUNS);
        registerWord("seen", ADJECTIVES);
        registerWord("silence", NOUNS);
        registerWord("soup", NOUNS);
        registerWord("speak", VERBS);
        registerWord("spoke", VERBS);
        registerWord("set", VERBS);
        registerWord("game", NOUNS);
        registerWord("gave", VERBS);
        registerWord("gloves", NOUNS);
        registerWord("hands", NOUNS);
        registerWord("hardly", ADVERBS);
        registerWord("grow", VERBS);
        registerWord("footman", NOUNS);
        registerWord("found", VERBS);
        registerWord("better", ADJECTIVES);
        registerWord("book", NOUNS);
        registerWord("begin", VERBS);
        registerWord("bill", NAME);
        registerWord("because", ADVERBS);
        registerWord("beautiful", ADJECTIVES);
        registerWord("being", VERBS);
        registerWord("an", CONJUNCTION);
        registerWord("anything", ADVERBS);
        registerWord("are", VERBS);
        registerWord("among", VERBS);
        registerWord("air", NOUNS);
        registerWord("nothing", ADVERBS);
        registerWord("twice", ADVERBS);
        registerWord("peeped", VERBS);
        registerWord("pictures", VERBS);
        registerWord("and ", DETERMINER);
        registerWord("tired", ADJECTIVES);
        registerWord("aftaid", ADJECTIVES);
        registerWord("hole", NOUNS);
        registerWord("half", ADVERBS);
        registerWord("same", ADJECTIVES);
        registerWord("confused", ADJECTIVES);
        registerWord("clamour", VERBS);
        registerWord("pig", NOUNS);
        registerWord("baby", NOUNS);
        registerWord("friend", NOUNS);
        registerWord("friends", NOUNS);
        registerWord("teacups", NOUNS);
        registerWord("queen", NOUNS);
        registerWord("king", NOUNS);
        registerWord("unfortunate", ADJECTIVES);
        registerWord("guests", NOUNS);
        registerWord("never", ADVERBS);
        registerWord("shrill", ADJECTIVES);
        registerWord("voice", NOUNS);
        registerWord("duchesss", NOUNS);
        registerWord("knee", NOUNS);
        registerWord("shriek", VERBS);
        registerWord("tinkling", ADJECTIVES);
        registerWord("miserable", ADJECTIVES);
        registerWord("sheep", NOUNS);
        registerWord("ive", VERBS);
        registerWord("attended", VERBS);
        registerWord("exclaimed", VERBS);
        registerWord("understand", VERBS);
        registerWord("presented", VERBS);
        registerWord("any", DETERMINER);
        registerWord("youd", VERBS);
        registerWord("theres", VERBS);
        registerWord("or", CONJUNCTION);
        registerWord("but", CONJUNCTION);
        registerWord("clear", ADJECTIVES);
        registerWord("way", NOUNS);
        registerWord("usual", ADJECTIVES);
        registerWord("isnt", ADVERBS);
        registerWord("sharply", ADJECTIVES);
        registerWord("stood", VERBS);
        registerWord("alices", NAME);
        registerWord("pray", VERBS);
        registerWord("mineral", NOUNS);
        registerWord("things", NOUNS);
        registerWord("its", ADJECTIVES);
        registerWord("duchess", NOUNS);
        registerWord("otherwise", ADVERBS);
        registerWord("than", CONJUNCTION);
        registerWord("then", ADVERBS);
        registerWord("agree", VERBS);
        registerWord("shared", VERBS);
        registerWord("sneezing", VERBS);
        registerWord("ending", VERBS);
        registerWord("dogs", NOUNS);
        registerWord("cats", NOUNS);
        registerWord("mouse", NOUNS);
        registerWord("softly", ADJECTIVES);
        registerWord("history", NOUNS);
        registerWord("passion", NOUNS);
        registerWord("question", NOUNS);
        registerWord("we", PRONOUNS);
        registerWord("either", DETERMINER);
        registerWord("wont", ADVERBS);
        registerWord("cant", ADVERBS);
        registerWord("talk", VERBS);
        registerWord("dont", ADVERBS);
        registerWord("like", VERBS);
        registerWord("low", ADJECTIVES);
        registerWord("led", VERBS);
        registerWord("duck", NOUNS);
        registerWord("uncomfortable", ADJECTIVES);
        registerWord("assembled", VERBS);
        registerWord("queer", ADJECTIVES);
        registerWord("looking", VERBS);
        registerWord("dodo", NOUNS);
        registerWord("party", NOUNS);
        registerWord("refused", VERBS);
        registerWord("ready", VERBS);
        registerWord("is", VERBS);
        registerWord("driest", ADJECTIVES);
        registerWord("older", ADJECTIVES);
        registerWord("thing", NOUNS);
        registerWord("bad", ADJECTIVES);
        registerWord("did", VERBS);
        registerWord("indeed", INTERJECTION);
        registerWord("age", NOUNS);
        registerWord("there", NOUNS);
        registerWord("itll", ADVERBS);
        registerWord("my", ADJECTIVES);
        registerWord("shore", NOUNS);
        registerWord("waving", VERBS);
        registerWord("paw", NOUNS);
        registerWord("round", ADJECTIVES);
        registerWord("lives", VERBS);
        registerWord("remarked", VERBS);
        registerWord("help", VERBS);
        registerWord("mad", ADJECTIVES);
        registerWord("youre", ADVERBS);
        registerWord("call", VERBS);
        registerWord("invited", VERBS);
        registerWord("raving", VERBS);
        registerWord("surprised", ADJECTIVES);
        registerWord("bye", PREPOSITION);
        registerWord("natural", ADJECTIVES);
        registerWord("croquet", NOUNS);
        registerWord("most", DETERMINER);
        registerWord("walked", VERBS);
        registerWord("sitting", VERBS);
        registerWord("fig", NOUNS);
        registerWord("replied", VERBS);
        registerWord("purring", VERBS);
        registerWord("growing", VERBS);
        registerWord("growls", VERBS);
        registerWord("visit", VERBS);
        registerWord("branch", NOUNS);
        registerWord("fur", NOUNS);
        registerWord("large", ADJECTIVES);
        registerWord("nearer", PREPOSITION);
        registerWord("near", PREPOSITION);
        registerWord("instead", ADVERBS);
        registerWord("lefthand", NOUNS);
        registerWord("bit", ADVERBS);
        registerWord("mushroom", NOUNS);
        registerWord("raised", VERBS);
        registerWord("timidly", ADJECTIVES);
        registerWord("room", NOUNS);
        registerWord("wasnt", ADVERBS);
        registerWord("tone", NOUNS);
        registerWord("down", PREPOSITION);
        registerWord("fun", NOUNS);
        registerWord("elbows", NOUNS);
        registerWord("almost", ADVERBS);
        registerWord("wish", VERBS);
        registerWord("i", NOUNS);
        registerWord("learn", VERBS);
        registerWord("high", ADVERBS);
        registerWord("nibbled", VERBS);
        registerWord("hare", NOUNS);
        registerWord("march", ADJECTIVES);
        registerWord("talking", VERBS);
        registerWord("over", ADJECTIVES);
        registerWord("encouraging", VERBS);
        registerWord("suppose", VERBS);
        registerWord("tree", NOUNS);
        registerWord("rather", ADVERBS);
        registerWord("didnt", VERBS);
        registerWord("saying", VERBS);
        registerWord("raven", NOUNS);
        registerWord("mean", VERBS);
        registerWord("severity", NOUNS);
        registerWord("writing", VERBS);
        registerWord("desk", NOUNS);
        registerWord("remarks", VERBS);
        registerWord("cutting", VERBS);
        registerWord("cut", VERBS);
        registerWord("only", ADVERBS);
        registerWord("believe", VERBS);
        registerWord("angily", ADJECTIVES);
        registerWord("civil", ADJECTIVES);
        registerWord("personal", ADJECTIVES);
        registerWord("aloud", ADVERBS);
        registerWord("exactly", INTERJECTION);
        registerWord("more", DETERMINER);
        registerWord("suppose", VERBS);
        registerWord("fast", ADJECTIVES);
        registerWord("saw", VERBS);
        registerWord("asleep", ADJECTIVES);
        registerWord("dormouse", NOUNS);
        registerWord("laid", VERBS);
        registerWord("great", ADJECTIVES);
        registerWord("guess", VERBS);
        registerWord("am", VERBS);
        registerWord("im", VERBS);
        registerWord("plenty", ADVERBS);
        registerWord("glad", VERBS);
        registerWord("begun", VERBS);
        registerWord("began", VERBS);
        registerWord("asking", VERBS);
        registerWord("riddles", NOUNS);
        registerWord("hair", NOUNS);
        registerWord("corner", NOUNS);
        registerWord("crowded", ADJECTIVES);
        registerWord("together", ADVERBS);
        registerWord("song", NOUNS);
        registerWord("were", VERBS);
        registerWord("liked", VERBS);
        registerWord("spoon", NOUNS);
        registerWord("jumped", VERBS);
        registerWord("fly", VERBS);
        registerWord("flying", VERBS);
        registerWord("hungry", ADJECTIVES);
        registerWord("eleven", NUMERAL);
        registerWord("ten", NUMERAL);
        registerWord("nine", NUMERAL);
        registerWord("eight", NUMERAL);
        registerWord("seven", NUMERAL);
        registerWord("six", NUMERAL);
        registerWord("five", NUMERAL);
        registerWord("four", NUMERAL);
        registerWord("three", NUMERAL);
        registerWord("two", NUMERAL);
        registerWord("morning", NOUNS);
        registerWord("stop", VERBS);
        registerWord("idea", NOUNS);
        registerWord("shook", VERBS);
        registerWord("beginning", VERBS);
        registerWord("ask", VERBS);
        registerWord("ventured", VERBS);
        registerWord("twinkle", VERBS);
        registerWord("roses", NOUNS);
        registerWord("others", NOUNS);
        registerWord("everythings", PRONOUNS);
        registerWord("curious", ADJECTIVES);
        registerWord("today", NOUNS);
        registerWord("into", PREPOSITION);
        registerWord("pocket", NOUNS);
        registerWord("flower", NOUNS);
        registerWord("throw", VERBS);
        registerWord("through", PREPOSITION);
        registerWord("though", CONJUNCTION);
        registerWord("try", VERBS);
        registerWord("turned", VERBS);
        registerWord("turning", VERBS);
        registerWord("without", PREPOSITION);
        registerWord("considering", VERBS);
        registerWord("conversations", NOUNS);
        registerWord("daisy", NOUNS);
        registerWord("chain", NOUNS);
        registerWord("hurried", VERBS);
        registerWord("killing", VERBS);
        registerWord("dipped", VERBS);
        registerWord("world", NOUNS);
        registerWord("tumbling", VERBS);
        registerWord("centre", NOUNS);
        registerWord("earth", NOUNS);
        registerWord("miles", NOUNS);
        registerWord("somewhere", ADVERBS);
        registerWord("latitude", NOUNS);
        registerWord("longitude", NOUNS);
        registerWord("nice", ADJECTIVES);
        registerWord("maam", NOUNS);
        registerWord("sir", NOUNS);
        registerWord("new", ADJECTIVES);
        registerWord("zealand", NOUNS);
        registerWord("australia", NOUNS);
        registerWord("downward", ADVERBS);
        registerWord("heads", NOUNS);
        registerWord("waistcoat", NOUNS);
        registerWord("out", ADVERBS);
        registerWord("coming", VERBS);
        registerWord("burning", ADJECTIVES);
        registerWord("watch", NOUNS);
        registerWord("orange", ADJECTIVES);
        registerWord("marmalade", NOUNS);
        registerWord("candle", NOUNS);
        registerWord("drink", VERBS);
        registerWord("flavour", NOUNS);
        registerWord("hot", ADJECTIVES);
        registerWord("poker", NOUNS);
        registerWord("telescope", NOUNS);
        registerWord("key", NOUNS);
        registerWord("fond", ADJECTIVES);
        registerWord("creep", VERBS);
        registerWord("smaller", ADJECTIVES);
        registerWord("anxiously", ADJECTIVES);
        registerWord("eat", VERBS);
        registerWord("ate", VERBS);
        registerWord("people", NOUNS);
        registerWord("pretending", VERBS);
        registerWord("opened", VERBS);
        registerWord("climb", VERBS);
        registerWord("cake", NOUNS);
        registerWord("marked", ADJECTIVES);
        registerWord("daisies", NOUNS);
        registerWord("suddenly", ADJECTIVES);
        registerWord("pink", ADJECTIVES);
        registerWord("remarkable", ADJECTIVES);
        registerWord("empty", ADJECTIVES);
        registerWord("jar", NOUNS);
        registerWord("name", NOUNS);
        registerWord("deep", VERBS);
        registerWord("cupboards", NOUNS);
        registerWord("patriotic", ADJECTIVES);
        registerWord("williams", NAME);
        registerWord("earls", NAME);
        registerWord("solemnly", ADJECTIVES);
        registerWord("us", PRONOUNS);
        registerWord("won", VERBS);
        registerWord("panting", VERBS);
        registerWord("race", NOUNS);
        registerWord("called", VERBS);
        registerWord("crossly", ADJECTIVES);
        registerWord("forehead", NOUNS);
        registerWord("pressed", VERBS);
        registerWord("real", ADJECTIVES);
        registerWord("mistake", NOUNS);
        registerWord("footsteps", NOUNS);
        registerWord("angry", ADJECTIVES);
        registerWord("sending", VERBS);
        registerWord("upstairs", NOUNS);
        registerWord("mary", NAME);
        registerWord("ann", NAME);
        registerWord("arm", NOUNS);
        registerWord("bottle", NOUNS);
        registerWord("fell", VERBS);
        registerWord("drunk", VERBS);
        registerWord("nay", CONJUNCTION);
        registerWord("jack", NAME);
        registerWord("box", NOUNS);
        registerWord("dead", ADJECTIVES);
        registerWord("window", NOUNS);
        registerWord("chimney", NOUNS);
        registerWord("rope", NOUNS);
        registerWord("kick", VERBS);
        registerWord("surprised", ADJECTIVES);
        registerWord("hit", VERBS);
        registerWord("hedge", NOUNS);
        registerWord("caterpillars", NOUNS);
        registerWord("caterpillar", NOUNS);
        registerWord("mouth", NOUNS);
        registerWord("brain", NOUNS);
        registerWord("injured", VERBS);
        registerWord("wait", VERBS);
        registerWord("father", NOUNS);
        registerWord("william", NAME);
        registerWord("fat", ADJECTIVES);
        registerWord("uncommonly", ADJECTIVES);
        registerWord("girl", NOUNS);
        registerWord("serpent", NOUNS);
        registerWord("repeated", ADJECTIVES);
        registerWord("story", NOUNS);
        registerWord("deepest", ADJECTIVES);
        registerWord("doubtfully", ADVERBS);
        registerWord("trees", NOUNS);
        registerWord("shaking", VERBS);
        registerWord("id", VERBS);
        registerWord("night", NOUNS);
        registerWord("day", NOUNS);
        registerWord("minute", NOUNS);
        registerWord("eggs", NOUNS);
        registerWord("tasted", VERBS);
        registerWord("raw", ADJECTIVES);
        registerWord("yours", PRONOUNS);
        registerWord("it", PRONOUNS);
        registerWord("axes", NOUNS);
        registerWord("chop", VERBS);
        registerWord("minded", VERBS);
        registerWord("jumping", VERBS);
        registerWord("twenty", NUMERAL);
        registerWord("ugly", ADJECTIVES);
        registerWord("games", NOUNS);
        registerWord("fetch", VERBS);
        registerWord("widly", ADJECTIVES);
        registerWord("kitchen", NOUNS);
        registerWord("camomile", NOUNS);
        registerWord("executioners", NOUNS);
        registerWord("queens", NOUNS);
        registerWord("really", ADVERBS);
        registerWord("lessons", NOUNS);
        registerWord("gryphon", NOUNS);
        registerWord("reeling", VERBS);
        registerWord("classics", ADJECTIVES);
        registerWord("master", NAME);
        registerWord("mock", VERBS);
        registerWord("laughing", VERBS);
        registerWord("stiff", ADJECTIVES);
        registerWord("encouraged", VERBS);
        registerWord("inquired", VERBS);
        registerWord("thirteen", NUMERAL);
        registerWord("fourteen", NUMERAL);
        registerWord("fifteen", NUMERAL);
        registerWord("sixteen", NUMERAL);
        registerWord("seventeen", NUMERAL);
        registerWord("eighteen", NUMERAL);
        registerWord("nineteen", NUMERAL);
        registerWord("thirty", NUMERAL);
        registerWord("forty", NUMERAL);
        registerWord("fifty", NUMERAL);
        registerWord("states", VERBS);
        registerWord("jury", NOUNS);
        registerWord("sounded", VERBS);
        registerWord("goldfish", NOUNS);
        registerWord("boy", NOUNS);
        registerWord("around", PREPOSITION);
        registerWord("listened", VERBS);
        registerWord("shouted", VERBS);
        registerWord("guinea", NOUNS);
        registerWord("pig", NOUNS);
        registerWord("pigs", NOUNS);
        registerWord("cries", VERBS);
        registerWord("bells", NOUNS);
        registerWord("eager", ADJECTIVES);
        registerWord("while",CONJUNCTION);
        registerWord("allow",VERBS);
        registerWord("deny",VERBS);
        registerWord("person",NOUNS);
        registerWord("politely",ADJECTIVES);
        registerWord("lory",NAME);
        registerWord("hundred",NUMERAL);
        registerWord("says",VERBS);
        registerWord("edwin",NAME);
        registerWord("shakespeare",NAME);
        registerWord("salt",NOUNS);
        registerWord("everybody",PRONOUNS);
        registerWord("offended",VERBS);
        registerWord("offend",VERBS);
        registerWord("afriend",ADJECTIVES);
        registerWord("comfits",NOUNS);
        registerWord("hate",VERBS);
        registerWord("magic",NOUNS);
        registerWord("thousand",NUMERAL);
        registerWord("trembled",VERBS);
        registerWord("minutes",NOUNS);
        registerWord("forgetting",VERBS);
        registerWord("stopped",VERBS);
        registerWord("listened",VERBS);
        registerWord("moment",NOUNS);
        registerWord("inwards",ADVERBS);
        registerWord("wider",ADJECTIVES);
        registerWord("wag",VERBS);
        registerWord("might",VERBS);
        registerWord("denied",VERBS);
        registerWord("dreadfully",ADJECTIVES);
        registerWord("english",ADJECTIVES);
        registerWord("breathe",VERBS);
        registerWord("answer",VERBS);
        registerWord("whats",ADVERBS);
        registerWord("riddle",NOUNS);
        registerWord("poured",VERBS);
        registerWord("nose",NOUNS);
        registerWord("yet",ADVERBS);
        registerWord("wonder",VERBS);
        registerWord("alive",ADJECTIVES);
        registerWord("knew",VERBS);
        registerWord("off",PREPOSITION);
        registerWord("shouting",VERBS);
        registerWord("kiss",VERBS);
        registerWord("nobody",PRONOUNS);
        registerWord("died",VERBS);
        registerWord("certainly",ADVERBS);
        registerWord("ancient",ADJECTIVES);
        registerWord("yourself",PRONOUNS);
        registerWord("ashamed",ADJECTIVES);
        registerWord("ought",VERBS);
        registerWord("grief",NOUNS);
        registerWord("crab",NOUNS);
        registerWord("yawned",VERBS);
        registerWord("turtle",NOUNS);
        registerWord("boots",NOUNS);
        registerWord("kindly",ADJECTIVES);
        registerWord("join",VERBS);
        registerWord("france",NOUNS);
        registerWord("stupid",ADJECTIVES);

        registerWord("!", WordType.GRAMMAR);
        registerWord("?", WordType.GRAMMAR);
        registerWord("\"", WordType.GRAMMAR);
        registerWord(".", WordType.GRAMMAR);
        registerWord(",", WordType.GRAMMAR);
    }

    public static WordType getWordType(String word) {
        if (nouns.contains(word))
            return NOUNS;
        if (numeral.contains(word))
            return NUMERAL;
        if (pronouns.contains(word))
            return PRONOUNS;
        if (determiners.contains(word))
            return DETERMINER;
        if (verbs.contains(word))
            return VERBS;
        if (adjectives.contains(word))
            return ADJECTIVES;
        if (adverbs.contains(word))
            return ADVERBS;
        if (conjunctions.contains(word))
            return CONJUNCTION;
        if (names.contains(word))
            return NAME;
        if (preposition.contains(word))
            return PREPOSITION;
        if (interjections.contains(word))
            return INTERJECTION;
        if (grammar.contains(word))
            return WordType.GRAMMAR;
        return WordType.UNKNOWN;
    }

    public static List<String> getAllType(WordType type) {
        if (type == PRONOUNS) {
            return pronouns;
        }
        if (type == NAME) {
            return names;
        }
        if (type == NOUNS) {
            return nouns;
        }
        if (type == ADJECTIVES) {
            return adjectives;
        }
        if (type == ADVERBS) {
            return adverbs;
        }
        if (type == WordType.GRAMMAR) {
            return grammar;
        }
        if (type == DETERMINER) {
            return determiners;
        }
        if (type == VERBS) {
            return verbs;
        }
        if (type == CONJUNCTION) {
            return conjunctions;
        }
        if (type == PREPOSITION) {
            return preposition;
        }
        if (type == INTERJECTION) {
            return interjections;
        }
        if (type == NUMERAL) {
            return numeral;
        }
        return null;
    }

    public static String getWord(WordType type, int wordIndex) {
        if (type == NUMERAL) {
            return numeral.get(wordIndex);
        }
        if (type == PRONOUNS) {
            return pronouns.get(wordIndex);
        }
        if (type == NAME) {
            return names.get(wordIndex);
        }
        if (type == NOUNS) {
            return nouns.get(wordIndex);
        }
        if (type == ADJECTIVES) {
            return adjectives.get(wordIndex);
        }
        if (type == ADVERBS) {
            return adverbs.get(wordIndex);
        }
        if (type == WordType.GRAMMAR) {
            return grammar.get(wordIndex);
        }
        if (type == DETERMINER) {
            return determiners.get(wordIndex);
        }
        if (type == VERBS) {
            return verbs.get(wordIndex);
        }
        if (type == CONJUNCTION) {
            return conjunctions.get(wordIndex);
        }
        if (type == PREPOSITION) {
            return preposition.get(wordIndex);
        }
        if (type == INTERJECTION) {
            return interjections.get(wordIndex);
        }
        return null;
    }

    public static int getIndexForWord(String s) {

        for (int wordIndex = 0; wordIndex < pronouns.size(); wordIndex++)
            if (pronouns.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < names.size(); wordIndex++)
            if (names.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < nouns.size(); wordIndex++)
            if (nouns.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < adjectives.size(); wordIndex++)
            if (adjectives.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < adverbs.size(); wordIndex++)
            if (adverbs.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < grammar.size(); wordIndex++)
            if (grammar.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < determiners.size(); wordIndex++)
            if (determiners.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < verbs.size(); wordIndex++)
            if (verbs.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < conjunctions.size(); wordIndex++)
            if (conjunctions.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < preposition.size(); wordIndex++)
            if (preposition.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < interjections.size(); wordIndex++)
            if (interjections.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        for (int wordIndex = 0; wordIndex < numeral.size(); wordIndex++)
            if (numeral.get(wordIndex).equalsIgnoreCase(s))
                return wordIndex;
        return -1;
    }
}
