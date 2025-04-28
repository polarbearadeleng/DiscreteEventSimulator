import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Simulator {
    private final int numOfServers;
    private final int numOfCustomers;
    private final int qmax;
    private final Supplier<Double> serviceTime;
    // Each arrival is a Pair: (customerId, arrivalTime)
    private final List<Pair<Integer, Double>> arrivals;

    public Simulator(int numOfServers, int qmax, Supplier<Double> serviceTime,
                     int numOfCustomers, List<Pair<Integer, Double>> arrivals) {
        this.numOfServers = numOfServers;
        this.qmax = qmax;
        this.serviceTime = serviceTime;
        this.numOfCustomers = numOfCustomers;
        this.arrivals = arrivals;
    }

    public Pair<String, String> run() {
        // Build initial events from the input arrivals.
        List<ArriveEvent> initEvents = arrivals.stream()
                .map(pair -> {
                    int customerId = pair.t();
                    double arrivalTime = pair.u();
                    Customer customer = new Customer(customerId, arrivalTime);
                    return new ArriveEvent(customer, arrivalTime);
                })
                .toList();

        // initialPQ holds the events sorted by time (and event order)
        PQ<Event> initialPQ = new PQ<>(initEvents);

        Shop shop = new Shop(numOfServers, serviceTime, qmax);

        // Create initial state.
        State state = new State(initialPQ, shop).init();

        String output = "";
        double totalWaiting = 0.0;
        int servedCount = 0;
        int leftCount = 0;

        while (state.get().isPresent()) {
            Event event = state.get().get();
            output += event + "\n";
            totalWaiting += event.getWaitingTimeContribution();
            servedCount += event.getServedCountContribution();
            leftCount += event.getLeftCountContribution();

            Optional<State> nextStateOpt = state.next();
            if (nextStateOpt.isEmpty()) {
                break;
            }
            state = nextStateOpt.get();
        }

        double avgWaiting = (servedCount == 0) ? 0.0 : totalWaiting / servedCount;
        String stats = String.format("[%.3f %d %d]", avgWaiting, servedCount, leftCount);
        return new Pair<>(output.trim(), stats);
    }
}

