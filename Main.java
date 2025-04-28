import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.function.Supplier;

class Main {
    private static Pair<Integer, Double> split(String line) {
        List<String> token = Stream.of(line.split(" ")).toList();
        return new Pair<Integer,Double>(Integer.parseInt(token.get(0)),
                Double.parseDouble(token.get(1)));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numOfServers = sc.nextInt();
        int qmax = sc.nextInt();
        int numOfCustomers = sc.nextInt();
        Supplier<Double> serviceTime = new DefaultServiceTime();

        sc.nextLine(); // removes trailing newline
        List<Pair<Integer,Double>> arrivals = sc.useDelimiter("\n")
                .tokens()
                .map(line -> split(line))
                .toList();

        Pair<String,String> output = new Simulator(numOfServers, qmax, serviceTime,
                numOfCustomers, arrivals).run();
        System.out.println(output.t());
        System.out.println(output.u());
    }
}