package ua.com.alevel.reactive.core;

import ua.com.alevel.config.LoaderPage;

import java.util.function.Consumer;

public class PubSub {

    private static final PubSub pubSubSingleton = new PubSub();
    private Consumer<LoaderPage> publisher;

    private PubSub() { }

    public static PubSub getInstance() {
        return pubSubSingleton;
    }

    public void publish(LoaderPage page) {
        publisher.accept(page);
    }

    public void subscribe(Consumer<LoaderPage> consumer) {
        this.publisher = consumer;
    }
}
