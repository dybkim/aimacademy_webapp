package com.aimacademyla.api.slack.thread;


import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackTeam;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackServiceThread implements Runnable{

    private SlackSession session;
    private SlackTeam slackTeam;
    private SlackMessagePostedListener messagePostedListener;

    private static final Logger logger = LogManager.getLogger(SlackServiceThread.class);

    @Override
    public void run(){
        session = SlackSessionFactory.createWebSocketSlackSession(slackTeam.getId());
        messagePostedListener = this::handleMessageEvent;
        session.addMessagePostedListener(messagePostedListener);

        try{
            session.connect();
        }catch(IOException e){
            e.printStackTrace();
            logger.error("Slack session failed to connect!");
        }
    }

    private void handleMessageEvent(SlackMessagePosted event, SlackSession session){
//        MessageEventHandler messageEventHandler = MessageEventHandlerFactory.generateEventHandler(event);
//        messageEventHandler.handleEvent(event, session);
    }

    public void setSlackTeam(SlackTeam slackTeam){
        this.slackTeam = slackTeam;
    }

    public SlackTeam getSlackTeam(){
        return slackTeam;
    }

    public SlackSession getSession() {
        return session;
    }

    public void setSession(SlackSession session) {
        this.session = session;
    }

    public SlackMessagePostedListener getMessagePostedListener() {
        return messagePostedListener;
    }

    public void setMessagePostedListener(SlackMessagePostedListener messagePostedListener) {
        this.messagePostedListener = messagePostedListener;
    }
}


