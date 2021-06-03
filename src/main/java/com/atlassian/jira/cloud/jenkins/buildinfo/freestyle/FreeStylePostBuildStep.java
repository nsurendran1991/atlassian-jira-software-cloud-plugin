package com.atlassian.jira.cloud.jenkins.buildinfo.freestyle;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.cloud.jenkins.buildinfo.pipeline.JiraSendBuildInfoStep;
import com.atlassian.jira.cloud.jenkins.buildinfo.service.FreestyleBuildInfo;
import com.atlassian.jira.cloud.jenkins.buildinfo.service.JiraBuildInfoRequest;
import com.atlassian.jira.cloud.jenkins.common.factory.JiraSenderFactory;
import com.atlassian.jira.cloud.jenkins.common.response.JiraSendInfoResponse;
import com.atlassian.jira.cloud.jenkins.config.JiraCloudPluginConfig;
import com.atlassian.jira.cloud.jenkins.config.JiraCloudSiteConfig;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.ListBoxModel;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

public class FreeStylePostBuildStep extends Recorder implements Serializable, SimpleBuildStep {

    /** */
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(FreeStylePostBuildStep.class);

    private String site;
    private String branch;

    @DataBoundConstructor
    public FreeStylePostBuildStep() {
        // Empty constructor

    }

    @Nullable
    public String getSite() {
        return site;
    }

    @Nullable
    public String getBranch() {
        return branch;
    }

    @DataBoundSetter
    public void setSite(final String site) {
        this.site = site;
    }

    @DataBoundSetter
    public void setBranch(final String branch) {
        this.branch = branch;
    }

    // @Override
    // public boolean perform(AbstractBuild build, Launcher launcher, BuildListener
    // listener)
    // throws InterruptedException, IOException {
    //
    //
    // }

    @Override
    public boolean perform(
            final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener)
            throws InterruptedException, IOException {
        log.info("Reached Perform");
        final JiraBuildInfoRequest request =
                new FreestyleBuildInfo(this.getSite(), this.branch, build);
        final JiraSendInfoResponse response =
                JiraSenderFactory.getInstance().getJiraBuildInfoSender().sendBuildInfo(request);

        log.info("Reached Perform" + response);
        return super.perform(build, launcher, listener);
    }

    @Override
    public void perform(
            @Nonnull final Run<?, ?> run,
            @Nonnull final FilePath filePath,
            @Nonnull final Launcher launcher,
            @Nonnull final TaskListener taskListener)
            throws InterruptedException, IOException {

    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Inject private transient JiraCloudPluginConfig globalConfig;

        @Override
        public String getDisplayName() {
            return "Send build information to Jira";
        }

        @Override
        public boolean isApplicable(final Class aClass) {
            return true;
        }

        @Override
        public boolean configure(final StaplerRequest req, final JSONObject json)
                throws FormException {
            return super.configure(req, json);
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillSiteItems() {
            ListBoxModel items = new ListBoxModel();
            final List<JiraCloudSiteConfig> siteList = globalConfig.getSites();
            for (JiraCloudSiteConfig siteConfig : siteList) {
                items.add(siteConfig.getSite(), siteConfig.getSite());
            }

            return items;
        }
    }
}
