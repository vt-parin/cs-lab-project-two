import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Console application for LetMeCook. Owns user interaction and input
 * validation; stores recipes in a LinkedChain&lt;Recipe&gt; and each
 * recipe's ingredients in a LinkedChain&lt;Ingredient&gt;. All storage is
 * done through LinkedChain's public methods.
 */
public class CookbookApp
{
    private final LinkedChain<Recipe> cookbook = new LinkedChain<Recipe>();
    private final BufferedReader in;
    private final PrintStream out;

    /**
     * @param input  where user input is read from (System.in, or a test)
     * @param output where messages are printed
     */
    public CookbookApp(InputStream input, PrintStream output)
    {
        this.in = new BufferedReader(new InputStreamReader(input));
        this.out = output;
    }

    /**
     * Starts the program.
     *
     * @param args unused
     */
    public static void main(String[] args)
    {
        new CookbookApp(System.in, System.out).start();
    }

    /** Runs the menu loop until the user quits or input ends. */
    public void start()
    {
        out.println("Welcome to LetMeCook!");
        while (true)
        {
            out.println();
            out.println("1) Add recipe  2) Show all  3) Search  "
                + "4) Remove  5) Quit");
            String choice = prompt("Choose an option: ");
            if (choice == null || choice.trim().equals("5"))
            {
                out.println("Goodbye!");
                return;
            }
            switch (choice.trim())
            {
                case "1":
                    addRecipeWorkflow();
                    break;
                case "2":
                    displayRecipes();
                    break;
                case "3":
                    String term = prompt("Search term (recipe or ingredient): ");
                    if (term == null)
                    {
                        out.println("Goodbye!");
                        return;
                    }
                    searchRecipeWorkflow(term);
                    break;
                case "4":
                    String name = prompt("Name of recipe to remove: ");
                    if (name == null)
                    {
                        out.println("Goodbye!");
                        return;
                    }
                    removeRecipeWorkflow(name);
                    break;
                default:
                    out.println("Invalid option. Please enter 1-5.");
            }
        }
    }

    /**
     * Prompts for a recipe, re-prompting on bad input, then stores it.
     * If input ends mid-way the recipe is not saved.
     */
    public void addRecipeWorkflow()
    {
        out.println("--- Add a recipe ---");
        String name = promptNonBlank("Recipe name: ",
            "Recipe name cannot be blank.");
        if (name == null)
        {
            abortAdd();
            return;
        }
        LinkedChain<Ingredient> ingredients = readIngredients();
        if (ingredients == null)
        {
            abortAdd();
            return;
        }
        String instructions = promptNonBlank("Instructions: ",
            "Instructions cannot be blank.");
        if (instructions == null)
        {
            abortAdd();
            return;
        }
        try
        {
            Recipe recipe = new Recipe(name, ingredients, instructions);
            cookbook.addToEnd(recipe);
            out.println("Saved recipe: " + recipe.getName());
        }
        catch (IllegalArgumentException e)
        {
            out.println("Could not save recipe: " + e.getMessage());
        }
    }

    /**
     * Searches recipe names and ingredient names (case-insensitive,
     * substring match) and prints every match.
     *
     * @param term the text to search for
     */
    public void searchRecipeWorkflow(String term)
    {
        if (term == null || term.isBlank())
        {
            out.println("A search term is required.");
            return;
        }
        Recipe[] all = cookbook.toArray(new Recipe[cookbook.size()]);
        int matches = 0;
        StringBuilder found = new StringBuilder();
        for (Recipe r : all)
        {
            if (r.nameContains(term) || r.usesIngredient(term))
            {
                matches++;
                found.append(r).append("\n");
            }
        }
        if (matches == 0)
        {
            out.println("No matches found for \"" + term.trim() + "\".");
            return;
        }
        out.println("Found " + matches + " match(es) for \""
            + term.trim() + "\":");
        out.print(found);
    }

    /**
     * Removes the first recipe with the given name (case-insensitive).
     *
     * @param name the recipe name
     */
    public void removeRecipeWorkflow(String name)
    {
        if (name == null || name.isBlank())
        {
            out.println("A recipe name is required.");
            return;
        }
        Recipe[] all = cookbook.toArray(new Recipe[cookbook.size()]);
        for (Recipe r : all)
        {
            if (r.hasName(name))
            {
                if (cookbook.remove(r))
                {
                    out.println("Removed recipe: " + r.getName());
                    return;
                }
            }
        }
        out.println("Recipe not found: " + name.trim()
            + " (cookbook unchanged).");
    }

    /** Prints every saved recipe, or a message if there are none. */
    public void displayRecipes()
    {
        if (cookbook.isEmpty())
        {
            out.println("Your cookbook is empty. Add a recipe first!");
            return;
        }
        Recipe[] all = cookbook.toArray(new Recipe[cookbook.size()]);
        out.println("Your cookbook (" + all.length + " recipe(s)):");
        for (Recipe r : all)
        {
            out.println(r);
        }
    }

    /** @return how many recipes are saved */
    public int getRecipeCount()
    {
        return cookbook.size();
    }

    // ---------- input helpers ----------

    private String prompt(String message)
    {
        out.print(message);
        out.flush();
        try
        {
            return in.readLine();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private String promptNonBlank(String message, String error)
    {
        while (true)
        {
            String line = prompt(message);
            if (line == null)
            {
                return null;
            }
            if (!line.isBlank())
            {
                return line.trim();
            }
            out.println(error);
        }
    }

    private LinkedChain<Ingredient> readIngredients()
    {
        LinkedChain<Ingredient> chain = new LinkedChain<Ingredient>();
        while (true)
        {
            String name = prompt("Ingredient " + (chain.size() + 1)
                + " name (blank to finish): ");
            if (name == null)
            {
                return null;
            }
            if (name.isBlank())
            {
                if (chain.isEmpty())
                {
                    out.println("A recipe needs at least one ingredient.");
                    continue;
                }
                return chain;
            }
            Double amount = promptAmount();
            if (amount == null)
            {
                return null;
            }
            String unit = prompt("Unit (blank if none): ");
            if (unit == null)
            {
                return null;
            }
            chain.addToEnd(new Ingredient(name, amount, unit));
        }
    }

    private Double promptAmount()
    {
        while (true)
        {
            String line = prompt("Amount: ");
            if (line == null)
            {
                return null;
            }
            try
            {
                double value = Double.parseDouble(line.trim());
                if (Double.isNaN(value) || Double.isInfinite(value)
                    || value <= 0)
                {
                    out.println("Amount must be a positive number.");
                }
                else
                {
                    return value;
                }
            }
            catch (NumberFormatException e)
            {
                out.println("Amount must be a number, e.g. 1.5.");
            }
        }
    }

    private void abortAdd()
    {
        out.println("Input ended; recipe not saved.");
    }
}
