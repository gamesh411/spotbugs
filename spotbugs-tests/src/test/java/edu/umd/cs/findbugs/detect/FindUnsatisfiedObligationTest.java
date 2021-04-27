package edu.umd.cs.findbugs.detect;

import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;

import edu.umd.cs.findbugs.test.SpotBugsRule;

public class FindUnsatisfiedObligationTest {
    @Rule
    public SpotBugsRule spotbugs = new SpotBugsRule();

    /**
     * @see <a href="https://github.com/spotbugs/spotbugs/issues/60">GitHub
     *      issue</a>
     */
    @Test
    public void testIssue60() {
        spotbugs.performAnalysis(Paths.get("../spotbugsTestCases/build/classes/java/main/lambdas/Issue60.class"));
        // TODO: use assertDoesNotThrow when upgrading to jUnit 5
    }
}
