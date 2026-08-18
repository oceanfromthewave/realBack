package practice.phase18;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import practice.phase18.config.AppConfig;
import practice.phase18.domain.Reservation;
import practice.phase18.usecase.ReserveStockUseCase;

import java.time.Duration;

public class WiringDemo {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            ReserveStockUseCase useCase = ctx.getBean(ReserveStockUseCase.class);

            Reservation created = useCase.reserve("product-1", 3, Duration.ofMinutes(10));
            System.out.println("created: id=" + created.getId() + ", status=" + created.getStatus());

            System.out.println("adapter bean: " + ctx.getBean(
                    practice.phase18.port.ReservationRepository.class).getClass().getName());
        }
    }
}
