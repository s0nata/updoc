package semantic.wmd4j;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import semantic.wmd4j.emd.EarthMovers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.nd4j.common.io.StringUtils.isEmpty;


/**
 * Created by Majer on 21.9.2016.
 */
public class WordMovers {

    private static final double DEFAULT_STOPWORD_WEIGHT = 0.5;
    private static final double DEFAULT_MAX_DISTANCE = 1;

    private final WordVectors wordVectors;
    private final Set<String> stopwords;
    private final double stopwordWeight;

    private final EarthMovers earthMovers;

    private WordMovers(Builder builder) {
        this.wordVectors = builder.wordVectors;
        this.stopwords = builder.stopwords;
        this.stopwordWeight = builder.stopwordWeight;
        this.earthMovers = new EarthMovers();
    }

    public double distance(String a, String b) {

        if (isEmpty(a) || isEmpty(b))
            throw new IllegalArgumentException();

        return distance(a.split(" "), b.split(" "));
    }

    public double distance(String[] tokensA, String[] tokensB) {

        if (tokensA.length < 1 || tokensB.length < 1)
            throw new IllegalArgumentException();

        Map<String, FrequencyVector> mapA = bagOfVectors(tokensA);
        Map<String, FrequencyVector> mapB = bagOfVectors(tokensB);

        if (mapA.size() == 0 || mapB.size() == 0) {
            throw new NoSuchElementException(
                    "Can't find any word vectors for given input text ..." + Arrays.toString(tokensA) + "|" +
                            Arrays.toString(tokensB));
        }
        //vocabulary of current tokens
        List<String> vocab = Stream.of(mapA.keySet(), mapB.keySet())
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        double[][] matrix = new double[vocab.size()][vocab.size()];

        for (int i = 0; i < matrix.length; i++) {
            String tokenA = vocab.get(i);
            for (int j = 0; j < matrix.length; j++) {
                String tokenB = vocab.get(j);
                if (mapA.containsKey(tokenA) && mapB.containsKey(tokenB)) {
                    double distance = mapA.get(tokenA).getVector().distance2(mapB.get(tokenB).getVector());
                    //if tokenA and tokenB are stopwords, calculate distance according to stopword weight
                    if (stopwords != null && tokenA.length() != 1 && tokenB.length() != 1)
                        distance *= stopwords.contains(tokenA) && stopwords.contains(tokenB) ? 1 : stopwordWeight;
                    matrix[i][j] = distance;
                    matrix[j][i] = distance;
                }
            }
        }

        double[] freqA = frequencies(vocab, mapA);
        double[] freqB = frequencies(vocab, mapB);

        return earthMovers.distance(freqA, freqB, matrix, 0);
    }

    private Map<String, FrequencyVector> bagOfVectors(String[] tokens) {

        Map<String, FrequencyVector> map = new LinkedHashMap<>(tokens.length);
        Arrays.stream(tokens)
                .filter(x -> wordVectors.hasWord(x))
                .forEach(x -> map.merge(x, new FrequencyVector(wordVectors.getWordVectorMatrix(x)), (v, o) -> {
                    v.incrementFrequency();
                    return v;
                }));

        return map;
    }

    /*
    Normalized frequencies for vocab
     */
    private double[] frequencies(List<String> vocab, Map<String, FrequencyVector> map) {
        return vocab.stream().mapToDouble(x -> {
            if (map.containsKey(x)) {
                return (double) map.get(x).getFrequency() / map.size();
            }
            return 0d;
        }).toArray();
    }

    public static Builder Builder() {
        return new Builder();
    }

    public static final class Builder {

        private WordVectors wordVectors;
        private Set<String> stopwords;

        private double stopwordWeight = DEFAULT_STOPWORD_WEIGHT;

        private Builder() {
        }

        public WordMovers build() {
            return new WordMovers(this);
        }

        public Builder wordVectors(WordVectors wordVectors) {
            this.wordVectors = wordVectors;
            return this;
        }

        public Builder stopwords(Set<String> stopwords) {
            this.stopwords = stopwords;
            return this;
        }

        public Builder stopwordWeight(double stopwordWeight) {
            this.stopwordWeight = stopwordWeight;
            return this;
        }

    }
}
