import java.util.Optional;

class LeaveEvent extends Event {
    private static final int EVENTORDER = 3;

    public LeaveEvent(double eventTime, Customer customer) {
        super(eventTime, customer);
    }

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        return Optional.of(new Pair<>(Optional.empty(), shop));
    }

    @Override
    public double getWaitingTimeContribution() {
        return 0.0;
    }

    @Override
    public int getServedCountContribution() {
        return 0;
    }

    @Override
    public int getLeftCountContribution() {
        return 1;
    }

    public int getEventOrder() {
        return EVENTORDER;
    }

    @Override
    public String toString() {
        return String.format("%.3f customer %d leaves", super
                .getEventTime(), super.getCustomer().getCustomerId());
    }
}