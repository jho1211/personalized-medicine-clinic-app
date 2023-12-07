package model;

import java.util.*;

// Sequence aligner stores two sequences and compares their similarity to find the
// section from both sequences that are most similar. Stores the redacted sequence from ref after alignment and
// the score of the maximum alignment
public class SequenceAligner {
    private String targetSeq;
    private String refSeq;
    private String redSeq;
    private int score;

    // REQUIRES: Length of seqB should be larger than seqA and needs to be valid genome sequence
    // i.e. consists of ATGC only
    // EFFECTS: Constructs a new sequence aligner object with the two sequences to compare initialized
    // The redacted sequence is empty and the score is -1 to begin with
    public SequenceAligner(String targetSeq, String refSeq) {
        this.targetSeq = targetSeq;
        this.refSeq = refSeq;
        this.redSeq = "";
        this.score = -1;
    }

    // REQUIRES: seqA.length() == seqB.length()
    // EFFECTS: Returns the similarity score between two sequences
    // Each match in a char counts as +1, each mismatch counts as 0
    public int calculateSimilarityScore(String seqA, String seqB) {
        int score = 0;
        for (int i = 0; i < seqA.length(); i++) {
            if (seqA.charAt(i) == seqB.charAt(i)) {
                score++;
            }
        }

        return score;
    }


    // REQUIRES: seqA.length() < refSeq.length()
    // MODIFIES: this
    // EFFECTS: Returns the section of seqA and refSeq which are most similar in terms of
    // similarity score, calculated using the calculateSimilarityScore function and also returns a
    // redacted version of seqA with the same chars only present in that order
    // If multiple sections have same highest score, then return the first section with highest score
    public void getMostSimilarAlignment() {
        int startIdx = 0;
        int endIdx = targetSeq.length();
        int highestScore = 0;
        String mostSimilarSectionRef = refSeq.substring(0, targetSeq.length());

        while (endIdx <= refSeq.length()) {
            String secRef = refSeq.substring(startIdx, endIdx);
            int score = calculateSimilarityScore(targetSeq, secRef);

            if (score > highestScore) {
                highestScore = score;
                mostSimilarSectionRef = secRef;
            }

            startIdx++;
            endIdx++;
        }

        this.score = highestScore;
        this.redSeq = nonSimilarCharsRedacted(targetSeq, mostSimilarSectionRef);
    }

    // REQUIRES: seqA.length() < refSeq.length()
    // MODIFIES: Returns seqA but with all the non-similar characters converted to dashes
    public String nonSimilarCharsRedacted(String seqA, String seqB) {
        StringBuilder redSeq = new StringBuilder();
        for (int i = 0; i < seqA.length(); i++) {
            if (seqA.charAt(i) == seqB.charAt(i)) {
                redSeq.append(seqA.charAt(i));
            } else {
                redSeq.append("-");
            }
        }

        return redSeq.toString();
    }

    public int getSimilarityPercentage() {
        if (score == -1) {
            return -1;
        }

        return Math.round((float) score / (float) targetSeq.length() * 100);
    }

    // EFFECTS: Returns the sequence A, the target sequence
    public String getTargetSeq() {
        return this.targetSeq;
    }

    // EFFECTS: Returns the reference sequence
    public String getRefSeq() {
        return this.refSeq;
    }

    // EFFECTS: Returns the similarity score of the most similar alignment
    public int getSimilarityScore() {
        return this.score;
    }

    // EFFECTS: Returns the reference seq with the non-similar parts redacted with dashes
    public String getRedactedSeq() {
        return this.redSeq;
    }
}
