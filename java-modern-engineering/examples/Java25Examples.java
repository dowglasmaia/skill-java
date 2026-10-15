// Java 25, recursos finalizados. Arquivo compacto e instance main.
import module java.base;

record Point(int x, int y) {}
private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

static class Base {
    final int value;
    Base(int value) { this.value = value; }
}
static final class Positive extends Base {
    Positive(int value) {
        if (value <= 0) throw new IllegalArgumentException("value must be positive");
        super(value);
    }
}
static boolean isInteger(String input) {
    try { Integer.parseInt(input); return true; }
    catch (NumberFormatException _) { return false; }
}
void main() {
    assert isInteger("12");
    assert !isInteger("x");
    Object point = new Point(3, 9);
    assert point instanceof Point(int x, _) && x == 3;
    var windows = java.util.stream.Stream.of(1, 2, 3, 4, 5)
        .gather(java.util.stream.Gatherers.windowFixed(2)).toList();
    assert windows.equals(List.of(List.of(1, 2), List.of(3, 4), List.of(5)));
    assert !REQUEST_ID.isBound();
    ScopedValue.where(REQUEST_ID, "request-1").run(() -> {
        assert REQUEST_ID.get().equals("request-1");
    });
    assert !REQUEST_ID.isBound();
    assert new Positive(2).value == 2;
    boolean rejected = false;
    try { new Positive(0); }
    catch (IllegalArgumentException _) { rejected = true; }
    assert rejected;
    IO.println("Java25Examples OK");
}
