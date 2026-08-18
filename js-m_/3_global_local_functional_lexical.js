// ============================================================================
// 3. GLOBAL, LOCAL, FUNCTIONAL, & LEXICAL SCOPE
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - SCOPES & EXECUTION CONTEXTS:
 * 1. Global Scope: Outermost execution context. Variables declared here are accessible anywhere in the program.
 * 2. Functional Scope: Variables declared with `var`, `let`, or `const` inside a function are private to that function.
 * 3. Block Scope: Introduced in ES6. `{ ... }` blocks create isolated scopes for `let` and `const`.
 * 4. Lexical Scope (Static Scope):
 *    - Scope is determined by the physical placement of variables and blocks in the source code at AUTHOR time, NOT at runtime!
 *    - Inner functions retain access to variables in their outer enclosing lexical scopes via the `[[Scopes]]` internal property.
 * 5. Scope Chain:
 *    - When resolving an identifier, the JavaScript engine inspects the current Lexical Environment. If not found, it traverses up the outer environment reference chain until it reaches the Global Environment (or throws `ReferenceError`).
 */
console.log("\n--- 3. GLOBAL, LOCAL, FUNCTIONAL, & LEXICAL SCOPE ---");

// Ex 3.1: Global vs Functional Scope
var appName = "GlobalApp";
function initApp() {
  var moduleName = "AuthModule";
  console.log(
    "Ex 3.1 - Inside function: Can access global (" +
      appName +
      ") and local (" +
      moduleName +
      ")",
  );
}
initApp();
try {
  console.log(moduleName);
} catch (e) {
  console.log(
    "Ex 3.1 - Outside function: moduleName is not accessible ->",
    e.name,
  );
}

// Ex 3.2: Block Scope with Nested Blocks
function blockScopeDemo() {
  let outer = "outer_val";
  if (true) {
    let inner = "inner_val";
    console.log("Ex 3.2 - Inner block sees outer:", outer, "and inner:", inner);
  }
  try {
    console.log(inner);
  } catch (e) {
    console.log(
      "Ex 3.2 - Outer block cannot see inner block variable ->",
      e.name,
    );
  }
}
blockScopeDemo();

// Ex 3.3: Lexical Scope Resolution Chain (Static Author-time Resolution)
const globalGreeting = "Hello";
function outerFn() {
  const outerUser = "Alice";
  function middleFn() {
    const middleRole = "Architect";
    function innerFn() {
      // Traverses 3 scope layers: innerFn -> middleFn -> outerFn -> global
      return `${globalGreeting}, ${outerUser} (${middleRole}) from Lexical Scope!`;
    }
    return innerFn();
  }
  return middleFn();
}
console.log("Ex 3.3 - " + outerFn());

// Ex 3.4: Lexical Scope vs Dynamic Scope Gotcha
const configValue = "GLOBAL_CONFIG";
function printer() {
  console.log(
    "Ex 3.4 - Lexical lookup reads author-time location:",
    configValue,
  );
}
function caller() {
  const configValue = "LOCAL_CALLER_CONFIG"; // Does NOT override lexical scope!
  printer();
}
caller();

// Ex 3.5: Shadowing (Variable Shadowing)
const shadowVar = "Original Global";
function shadowDemo() {
  const shadowVar = "Shadowed Local"; // Masks outer variable in this scope
  console.log("Ex 3.5 - Shadowed variable inside function:", shadowVar);
}
shadowDemo();
console.log("Ex 3.5 - Global variable remains untouched:", shadowVar);
