import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RecipeTest
{
    private LinkedChain<Ingredient> chain()
    {
        LinkedChain<Ingredient> c = new LinkedChain<Ingredient>();
        c.addToEnd(new Ingredient("Spaghetti", 200, "g"));
        c.addToEnd(new Ingredient("Garlic", 2, "cloves"));
        return c;
    }

    @Test
    void constructorAndGetters_normal()
    {
        LinkedChain<Ingredient> c = chain();
        Recipe r = new Recipe(" Pasta ", c, " Boil it. ");
        assertEquals("Pasta", r.getName());
        assertSame(c, r.getIngredients());
        assertEquals(2, r.getIngredients().size());
        assertEquals("Boil it.", r.getInstructions());
    }

    @Test
    void constructor_rejectsBadInput()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Recipe("  ", chain(), "Boil."));
        assertThrows(IllegalArgumentException.class,
            () -> new Recipe("Pasta", new LinkedChain<Ingredient>(), "Boil."));
        assertThrows(IllegalArgumentException.class,
            () -> new Recipe("Pasta", null, "Boil."));
        assertThrows(IllegalArgumentException.class,
            () -> new Recipe("Pasta", chain(), " "));
    }

    @Test
    void nameContains_normalAndBad()
    {
        Recipe r = new Recipe("Garlic Pasta", chain(), "Boil.");
        assertTrue(r.nameContains("pasta"));
        assertFalse(r.nameContains("soup"));
        assertFalse(r.nameContains(""));
        assertFalse(r.nameContains(null));
    }

    @Test
    void usesIngredient_normalAndBad()
    {
        Recipe r = new Recipe("Pasta", chain(), "Boil.");
        assertTrue(r.usesIngredient("GARLIC"));
        assertFalse(r.usesIngredient("tomato"));
        assertFalse(r.usesIngredient("   "));
        assertFalse(r.usesIngredient(null));
    }

    @Test
    void hasName_normalAndBad()
    {
        Recipe r = new Recipe("Pasta", chain(), "Boil.");
        assertTrue(r.hasName("pasta "));
        assertFalse(r.hasName("Pas"));
        assertFalse(r.hasName(null));
    }

    @Test
    void equals_andToString()
    {
        Recipe a = new Recipe("Pasta", chain(), "Boil.");
        Recipe b = new Recipe("pasta", chain(), "Boil.");
        Recipe c = new Recipe("Pasta", chain(), "Bake.");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertTrue(a.toString().contains("200 g Spaghetti"));
    }
}
