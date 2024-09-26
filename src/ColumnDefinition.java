import java.util.function.Function;

public class ColumnDefinition<T> {
    private final String header;
    private final Function<T, String> valueFunction;

    public ColumnDefinition(String header, Function<T, String> valueFunction) {
        this.header = header;
        this.valueFunction = valueFunction;
    }

    public String getHeader() {
        return header;
    }

    public Function<T, String> getValueFunction() {
        return valueFunction;
    }
}
