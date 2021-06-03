package com.atlassian.jira.cloud.jenkins.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.cloud.jenkins.common.model.IssueKey;
import com.atlassian.jira.cloud.jenkins.common.service.FreestyleIssueKeyExtractor;

import hudson.model.AbstractBuild;
import hudson.plugins.git.BranchSpec;
import hudson.plugins.git.GitSCM;
import hudson.scm.SCM;

public class FreestyleBranchNameIssueKeyExtractor implements FreestyleIssueKeyExtractor {

private static final Logger logger = LoggerFactory.getLogger(FreestyleBranchNameIssueKeyExtractor.class);

@Override
public Set<String> extractIssueKeys(final AbstractBuild<?, ?> freestyleBuild) {
// The action is only injected for Freestyle jobs
// The action is not injected for Pipeline (single branch) jobs
// final SCMRevisionAction scmAction =
// freestyleBuild.getAction(SCMRevisionAction.class);
SCM scmList = freestyleBuild.getProject().getScm();
GitSCM scm1 = (GitSCM) scmList;
List<BranchSpec> branches = scm1.getBranches();
Set<String> sets = IssueKeyStringExtractor.extractIssueKeys(branches.get(0).getName()).stream()
.map(IssueKey::toString).collect(Collectors.toSet());
/*
 * * if (scmAction == null) { logger.debug("SCMRevisionAction is null"); return
 * Collections.emptySet(); }
 * 
 * final SCMRevision revision = scmAction.getRevision(); final ScmRevision
 * scmRevision = new ScmRevision(revision.getHead().getName());
 * 
 * return extractIssueKeys(scmRevision);
 */
return sets;
}
/*
 * private Set<String> extractIssueKeys(final ScmRevision scmRevision) { return
 * IssueKeyStringExtractor.extractIssueKeys(scmRevision.getHead()) .stream()
 * .map(IssueKey::toString) .collect(Collectors.toSet()); }
 */
}
