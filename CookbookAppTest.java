import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

public class CookbookAppTest
{
    private ByteArrayOutputStream buffer;

    private CookbookApp app(String input)
    {
        buffer = new ByteArrayOutputStream();
        return new CookbookApp(
            new ByteArrayInputStream(input.getBytes()),
            new PrintStream(buffer));
    }

    private String output()
    {
        return buffer.toString();
    }

    /** Input lines that add "Pasta" with one ingredient. */
    private static final String PASTA =
        "Pasta\nSpaghetti\n200\ng\n\nBoil it.\n";
    private static final String SOUP =
        "Tomato Soup\nTomato\n4\n\n\nSimmer.\n";

    // ---- addRecipeWorkflow ----
    @Test
    void addRecipe_normal()
    {
        CookbookApp a = app(PASTA);
        a.addRecipeWorkflow();
        assertEquals(1, a.getRecipeCount());
        assertTrue(output().contains("Saved recipe: Pasta"));
    }

    @Test
    void addRecipe_blankNameReprompts()
    {
        CookbookApp a = app("   \n\nPasta\nSpaghetti\n200\ng\n\nBoil.\n");
        a.addRecipeWorkflow();
        assertEquals(1, a.getRecipeCount());
        assertTrue(output().contains("Recipe name cannot be blank."));
    }

    @Test
    void addRecipe_badAmountsReprompt()
    {
        CookbookApp a = app("Pasta\nSpaghetti\nabc\n-5\n0\nNaN\n200\ng\n\nBoil.\n");
        a.addRecipeWorkflow();
        assertEquals(1, a.getRecipeCount());
        assertTrue(output().contains("Amount must be a number"));
        assertTrue(output().contains("Amount must be a positive number."));
        a.displayRecipes();
        assertTrue(output().contains("200 g Spaghetti"));
    }

    @Test
    void addRecipe_requiresAtLeastOneIngredient()
    {
        CookbookApp a = app("Pasta\n\nSpaghetti\n200\ng\n\nBoil.\n");
        a.addRecipeWorkflow();
        assertEquals(1, a.getRecipeCount());
        assertTrue(output().contains("at least one ingredient"));
    }

    @Test
    void addRecipe_blankInstructionsReprompt()
    {
        CookbookApp a = app("Pasta\nSpaghetti\n200\ng\n\n   \nBoil.\n");
        a.addRecipeWorkflow();
        assertEquals(1, a.getRecipeCount());
        assertTrue(output().contains("Instructions cannot be blank."));
    }

    @Test
    void addRecipe_inputEndsEarlyDoesNotCrashOrSave()
    {
        CookbookApp a = app("Pasta\nSpaghetti\n");
        a.addRecipeWorkflow();
        assertEquals(0, a.getRecipeCount());
        assertTrue(output().contains("recipe not saved"));
    }

    // ---- searchRecipeWorkflow ----
    @Test
    void search_byNameAndByIngredient()
    {
        CookbookApp a = app(PASTA + SOUP);
        a.addRecipeWorkflow();
        a.addRecipeWorkflow();
        a.searchRecipeWorkflow("pasta");
        assertTrue(output().contains("Found 1 match(es)"));
        assertTrue(output().contains("200 g Spaghetti"));
        a.searchRecipeWorkflow("tomato");
        assertTrue(output().contains("Tomato Soup"));
    }

    @Test
    void search_emptyTermReportsRequired()
    {
        CookbookApp a = app(PASTA);
        a.addRecipeWorkflow();
        a.searchRecipeWorkflow("   ");
        a.searchRecipeWorkflow(null);
        assertTrue(output().contains("A search term is required."));
    }

    @Test
    void search_noMatchesAndEmptyCookbook()
    {
        CookbookApp a = app("");
        a.searchRecipeWorkflow("pasta");
        assertTrue(output().contains("No matches found"));
    }

    // ---- removeRecipeWorkflow ----
    @Test
    void remove_normal()
    {
        CookbookApp a = app(PASTA + SOUP);
        a.addRecipeWorkflow();
        a.addRecipeWorkflow();
        a.removeRecipeWorkflow("pasta");
        assertEquals(1, a.getRecipeCount());
        assertTrue(output().contains("Removed recipe: Pasta"));
        a.searchRecipeWorkflow("Pasta");
        assertTrue(output().contains("No matches found"));
    }

    @Test
    void remove_missingRecipeLeavesCookbookUnchanged()
    {
        CookbookApp a = app(PASTA);
        a.addRecipeWorkflow();
        a.removeRecipeWorkflow("Lasagna");
        assertEquals(1, a.getRecipeCount());
        assertTrue(output().contains("Recipe not found"));
    }

    @Test
    void remove_fromEmptyCookbookIsSafe()
    {
        CookbookApp a = app("");
        a.removeRecipeWorkflow("Pasta");
        assertEquals(0, a.getRecipeCount());
        assertTrue(output().contains("Recipe not found"));
    }

    @Test
    void remove_blankNameRejected()
    {
        CookbookApp a = app(PASTA);
        a.addRecipeWorkflow();
        a.removeRecipeWorkflow(" ");
        a.removeRecipeWorkflow(null);
        assertEquals(1, a.getRecipeCount());
        assertTrue(output().contains("A recipe name is required."));
    }

    @Test
    void remove_duplicateNamesRemovesOnlyOne()
    {
        CookbookApp a = app(PASTA + PASTA);
        a.addRecipeWorkflow();
        a.addRecipeWorkflow();
        assertEquals(2, a.getRecipeCount());
        a.removeRecipeWorkflow("Pasta");
        assertEquals(1, a.getRecipeCount());
    }

    // ---- displayRecipes ----
    @Test
    void display_normal()
    {
        CookbookApp a = app(PASTA);
        a.addRecipeWorkflow();
        a.displayRecipes();
        assertTrue(output().contains("Your cookbook (1 recipe(s))"));
        assertTrue(output().contains("Instructions: Boil it."));
    }

    @Test
    void display_emptyCookbookMessage()
    {
        CookbookApp a = app("");
        a.displayRecipes();
        assertTrue(output().contains("Your cookbook is empty"));
    }

    // ---- getRecipeCount ----
    @Test
    void getRecipeCount_zeroThenOne()
    {
        CookbookApp a = app(PASTA);
        assertEquals(0, a.getRecipeCount());
        a.addRecipeWorkflow();
        assertEquals(1, a.getRecipeCount());
    }

    // ---- start ----
    @Test
    void start_fullSessionThroughMenu()
    {
        CookbookApp a = app("1\n" + PASTA + "2\n3\npasta\n4\nPasta\n5\n");
        a.start();
        assertEquals(0, a.getRecipeCount());
        assertTrue(output().contains("Saved recipe: Pasta"));
        assertTrue(output().contains("Removed recipe: Pasta"));
        assertTrue(output().contains("Goodbye!"));
    }

    @Test
    void start_badMenuChoicesAndEndOfInputDoNotCrash()
    {
        CookbookApp a = app("9\nabc\n\n3\n\n4\n  \n");
        a.start();
        assertTrue(output().contains("Invalid option"));
        assertTrue(output().contains("A search term is required."));
        assertTrue(output().contains("A recipe name is required."));
        assertTrue(output().contains("Goodbye!"));
    }
}
