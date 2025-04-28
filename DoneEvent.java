import java.util.Optional;

public class DoneEvent extends Event {
    private final Server server;
    private static final int EVENTORDER = 3;

    public DoneEvent(double eventTime, Customer customer, Server
            server) {
        super(eventTime, customer);
        this.server = server;
    }

    public int getEventOrder() {
        return EVENTORDER;
    }

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        // Shop updatedShop = shop.update(server);
        return Optional.of(new Pair<>(Optional.<Event>empty(),
                shop));
    }

    @Override
    public String toString() {
        return String.format("%.3f customer %d done",
                getEventTime(),
                getCustomer().getCustomerId());
    }
}