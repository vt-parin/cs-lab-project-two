import java.util.Objects;

/**
 * One ingredient of a recipe: a name, a positive amount, and a unit.
 * Invalid values are rejected here as a backstop behind the console
 * validation in CookbookApp.
 */
public class Ingredient
{
    private final String name;
    private final double amount;
    private final String unit;

    /**
     * @param name   ingredient name; must not be null or blank
     * @param amount quantity; must be finite and greater than zero
     * @param unit   unit of measure; may be empty (e.g. "2 eggs"), not null
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Ingredient(String name, double amount, String unit)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException(
                "Ingredient name must not be blank");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0)
        {
            throw new IllegalArgumentException(
                "Ingredient amount must be a positive number");
        }
        if (unit == null)
        {
            throw new IllegalArgumentException("Ingredient unit must not be null");
        }
        this.name = name.trim();
        this.amount = amount;
        this.unit = unit.trim();
    }

    /** @return the ingredient name */
    public String getName()
    {
        return name;
    }

    /** @return the amount */
    public double getAmount()
    {
        return amount;
    }

    /** @return the unit (possibly empty) */
    public String getUnit()
    {
        return unit;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof Ingredient))
        {
            return false;
        }
        Ingredient o = (Ingredient) other;
        return name.equalsIgnoreCase(o.name)
            && Double.compare(amount, o.amount) == 0
            && unit.equalsIgnoreCase(o.unit);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name.toLowerCase(), amount, unit.toLowerCase());
    }

    @Override
    public String toString()
    {
        String amt = (amount == Math.rint(amount))
            ? String.valueOf((long) amount) : String.valueOf(amount);
        return amt + (unit.isEmpty() ? "" : " " + unit) + " " + name;
    }
}
