package com.atlassian.jira.cloud.jenkins.buildinfo.service;

import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import hudson.model.AbstractBuild;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public class JiraBuildInfoRequest {

    private final String site;
    private final String branch;
    private final WorkflowRun build;
    private final AbstractBuild<?, ?> freestyleBuild;

    public JiraBuildInfoRequest(
            @Nullable final String site, @Nullable final String branch, final WorkflowRun build) {
        this.site = site;
        this.branch = branch;
        this.build = requireNonNull(build);
        this.freestyleBuild = null;
    }

    public JiraBuildInfoRequest(
            @Nullable final String site,
            @Nullable final String branch,
            final AbstractBuild<?, ?> freestyleBuild) {
        this.site = site;
        this.branch = branch;
        this.freestyleBuild = requireNonNull(freestyleBuild);
        this.build = null;
    }

    @Nullable
    public String getSite() {
        return site;
    }

    @Nullable
    public String getBranch() {
        return branch;
    }

    public WorkflowRun getBuild() {
        return build;
    }

    public AbstractBuild<?, ?> getFreestyleBuild() {
        return freestyleBuild;
    }
}
