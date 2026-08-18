// ============================================================================
// 4. CLOSURE VS HOISTING
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - CLOSURES & HOISTING:
 * 1. Hoisting:
 *    - During the Compilation/Creation Phase, the V8 engine scans code and registers function declarations and variable names in the Environment Record.
 *    - Function Declarations (`function foo() {}`) are hoisted with their FULL body implementation (can be invoked before declaration).
 *    - `var` is hoisted and initialized to `undefined`.
 *    - Function Expressions (`var foo = () => {}`) only have the `var foo` hoisted as `undefined` (calling `foo()` before throws `TypeError: foo is not a function`).
 * 2. Closures:
 *    - A Closure is the combination of a function bundled together with references to its surrounding state (Lexical Environment).
 *    - Allows an inner function to access an outer function’s scope even AFTER the outer function has returned and its Execution Context was popped off the Call Stack.
 *    - Memory Mechanics: Variables captured by closures are allocated on the V8 Heap rather than the Call Stack to prevent garbage collection.
 * 3. Common Use Cases: Data privacy/encapsulation (private variables), function factories, memoization, currying, event handlers.
 */
console.log("\n--- 4. CLOSURE VS HOISTING ---");

// Ex 4.1: Hoisting - Function Declaration vs Function Expression
console.log("Ex 4.1 - Function declaration hoisted:", hoistedFunc());
function hoistedFunc() {
  return "I am hoisted completely!";
}
try {
  expressionFunc(); // TypeError: expressionFunc is not a function (var is undefined)
} catch (e) {
  console.log(
    "Ex 4.1 - Var function expression hoisted only as undefined ->",
    e.name + ":",
    e.message,
  );
}
var expressionFunc = function () {
  return "Expression";
};

// Ex 4.2: Closure - Private State Encapsulation (Module Pattern)
function createBankAccount(initialBalance) {
  let balance = initialBalance; // Private state variable (Heap allocated)
  return {
    deposit(amount) {
      balance += amount;
      return balance;
    },
    withdraw(amount) {
      if (amount > balance) return "Insufficient funds";
      balance -= amount;
      return balance;
    },
    getBalance() {
      return balance;
    },
  };
}
const account = createBankAccount(100);
account.deposit(50);
console.log("Ex 4.2 - Private Balance via closure:", account.getBalance());
console.log("Ex 4.2 - Direct property access undefined:", account.balance); // undefined!

// Ex 4.3: Closure - Function Factory
function multiplier(factor) {
  return (number) => number * factor;
}
const double = multiplier(2);
const triple = multiplier(3);
console.log(
  "Ex 4.3 - Function Factory: double(5) =",
  double(5),
  "| triple(5) =",
  triple(5),
);

// Ex 4.4: Closure - Memoization Engine
function memoize(fn) {
  const cache = new Map();
  return function (...args) {
    const key = JSON.stringify(args);
    if (cache.has(key)) return `[CACHE_HIT] ${cache.get(key)}`;
    const result = fn(...args);
    cache.set(key, result);
    return `[COMPUTED] ${result}`;
  };
}
const memoizedSquare = memoize((n) => n * n);
console.log("Ex 4.4 - Call 1:", memoizedSquare(5));
console.log("Ex 4.4 - Call 2:", memoizedSquare(5));

// Ex 4.5: Closure Memory Leak Trap (Retaining Heavy Outer Scope)
function createHeavyClosure() {
  const heavyData = new Array(10000).fill("payload");
  return function getFirstElement() {
    return heavyData[0]; // Captures entire heavyData array in memory!
  };
}
console.log(
  "Ex 4.5 - Closure holds reference to outer heap array:",
  createHeavyClosure()(),
);
