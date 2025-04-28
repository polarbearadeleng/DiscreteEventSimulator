public class Customer implements Comparable<Customer> {
    private final int id;
    private final double arrivalTime;

    public Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
    }

    public int getCustomerId() {
        return this.id;
    }

    public double getCustomerArrivalTime() {
        return this.arrivalTime;
    }

    @Override
    public int compareTo(Customer other) {
        if (this.arrivalTime != other.arrivalTime) {
            return Double.compare(this.arrivalTime, other.arrivalTime);
        } else {
            if (this.id < other.id) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public String toString() {
        return String.format("%.3f customer %d arrives", arrivalTime, id);
    }
}
