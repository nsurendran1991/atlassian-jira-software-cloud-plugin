package com.atlassian.jira.cloud.jenkins.deploymentinfo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.atlassian.jira.cloud.jenkins.common.model.IssueKey;
import com.atlassian.jira.cloud.jenkins.common.service.FreestyleIssueKeyExtractor;
import com.atlassian.jira.cloud.jenkins.util.IssueKeyStringExtractor;

import hudson.model.AbstractBuild;
import hudson.plugins.git.GitChangeSet;
import hudson.scm.ChangeLogSet;

public class FreestyleChangeLogIssueKeyExtractor implements FreestyleIssueKeyExtractor{
@Override
public Set<String> extractIssueKeys(final AbstractBuild<?, ?> freestyleBuild) {
final Set<IssueKey> allIssueKeys = new HashSet<>();
       final List<ChangeLogSet<? extends ChangeLogSet.Entry>> changeSets =
        freestyleBuild.getChangeSets();
       for (ChangeLogSet<? extends ChangeLogSet.Entry> changeSet : changeSets) {
           final Object[] changeSetEntries = changeSet.getItems();
           for (Object item : changeSetEntries) {
               final ChangeLogSet.Entry changeSetEntry = (ChangeLogSet.Entry) item;
               if (changeSetEntry instanceof GitChangeSet) {
                   allIssueKeys.addAll(
                           IssueKeyStringExtractor.extractIssueKeys(
                                   ((GitChangeSet) changeSetEntry).getComment()));
               }
               allIssueKeys.addAll(
                       IssueKeyStringExtractor.extractIssueKeys(changeSetEntry.getMsg()));
               if (allIssueKeys.size() >= ISSUE_KEY_MAX_LIMIT) {
                   break;
               }
           }
       }
       return allIssueKeys
               .stream()
               .limit(ISSUE_KEY_MAX_LIMIT)
               .map(IssueKey::toString)
               .collect(Collectors.toSet());
   }
}
