import java.util.Optional;

public class WaitEvent extends Event {
    private final Server server;
    private final boolean printed;
    private final Optional<Customer> waitingQueue;
    private static final int EVENTORDER = 1;

    public WaitEvent(double eventTime, Customer customer, Server server) {
        super(eventTime, customer);
        this.server = server;
        this.printed = false;
        this.waitingQueue = Optional.empty();
    }

    // Overloaded constructor for rescheduling the wait event.
    public WaitEvent(double eventTime, Customer customer, Server server,
                     boolean printed) {
        super(eventTime, customer);
        this.server = server;
        this.printed = printed;
        this.waitingQueue = Optional.of(customer);
    }

    public boolean isIdleWait(double currentTime) {
        return this.server.getAvailableTime() <= currentTime;
    }

    @Override
    public int getEventOrder() {
        return EVENTORDER;
    }

    /*
    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        double currentTime = this.getEventTime();
        for (Server s : shop.getServers()) {
            if (s.getServerId() == this.server.getServerId()) {
                if (s.getAvailableTime() <= currentTime) {
                    Pair<Optional<Customer>, Server> result = s.pollWaitingCustomer();
                    Server updatedServer = result.u();
                    return Optional.of(new Pair<>(Optional.of(new ServeEvent(
                            currentTime,
                            getCustomer(),
                            updatedServer)),
                            shop));
                } else {
                    double newTime = s.getAvailableTime();
                    WaitEvent rescheduled = new WaitEvent(newTime, getCustomer(), s,
                            true);
                    return Optional.of(new Pair<>(Optional.of(rescheduled), shop));
                }
            }
        }
        return Optional.of(new Pair<>(Optional.empty(), shop));
    }
    */

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        double currentTime = this.getEventTime();
        return shop.findLatestServer(this.server)
                .flatMap(s -> {
                    if (s.getAvailableTime() <= currentTime) {
                        Pair<Optional<Customer>, Server> result = s.pollWaitingCustomer();
                        Server updatedServer = result.u();
                        Shop updatedShop = shop.update(updatedServer);
                        return Optional.of(new Pair<>(
                                Optional.of(new ServeEvent(currentTime,
                                        getCustomer(),
                                        updatedServer)),
                                updatedShop));
                    } else {
                        double newTime = s.getAvailableTime();
                        WaitEvent rescheduled = new WaitEvent(
                                newTime, getCustomer(), s, true);
                        return Optional.of(new Pair<>(Optional.of(rescheduled), shop));
                    }
                });
    }

    @Override
    public String toString() {
        return (!printed)
                ? String.format("%.3f customer %d waits at server %d",
                getEventTime(),
                getCustomer().getCustomerId(),
                this.server.getServerId())
                : "";
    }
}