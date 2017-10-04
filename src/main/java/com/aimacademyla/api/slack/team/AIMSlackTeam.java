package com.aimacademyla.api.slack.team;

import com.ullink.slack.simpleslackapi.SlackTeam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AIMSlackTeam implements SlackTeam{

    @Value("xoxb-74759316407-mMLHsNG3ltLuvyVd3FRflWRD")
    private String id;

    private String name;

    private String domain;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
