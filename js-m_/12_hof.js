// ============================================================================
// 12. HIGHER-ORDER FUNCTIONS (HOF)
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - HIGHER-ORDER FUNCTIONS (HOF):
 * 1. Definition: A function that does at least one of the following:
 *    - Takes one or more functions as arguments (e.g. `map`, `filter`, event listeners).
 *    - Returns a function as its result (e.g. function factories, currying, middleware, decorators).
 * 2. Why HOFs matter: Enables First-Class Functions, Composition, Declarative programming, and Aspect-Oriented Middleware.
 */
console.log("\n--- 12. HIGHER-ORDER FUNCTIONS (HOF) ---");

// Ex 12.1: Function as Argument
function executeOperation(a, b, operationFn) {
  return operationFn(a, b);
}
console.log(
  "Ex 12.1 - HOF passing function as arg (Sum):",
  executeOperation(10, 20, (x, y) => x + y),
);
console.log(
  "Ex 12.1 - HOF passing function as arg (Multiply):",
  executeOperation(10, 20, (x, y) => x * y),
);

// Ex 12.2: Function returning a Function (Logging Decorator / Middleware)
function withLogging(fn) {
  return function (...args) {
    const result = fn(...args);
    return `[LOGGED] Invoked ${fn.name || "anonymous"} with args ${JSON.stringify(args)} -> Output: ${result}`;
  };
}
const loggedAdd = withLogging(function add(a, b) {
  return a + b;
});
console.log("Ex 12.2 -", loggedAdd(5, 7));

// Ex 12.3: Function Composition (compose: f(g(x)))
const compose = (f, g) => (x) => f(g(x));
const add5 = (x) => x + 5;
const multiply10 = (x) => x * 10;
const addThenMultiply = compose(multiply10, add5); // multiply10(add5(2)) = 7 * 10 = 70
console.log(
  "Ex 12.3 - Function Composition compose(multiply10, add5)(2) =",
  addThenMultiply(2),
);

// Ex 12.4: Pipe Pattern (pipe: g(f(x)) - Left to right)
const pipe =
  (...fns) =>
  (x) =>
    fns.reduce((v, fn) => fn(v), x);
const formatText = pipe(
  (s) => s.trim(),
  (s) => s.toLowerCase(),
  (s) => `[SANITIZED: ${s}]`,
);
console.log("Ex 12.4 - Pipe pipeline:", formatText("   HeLLo WoRLd   "));

// Ex 12.5: Predicate Negator HOF
const not =
  (predicate) =>
  (...args) =>
    !predicate(...args);
const isEven = (n) => n % 2 === 0;
const isOdd = not(isEven);
console.log("Ex 12.5 - HOF Negator isOdd(7):", isOdd(7));
