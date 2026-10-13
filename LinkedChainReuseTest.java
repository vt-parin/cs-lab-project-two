import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/** Shows ONE generic LinkedChain class holding several element types. */
public class LinkedChainReuseTest
{
    @Test
    void sameClassHoldsRecipesIngredientsAndStrings()
    {
        LinkedChain<Ingredient> ings = new LinkedChain<Ingredient>();
        ings.addToEnd(new Ingredient("Egg", 2, ""));
        LinkedChain<Recipe> recipes = new LinkedChain<Recipe>();
        Recipe omelet = new Recipe("Omelet", ings, "Whisk and fry.");
        recipes.addToEnd(omelet);
        LinkedChain<String> tags = new LinkedChain<String>();
        tags.addToEnd("breakfast");

        assertEquals(omelet, recipes.get(0));
        assertEquals("Egg", ings.get(0).getName());
        assertTrue(tags.contains("breakfast"));
        assertEquals(ings.getClass(), recipes.getClass());
        assertEquals(recipes.getClass(), tags.getClass());
    }

    // Spec test plan for the provided LinkedChain (add/remove/contains/size)
    @Test
    void add_normalAndNull()
    {
        LinkedChain<String> c = new LinkedChain<String>();
        c.add("Pancakes");
        assertEquals(1, c.size());
        assertThrows(IllegalArgumentException.class, () -> c.add(null));
        assertEquals(1, c.size());
    }

    @Test
    void remove_normalEmptyAndAbsent()
    {
        LinkedChain<String> c = new LinkedChain<String>();
        assertFalse(c.remove("Soup"));
        c.addToEnd("Waffles");
        c.addToEnd("Pancakes");
        assertTrue(c.remove("Pancakes"));
        assertEquals(1, c.size());
        assertFalse(c.remove("Soup"));
        assertEquals(1, c.size());
    }

    @Test
    void contains_andSize()
    {
        LinkedChain<String> c = new LinkedChain<String>();
        assertFalse(c.contains("Soup"));
        assertEquals(0, c.size());
        c.addToEnd("Waffles");
        assertTrue(c.contains("Waffles"));
    }
}
