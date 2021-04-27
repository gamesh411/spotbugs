package edu.umd.cs.findbugs.nullness;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.test.SpotBugsRule;


/**
 * Unit test to reproduce <a href="https://github.com/spotbugs/spotbugs/issues/259">#259</a>.
 */
public class Issue259Test {
    @Rule
    public SpotBugsRule spotbugs = new SpotBugsRule();

    @Test
    public void test() {
        BugCollection bugCollection = spotbugs.performAnalysis(
                Paths.get("../spotbugsTestCases/build/classes/java/main/ghIssues/Issue259.class"));

        List<String> RCNbugs = StreamSupport.stream(bugCollection.spliterator(), false)
                .map(bugInstance -> bugInstance.getAbbrev())
                .filter(abbrev -> abbrev == "RCN")
                .collect(Collectors.toList());

        assertThat(RCNbugs, empty());
    }
}
