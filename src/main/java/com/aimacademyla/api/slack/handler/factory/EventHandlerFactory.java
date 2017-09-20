package com.aimacademyla.api.slack.handler.factory;

import com.aimacademyla.api.slack.handler.EventHandler;

public interface EventHandlerFactory {
    EventHandler generateEventHandler();
}
