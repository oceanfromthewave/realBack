package practice.phase1;

public class Main {
    public static void main(String[] args) throws Exception {
        Notifier real = new EmailNotifier();
        Notifier logged = new LoggingNotifier(real);
        OrderService service = new OrderService(logged);
        service.placeOrder("Book");

        SimpleContainer container = new SimpleContainer();
        Object created = container.create(EmailNotifier.class);
        System.out.println("container created: " + created);

        try {
            container.create(OrderService.class);
        } catch (Exception e) {
            System.out.println("expected failure: " + e);
        }
    }
}
