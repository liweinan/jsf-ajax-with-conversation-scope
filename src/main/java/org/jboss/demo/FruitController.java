package org.jboss.demo;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 01 25 2013
 *
 * @author <a href="mailto:l.weinan@gmail.com">Weinan Li</a>
 */
@Named("fruitRepo")
@ConversationScoped
public class FruitController implements Serializable {

    private String fruit;

    private String cid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }

    private List<String> fruits = new ArrayList<String>();

    public List<String> getFruits() {
        return fruits;
    }

    public void setFruits(List<String> fruits) {
        this.fruits = fruits;
    }

    @Inject
    private Conversation conversation;

    boolean conversationStarted = false;

    public synchronized void add() {
        System.out.println("CONVERSATION-STARTED: " + conversationStarted);
        if (!conversationStarted) {
            conversation.begin();
            conversationStarted = true;
            cid = conversation.getId();
            System.out.println("CONVERSATION-ID: " + cid);
        }

        fruits.add(fruit);
        fruit = null;
    }

    public void clear() {
        synchronized (this) {
            if (conversationStarted) {
                conversationStarted = false;
                conversation.end();
                cid = null;
                fruits = new ArrayList<String>();
                System.out.println("CONVERSATION-END");
            }
        }
    }
}
