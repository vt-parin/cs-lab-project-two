/**
 * A saved recipe: a name, a LinkedChain of Ingredient objects, and
 * instructions. Matching helpers live here so CookbookApp only has to
 * ask "does this recipe match?".
 */
public class Recipe
{
    private final String name;
    private final LinkedChain<Ingredient> ingredients;
    private final String instructions;

    /**
     * @param name         recipe name; must not be null or blank
     * @param ingredients  chain with at least one ingredient
     * @param instructions cooking instructions; must not be null or blank
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Recipe(String name, LinkedChain<Ingredient> ingredients,
        String instructions)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Recipe name must not be blank");
        }
        if (ingredients == null || ingredients.isEmpty())
        {
            throw new IllegalArgumentException(
                "Recipe needs at least one ingredient");
        }
        if (instructions == null || instructions.isBlank())
        {
            throw new IllegalArgumentException(
                "Recipe instructions must not be blank");
        }
        this.name = name.trim();
        this.ingredients = ingredients;
        this.instructions = instructions.trim();
    }

    /** @return the recipe name */
    public String getName()
    {
        return name;
    }

    /** @return the chain of ingredients */
    public LinkedChain<Ingredient> getIngredients()
    {
        return ingredients;
    }

    /** @return the instructions */
    public String getInstructions()
    {
        return instructions;
    }

    /**
     * @param term text to look for (case-insensitive)
     * @return true if the recipe name contains the term
     */
    public boolean nameContains(String term)
    {
        return term != null && !term.isBlank()
            && name.toLowerCase().contains(term.trim().toLowerCase());
    }

    /**
     * Walks the ingredient chain: O(m) for m ingredients.
     *
     * @param term text to look for (case-insensitive)
     * @return true if any ingredient name contains the term
     */
    public boolean usesIngredient(String term)
    {
        if (term == null || term.isBlank())
        {
            return false;
        }
        String t = term.trim().toLowerCase();
        Ingredient[] all = ingredients.toArray(
            new Ingredient[ingredients.size()]);
        for (Ingredient ing : all)
        {
            if (ing.getName().toLowerCase().contains(t))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @param other a recipe name
     * @return true if the name equals this recipe's name, ignoring case
     */
    public boolean hasName(String other)
    {
        return other != null && name.equalsIgnoreCase(other.trim());
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof Recipe))
        {
            return false;
        }
        Recipe o = (Recipe) other;
        return name.equalsIgnoreCase(o.name)
            && instructions.equals(o.instructions);
    }

    @Override
    public int hashCode()
    {
        return name.toLowerCase().hashCode() * 31 + instructions.hashCode();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n  Ingredients:\n");
        Ingredient[] all = ingredients.toArray(
            new Ingredient[ingredients.size()]);
        for (Ingredient ing : all)
        {
            sb.append("    - ").append(ing).append("\n");
        }
        sb.append("  Instructions: ").append(instructions);
        return sb.toString();
    }
}
