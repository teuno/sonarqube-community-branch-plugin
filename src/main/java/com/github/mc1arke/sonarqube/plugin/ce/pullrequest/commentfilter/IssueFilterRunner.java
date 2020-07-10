package com.github.mc1arke.sonarqube.plugin.ce.pullrequest.commentfilter;

import com.github.mc1arke.sonarqube.plugin.ce.pullrequest.PostAnalysisIssueVisitor;
import com.github.mc1arke.sonarqube.plugin.ce.pullrequest.commentfilter.SeverityComparator;
import com.github.mc1arke.sonarqube.plugin.ce.pullrequest.commentfilter.TypeComparator;
import org.sonar.api.rule.Severity;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IssueFilterRunner {

    private List<Predicate<PostAnalysisIssueVisitor.ComponentIssue>> filters;
    private Integer maxAmountOfIssues;

    public IssueFilterRunner(List<Predicate<PostAnalysisIssueVisitor.ComponentIssue>> filters) {
        this.filters = filters;
    }

    public IssueFilterRunner(List<Predicate<PostAnalysisIssueVisitor.ComponentIssue>> filters, Integer maxAmountOfIssues) {
        this.filters = filters;
        this.maxAmountOfIssues = maxAmountOfIssues;
    }

    public List<PostAnalysisIssueVisitor.ComponentIssue> filterIssues(List<PostAnalysisIssueVisitor.ComponentIssue> issues) {
        Stream<PostAnalysisIssueVisitor.ComponentIssue> stream = issues.stream()
                .filter(filters.stream().reduce(issue -> true, Predicate::and))
                .sorted(new SeverityComparator().thenComparing(new TypeComparator()));

        if (maxAmountOfIssues != null && maxAmountOfIssues > 0) stream = stream.limit(maxAmountOfIssues);

        return stream.collect(Collectors.toUnmodifiableList());
    }

    public List<Predicate<PostAnalysisIssueVisitor.ComponentIssue>> getFilters() {
        return filters;
    }

    public Integer getMaxAmountOfIssues() {
        return maxAmountOfIssues;
    }
}
