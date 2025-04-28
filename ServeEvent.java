import java.util.Optional;

public class ServeEvent extends Event {
    private final Server server;
    private static final int EVENTORDER = 2;

    public ServeEvent(double eventTime, Customer customer, Server server) {
        super(eventTime, customer);
        this.server = server;
    }

    @Override
    public double getWaitingTimeContribution() {
        return getEventTime() - getCustomer().getCustomerArrivalTime();
    }

    @Override
    public int getServedCountContribution() {
        return 1;
    }

    @Override
    public int getLeftCountContribution() {
        return 0;
    }

    /*
    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        return shop.getServers().stream()
                .filter(s -> s.getServerId() == this.server.getServerId())
                .findFirst()
                .map(updatedServer -> {
                    double serviceTime = shop.generateServiceTime();
                    double doneTime = this.getEventTime() + serviceTime;
                    Server uServer = updatedServer.serve(getCustomer(),
                            this.getEventTime(), serviceTime);
                    DoneEvent doneEvent = new DoneEvent(doneTime, getCustomer(), uServer);
                    Shop updatedShop = shop.update(uServer);
                    return new Pair<>(Optional.<Event>of(doneEvent), updatedShop);
                })
                .or(() -> Optional.of(new Pair<>(Optional.empty(), shop)));
    }
    */

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        return shop.findLatestServer(this.server)
                .map(updatedServer -> {
                    double serviceTime = shop.generateServiceTime();
                    double doneTime = this.getEventTime() + serviceTime;
              
                    Server uServer = updatedServer
                            .serve(getCustomer(), this.getEventTime(), serviceTime);
                    DoneEvent doneEvent = new DoneEvent(
                            doneTime, getCustomer(), uServer);
                    Shop updatedShop = shop.update(uServer);
                    return new Pair<>(Optional.<Event>of(doneEvent), updatedShop);
                })
                .or(() -> Optional.of(new Pair<>(Optional.empty(), shop)));
    }

    /*
    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        double startTime = this.getEventTime();
        double serviceTime = shop.generateServiceTime();
        double doneTime = startTime + serviceTime;
        Server updatedServer = server.serve(getCustomer(), serviceTime, startTime);
        Shop updatedShop = shop.update(updatedServer);
        DoneEvent doneEvent = new DoneEvent(doneTime, getCustomer(), updatedServer);
        return Optional.of(new Pair<>(Optional.of(doneEvent), updatedShop));
    }
    */

    @Override
    public int getEventOrder() {
        return EVENTORDER;
    }

    @Override
    public String toString() {
        return String.format("%.3f customer %d serves by server %d",
                getEventTime(),
                getCustomer().getCustomerId(),
                server.getServerId());
    }
}