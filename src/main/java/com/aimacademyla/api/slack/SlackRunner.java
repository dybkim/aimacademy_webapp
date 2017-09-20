package com.aimacademyla.api.slack;

import com.aimacademyla.api.slack.team.AIMSlackTeam;
import com.aimacademyla.api.slack.thread.SlackServiceThread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SlackRunner {

    public static void main(String[] args){
        SlackServiceThread slackServiceThread = new SlackServiceThread();

        AIMSlackTeam aimSlackTeam = new AIMSlackTeam();
        aimSlackTeam.setId("xoxb-74759316407-mMLHsNG3ltLuvyVd3FRflWRD");

        slackServiceThread.setSlackTeam(aimSlackTeam);

        Executor executor = Executors.newCachedThreadPool();
        executor.execute(slackServiceThread);
    }
}
