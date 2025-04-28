import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

public class Server {
    private final int id;
    private final double availableTime;
    private final List<Customer> waitingQueue;
    private final int qmax;

    public Server(int id, int qmax) {
        this.id = id;
        this.availableTime = 0.0;
        this.waitingQueue = List.of();
        this.qmax = qmax;
    }

    private Server(int id, double availableTime, List<Customer> waitingQueue, int qmax) {
        this.id = id;
        this.availableTime = availableTime;
        this.waitingQueue = waitingQueue;
        this.qmax = qmax;
    }

    public int getServerId() {
        return this.id;
    }

    public double getAvailableTime() {
        return this.availableTime;
    }

    public boolean isIdle(double currentTime) {
        return (this.availableTime <= currentTime) && this.waitingQueue.isEmpty();
    }

    // Check if there is space in the queue for the customer to join
    public boolean canQueue() {
        return waitingQueue.size() < qmax;
    }

    public boolean canServe(Customer customer) {
        if (availableTime <= customer.getCustomerArrivalTime()) {
            return true;
        } else {
            return false;
        }
    }

    public Server addToQueue(Customer customer) {
        // Create a new Server with updated state (e.g. updated waitingQueue)
        List<Customer> newQueue = Stream.concat(this.waitingQueue.stream(), Stream.of(customer))
                .toList();
        return new Server(id, availableTime, newQueue, qmax);
    }

    public Server serve(Customer customer, double serviceTime, double currentTime) {
        double nextAvailableTime = currentTime + serviceTime;
        return new Server(id, nextAvailableTime, waitingQueue, qmax);
    }

    public Pair<Optional<Customer>, Server> pollWaitingCustomer() {
        Optional<Customer> firstCustomer = waitingQueue.stream().findFirst();
        List<Customer> newQueue = waitingQueue.stream().skip(1).toList();
        Server updatedServer = new Server(id, availableTime, newQueue, qmax);
        return new Pair<>(firstCustomer, updatedServer);
    }

    public Server setIdle(double currentTime) {
        return new Server(id, currentTime, waitingQueue, qmax);
    }

    public String toString() {
        return "server " + this.id;
    }

    public List<Customer> getWaitingQueue() {
        return this.waitingQueue;
    }
}