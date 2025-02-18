package com.amazon.opendistroforelasticsearch.knn.plugin.scripts;

import com.amazon.opendistroforelasticsearch.knn.KNNTestCase;
import com.amazon.opendistroforelasticsearch.knn.plugin.script.KNNScoringUtil;

public class KNNScoringUtilTests extends KNNTestCase {

    public void testL2SquaredScoringFunction() {
        float[] queryVector = {1.0f, 1.0f, 1.0f};
        float[] inputVector = {4.0f, 4.0f, 4.0f};

        Float distance = KNNScoringUtil.l2Squared(queryVector, inputVector);
        assertTrue(distance == 27.0f);
    }

    public void testCosineSimilScoringFunction() {
        float[] queryVector = {1.0f, 1.0f, 1.0f};
        float[] inputVector = {4.0f, 4.0f, 4.0f};

        float queryVectorMagnitude = KNNScoringUtil.getVectorMagnitudeSquared(queryVector);
        float inputVectorMagnitude = KNNScoringUtil.getVectorMagnitudeSquared(inputVector);
        float dotProduct = 12.0f;
        float expectedScore = (float) (dotProduct / (Math.sqrt(queryVectorMagnitude * inputVectorMagnitude)));


        Float actualScore = KNNScoringUtil.cosinesimil(queryVector, inputVector);
        assertEquals(expectedScore, actualScore, 0.0001);
    }

    public void testCosineSimilOptimizedScoringFunction() {
        float[] queryVector = {1.0f, 1.0f, 1.0f};
        float[] inputVector = {4.0f, 4.0f, 4.0f};
        float queryVectorMagnitude = KNNScoringUtil.getVectorMagnitudeSquared(queryVector);
        float inputVectorMagnitude = KNNScoringUtil.getVectorMagnitudeSquared(inputVector);
        float dotProduct = 12.0f;
        float expectedScore = (float) (dotProduct / (Math.sqrt(queryVectorMagnitude * inputVectorMagnitude)));

        Float actualScore = KNNScoringUtil.cosinesimilOptimized(queryVector, inputVector, queryVectorMagnitude);
        assertEquals(expectedScore, actualScore, 0.0001);
    }

    public void testGetInvalidVectorMagnitudeSquared() {
        float[] queryVector = null;
        // vector cannot be null
        expectThrows(IllegalStateException.class, () -> KNNScoringUtil.getVectorMagnitudeSquared(queryVector));
    }

    public void testConvertInvalidVectorToPrimitive() {
        float[] primitiveVector = null;
        assertEquals(primitiveVector, KNNScoringUtil.convertVectorToPrimitive(primitiveVector));
    }

    public void testCosineSimilQueryVectorZeroMagnitude() {
        float[] queryVector = {0,0};
        float[] inputVector = {4.0f, 4.0f};
        assertEquals(Float.MIN_VALUE, KNNScoringUtil.cosinesimil(queryVector, inputVector), 0.00001);
    }

    public void testCosineSimilOptimizedQueryVectorZeroMagnitude() {
        float[] inputVector = {4.0f, 4.0f};
        float[] queryVector = {0, 0};
        assertTrue(Float.MIN_VALUE == KNNScoringUtil.cosinesimilOptimized(queryVector, inputVector, 0.0f));
    }
}
