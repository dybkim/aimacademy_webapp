package com.aimacademyla.api.slack.handler;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

public interface EventHandler {
    void handleEvent(SlackMessagePosted event, SlackSession slackSession);
}
