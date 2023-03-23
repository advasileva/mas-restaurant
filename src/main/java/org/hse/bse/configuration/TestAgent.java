package org.hse.bse.configuration;

import java.lang.annotation.Annotation;

public class TestAgent implements JadeAgent{
    private static int counter = 0;
    private int currentNumber;
    private int health;

    public TestAgent() {
        currentNumber = ++counter;
        health = 100;
    }

    public int getHealth() {
        return health;
    }

    public void kick(TestAgent agent) {
        agent.health -= 5;
    }

    public String value() {
        return "lol";
    }

    public int number() {
        return currentNumber;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
