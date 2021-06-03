package com.atlassian.jira.cloud.jenkins.buildinfo.service;

import hudson.model.AbstractBuild;

public class FreestyleBuildInfo extends JiraBuildInfoRequest {
public FreestyleBuildInfo(final String site, final String branch, final AbstractBuild<?, ?> freestyleBuild) {
super(site, branch, freestyleBuild);

}

}
