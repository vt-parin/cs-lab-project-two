import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IngredientTest
{
    @Test
    void constructorAndGetters_normal()
    {
        Ingredient i = new Ingredient("  Flour ", 2.5, " cups ");
        assertEquals("Flour", i.getName());
        assertEquals(2.5, i.getAmount(), 0.0001);
        assertEquals("cups", i.getUnit());
    }

    @Test
    void constructor_rejectsBlankName()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Ingredient("   ", 1, "g"));
        assertThrows(IllegalArgumentException.class,
            () -> new Ingredient(null, 1, "g"));
    }

    @Test
    void constructor_rejectsBadAmounts()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Ingredient("Salt", -1, "g"));
        assertThrows(IllegalArgumentException.class,
            () -> new Ingredient("Salt", 0, "g"));
        assertThrows(IllegalArgumentException.class,
            () -> new Ingredient("Salt", Double.NaN, "g"));
    }

    @Test
    void constructor_rejectsNullUnit()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Ingredient("Salt", 1, null));
    }

    @Test
    void equals_ignoresCase_andToStringFormats()
    {
        assertEquals(new Ingredient("Egg", 2, ""), new Ingredient("egg", 2, ""));
        assertNotEquals(new Ingredient("Egg", 2, ""), new Ingredient("Egg", 3, ""));
        assertEquals("2 eggs", new Ingredient("eggs", 2, "").toString());
        assertEquals("1.5 cup milk", new Ingredient("milk", 1.5, "cup").toString());
    }
}
