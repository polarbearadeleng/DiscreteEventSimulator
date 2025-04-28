import java.util.List;
import java.util.Optional;

class State {
    private final Shop shop;
    private final Optional<Event> lastProcessedEvent;
    private final PQ<Event> events;
    private final String log;
    private static final int TEN = 10;
    private static final int TWO = 2;

    public Optional<String> computeLog(Optional<Event> lastEvent) {
        return lastEvent
                .map(event -> List.of(event).toString())
                .or(() -> Optional.of(List.of().toString()));
    }

    public State(PQ<Event> events, Shop shop) {
        if (events.isEmpty()) {
            Pair<Optional<Event>, PQ<Event>> pollPair = events.poll();
            this.events = pollPair.u();
            this.lastProcessedEvent = pollPair.t();
            this.shop = shop;
            this.log = String.valueOf(computeLog(lastProcessedEvent));
        } else {
            this.events = events;
            this.shop = shop;
            this.lastProcessedEvent = Optional.empty();
            this.log = "";
        }
    }

    private State(PQ<Event> events, Shop shop, Optional<Event> lastEvent) {
        this.events = events;
        this.shop = shop;
        this.lastProcessedEvent = lastEvent;
        this.log = String.valueOf(computeLog(lastEvent));
    }

    public State init() {
        if (events.isEmpty()) {
            return this;
        }
        // Poll from the events queue once.
        Pair<Optional<Event>, PQ<Event>> pollPair = events.poll();
        Optional<Event> optEvent = pollPair.t();

        return new State(pollPair.u(), shop, optEvent);
    }

    public Optional<State> next() {
        return lastProcessedEvent
                .flatMap(event ->
                        event.next(shop)
                                .flatMap(nextResult -> {
                                    Shop updatedShop = nextResult.u();
                                    Optional<Event> newEventOpt = nextResult.t();

                                    Optional<PQ<Event>> finalQueueOpt = newEventOpt
                                            .map(newEvent -> events.add(newEvent))
                                            .or(() -> Optional.of(events));

                                    return finalQueueOpt.flatMap(finalQueue -> {
                                        Pair<Optional<Event>, PQ<Event>> nextPair =
                                                finalQueue.poll();
                                        return nextPair.t().flatMap(e ->
                                                Optional.of(new State(nextPair.u(),
                                                        updatedShop, Optional.of(e)))
                                        );
                                    });
                                })
                )
                .or(() -> {
                    if (events.isEmpty()) {
                        return Optional.empty();
                    }
                    Pair<Optional<Event>, PQ<Event>> initPair = events.poll();
                    return initPair.t().flatMap(e ->
                            Optional.of(new State(initPair.u(), shop, Optional.of(e)))
                    );
                });
    }

    public Optional<Event> get() {
        return this.lastProcessedEvent;
    }

    public String toString() {
        if (this.log.length() != 0) {
            return this.log.substring(TEN, this.log.length() - TWO);
        } else {
            return "";
        }
    }
}