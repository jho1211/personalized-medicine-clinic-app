package model;

import org.junit.jupiter.api.Test;

import javax.sound.midi.Sequence;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceAlignerTest {
    @Test
    public void testConstructor() {
        SequenceAligner seqAligner = new SequenceAligner("ATGC", "ATGCATGC");
        assertEquals("ATGC", seqAligner.getTargetSeq());
        assertEquals("ATGCATGC", seqAligner.getRefSeq());
    }

    @Test
    public void testGetSimilarityScoreNotSimilarSeqs() {
        SequenceAligner seqAligner = new SequenceAligner("ATGC", "GACT");
        assertEquals(0, seqAligner.calculateSimilarityScore("ATGC", "GACT"));
    }

    @Test
    public void testGetSimilarityScoreSlightlySimilarSeqs() {
        SequenceAligner seqAligner = new SequenceAligner("ATGC", "AGGT");
        assertEquals(2, seqAligner.calculateSimilarityScore("ATGC", "AGGT"));
    }

    @Test
    public void testGetSimilarityScoreIdenticalSeqs() {
        SequenceAligner seqAligner = new SequenceAligner("AGGG", "AGGG");
        assertEquals(4, seqAligner.calculateSimilarityScore("AGGG", "AGGG"));
    }

    @Test
    public void testGetMostSimilarSequenceVeryLowSimilarity() {
        SequenceAligner seqAligner = new SequenceAligner("GGGG", "ATGCATGCATGC");
        seqAligner.getMostSimilarAlignment();
        assertEquals(1, seqAligner.getSimilarityScore());
        assertEquals("--G-", seqAligner.getRedactedSeq());
    }

    @Test
    public void testGetMostSimilarSequenceMediumSimilarity() {
        SequenceAligner seqAligner = new SequenceAligner("TGACTG", "ATGCATGCATGC");
        seqAligner.getMostSimilarAlignment();
        assertEquals(4, seqAligner.getSimilarityScore());
        assertEquals("TG--TG", seqAligner.getRedactedSeq());
    }

    @Test
    public void testGetMostSimilarSequenceMaxSimilarity() {
        SequenceAligner seqAligner = new SequenceAligner("TGCATGCA", "ATGCATGCATGC");
        seqAligner.getMostSimilarAlignment();
        assertEquals("TGCATGCA", seqAligner.getRedactedSeq());
        assertEquals(8, seqAligner.getSimilarityScore());
    }

    @Test
    public void testRedactSequenceNoSimilarity() {
        SequenceAligner seqAligner = new SequenceAligner("ATGC", "GGCG");
        String redSeq = seqAligner.nonSimilarCharsRedacted("ATGC", "GGCG");
        assertEquals("----", redSeq);
    }

    @Test
    public void testRedactSequenceSomeSimilarity() {
        SequenceAligner seqAligner = new SequenceAligner("GCAT", "ATAT");
        String redSeq = seqAligner.nonSimilarCharsRedacted("GCAT", "ATAT");
        assertEquals("--AT", redSeq);
    }

    @Test
    public void testRedactSequenceMaxSimilarity() {
        SequenceAligner seqAligner = new SequenceAligner("GCAT", "GCAT");
        String redSeq = seqAligner.nonSimilarCharsRedacted("GCAT", "GCAT");
        assertEquals("GCAT", redSeq);
    }

    @Test
    public void testCalculateSimilarityPercentageExact() {
        SequenceAligner seqAligner = new SequenceAligner("GCAT", "GCAT");
        seqAligner.getMostSimilarAlignment();
        int res = seqAligner.getSimilarityPercentage();
        assertEquals(100, res);
    }

    @Test
    public void testCalculateSimilarityPercentageDiff() {
        SequenceAligner seqAligner = new SequenceAligner("ATGC", "GCAT");
        seqAligner.getMostSimilarAlignment();
        int res = seqAligner.getSimilarityPercentage();
        assertEquals(0, res);
    }

    @Test
    public void testCalculateSimilarityPercentageClose() {
        SequenceAligner seqAligner = new SequenceAligner("ATTC", "AGGC");
        seqAligner.getMostSimilarAlignment();
        int res = seqAligner.getSimilarityPercentage();
        assertEquals(50, res);
    }

    @Test
    public void testCalculateSimilarityPercentageNoAlignment() {
        SequenceAligner seqAligner = new SequenceAligner("ATGCATGC", "ATGCATGC");
        int res = seqAligner.getSimilarityPercentage();
        assertEquals(-1, res);
    }
}
