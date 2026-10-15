// Java 17+, sem dependencias externas. Execute com -ea.
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Java17Examples {
    enum Status { ACTIVE, INACTIVE, UNKNOWN }
    record CustomerResponse(UUID id, String name, String email) {}
    record Basket(List<String> items) {
        Basket { items = List.copyOf(items); }
    }

    static String label(Status status) {
        return switch (status) {
            case ACTIVE -> "Active";
            case INACTIVE -> "Inactive";
            default -> "Unknown";
        };
    }

    static int stringLength(Object value) {
        if (value instanceof String text) return text.length();
        return 0;
    }

    static CustomerResponse requireCustomer(Optional<CustomerResponse> customer, UUID id) {
        return customer.orElseThrow(() -> new IllegalArgumentException("Missing customer " + id));
    }

    static String message(String name, int orders) {
        return "Customer %s has %d orders".formatted(name, orders);
    }

    static List<String> normalized(List<String> names) {
        return names.stream().map(String::strip).filter(s -> !s.isEmpty()).toList();
    }

    // Loop permite parar no primeiro defeito e guardar o indice.
    static int firstBlankIndex(List<String> names) {
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).isBlank()) return i;
        }
        return -1;
    }

    static final class TaxCalculator {
        BigDecimal calculate(BigDecimal net, BigDecimal rate) {
            // Regra ilustrativa explicitamente definida: duas casas, HALF_EVEN.
            return net.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    static final class InvoiceService {
        private final TaxCalculator taxes;
        InvoiceService(TaxCalculator taxes) { this.taxes = java.util.Objects.requireNonNull(taxes); }
        BigDecimal gross(BigDecimal net, BigDecimal rate) {
            return net.add(taxes.calculate(net, rate));
        }
    }

    static String sql() {
        return """
            SELECT id, name
            FROM customer
            WHERE id = ?
            """;
        // O chamador vincula id via PreparedStatement; nao interpola entrada.
    }

    public static void main(String[] args) {
        assert label(Status.ACTIVE).equals("Active");
        assert label(Status.UNKNOWN).equals("Unknown");
        assert stringLength("java") == 4;
        assert stringLength(null) == 0;
        var source = new ArrayList<>(List.of("one"));
        var basket = new Basket(source);
        source.add("two");
        assert basket.items().equals(List.of("one")) : "snapshot";
        boolean immutable = false;
        try { basket.items().add("three"); }
        catch (UnsupportedOperationException expected) { immutable = true; }
        assert immutable;
        var id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        var customer = new CustomerResponse(id, "Ana", "ana@example.test");
        assert requireCustomer(Optional.of(customer), id).equals(customer);
        boolean missing = false;
        try { requireCustomer(Optional.empty(), id); }
        catch (IllegalArgumentException expected) { missing = true; }
        assert missing;
        assert normalized(List.of(" Ana ", " ", "Rui")).equals(List.of("Ana", "Rui"));
        assert firstBlankIndex(List.of("a", " ", "b")) == 1;
        assert firstBlankIndex(List.of("a")) == -1;
        assert message("Ana", 2).equals("Customer Ana has 2 orders");
        assert sql().contains("WHERE id = ?");
        var service = new InvoiceService(new TaxCalculator());
        assert service.gross(new BigDecimal("100.00"), new BigDecimal("0.23"))
            .compareTo(new BigDecimal("123.00")) == 0;
        Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        assert Instant.now(clock).equals(Instant.parse("2026-01-01T00:00:00Z"));
        System.out.println("Java17Examples OK");
    }
}
