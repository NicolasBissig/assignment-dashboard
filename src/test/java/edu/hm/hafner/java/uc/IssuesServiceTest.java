package edu.hm.hafner.java.uc;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.java.db.EntityService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link IssuesService}.
 *
 * @author Ullrich Hafner
 */
class IssuesServiceTest {
    @Test
    void shouldCreateCategoryDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByCategory("dummy-id", "");

        // Then
        assertThat(toJson(distribution)).isEqualTo("{\"labels\":[\"Design\",\"Documentation\",\"Best Practices\",\"Performance\",\"Code Style\",\"Error Prone\"],\"datasets\":[{\"data\":[15,3,20,6,53,12]}]}");
    }

    @Test
    void shouldCreateTypeDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByType("dummy-id", "");

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
}

