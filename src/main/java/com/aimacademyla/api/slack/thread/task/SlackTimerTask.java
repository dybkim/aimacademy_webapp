package com.aimacademyla.api.slack.thread.task;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;

import java.util.TimerTask;

public abstract class SlackTimerTask extends TimerTask{

    protected SlackSession slackSession;
    protected SlackChannel slackChannel;

    SlackTimerTask(SlackSession slackSession, SlackChannel slackChannel){
        this.slackSession = slackSession;
        this.slackChannel = slackChannel;
    }

    public void setSlackSession(SlackSession slackSession) {
        this.slackSession = slackSession;
    }

    public void setSlackChannel(SlackChannel slackChannel) {
        this.slackChannel = slackChannel;
    }
}
