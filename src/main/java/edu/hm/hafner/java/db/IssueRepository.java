package edu.hm.hafner.java.db;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to access {@link IssueEntity issue entities}.
 *
 * @author Michael Schmid
 */
public interface IssueRepository extends JpaRepository<IssueEntity, Integer> {
}
