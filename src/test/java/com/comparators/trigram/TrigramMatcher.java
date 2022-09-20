package com.comparators.trigram;

import java.util.ArrayList;
import java.util.List;

public class TrigramMatcher {

    public boolean matchTrigrams(List<String> trigrams1, List<String> trigrams2) {
        int firstListSize = trigrams1.size();
        if (firstListSize != trigrams2.size()) {
            throw new IllegalArgumentException("The trigram lists are not equal in size: ");
        }

        List<Double> equationList = new ArrayList<>(firstListSize);

        for (String trigram : trigrams1) {
            equationList.add(trigrams2.contains(trigram) ? 1.0 : 0);
        }

        double compareResult = equationList.stream().reduce(0d, Double::sum);

        compareResult = compareResult / firstListSize;

        double matchingThreshold = 0.98d;
        return compareResult > matchingThreshold;
    }
}
