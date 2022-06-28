package mapper;

import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class encapsulates the implementation of a bag of words structure as a HashBag<String>.
 */
public class WordBag {

    private final HashBag<String> lemmas;

    /**
     * Creates a new empty bag of words.
     */
    public WordBag() {

        lemmas = new HashBag<String>();
    }

    /**
     * Creates a non-empty bag of words from a list.
     *
     * @param words List of words
     */
    public WordBag(Collection<String> words) {

        lemmas = new HashBag<String>();
        lemmas.addAll(words);
    }

    /**
     * @see org.apache.commons.collections4.bag.HashBag#add(Object)
     */
    public void add(String str) {

        this.lemmas.add(str);
    }

    /**
     * @see org.apache.commons.collections4.bag.HashBag#addAll(Collection)
     */
    public void addAll(Collection<String> coll) {

        this.lemmas.addAll(coll);
    }

    /**
     * @see org.apache.commons.collections4.bag.HashBag#contains(Object)
     */
    public boolean contains(Object obj) {

        return this.lemmas.contains(obj);
    }

    /**
     * @see org.apache.commons.collections4.bag.HashBag#size()
     */
    public int size() {
        return this.lemmas.size();
    }

    /**
     * @see org.apache.commons.collections4.bag.HashBag#getCount(Object)
     */
    public int getCount(Object obj) {
        return this.lemmas.getCount(obj);
    }

    /**
     * @see org.apache.commons.collections4.bag.HashBag#remove(Object)
     */
    public void remove(Object obj) {
        this.lemmas.remove(obj);
    }

    /**
     * Calculates the Jaccard similarity between this bag of words and another one.
     *
     * @param other the other bag of words to compare against
     * @return Returns the Jaccard similarity value
     */
    public double jaccardSim(WordBag other) {

        double commonLemmasNumber = 0;

        for (String str : other.raw()) {

            if (this.lemmas.contains(str)) {
                commonLemmasNumber += 1;
            }
        }

        return commonLemmasNumber / (this.size() + other.size() - commonLemmasNumber);
    }

    /**
     * Compute cosine similarity between this bag of words and another one.
     *
     * @param other the other bag of words to compare against
     * @return the cosine similarity value
     */
    public double cosineSim(WordBag other) {

        CosineSimilarity dist = new CosineSimilarity();

        // TODO could be written more elegantly with lambdas
        Map<CharSequence, Integer> leftVector = new HashMap<>();
        for (String tLemma : this.lemmas) {
            leftVector.put(tLemma, this.lemmas.getCount(tLemma));
        }

        Map<CharSequence, Integer> rightVector = new HashMap<>();
        for (String oLemma : other.lemmas) {
            rightVector.put(oLemma, other.lemmas.getCount(oLemma));
        }

        return dist.cosineSimilarity(leftVector, rightVector);
    }

    public String asSentence() {
        StringBuilder sentence = new StringBuilder();
        for (String lemma : this.lemmas.uniqueSet()) {
            for (int i = 0; i < this.lemmas.getCount(lemma); i++) {
                sentence.append(lemma);
                sentence.append(" ");
            }
        }

        return sentence.toString();
    }

    /**
     * Returns the underlying HashBag<> as is.
     *
     * @return Returns the underlying HashBag<>
     */
    public HashBag<String> raw() {
        return this.lemmas;
    }

    /**
     * @see org.apache.commons.collections4.bag.HashBag#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof WordBag)) {
            return false;
        }

        WordBag other = (WordBag) obj;

        return this.lemmas.equals(other);
    }

    /**
     * @see HashBag#toString()
     */
    @Override
    public String toString() {
        return this.lemmas.toString();
    }

    public List<String> toList() {
        return Arrays.stream(this.lemmas.toArray()).map(x -> x.toString()).collect(Collectors.toList());
    }
}
