package com.comparators;

import com.comparators.trigram.TrigramGenerator;
import com.comparators.trigram.TrigramMatcher;

public class FuzzyComparator {

    private final TrigramGenerator trigramGenerator;
    private final TrigramMatcher trigramMatcher;

    public FuzzyComparator(TrigramGenerator trigramGenerator, TrigramMatcher trigramMatcher) {
        this.trigramGenerator = trigramGenerator;
        this.trigramMatcher = trigramMatcher;
    }

    public boolean compare(String string1, String string2) {

        string1 = string1.trim().replace(" ", "");
        string2 = string2.trim().replace(" ", "");

        if (string1.length() != string2.length()) {
            int absLengthDifference = Math.abs(string1.length() - string2.length());
            if (string1.length() > string2.length()) {
                StringBuilder string2Builder = new StringBuilder(string2);
                for (int i = 0; i < absLengthDifference; i++) {
                    string2Builder.append(" ");
                }
                string2 = string2Builder.toString();
            } else {
                StringBuilder string1Builder = new StringBuilder(string1);
                for (int i = 0; i < absLengthDifference; i++) {
                    string1Builder.append(" ");
                }
                string1 = string1Builder.toString();
            }
        }

        return trigramMatcher.matchTrigrams(trigramGenerator.generateTrigrams(string1),
                trigramGenerator.generateTrigrams(string2));
    }
}
