package edu.hm.hafner.java.uc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.MultipleNodeAssert;
import org.xmlunit.assertj.SingleNodeAssert;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;
import org.xmlunit.builder.Input.Builder;

import static org.xmlunit.assertj.XmlAssert.*;

class IssuesTestDataTest {

    private static final String FILES_XPATH = "//ns:file";
    private static final String VIOLATIONS_XPATH = FILES_XPATH + "/ns:violation";

    private static final String BEGIN_LINE = "beginline";
    private static final String END_LINE = "endline";
    private static final String BEGIN_COLUMN = "begincolumn";
    private static final String END_COLUMN = "endcolumn";
    private static final String RULE = "rule";
    private static final String RULESET = "ruleset";
    private static final String PACKAGE = "package";
    private static final String CLASS = "class";
    private static final String VARIABLE = "variable";
    private static final String EXTERNAL_INFO_URL = "externalInfoUrl";
    private static final String PRIORITY = "priority";
    private static final String METHOD = "method";

    @Test
    void shouldVerifyPmd() throws IOException {
        final Builder pmdReport = Input.from(
                IOUtils.toByteArray(Objects.requireNonNull(IssuesTestData.class.getResource("/test/pmd.xml"))));
        final Map<String, String> pmdNamespace = new HashMap<>();
        pmdNamespace.put("ns", "http://pmd.sourceforge.net/report/2.0.0");

        // register default namespace
        final XmlAssert assertThat = assertThat(pmdReport).withNamespaceContext(pmdNamespace);

        // assert file count
        final MultipleNodeAssert assertThatFiles = assertThat.nodesByXPath(FILES_XPATH);
        assertThatFiles.hasSize(43);

        // assert first and last file names end correctly
        final var assertThatNames = assertThatFiles.extractingAttribute("name");
        assertThatNames.first().asString().endsWith("JenkinsFacade.java");
        assertThatNames.last().asString().endsWith("QualityGateTest.java");

        // assert violation count
        final MultipleNodeAssert violations = assertThat.nodesByXPath(VIOLATIONS_XPATH);
        violations.hasSize(109);

        // assert correct ruleset violations
        final var rulesets = violations.extractingAttribute("ruleset");
        rulesets.filteredOn("Best Practices"::equals).hasSize(20);
        rulesets.filteredOn("Code Style"::equals).hasSize(53);
        rulesets.filteredOn("Design"::equals).hasSize(15);
        rulesets.filteredOn("Documentation"::equals).hasSize(3);
        rulesets.filteredOn("Error Prone"::equals).hasSize(12);
        rulesets.filteredOn("Performance"::equals).hasSize(6);

        // assert first and last violation
        assertViolationNode(violations.first(), attributesForFirstViolation(),
                "Parameter 'descriptorType' is not assigned and could be declared final");

        assertViolationNode(violations.last(), attributesForLastViolation(),
                "Parameter 'builder' is not assigned and could be declared final");
    }

    private void assertViolationNode(final SingleNodeAssert node, final Map<String, String> attributes,
            final String text) {
        attributes.forEach(node::hasAttribute);
        node.matches(n -> n.getTextContent().contains(text));
    }

    private Map<String, String> attributesForFirstViolation() {
        return Map.ofEntries(
                Map.entry(BEGIN_LINE, "59"),
                Map.entry(END_LINE, "59"),
                Map.entry(BEGIN_COLUMN, "13"),
                Map.entry(END_COLUMN, "35"),
                Map.entry(RULE, "MethodArgumentCouldBeFinal"),
                Map.entry(RULESET, "Code Style"),
                Map.entry(PACKAGE, "io.jenkins.plugins.analysis.core"),
                Map.entry(CLASS, "JenkinsFacade"),
                Map.entry(VARIABLE, "descriptorType"),
                Map.entry(EXTERNAL_INFO_URL,
                        "https://pmd.github.io/pmd-6.1.0/pmd_rules_java_codestyle.html#methodargumentcouldbefinal"),
                Map.entry(PRIORITY, "3"));
    }

    private Map<String, String> attributesForLastViolation() {
        return Map.ofEntries(
                Map.entry(BEGIN_LINE, "300"),
                Map.entry(END_LINE, "300"),
                Map.entry(BEGIN_COLUMN, "32"),
                Map.entry(END_COLUMN, "81"),
                Map.entry(RULE, "MethodArgumentCouldBeFinal"),
                Map.entry(RULESET, "Code Style"),
                Map.entry(PACKAGE, "io.jenkins.plugins.analysis.core.quality"),
                Map.entry(CLASS, "QualityGateTest"),
                Map.entry(METHOD, "testThreshold"),
                Map.entry(VARIABLE, "builder"),
                Map.entry(EXTERNAL_INFO_URL,
                        "https://pmd.github.io/pmd-6.1.0/pmd_rules_java_codestyle.html#methodargumentcouldbefinal"),
                Map.entry(PRIORITY, "3"));
    }
}
