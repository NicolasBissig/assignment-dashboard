package edu.hm.hafner.java.uc;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.gson.Gson;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.java.db.EntityService;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link IssuesService}.
 *
 * @author Ullrich Hafner
 */
class IssuesServiceTest {

    private static final String DUMMY_TOOL = "dummy-id";
    private static final String LABELS_JSON_PATH = "$.labels";
    private static final String CATEGORY = "category";
    private static final String TYPE = "type";
    private static final String SEVERITY = "severity";
    private static final String FILE_NAME = "fileName";

    @Test
    void shouldCreateCategoryDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByCategory(DUMMY_TOOL, "");

        // Then
        assertThat(toJson(distribution)).isEqualTo("{\"labels\":[\"Design\",\"Documentation\",\"Best Practices\",\"Performance\",\"Code Style\",\"Error Prone\"],\"datasets\":[{\"data\":[15,3,20,6,53,12]}]}");
    }

    @Test
    void shouldCreateTypeDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByType(DUMMY_TOOL, "");

        // Then
        assertThat(toJson(distribution)).isEqualTo("{\"labels\":[\"OptimizableToArrayCall\",\"LooseCoupling\",\"MethodArgumentCouldBeFinal\",\"UncommentedEmptyMethodBody\",\"ConfusingTernary\",\"MissingSerialVersionUID\",\"GuardLogStatement\",\"UnusedFormalParameter\",\"LoggerIsNotStaticFinal\",\"AssignmentInOperand\",\"ImmutableField\",\"CompareObjectsWithEquals\",\"UnnecessaryConstructor\",\"CyclomaticComplexity\",\"UnusedPrivateMethod\",\"ConsecutiveLiteralAppends\",\"CallSuperInConstructor\",\"UnusedPrivateField\",\"AppendCharacterWithChar\",\"ExcessivePublicCount\",\"NPathComplexity\",\"ExcessiveImports\",\"AvoidDeeplyNestedIfStmts\",\"AccessorClassGeneration\",\"UncommentedEmptyConstructor\"],\"datasets\":[{\"data\":[1,1,13,2,9,4,8,2,4,1,2,3,13,3,3,4,18,1,1,2,3,4,1,5,1]}]}");
    }

    private String toJson(final Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    private IssuesService createService() {
        EntityService entityService = mock(EntityService.class);
        IssuesService issuesService = new IssuesService(entityService);
        IssuesTestData data = new IssuesTestData(entityService, issuesService);
        Report testData = data.createTestData();

        when(entityService.selectReportByToolIdAndOriginReportFile(anyString(), anyString())).thenReturn(Optional.of(testData));

        return new IssuesService(entityService);
    }

    @ParameterizedTest(name = "{index} => propertyName \"{0}\" should produce {1} labels")
    @MethodSource("expectedLabelCountPerPropertyName")
    void shouldReturnCorrectPropertyDistribution(final String propertyName, final int expectedLabels) {
        // Given
        final IssuesService service = createService();
        final Function<Issue, String> propertiesMapper = Issue.getPropertyValueGetter(propertyName);

        // When
        IssuePropertyDistribution distribution = service.getPropertyDistribution(DUMMY_TOOL, "", propertiesMapper);

        // Then
        assertThatJson(toJson(distribution)).inPath(LABELS_JSON_PATH).isArray().hasSize(expectedLabels);
    }

    static Stream<Arguments> expectedLabelCountPerPropertyName() {
        return Stream.of(Arguments.of(CATEGORY, 6), Arguments.of(TYPE, 25), Arguments.of(SEVERITY, 2),
                Arguments.of(FILE_NAME, 43));
    }
}

