import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.function.Supplier;

public class Shop {
    private final List<Server> servers;
    private final Supplier<Double> serviceTimeSupplier;
    /* Supplier passed to the shop so that everytime a server
     * in the shop serves a customer, the service time will
     * be generated.
     */

    private static List<Server> createServerList(int numOfServers, int qmax) {
        return (numOfServers != 0) ?
            IntStream.rangeClosed(1, numOfServers)
                    .mapToObj(id -> new Server(id, qmax))
                    .toList()
                : List.of();
    }

    /* Public constructor, takes in int numOfServers, Supplier<Double>
     * serviceTimeSupplier and int qmax. qmax is the limit of the number
     * of customers that can be in the queue.
     */
    public Shop(int numOfServers, Supplier<Double> serviceTimeSupplier, int qmax) {
        this.servers = createServerList(numOfServers, qmax);
        this.serviceTimeSupplier = serviceTimeSupplier;
        /* For the number of servers there are, map each server id to
         * a new Server instance, taking in their id and the maximum
         * number of customers they can accept in their queue.
         */
    }

    /* Private constructor, takes in List<Server> servers, and Supplier
     * <Double> serviceTimeSupplier.
     */
    private Shop(List<Server> servers, Supplier<Double> serviceTimeSupplier) {
        this.servers = servers;
        this.serviceTimeSupplier = serviceTimeSupplier;
    }

    public List<Server> getServers() {
        return servers;
    }

    /* public boolean isThereServer(Customer customer) {
        return servers.stream()
                .filter(server -> server.canServe(customer))
                .count() > 0;
    }
     */

    public Optional<Server> findIdleServer(Customer customer, double currentTime) {
        return servers.stream()
                .filter(server -> server.isIdle(currentTime))
                .findFirst();
    }

    public Optional<Server> findServerWithWaitingSpace() {
        return servers.stream()
                .filter(s -> s.canQueue())
                .findFirst();
    }

    public Shop addCustToQueue(Server server, Customer customer) {
        List<Server> updatedServerList = servers.stream()
                .map(s -> s.getServerId() == server.getServerId() ? s.addToQueue(customer) : s)
                .toList();
        return new Shop(updatedServerList, serviceTimeSupplier);
    }

    public Shop update(Server server) {
        List<Server> updatedServerList = servers.stream()
                .map(s -> s.getServerId() == server.getServerId() ? server : s)
                .toList();
        return new Shop(updatedServerList, serviceTimeSupplier);
    }

    public Optional<Server> findLatestServer(Server server) {
        return servers.stream()
                .filter(s -> s.getServerId() == server.getServerId())
                .findFirst();
    }


    public double generateServiceTime() {
        return serviceTimeSupplier.get();
    }

    public String toString() {
        return getServers().toString();
    }
}