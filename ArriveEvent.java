import java.util.Optional;

public class ArriveEvent extends Event {
    private static final int EVENTORDER = 1;

    public ArriveEvent(Customer customer, double arrivalTime) {
        super(arrivalTime, customer);
    }

    @Override
    public int getEventOrder() {
        return EVENTORDER;
    }

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        double currentTime = this.getEventTime();

        Optional<Pair<Optional<Event>, Shop>> idleResult = shop.findIdleServer(
                getCustomer(), currentTime)
                .map(server ->
                        new Pair<>(Optional.of(new ServeEvent(currentTime,
                                getCustomer(), server)), shop)
                );

        return idleResult
                .or(() ->
                        shop.findServerWithWaitingSpace()
                                .flatMap(server -> {
                                    Shop updatedShop = shop.addCustToQueue(server,
                                            getCustomer());
                                    return updatedShop.findLatestServer(server)
                                            .map(updatedServer ->
                                                    new Pair<>(Optional.of(new WaitEvent(
                                                            currentTime,
                                                            getCustomer(),
                                                            updatedServer)),
                                                            updatedShop));
                                })
                )
                .or(() ->
                        Optional.of(new Pair<>(Optional.of(new LeaveEvent(
                                currentTime,
                                getCustomer())),
                                shop))
                );
    }

    @Override
    public String toString() {
        return String.format("%.3f customer %s arrives",
                getCustomer().getCustomerArrivalTime(),
                getCustomer().getCustomerId());
    }
}