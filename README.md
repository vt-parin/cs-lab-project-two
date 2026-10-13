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

