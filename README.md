# LetMeCook — Project 2, Deliverable 3 (Prototype + Tests)

A console cookbook: add recipes (name, ingredients, instructions), show all,
search by recipe name or ingredient, and remove. Recipes are stored in a
`LinkedChain<Recipe>`; each recipe's ingredients in a `LinkedChain<Ingredient>`.

## How to compile and run

Files: `src/` (LinkedChain, Ingredient, Recipe, CookbookApp) and `test/` (JUnit 5 tests).
Download `junit-platform-console-standalone-<version>.jar` (JUnit 5) into `lib/`.

```
mkdir out
javac -cp lib/junit-platform-console-standalone.jar -d out src/*.java test/*.java

# run the program
java -cp out CookbookApp

# run the tests
java -jar lib/junit-platform-console-standalone.jar -cp out --scan-classpath
```
(On Windows use `;` instead of `:` if you build a multi-entry classpath.)
Current result: **34 tests, 34 passing**.

## Reflection

**What changed from the specification?**
- *LinkedChain API.* The spec described our own LinkedChain with `head`, `tail`,
  an appending `add`, and O(1) appends. The provided class has no tail, its `add()`
  inserts at the FRONT, and `addToEnd()` is O(n). Our app uses `addToEnd()` so recipes and
  ingredients display in the order entered. Consequence: the spec's "tail pointer for O(1)
  appends" (Section 8) did not happen, and our Generic Contract's "stores references in
  insertion order" is only true when using `addToEnd`. Origin: [TEAM: discussion / GenAI / own reflection].
- *Raw types → generics.* Spec wrote `LinkedChain ingredients`; code uses `LinkedChain<Ingredient>`.
- *Constructors validate.* Spec: `Recipe` constructor catches "no ingredients/instructions" as
  an alternative. Built: `Recipe` and `Ingredient` constructors throw `IllegalArgumentException`
  as a backstop, while `CookbookApp` re-prompts first so users never see the exception.
- *CookbookApp.* Added a constructor taking `InputStream`/`PrintStream` (so tests can drive it),
  `main`, `displayRecipes()` (spec only mentioned it under validation) and `getRecipeCount()`.
  Search is one method matching name OR ingredient (substring, case-insensitive). `start()` prompts
  for the term/name and passes it to `searchRecipeWorkflow(term)` / `removeRecipeWorkflow(name)`
  as the spec's signatures require; `addRecipeWorkflow()` prompts internally.
- *Recipe/Ingredient helpers.* Added `equals`/`hashCode` (needed because `LinkedChain.remove`/`contains`
  use `equals`), `toString`, and `Recipe.nameContains / usesIngredient / hasName`.
- *Tests.* Spec's LinkedChain null-add test expected "rejects, size unchanged"; the provided chain throws
  `IllegalArgumentException`, and the test asserts that. Added tests for Recipe, Ingredient, start(), duplicates, and EOF.

**What changed in the LinkedChain?** Nothing — used unchanged. Only public methods are called:
`addToEnd`, `remove(T)`, `size`, `isEmpty`, `toArray`. We iterate with `toArray(new T[size()])` (O(n))
instead of `get(i)` in a loop (which would be O(n²)). [TEAM: confirm you used the original file.]

**What happened to our unknowns?**
- *Cleanest way to search a generic structure:* figured out — copy to a typed array with `toArray`,
  loop, and let the domain class (`Recipe`) answer "do I match?".
- *Comparing objects inside the chain:* figured out — the chain relies on `equals`, so `Recipe`/`Ingredient` override it.
- *remove on empty/absent:* figured out — `remove(T)` returns `false` and leaves the chain unchanged; tests cover both.
- *Validate without duplicating code:* partly — prompt helpers (`promptNonBlank`, `promptAmount`) are shared,
  but rules also exist in the constructors. Still open: whether to centralize them in one validator class.
- *Misunderstanding the generic design (biggest risk):* [TEAM: your honest answer].

**What doesn't work yet?**
- No stretch goals (What Can I Cook?, edit, tags, favorites).
- No persistence — recipes vanish when the program exits.
- Getters take no arguments, so their "bad-input" case is covered through the constructor tests instead.
- Search is a single combined name/ingredient search; there is no "ingredient only" mode.
- Reading input only handles one line per answer (no multi-line instructions).

**Big-O of app methods vs. spec**

| Method | Spec | Actual | Matches? |
|---|---|---|---|
| `addRecipeWorkflow` | O(1) additions | O(k²) for k ingredients + O(n) for the cookbook append (`addToEnd` walks the chain) | No — no tail pointer |
| `searchRecipeWorkflow` | O(n·m) | O(n·m) | Yes |
| `removeRecipeWorkflow` | O(n) | O(n) (scan + `remove(T)`) | Yes |
| `displayRecipes` | — | O(n·m) | — |
| `start` | O(k·n) | O(k·n·m) worst case | Roughly |

## Next Steps


1. **Finish MVP polish** — : separate ingredient-only search if the team wants it; multi-line instructions; centralize validation.
2. **Decide on the tail pointer** — : either modify `LinkedChain` (add a `tail`) to make appends O(1) as the spec promised, or update the spec/Big-O to match `addToEnd`.
3. **Stretch goals** — . Suggested: commit to *What Can I Cook?* and *Edit recipe*; drop *tags* and *favorites*. Mark each as committed or dropped here.
4. **New tests** — one normal + one bad-input per new public method. E.g. `whatCanICook(ingredients)`: matching recipe / empty ingredient list; `editRecipe(name)`: change instructions / unknown recipe name.
5. **Cleanup before final** — : re-run all tests, update README and Big-O table.

## Attribution

| File | Who | How |
|---|---|---|
| LinkedChain.java | CS 2114 staff | Provided; unchanged |
| Ingredient.java | [Rohit] | GenAI-assisted draft; |
| Recipe.java | [Allycia] | GenAI-assisted draft; |
| CookbookApp.java | [Ria] | GenAI-assisted draft; |
| IngredientTest.java | [Rohit] | GenAI-assisted; |
| RecipeTest.java | [Allycia] | GenAI-assisted; |
| CookbookAppTest.java | [Tia] | GenAI-assisted; |
| LinkedChainReuseTest.java | [Parin] | GenAI-assisted; |
| README.md | [Parin] | Drafted; revised by GenAI |

**GenAI note:** Used GenAI to help fix errors and for understanding of class structures to implement