public class ModuleSetting {
    public enum Type { BOOLEAN, SLIDER, TEXT }

    private String name;
    private Type type;
    private Object value;
    private double min;
    private double max;

    public ModuleSetting(String name, Type type, Object value) {
        this(name, type, value, 0, 1);
    }

    public ModuleSetting(String name, Type type, Object value, double min, double max) {
        if (name == null || type == null) {
            throw new IllegalArgumentException("name ve type null olamaz");
        }
        if (min > max) {
            throw new IllegalArgumentException("min max'tan büyük olamaz");
        }
        this.name = name;
        this.type = type;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public String getName() { return name; }
    public Type getType() { return type; }
    public Object getValue() { return value; }
    public double getMin() { return min; }
    public double getMax() { return max; }
}
