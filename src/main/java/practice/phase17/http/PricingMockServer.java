package practice.phase17.http;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.IOException;

/**
 * HTTP Client(RestClient/WebClient) 실습용 Mock 외부 API 서버 (실험/테스트 인프라, 직접 타이핑 대상 아님).
 * GET /price/{productId}?delayMs=N  -> N ms 슬립 후 {"productId":"X","price":N} 리턴
 * GET /down/{productId}             -> 항상 500 (Circuit Breaker 실습용)
 */
public class PricingMockServer {

    private final Tomcat tomcat = new Tomcat();

    public void start(int port) throws Exception {
        tomcat.setPort(port);
        tomcat.getConnector();
        Context context = tomcat.addContext("", null);
        Tomcat.addServlet(context, "priceServlet", new PriceServlet());
        context.addServletMappingDecoded("/price/*", "priceServlet");
        Tomcat.addServlet(context, "downServlet", new DownServlet());
        context.addServletMappingDecoded("/down/*", "downServlet");
        tomcat.start();
    }

    public void stop() throws Exception {
        tomcat.stop();
    }

    static class PriceServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String productId = req.getPathInfo() == null ? "unknown" : req.getPathInfo().substring(1);
            long delayMs = req.getParameter("delayMs") == null ? 0 : Long.parseLong(req.getParameter("delayMs"));
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            int price = Math.abs(productId.hashCode() % 1000) + 1000;
            resp.setContentType("application/json");
            resp.getWriter().write("{\"productId\":\"" + productId + "\",\"price\":" + price + "}");
        }
    }

    static class DownServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "pricing service down");
        }
    }

    public static void main(String[] args) throws Exception {
        PricingMockServer server = new PricingMockServer();
        server.start(8090);
        System.out.println("PricingMockServer started on :8090 (Ctrl+C to stop)");
        Thread.currentThread().join();
    }
}
