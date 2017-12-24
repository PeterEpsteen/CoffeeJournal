package com.example.peter.coffeejournal;

/**
 * Created by peter on 11/24/17.
 */

public class Bean {

    String beanName;
    int beanWeight;

    public Bean() {
        beanName = "";
        beanWeight = 0;
    }

    public Bean(String name, int weight) {
        beanWeight = weight;
        beanName = name;
    }

    public int getBeanWeight() {
        return beanWeight;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setBeanWeight(int beanWeight) {
        this.beanWeight = beanWeight;
    }
}
