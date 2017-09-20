package com.aimacademyla.api.slack.handler.impl;

import com.aimacademyla.api.slack.handler.EventHandler;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

public abstract class AbstractEventHandler implements EventHandler {

    @Override
    public void handleEvent(SlackMessagePosted event, SlackSession slackSession) {

    }

    public abstract void parseEvent(SlackMessagePosted event);
}
