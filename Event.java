import java.util.Optional;

public abstract class Event implements Comparable<Event> {
    private final double eventTime;
    private final Customer customer;

    public Event(double eventTime, Customer customer) {
        this.eventTime = eventTime;
        this.customer = customer;
    }

    public double getEventTime() {
        return this.eventTime;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public double getWaitingTimeContribution() {
        return 0.0;
    }

    public int getServedCountContribution() {
        return 0;
    }

    public int getLeftCountContribution() {
        return 0;
    }

    public abstract int getEventOrder();

    public abstract Optional<Pair<Optional<Event>, Shop>> next(Shop shop);

    @Override
    public int compareTo(Event other) {
        int cmp = Double.compare(this.eventTime, other.eventTime);
        if (cmp != 0) {
            return cmp;
        } else {
            cmp = Double.compare(this.getCustomer()
                    .getCustomerArrivalTime(), other.getCustomer().getCustomerArrivalTime());
            if (cmp != 0) {
                return cmp;
            } else {
                return Integer.compare(this.getEventOrder(), other.getEventOrder());
            }
        }
    }
}
