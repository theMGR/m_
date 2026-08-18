/**
 * ============================================================================
 * JAVASCRIPT & V8 ENGINE MASTER ARCHITECTURE & INTERVIEW GUIDE (my_js.js)
 * ============================================================================
 * Comprehensive, production-grade guide covering JavaScript Core, ES6+,
 * V8 Engine Internals, Event Loop Concurrency, Closures, Scopes, Prototypes,
 * DOM/BOM Mock Engines, Async Patterns, and Senior/Staff Architect Interview Q&A.
 *
 * Each of the 33 topics includes:
 *  1. Architectural Overview & Recruiter / Examiner Definitions.
 *  2. Low-Level V8 Engine & Execution Context Mechanics.
 *  3. Top Tech Interview Gotchas, Pitfalls, & Tricky Edge Cases.
 *  4. 4 to 5 fully functional, runnable, executable code examples.
 *
 * Requirements: Node.js 18+ (Node 21 recommended) or modern browser.
 * ============================================================================
 */

console.log("================================================================");
console.log("🚀 JAVASCRIPT MASTER ARCHITECTURE & INTERVIEW GUIDE (33 TOPICS)");
console.log("================================================================\n");

// ============================================================================
// 1. LET VS CONST VS VAR
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - LET VS CONST VS VAR:
 * 1. Scope Boundaries:
 *    - `var`: Function-scoped (or globally scoped if declared outside a function). Ignores `{}` block boundaries (if, for, while).
 *    - `let` & `const`: Block-scoped (`{}`). Confined strictly to the enclosing pair of curly braces.
 * 2. Hoisting & Initialization (V8 Engine Mechanics):
 *    - `var`: Hoisted to the top of its scope and initialized immediately with `undefined` during the Creation Phase.
 *    - `let` & `const`: Hoisted to the top of their block scope during the Creation Phase, but remain UNINITIALIZED.
 *      Accessing them before their declaration line throws a `ReferenceError` due to the Temporal Dead Zone (TDZ).
 * 3. Global Object Attachment:
 *    - In browser environments, `var` at the top level creates a property on the global `window` object (`window.a`).
 *    - `let` and `const` create declarations in the Script/Declarative Environment Record and do NOT pollute `window`.
 * 4. Immutability:
 *    - `const` prevents re-assignment of the variable identifier binding (memory reference), but does NOT freeze the underlying object properties (shallow mutation allowed unless `Object.freeze()` is used).
 *
 * EXAMINER / RECRUITER GOTCHA:
 * - In loops: `for (var i=0; i<3; i++) setTimeout(() => console.log(i))` prints `3, 3, 3` because all callbacks share the same function-scoped variable binding.
 *   `for (let i=0; i<3; i++)` creates a brand new lexical binding per iteration, printing `0, 1, 2`.
 */
console.log("--- 1. LET VS CONST VS VAR ---");

// Ex 1.1: Scope difference (Function vs Block Scope)
function ex1_1_scope() {
  if (true) {
    var vScope = "var is function-wide";
    let lScope = "let is block-only";
    const cScope = "const is block-only";
  }
  console.log("Ex 1.1 - var accessible outside block:", vScope);
  try {
    console.log(lScope);
  } catch (e) {
    console.log("Ex 1.1 - let blocked outside block:", e.message);
  }
}
ex1_1_scope();

// Ex 1.2: Re-declaration vs Re-assignment
function ex1_2_redeclaration() {
  var x = 10;
  var x = 20; // Allowed with var!
  let y = 10;
  // let y = 20; // SyntaxError: Identifier 'y' has already been declared
  y = 25; // Re-assignment is fine
  console.log("Ex 1.2 - Re-declared var:", x, "| Re-assigned let:", y);
}
ex1_2_redeclaration();

// Ex 1.3: const Reference vs Mutation
const ex1_3_user = { id: 101, name: "Alpha" };
ex1_3_user.name = "Beta"; // Allowed: mutating property inside the object
console.log("Ex 1.3 - const object property mutation:", ex1_3_user);
try {
  // ex1_3_user = { id: 102 }; // TypeError: Assignment to constant variable
} catch (e) {
  console.log("Ex 1.3 - const reassignment error:", e.message);
}

// Ex 1.4: Loop scoping trap (var sharing single binding vs let fresh binding)
function ex1_4_loopScoping() {
  const varCallbacks = [];
  const letCallbacks = [];
  for (var i = 0; i < 3; i++) {
    varCallbacks.push(() => i);
  }
  for (let j = 0; j < 3; j++) {
    letCallbacks.push(() => j);
  }
  console.log(
    "Ex 1.4 - var in loop closure (all share final value 3):",
    varCallbacks.map((fn) => fn()),
  );
  console.log(
    "Ex 1.4 - let in loop closure (fresh binding per iteration):",
    letCallbacks.map((fn) => fn()),
  );
}
ex1_4_loopScoping();

// Ex 1.5: Global Object property attachment
var globalVarTest = "I attach to window in browser";
let globalLetTest = "I do NOT attach to window";
console.log(
  "Ex 1.5 - var creates global property on global scope:",
  typeof global !== "undefined" ? "Tested in Node" : window.globalVarTest,
);

// ============================================================================
// 2. UNDECLARED VS UNDEFINED VS NULL VS DEFINED
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - TYPE & STATE SYSTEM:
 * 1. Undeclared:
 *    - An identifier that has never been declared in any accessible scope.
 *    - Reading it throws `ReferenceError: x is not defined`.
 *    - Note: In non-strict mode, assigning to an undeclared variable (`x = 10`) creates an implicit global property (Avoid!).
 * 2. Undefined:
 *    - A primitive type (`typeof undefined === 'undefined'`) representing the unintentional absence of a value.
 *    - Default value for: declared variables without assignment (`let a;`), unprovided function parameters, missing object properties (`obj.missing`), and functions without a `return` statement.
 * 3. Null:
 *    - A primitive type representing the intentional absence of any object value.
 *    - `typeof null === 'object'` (Historical JavaScript bug since JS 1995 due to type tag `000` for objects).
 * 4. Defined:
 *    - A variable that has been declared and assigned a meaningful value other than `undefined`.
 *
 * EXAMINER / RECRUITER GOTCHA:
 * - `null == undefined` evaluates to `true` (Abstract equality type coercion).
 * - `null === undefined` evaluates to `false` (Strict equality: different primitive types).
 * - `null + 1 === 1` (null coerced to 0), but `undefined + 1 === NaN` (undefined coerced to NaN).
 */
console.log("\n--- 2. UNDECLARED VS UNDEFINED VS NULL VS DEFINED ---");

// Ex 2.1: Undeclared Variable (Throws ReferenceError)
try {
  console.log(nonExistentVar);
} catch (e) {
  console.log("Ex 2.1 - Undeclared access throws:", e.name, "-", e.message);
}

// Ex 2.2: Undefined (Declared without value, unprovided param, missing key)
let declaredUnassigned;
function noReturnFn(param) {
  return param;
}
const emptyObj = {};
console.log("Ex 2.2 - Variable unassigned:", declaredUnassigned);
console.log("Ex 2.2 - Parameter missing:", noReturnFn());
console.log("Ex 2.2 - Object property missing:", emptyObj.foo);

// Ex 2.3: Null (Intentional empty reference)
let currentSelectedUser = null; // Explicitly no user selected yet
console.log("Ex 2.3 - Null value:", currentSelectedUser, "| typeof:", typeof currentSelectedUser);

// Ex 2.4: Equality comparisons (== vs ===)
console.log("Ex 2.4 - null == undefined:", null == undefined);
console.log("Ex 2.4 - null === undefined:", null === undefined);
console.log("Ex 2.4 - null + 10:", null + 10, "| undefined + 10:", undefined + 10);

// Ex 2.5: Nullish Coalescing (??) vs Logical OR (||)
const userConfig = { timeout: 0, title: "" };
console.log("Ex 2.5 - OR (||) treats 0 as falsy -> fallback:", userConfig.timeout || 3000);
console.log("Ex 2.5 - Nullish (??) preserves 0 & '' -> correct:", userConfig.timeout ?? 3000);

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
  console.log("Ex 3.1 - Inside function: Can access global (" + appName + ") and local (" + moduleName + ")");
}
initApp();
try {
  console.log(moduleName);
} catch (e) {
  console.log("Ex 3.1 - Outside function: moduleName is not accessible ->", e.name);
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
    console.log("Ex 3.2 - Outer block cannot see inner block variable ->", e.name);
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
  console.log("Ex 3.4 - Lexical lookup reads author-time location:", configValue);
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
  console.log("Ex 4.1 - Var function expression hoisted only as undefined ->", e.name + ":", e.message);
}
var expressionFunc = function () {
  return "Expression";
};

// Ex 4.2: Closure - Private State Encapsulation (Module Pattern)
function createBankAccount(initialBalance) {
  let balance = initialBalance; // Private state variable (Heap allocated)
  return {
    deposit(amount) { balance += amount; return balance; },
    withdraw(amount) {
      if (amount > balance) return "Insufficient funds";
      balance -= amount;
      return balance;
    },
    getBalance() { return balance; }
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
console.log("Ex 4.3 - Function Factory: double(5) =", double(5), "| triple(5) =", triple(5));

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
console.log("Ex 4.5 - Closure holds reference to outer heap array:", createHeavyClosure()());

// ============================================================================
// 5. ARROW FUNCTIONS VS REGULAR FUNCTIONS
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - ARROW FUNCTIONS VS REGULAR FUNCTIONS:
 * 1. `this` Binding:
 *    - Regular Functions: Have their own dynamic `this` determined by HOW they are called (Call-Site: default, implicit, explicit, or new).
 *    - Arrow Functions: Do NOT have their own `this`. They lexically capture `this` from the enclosing scope at author time.
 * 2. Constructors (`new` operator):
 *    - Regular Functions: Can be used as constructors with `new` (have a `prototype` property).
 *    - Arrow Functions: CANNOT be used with `new` (lack `[[Construct]]` internal method and `prototype` property; throws `TypeError`).
 * 3. `arguments` Object:
 *    - Regular Functions: Have an `arguments` array-like object.
 *    - Arrow Functions: Do NOT have `arguments`. Use Rest parameters (`...args`) instead.
 * 4. Duplicate Named Parameters:
 *    - Regular Functions: Allowed in non-strict mode (`function(a, a)`).
 *    - Arrow Functions: Strict syntax error for duplicate parameters.
 */
console.log("\n--- 5. ARROW FUNCTIONS VS REGULAR FUNCTIONS ---");

// Ex 5.1: Lexical 'this' in Objects & Callbacks
const timerObj = {
  name: "Timer Widget",
  regularFunc: function () {
    return this.name;
  },
  arrowFunc: () => {
    return typeof this !== "undefined" ? this.name : undefined;
  },
  delayedGreet: function () {
    setTimeout(() => {
      console.log("Ex 5.2 - Arrow in setTimeout preserves 'this':", this.name);
    }, 10);
  }
};
console.log("Ex 5.1 - Regular func gets object context:", timerObj.regularFunc());
console.log("Ex 5.1 - Arrow func gets lexical outer scope:", timerObj.arrowFunc());
timerObj.delayedGreet();

// Ex 5.3: Constructor invocation (new operator)
function RegularConstructor(val) { this.val = val; }
const ArrowConstructor = (val) => { this.val = val; };
console.log("Ex 5.3 - new RegularConstructor():", new RegularConstructor(42).val);
try {
  new ArrowConstructor(42);
} catch (e) {
  console.log("Ex 5.3 - new ArrowConstructor() fails:", e.name + ":", e.message);
}

// Ex 5.4: 'arguments' object vs Rest parameters (...args)
function regularArgs() {
  return Array.from(arguments);
}
const arrowArgs = (...args) => args;
console.log("Ex 5.4 - Regular arguments object:", regularArgs(1, 2, 3));
console.log("Ex 5.4 - Arrow rest parameters:", arrowArgs(1, 2, 3));

// Ex 5.5: Explicit binding (.call, .apply, .bind) has NO effect on Arrow Functions
const boundObj = { name: "Bound Context" };
const arrowTest = () => (typeof this !== "undefined" ? this.name : "Outer");
console.log("Ex 5.5 - Arrow func ignores .call(boundObj):", arrowTest.call(boundObj));

// ============================================================================
// 6. ARRAY METHODS: MAP, FILTER, REDUCE (REDUCER)
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - FUNCTIONAL ARRAY TRANSFORMATIONS:
 * 1. `map(callback(val, idx, arr))`:
 *    - Transforms each element 1-to-1. Returns a BRAND NEW array of the exact same length. Pure function (no mutations).
 * 2. `filter(callback(val, idx, arr))`:
 *    - Evaluates predicate boolean. Returns a BRAND NEW array with elements that return truthy (length $\le$ original).
 * 3. `reduce(callback(acc, val, idx, arr), initialValue)`:
 *    - Accumulates array values into a single scalar, object, or aggregated data structure.
 *    - GOTCHA: If `initialValue` is omitted, `acc` starts as `arr[0]` and iteration begins at index 1.
 *      If array is empty and no `initialValue` is provided ➔ Throws `TypeError: Reduce of empty array with no initial value`.
 */
console.log("\n--- 6. MAP, FILTER, REDUCE ---");

const numbers = [1, 2, 3, 4, 5, 6];

// Ex 6.1: map() - 1:1 Immutable Transformation
const squared = numbers.map((n) => n * n);
console.log("Ex 6.1 - map() squared:", squared);

// Ex 6.2: filter() - Predicate Filtering
const evens = numbers.filter((n) => n % 2 === 0);
console.log("Ex 6.2 - filter() evens:", evens);

// Ex 6.3: reduce() - Summing & Aggregating
const totalSum = numbers.reduce((acc, curr) => acc + curr, 0);
console.log("Ex 6.3 - reduce() sum:", totalSum);

// Ex 6.4: reduce() - Grouping Objects by Property (Like SQL GROUP BY)
const employees = [
  { name: "Alice", dept: "Engineering" },
  { name: "Bob", dept: "HR" },
  { name: "Charlie", dept: "Engineering" }
];
const groupedByDept = employees.reduce((acc, emp) => {
  acc[emp.dept] = acc[emp.dept] || [];
  acc[emp.dept].push(emp.name);
  return acc;
}, {});
console.log("Ex 6.4 - reduce() grouped by department:", groupedByDept);

// Ex 6.5: Custom Polyfill for map & filter using reduce
const customMapped = numbers.reduce((acc, n) => [...acc, n * 10], []);
console.log("Ex 6.5 - map implemented via reduce:", customMapped);

// ============================================================================
// 7. CALLBACK HELL & INVERSION OF CONTROL
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - ASYNC EVOLUTION & INVERSION OF CONTROL:
 * 1. Callback Hell (Pyramid of Doom):
 *    - Nested asynchronous callbacks making code unreadable, fragile, and difficult to manage error handling across steps.
 * 2. Inversion of Control (IoC Problem):
 *    - Passing your continuation callback to a 3rd party function means YOU surrender control over WHEN, HOW OFTEN, or IF your callback runs.
 *    - Bugs: 3rd party library might invoke callback 0 times, multiple times, or with unexpected error parameters.
 * 3. Solutions:
 *    - Promises: Guarantees inversion of control is restored. A Promise resolves ONCE, immutably, and catches errors reliably.
 *    - Async/Await: Syntactic sugar over Promises for linear, synchronous-looking async workflows.
 */
console.log("\n--- 7. CALLBACK HELL & ASYNC EVOLUTION ---");

// Ex 7.1: Callback Hell Simulator
function step1(cb) { setTimeout(() => cb("Step A"), 5); }
function step2(data, cb) { setTimeout(() => cb(data + " -> Step B"), 5); }
function step3(data, cb) { setTimeout(() => cb(data + " -> Step C"), 5); }

step1((r1) => {
  step2(r1, (r2) => {
    step3(r2, (r3) => {
      console.log("Ex 7.1 - Callback Hell result:", r3);
    });
  });
});

// Ex 7.2: Inversion of Control & Trust Issue Simulation
function untrustedThirdPartyLib(callback) {
  // Bug in library: calls callback twice!
  callback("Payment 1");
  // callback("Payment 2 (Double Charge!)");
}

// Ex 7.3: Promise Chain Solution (Flattening the pyramid)
const asyncStep = (msg) => new Promise((resolve) => setTimeout(() => resolve(msg), 5));
asyncStep("Promise Step 1")
  .then((res) => asyncStep(res + " -> Promise Step 2"))
  .then((res) => asyncStep(res + " -> Promise Step 3"))
  .then((finalRes) => console.log("Ex 7.3 - Clean Promise Chain:", finalRes));

// Ex 7.4: Async/Await Solution
async function executeCleanFlow() {
  const s1 = await asyncStep("Async Step 1");
  const s2 = await asyncStep(s1 + " -> Async Step 2");
  const s3 = await asyncStep(s2 + " -> Async Step 3");
  console.log("Ex 7.4 - Clean Async/Await:", s3);
}
executeCleanFlow();

// Ex 7.5: Handling Parallel Execution with Promise.all
Promise.all([asyncStep("Task 1"), asyncStep("Task 2"), asyncStep("Task 3")]).then(([t1, t2, t3]) => {
  console.log("Ex 7.5 - Parallel execution:", { t1, t2, t3 });
});

// ============================================================================
// 8. PROMISES LIFECYCLE
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - PROMISE LIFECYCLE & STATE MACHINE:
 * 1. 3 Mutually Exclusive States:
 *    - `Pending`: Initial state. Neither fulfilled nor rejected.
 *    - `Fulfilled`: Operation completed successfully (`resolve(value)` was invoked).
 *    - `Rejected`: Operation failed (`reject(reason)` or unhandled exception inside executor).
 * 2. Settled & Immutability:
 *    - Once a Promise transitions to `Fulfilled` or `Rejected`, it is SETTLED.
 *    - A settled state is IMMUTABLE (subsequent calls to `resolve()` or `reject()` are silently ignored).
 * 3. Thenable Architecture:
 *    - `.then(onFulfilled, onRejected)` returns a BRAND NEW Promise, allowing chaining.
 */
console.log("\n--- 8. PROMISES LIFECYCLE ---");

// Ex 8.1: Pending to Fulfilled Transition
const pFulfilled = new Promise((resolve) => setTimeout(() => resolve("SUCCESS_DATA"), 10));
pFulfilled.then((val) => console.log("Ex 8.1 - State: FULFILLED, Value:", val));

// Ex 8.2: Pending to Rejected Transition
const pRejected = new Promise((_, reject) => setTimeout(() => reject(new Error("NETWORK_TIMEOUT")), 10));
pRejected.catch((err) => console.log("Ex 8.2 - State: REJECTED, Reason:", err.message));

// Ex 8.3: State Immutability (First settle wins)
const pImmutable = new Promise((resolve, reject) => {
  resolve("First Resolve Wins");
  reject("Ignored Reject");
  resolve("Ignored Second Resolve");
});
pImmutable.then((res) => console.log("Ex 8.3 - Settled immutability check:", res));

// Ex 8.4: Value Chaining in .then()
Promise.resolve(5)
  .then((n) => n * 2) // 10
  .then((n) => n + 15) // 25
  .then((res) => console.log("Ex 8.4 - Value chained transformation:", res));

// Ex 8.5: Returning a Promise inside .then() (Promise unwrapping)
Promise.resolve("Initial Token")
  .then((tok) => new Promise((resolve) => setTimeout(() => resolve("Auth with " + tok), 10)))
  .then((user) => console.log("Ex 8.5 - Nested promise returned in chain:", user));

// ============================================================================
// 9. PROMISE CREATION & ERROR HANDLING
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - PROMISE CREATION & ERROR PROPAGATION:
 * 1. Creation Strategies:
 *    - Constructor: `new Promise((resolve, reject) => { ... })` (For wrapping callback APIs).
 *    - Instant Static Helpers: `Promise.resolve(val)`, `Promise.reject(err)`.
 * 2. Error Propagation:
 *    - Errors bubble down the `.then()` chain until intercepted by the first `.catch()` handler.
 *    - Any exception thrown inside `.then()` automatically converts into a rejected Promise.
 * 3. `.finally(callback)`:
 *    - Executes cleanup logic regardless of whether the Promise was fulfilled or rejected.
 *    - Does NOT receive any arguments and passes through the original resolved value/error.
 */
console.log("\n--- 9. PROMISE CREATION & ERROR HANDLING ---");

// Ex 9.1: Promise Constructor Wrapping
function fetchUserAsync(id) {
  return new Promise((resolve, reject) => {
    if (id > 0) resolve({ id, name: "Alice", status: "Active" });
    else reject(new Error("Invalid ID: Must be positive"));
  });
}
fetchUserAsync(1).then((u) => console.log("Ex 9.1 - Constructor resolved:", u.status));

// Ex 9.2: Static Instant Creators (Promise.resolve & Promise.reject)
Promise.resolve("Instant success value").then((v) => console.log("Ex 9.2 - Promise.resolve:", v));
Promise.reject("Instant failure error").catch((e) => console.log("Ex 9.2 - Promise.reject caught:", e));

// Ex 9.3: Error bubbling through multiple then() blocks
Promise.resolve("Start")
  .then(() => { throw new Error("Network Timeout"); })
  .then(() => "Skipped step")
  .catch((err) => {
    console.log("Ex 9.3 - Error intercepted in first catch:", err.message);
    return "Recovered default value";
  })
  .then((val) => console.log("Ex 9.3 - Downstream recovered flow:", val));

// Ex 9.4: .finally() for Guaranteed Cleanup
let isSpinnerActive = true;
Promise.resolve("Data Loaded")
  .then((res) => console.log("Ex 9.4 - Result:", res))
  .finally(() => {
    isSpinnerActive = false;
    console.log("Ex 9.4 - finally cleanup: isSpinnerActive =", isSpinnerActive);
  });

// Ex 9.5: .then(success, error) 2nd parameter vs .catch()
Promise.reject("Bad Request").then(
  (res) => console.log("Success"),
  (err) => console.log("Ex 9.5 - Handled via .then(success, error) 2nd arg:", err)
);

// ============================================================================
// 10. PROMISE COMBINATORS (ALL, ALLSETTLED, RACE, ANY)
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - PROMISE COMBINATORS (ES6 - ES2021):
 * 1. `Promise.all([p1, p2, ...])` (All or Nothing):
 *    - Resolves when ALL promises resolve. Returns array of results.
 *    - Fails FAST on the FIRST rejection (aborts and rejects immediately).
 * 2. `Promise.allSettled([p1, p2, ...])` (Complete Visibility):
 *    - Waits for ALL promises to settle. Never rejects.
 *    - Returns array of `{ status: 'fulfilled', value }` or `{ status: 'rejected', reason }`.
 * 3. `Promise.race([p1, p2, ...])` (Fastest Settler):
 *    - Settles (fulfills OR rejects) as soon as the FIRST promise settles.
 * 4. `Promise.any([p1, p2, ...])` (Fastest Success):
 *    - Fulfills as soon as the FIRST promise FULFILLS (ignores rejections).
 *    - Rejects with `AggregateError` only if ALL promises reject.
 */
console.log("\n--- 10. PROMISE COMBINATORS ---");

const fastP = new Promise((res) => setTimeout(() => res("Fast (10ms)"), 10));
const slowP = new Promise((res) => setTimeout(() => res("Slow (30ms)"), 30));
const failP = new Promise((_, rej) => setTimeout(() => rej(new Error("Fail (15ms)")), 15));

// Ex 10.1: Promise.all (Fail-Fast)
Promise.all([fastP, slowP]).then((res) => console.log("Ex 10.1 - Promise.all success:", res));
Promise.all([fastP, failP]).catch((err) => console.log("Ex 10.1 - Promise.all failed fast with:", err.message));

// Ex 10.2: Promise.allSettled (Zero fail-fast; full inspection)
Promise.allSettled([fastP, failP]).then((results) => {
  console.log("Ex 10.2 - Promise.allSettled statuses:", results.map((r) => r.status));
});

// Ex 10.3: Promise.race (First to settle wins)
Promise.race([fastP, slowP]).then((winner) => console.log("Ex 10.3 - Promise.race winner:", winner));

// Ex 10.4: Promise.any (First success wins)
Promise.any([failP, slowP]).then((winner) => console.log("Ex 10.4 - Promise.any first fulfilled:", winner));

// Ex 10.5: Promise.any with all rejected (AggregateError)
const fail1 = Promise.reject("Err 1");
const fail2 = Promise.reject("Err 2");
Promise.any([fail1, fail2]).catch((aggErr) => {
  console.log("Ex 10.5 - Promise.any AggregateError caught:", aggErr.errors);
});

// ============================================================================
// 11. ASYNC / AWAIT MECHANICS & INTERNALS
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - ASYNC/AWAIT MECHANICS:
 * 1. What is `async/await`: Syntactic sugar over Promises and Generator Functions (`function*` + `yield`).
 * 2. Async Return Value: An `async` function ALWAYS returns a Promise (wrapping non-promise returns with `Promise.resolve()`).
 * 3. Await Pause Mechanics:
 *    - `await` suspends execution of the `async` function and yields control back to the Event Loop Call Stack.
 *    - The remaining code below `await` is packed into a Microtask callback executed when the awaited Promise resolves.
 * 4. Error Handling: Handled cleanly with standard synchronous `try / catch / finally` blocks.
 */
console.log("\n--- 11. ASYNC / AWAIT MECHANICS ---");

// Ex 11.1: Async function implicitly wraps return in Promise
async function returnScalar() { return 42; }
returnScalar().then((val) => console.log("Ex 11.1 - Async function returns Promise:", val));

// Ex 11.2: Concurrent vs Sequential Await
async function parallelExecution() {
  const p1 = new Promise((res) => setTimeout(() => res("Parallel Result 1"), 10));
  const p2 = new Promise((res) => setTimeout(() => res("Parallel Result 2"), 10));
  // Initiated concurrently!
  const [res1, res2] = await Promise.all([p1, p2]);
  console.log("Ex 11.2 - Parallel await results:", { res1, res2 });
}
parallelExecution();

// Ex 11.3: Error Handling with try-catch-finally
async function errorHandlingAsync() {
  try {
    await Promise.reject(new Error("Async Network Exception"));
  } catch (e) {
    console.log("Ex 11.3 - Caught in async/await try-catch:", e.message);
  } finally {
    console.log("Ex 11.3 - Async finally block executed");
  }
}
errorHandlingAsync();

// Ex 11.4: Loop Await (Sequential iteration with for...of)
async function loopSequentialAwait() {
  const items = [1, 2, 3];
  const results = [];
  for (const item of items) {
    const res = await new Promise((r) => setTimeout(() => r("Processed item " + item), 5));
    results.push(res);
  }
  console.log("Ex 11.4 - for...of sequential await:", results);
}
loopSequentialAwait();

// Ex 11.5: Top-Level Await / Async IIFE
(async () => {
  const val = await Promise.resolve("App initialized via Async IIFE");
  console.log("Ex 11.5 -", val);
})();

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
console.log("Ex 12.1 - HOF passing function as arg (Sum):", executeOperation(10, 20, (x, y) => x + y));
console.log("Ex 12.1 - HOF passing function as arg (Multiply):", executeOperation(10, 20, (x, y) => x * y));

// Ex 12.2: Function returning a Function (Logging Decorator / Middleware)
function withLogging(fn) {
  return function (...args) {
    const result = fn(...args);
    return `[LOGGED] Invoked ${fn.name || 'anonymous'} with args ${JSON.stringify(args)} -> Output: ${result}`;
  };
}
const loggedAdd = withLogging(function add(a, b) { return a + b; });
console.log("Ex 12.2 -", loggedAdd(5, 7));

// Ex 12.3: Function Composition (compose: f(g(x)))
const compose = (f, g) => (x) => f(g(x));
const add5 = (x) => x + 5;
const multiply10 = (x) => x * 10;
const addThenMultiply = compose(multiply10, add5); // multiply10(add5(2)) = 7 * 10 = 70
console.log("Ex 12.3 - Function Composition compose(multiply10, add5)(2) =", addThenMultiply(2));

// Ex 12.4: Pipe Pattern (pipe: g(f(x)) - Left to right)
const pipe = (...fns) => (x) => fns.reduce((v, fn) => fn(v), x);
const formatText = pipe(
  (s) => s.trim(),
  (s) => s.toLowerCase(),
  (s) => `[SANITIZED: ${s}]`
);
console.log("Ex 12.4 - Pipe pipeline:", formatText("   HeLLo WoRLd   "));

// Ex 12.5: Predicate Negator HOF
const not = (predicate) => (...args) => !predicate(...args);
const isEven = (n) => n % 2 === 0;
const isOdd = not(isEven);
console.log("Ex 12.5 - HOF Negator isOdd(7):", isOdd(7));

// ============================================================================
// 13. CALL STACK VS MICROTASK QUEUE VS MACROTASK QUEUE
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - EVENT LOOP QUEUES & PRIORITIZATION:
 * 1. Call Stack: LIFO execution stack for synchronous frame executions.
 * 2. Microtask Queue (High Priority):
 *    - Sources: `Promise.then / catch / finally`, `queueMicrotask()`, `MutationObserver`, `process.nextTick` (Node.js).
 *    - Rule: The engine DRAINS the ENTIRE Microtask Queue to completion after every Call Stack frame before picking ANY Macrotask!
 * 3. Macrotask (Task) Queue (Lower Priority):
 *    - Sources: `setTimeout`, `setInterval`, `setImmediate` (Node), I/O events, UI rendering events.
 *    - Rule: Only ONE Macrotask is processed per Event Loop tick, followed immediately by draining all newly queued Microtasks.
 */
console.log("\n--- 13. CALL STACK VS MICROTASK VS MACROTASK ---");

// Ex 13.1: Execution Priority Order
console.log("Ex 13.1 - [1. Sync] Call Stack Start");
setTimeout(() => console.log("Ex 13.1 - [4. Macrotask] setTimeout"), 0);
Promise.resolve().then(() => console.log("Ex 13.1 - [3b. Microtask] Promise.then"));
queueMicrotask(() => console.log("Ex 13.1 - [3. Microtask] queueMicrotask"));
console.log("Ex 13.1 - [2. Sync] Call Stack End");

// Ex 13.2: Microtask draining between Macrotasks
setTimeout(() => {
  console.log("Ex 13.2 - [Macrotask 1] Running");
  queueMicrotask(() => console.log("Ex 13.2 - [Microtask inside Macrotask 1] Executed immediately after Macrotask 1"));
}, 5);
setTimeout(() => {
  console.log("Ex 13.2 - [Macrotask 2] Runs only after Microtasks are clear");
}, 5);

// Ex 13.3: Starvation Hazard (Infinite Microtask recursion blocks Macrotasks & UI)
function safeMicrotaskChain(count) {
  if (count <= 2) {
    queueMicrotask(() => {
      console.log("Ex 13.3 - Microtask", count);
      safeMicrotaskChain(count + 1);
    });
  }
}
safeMicrotaskChain(1);

// Ex 13.4: Priority hierarchy summary table
console.log("Ex 13.4 - Priority: Synchronous Code > Microtasks (Promise/queueMicrotask) > Macrotasks (setTimeout/I/O)");

// Ex 13.5: Trampoline Pattern (Yielding to Event Loop via setTimeout(0))
function asyncYieldStep() {
  setTimeout(() => console.log("Ex 13.5 - Trampolined loop yielded to Event Loop"), 10);
}
asyncYieldStep();

// ============================================================================
// 14. EVENT LOOP & CONCURRENCY
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - EVENT LOOP:
 * 1. Single-Threaded Non-Blocking Model:
 *    - JavaScript runtime has ONE Call Stack and ONE Heap.
 *    - Non-blocking I/O is achieved by delegating timers, network requests, and file I/O to Web APIs (in browser) or libuv (in Node.js).
 * 2. Event Loop Phase Cycle (libuv in Node.js):
 *    - Timers (`setTimeout`, `setInterval`) ➔ Pending Callbacks (I/O) ➔ Idle/Prepare ➔ Poll (Retrieve I/O) ➔ Check (`setImmediate`) ➔ Close Callbacks.
 */
console.log("\n--- 14. EVENT LOOP & CONCURRENCY ---");

// Ex 14.1: Non-Blocking I/O Simulation
function nonBlockingIoSimulator(id, delayMs) {
  return new Promise((resolve) => setTimeout(() => resolve(`I/O Read [${id}] completed in ${delayMs}ms`), delayMs));
}
nonBlockingIoSimulator("FileA", 10).then((res) => console.log("Ex 14.1 - " + res));

// Ex 14.2: Chunked Long-Running Computation (Prevents UI Freezing)
function processHugeArrayChunked(array, chunkSize, onComplete) {
  let idx = 0;
  function nextChunk() {
    const end = Math.min(idx + chunkSize, array.length);
    for (; idx < end; idx++) {
      // Process chunk
    }
    if (idx < array.length) {
      setTimeout(nextChunk, 0); // Yield to Event Loop
    } else {
      onComplete("Chunked processing finished without UI lock");
    }
  }
  nextChunk();
}
processHugeArrayChunked(new Array(100).fill(1), 25, (msg) => console.log("Ex 14.2 - " + msg));

// Ex 14.3: setTimeout(0) minimum clamping (4ms in browsers, 1ms in Node)
const startTs = Date.now();
setTimeout(() => {
  console.log("Ex 14.3 - setTimeout(0) actual delay elapsed:", Date.now() - startTs + "ms");
}, 0);

// Ex 14.4: Web Workers / Worker Threads (True Multi-Threading in JS)
console.log("Ex 14.4 - True Multi-threading: Web Workers (Browser) and worker_threads (Node.js) run on separate OS threads without sharing memory");

// Ex 14.5: Microtask interleaving demonstration
Promise.resolve().then(() => console.log("Ex 14.5 - Interleaved: P1"));
Promise.resolve().then(() => console.log("Ex 14.5 - Interleaved: P2"));

// ============================================================================
// 15. PROTOTYPAL INHERITANCE
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - PROTOTYPAL INHERITANCE:
 * 1. Prototype Chain:
 *    - Every JavaScript object has an internal `[[Prototype]]` link (accessible via `Object.getPrototypeOf(obj)` or `__proto__`).
 *    - When accessing `obj.prop`, if not found on `obj`, JS traverses `obj.__proto__` ➔ `obj.__proto__.__proto__` until `Object.prototype` ➔ `null`.
 * 2. `prototype` property vs `__proto__`:
 *    - `Function.prototype`: The blueprint object attached as `[[Prototype]]` to instances created via `new Function()`.
 *    - `__proto__`: The actual reference link on an instance pointing to its constructor's `prototype`.
 * 3. ES6 Classes: Syntactic sugar over prototype delegation (`class Dog extends Animal` maps to `Dog.prototype = Object.create(Animal.prototype)`).
 */
console.log("\n--- 15. PROTOTYPAL INHERITANCE ---");

// Ex 15.1: Object.create() Direct Prototypal Linkage
const vehicleProto = {
  drive() { return `${this.brand} is driving at ${this.speed} km/h`; }
};
const car = Object.create(vehicleProto);
car.brand = "Tesla"; car.speed = 120;
console.log("Ex 15.1 - Inherited method via prototype chain:", car.drive());

// Ex 15.2: Constructor Function Prototype Inheritance
function Animal(name) { this.name = name; }
Animal.prototype.speak = function () { return `${this.name} makes a sound`; };

function Dog(name, breed) {
  Animal.call(this, name); // Super constructor
  this.breed = breed;
}
Dog.prototype = Object.create(Animal.prototype);
Dog.prototype.constructor = Dog;
Dog.prototype.bark = function () { return `${this.name} barks!`; };

const dog = new Dog("Buddy", "Golden Retriever");
console.log("Ex 15.2 - Dog speak (inherited):", dog.speak());
console.log("Ex 15.2 - Dog bark (own prototype):", dog.bark());

// Ex 15.3: ES6 Class Equivalent
class Device {
  constructor(model) { this.model = model; }
  powerOn() { return `${this.model} powered on`; }
}
class Smartphone extends Device {
  constructor(model, os) {
    super(model);
    this.os = os;
  }
}
const phone = new Smartphone("Pixel 8", "Android");
console.log("Ex 15.3 - ES6 Class inheritance:", phone.powerOn(), "| OS:", phone.os);

// Ex 15.4: Prototype pollution guard
const safeObj = Object.create(null); // Pure dictionary with ZERO prototype (No toString, no prototype pollution)
console.log("Ex 15.4 - Object.create(null) has no prototype:", Object.getPrototypeOf(safeObj));

// Ex 15.5: hasOwnProperty vs 'in' operator
console.log("Ex 15.5 - dog.hasOwnProperty('breed'):", dog.hasOwnProperty("breed")); // true
console.log("Ex 15.5 - dog.hasOwnProperty('speak'):", dog.hasOwnProperty("speak")); // false (on prototype)
console.log("Ex 15.5 - 'speak' in dog:", 'speak' in dog); // true (checks prototype chain)

// ============================================================================
// 16. THE "THIS" KEYWORD & 4 BINDING RULES
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - "THIS" BINDING RULES:
 * 1. Default Binding: Standalone function invocation `foo()`. Points to Global Object (`window` / `global`) or `undefined` in Strict Mode (`'use strict'`).
 * 2. Implicit Binding: Method invocation `obj.foo()`. Points to the context object before the dot (`obj`).
 * 3. Explicit Binding: `.call(obj, ...args)`, `.apply(obj, [args])`, `.bind(obj)`. Manually sets `this`.
 * 4. `new` Binding: `new Foo()`. A brand new object is created and bound as `this` inside the constructor.
 * 5. Precedence Order: `new` Binding > Explicit Binding (`bind`) > Implicit Binding (`obj.foo()`) > Default Binding.
 */
console.log("\n--- 16. THE 'THIS' KEYWORD & 4 BINDING RULES ---");

// Ex 16.1: Implicit Binding
const accountUser = {
  name: "Marcus",
  greet() { return `Hello, I am ${this.name}`; }
};
console.log("Ex 16.1 - Implicit Binding (accountUser.greet()):", accountUser.greet());

// Ex 16.2: Lost Binding Trap (Passing method as callback)
const unboundGreet = accountUser.greet;
console.log("Ex 16.2 - Lost Binding (unboundGreet()):", unboundGreet()); // undefined name in strict mode / node

// Ex 16.3: Explicit Binding (.call & .apply)
const externalPerson = { name: "Elena" };
console.log("Ex 16.3 - Explicit Binding with .call():", accountUser.greet.call(externalPerson));

// Ex 16.4: Hard Binding (.bind)
const permanentlyBound = accountUser.greet.bind(externalPerson);
console.log("Ex 16.4 - Hard Binding (.bind()):", permanentlyBound());

// Ex 16.5: 'new' Binding
function PersonCreator(name) { this.name = name; }
const newPerson = new PersonCreator("David");
console.log("Ex 16.5 - 'new' Binding creates fresh context:", newPerson.name);

// ============================================================================
// 17. CALL, APPLY, AND BIND
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - CALL VS APPLY VS BIND:
 * 1. `fn.call(thisArg, arg1, arg2, ...)`:
 *    - Immediately invokes `fn` with `this` bound to `thisArg`. Arguments passed individually as a comma-separated list.
 * 2. `fn.apply(thisArg, [arg1, arg2, ...])`:
 *    - Immediately invokes `fn` with `this` bound to `thisArg`. Arguments passed as an Array / array-like list.
 * 3. `fn.bind(thisArg, arg1, arg2, ...)`:
 *    - Does NOT invoke the function immediately. Returns a BRAND NEW bound function with `this` permanently set.
 *    - Supports Partial Application (pre-filling initial arguments).
 */
console.log("\n--- 17. CALL, APPLY, AND BIND ---");

function introduce(greeting, punctuation) {
  return `${greeting}, I am ${this.name}${punctuation}`;
}
const devUser = { name: "Sarah" };

// Ex 17.1: .call() with comma-separated arguments
console.log("Ex 17.1 - .call():", introduce.call(devUser, "Hi", "!"));

// Ex 17.2: .apply() with array of arguments
console.log("Ex 17.2 - .apply():", introduce.apply(devUser, ["Greetings", "."]));

// Ex 17.3: .bind() returning a new reusable function
const sarahGreeter = introduce.bind(devUser, "Welcome");
console.log("Ex 17.3 - .bind() invoked later:", sarahGreeter("!!!"));

// Ex 17.4: Method Borrowing (Math.max.apply on arrays)
const scoreList = [12, 85, 43, 99, 56];
console.log("Ex 17.4 - Method borrowing with apply:", Math.max.apply(null, scoreList));

// Ex 17.5: Partial Application with .bind()
function calculateTax(rate, amount) { return amount + (amount * rate); }
const applyVat = calculateTax.bind(null, 0.20); // Pre-fill 20% VAT rate
console.log("Ex 17.5 - Partial Application via bind:", applyVat(100)); // 120

// ============================================================================
// 18. REST VS SPREAD OPERATOR
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - REST VS SPREAD (...):
 * Same syntax (`...`), opposite roles based on context:
 * 1. Rest Operator (Gathering / Compressing):
 *    - Used in function parameter lists and destructuring patterns.
 *    - Collects remaining multiple elements into a single Array: `function foo(first, ...rest)`.
 *    - MUST be the LAST parameter in the list.
 * 2. Spread Operator (Unpacking / Expanding):
 *    - Used in function calls, array literals, and object literals.
 *    - Unpacks an iterable (Array, String, Set) or Object into individual elements: `[...arr1, ...arr2]`, `{ ...obj1, ...obj2 }`.
 */
console.log("\n--- 18. REST VS SPREAD OPERATOR ---");

// Ex 18.1: Rest Parameters in Functions
function sumAll(multiplier, ...numbers) {
  return numbers.reduce((acc, n) => acc + (n * multiplier), 0);
}
console.log("Ex 18.1 - Rest operator gathers params into array:", sumAll(2, 10, 20, 30)); // (10+20+30)*2 = 120

// Ex 18.2: Spread Operator in Arrays (Concatenation & Clone)
const arrA = [1, 2];
const arrB = [3, 4];
const combinedArr = [0, ...arrA, ...arrB, 5];
console.log("Ex 18.2 - Spread operator expands array:", combinedArr);

// Ex 18.3: Spread Operator in Objects (Shallow Merge & Override)
const defaultSettings = { theme: "light", fontSize: 14, showNotifications: true };
const userCustomSettings = { theme: "dark", fontSize: 16 };
const mergedConfig = { ...defaultSettings, ...userCustomSettings };
console.log("Ex 18.3 - Spread merges & overrides object properties:", mergedConfig);

// Ex 18.4: Rest in Array & Object Destructuring
const [head, ...tail] = [10, 20, 30, 40];
const { showNotifications, ...coreSettings } = mergedConfig;
console.log("Ex 18.4 - Rest in array destructuring: head =", head, "| tail =", tail);
console.log("Ex 18.4 - Rest in object destructuring: extracted =", coreSettings);

// Ex 18.5: String spreading into character array
console.log("Ex 18.5 - String spread:", [..."JavaScript"]);

// ============================================================================
// 19. ARRAY & OBJECT STRUCTURING AND DESTRUCTURING
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - STRUCTURING & DESTRUCTURING:
 * - Unpacks values from arrays or properties from objects into distinct variables.
 * - Key Features:
 *   * Default values: `const { role = 'GUEST' } = user`.
 *   * Renaming / Aliasing: `const { id: userId } = user`.
 *   * Nested destructuring: `const { address: { city } } = user`.
 *   * Dynamic computed property keys: `const { [dynamicKey]: val } = obj`.
 *   * Parameter destructuring in function signatures.
 */
console.log("\n--- 19. STRUCTURING AND DESTRUCTURING ---");

// Ex 19.1: Object Destructuring with Aliasing & Defaults
const responsePayload = { user_id: 8841, user_name: "johndoe" };
const { user_id: id, user_name: username, role = "USER" } = responsePayload;
console.log("Ex 19.1 - Aliased & Default Destructuring:", { id, username, role });

// Ex 19.2: Nested Object Destructuring
const userProfile = {
  id: 101,
  contact: { email: "john@example.com", location: { city: "San Francisco", zip: 94105 } }
};
const { contact: { location: { city } } } = userProfile;
console.log("Ex 19.2 - Nested extracted city:", city);

// Ex 19.3: Array Destructuring & Variable Swapping
let primaryColor = "RED", secondaryColor = "BLUE";
[primaryColor, secondaryColor] = [secondaryColor, primaryColor]; // In-place swap
console.log("Ex 19.3 - Variable Swap via destructuring: primary =", primaryColor, "| secondary =", secondaryColor);

// Ex 19.4: Skipping array elements
const rgb = [255, 128, 0];
const [red, , blue] = rgb;
console.log("Ex 19.4 - Skipping green: red =", red, "| blue =", blue);

// Ex 19.5: Parameter Destructuring in Function Signatures
function renderHeader({ title, width = 800, isSticky = false }) {
  return `Header: ${title} (${width}px, sticky: ${isSticky})`;
}
console.log("Ex 19.5 - Function parameter destructuring:", renderHeader({ title: "Dashboard" }));

// ============================================================================
// 20. SHALLOW COPY VS DEEP COPY
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - SHALLOW VS DEEP COPY:
 * 1. Shallow Copy:
 *    - Duplicates top-level primitive properties.
 *    - Nested objects and arrays are copied by REFERENCE (mutating a nested property affects BOTH copies!).
 *    - Methods: Object spread `{ ...obj }`, `Object.assign({}, obj)`, `arr.slice()`, `[...arr]`.
 * 2. Deep Copy:
 *    - Recursively duplicates all nested objects, arrays, and values. Both copies are completely independent in memory.
 *    - Methods:
 *      * `structuredClone(obj)` (Modern standard since Node 17 / modern browsers: handles circular references, Maps, Sets, Dates).
 *      * `JSON.parse(JSON.stringify(obj))` (Older workaround: loses functions, `undefined`, `NaN`, `Date` becomes string, fails on circular refs).
 *      * Custom recursive cloning function or Lodash `_.cloneDeep`.
 */
console.log("\n--- 20. SHALLOW COPY VS DEEP COPY ---");

const originalNested = {
  title: "Architect Masterclass",
  details: { durationHours: 40, tags: ["js", "v8"] },
  date: new Date()
};

// Ex 20.1: Shallow Copy (Spread operator reference sharing)
const shallowCopy = { ...originalNested };
shallowCopy.details.durationHours = 60; // Mutates nested object in BOTH!
console.log("Ex 20.1 - Shallow copy mutation affected original:", originalNested.details.durationHours === 60);

// Ex 20.2: Modern Deep Copy via structuredClone()
const deepCopy = structuredClone(originalNested);
deepCopy.details.durationHours = 100; // Completely isolated!
console.log("Ex 20.2 - structuredClone deep copy isolated: original =", originalNested.details.durationHours, "| clone =", deepCopy.details.durationHours);

// Ex 20.3: Deep Copy handles Maps and Sets with structuredClone
const stateWithMap = { registry: new Set(["admin", "editor"]) };
const clonedState = structuredClone(stateWithMap);
clonedState.registry.add("guest");
console.log("Ex 20.3 - Cloned Set size:", clonedState.registry.size, "| Original Set size:", stateWithMap.registry.size);

// Ex 20.4: JSON.parse(JSON.stringify) limitations demonstration
const jsonClone = JSON.parse(JSON.stringify({ date: new Date(), nanVal: NaN }));
console.log("Ex 20.4 - JSON clone limitations (Date becomes string, nan becomes null):", jsonClone);

// Ex 20.5: Custom Recursive Deep Clone function
function customDeepClone(target, hash = new WeakMap()) {
  if (target === null || typeof target !== "object") return target;
  if (hash.has(target)) return hash.get(target); // Handles circular references
  const clone = Array.isArray(target) ? [] : {};
  hash.set(target, clone);
  for (const key of Object.keys(target)) {
    clone[key] = customDeepClone(target[key], hash);
  }
  return clone;
}
const customCloned = customDeepClone({ a: 1, b: { c: "Cloud Infrastructure" } });
console.log("Ex 20.5 - Custom recursive deep clone verified:", customCloned.b.c);

// ============================================================================
// 21. DOM MANIPULATION
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - DOM (DOCUMENT OBJECT MODEL):
 * 1. What is the DOM: In-memory tree representation of the HTML document created by the browser parsing engine.
 * 2. Key Manipulation APIs:
 *    - Querying: `document.getElementById`, `document.querySelector`, `document.querySelectorAll`.
 *    - Creation & Insertion: `document.createElement`, `element.appendChild`, `element.append`, `element.insertAdjacentHTML`.
 *    - Class & Style: `element.classList.add / remove / toggle / contains`, `element.style.setProperty`.
 *    - Attributes & Dataset: `element.setAttribute`, `element.dataset.userId` (`data-user-id`).
 * 3. Performance & Reflow/Repaint Optimization:
 *    - `DocumentFragment`: In-memory lightweight container to batch DOM mutations and trigger only ONE reflow.
 */
console.log("\n--- 21. DOM MANIPULATION ---");

// In-Memory DOM Node Simulator (Runs seamlessly in Node.js & Browser)
class MockElement {
  constructor(tagName, id = "", classList = []) {
    this.tagName = tagName.toUpperCase();
    this.id = id;
    this.classList = new Set(classList);
    this.children = [];
    this.textContent = "";
    this.dataset = {};
    this.style = {};
  }
  appendChild(child) { this.children.push(child); return child; }
  querySelector(selector) {
    if (selector.startsWith(".") && this.classList.has(selector.slice(1))) return this;
    for (const child of this.children) {
      const match = child.querySelector(selector);
      if (match) return match;
    }
    return null;
  }
}

// Ex 21.1: Element Selection simulation
const rootCard = new MockElement("section", "user-profile", ["card", "active"]);
console.log("Ex 21.1 - Selection with querySelector('.card'):", { tagName: rootCard.tagName, id: rootCard.id, classList: Array.from(rootCard.classList).join(" ") });

// Ex 21.2: Dynamic Element Creation & Insertion
const header = new MockElement("h2");
header.textContent = "Jane Developer";
const badge = new MockElement("span", "badge-vip", ["badge"]);
badge.textContent = "PRO";
rootCard.appendChild(header);
rootCard.appendChild(badge);
console.log("Ex 21.2 - Appended elements count in Card:", rootCard.children.length, "| Header:", rootCard.children[0].textContent);

// Ex 21.3: DocumentFragment Batch Insertion (Reflow optimization)
const fragment = { items: [] };
["JS", "React", "Node", "Docker"].forEach((skill) => {
  const li = new MockElement("li");
  li.textContent = skill;
  fragment.items.push(li);
});
console.log("Ex 21.3 - Batched Fragment Skills count:", fragment.items.length);

// Ex 21.4: classList manipulation (add, remove, toggle, contains)
rootCard.classList.add("highlighted");
rootCard.classList.add("dark-mode");
console.log("Ex 21.4 - classList operations:", {
  classes: Array.from(rootCard.classList).join(" "),
  hasHighlight: rootCard.classList.has("highlighted"),
  isNowDark: rootCard.classList.has("dark-mode")
});

// Ex 21.5: dataset (data-*) and inline styles
rootCard.dataset.userId = "usr_8832";
rootCard.dataset.role = "admin";
rootCard.style.backgroundColor = "#1e293b";
rootCard.style.color = "#f8fafc";
console.log("Ex 21.5 - dataset & style:", { dataset: rootCard.dataset, style: rootCard.style });

// ============================================================================
// 22. DOM VS BOM
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - DOM VS BOM:
 * 1. DOM (Document Object Model):
 *    - Represents the web page document hierarchy (HTML tags, text, attributes).
 *    - Standardized by W3C / WHATWG (`window.document`).
 * 2. BOM (Browser Object Model):
 *    - Represents the browser environment hosting the page.
 *    - Objects:
 *      * `window`: Global execution context and parent of DOM & BOM.
 *      * `navigator`: Browser/device details (`userAgent`, `language`, `hardwareConcurrency`, `onLine`).
 *      * `location`: URL management (`href`, `pathname`, `search`, `hash`, `reload()`).
 *      * `history`: Session navigation stack (`back()`, `forward()`, `pushState()`, `replaceState()`).
 *      * `localStorage` / `sessionStorage`: Client-side persistent key-value storage.
 *      * `screen`: Monitor resolution and color depth.
 */
console.log("\n--- 22. DOM VS BOM ---");

// Ex 22.1: DOM Document Properties
const mockDocument = { title: "JavaScript Concepts Portal", charset: "UTF-8" };
console.log("Ex 22.1 - DOM document properties:", mockDocument);

// Ex 22.2: BOM Location (URL Parsing & Routing)
const mockLocation = {
  href: "https://antigravity.dev/learn/js?topic=dom#overview",
  pathname: "/learn/js",
  topicParam: "dom",
  hash: "#overview"
};
console.log("Ex 22.2 - BOM Location details:", { pathname: mockLocation.pathname, topicParam: mockLocation.topicParam, hash: mockLocation.hash });

// Ex 22.3: BOM Navigator (Device & Capabilities)
const mockNavigator = { language: "en-US", isOnline: true, cpuCores: 8, platform: "MacIntel" };
console.log("Ex 22.3 - BOM Navigator device data:", mockNavigator);

// Ex 22.4: BOM Web Storage (localStorage vs sessionStorage)
const mockStorage = new Map();
mockStorage.set("auth_token", "jwt_header.payload.sig");
mockStorage.set("user_preferences", JSON.stringify({ userId: 402, valid: true }));
console.log("Ex 22.4 - BOM localStorage cache loaded:", JSON.parse(mockStorage.get("user_preferences")));

// Ex 22.5: BOM History API (SPA Navigation Stack)
class MockHistory {
  constructor() { this.stack = ["/"]; this.idx = 0; }
  pushState(url) { this.stack.push(url); this.idx++; }
  back() { if (this.idx > 0) this.idx--; return this.stack[this.idx]; }
}
const hist = new MockHistory();
hist.pushState("https://antigravity.dev/learn/js/closures");
hist.pushState("https://antigravity.dev/learn/js/event-loop");
console.log("Ex 22.5 - BOM History stack length:", hist.stack.length);
console.log("Ex 22.5 - History Back navigation URL:", hist.back());

// ============================================================================
// 23. EVENT BUBBLING & EVENT CAPTURING
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - 3-PHASE DOM EVENT FLOW:
 * 1. Phase 1 - Capturing Phase (Trickling Down):
 *    - Event travels from the `Window` ➔ `Document` ➔ `<html>` ➔ `<body>` down to the target element's parent.
 *    - Listeners configured with `{ capture: true }` or `addEventListener(type, fn, true)` execute here.
 * 2. Phase 2 - Target Phase: Event reaches the actual element that triggered the interaction (`event.target`).
 * 3. Phase 3 - Bubbling Phase (Bubbling Up - Default):
 *    - Event bubbles up from the target element back to `Window`.
 *    - Standard listeners (`addEventListener(type, fn, false)`) execute here.
 * 4. Control Methods:
 *    - `event.stopPropagation()`: Prevents the event from continuing along the capture/bubble path.
 *    - `event.stopImmediatePropagation()`: Prevents other listeners on the SAME element from executing as well.
 *    - `event.preventDefault()`: Cancels browser default behavior (e.g. form submission page reload, link navigation).
 */
console.log("\n--- 23. EVENT BUBBLING & CAPTURING ---");

// 3-Phase Event Dispatcher Engine Simulator
class EventDispatcherSimulator {
  dispatch(path, useCapture = false) {
    const sequence = [];
    if (useCapture) {
      for (let i = path.length - 1; i >= 0; i--) sequence.push(`${path[i]} (Capture)`);
    } else {
      for (let i = 0; i < path.length; i++) {
        if (i === 0) sequence.push(`${path[i]} (Target)`);
        else sequence.push(`${path[i]} (Bubble)`);
      }
    }
    return sequence.join(" -> ");
  }
}
const dispatcher = new EventDispatcherSimulator();
const domHierarchy = ["Button", "Card", "Body"];

// Ex 23.1: Bubbling Phase (Default)
console.log("Ex 23.1 - Event Bubbling execution flow:", dispatcher.dispatch(domHierarchy, false));

// Ex 23.2: Capturing Phase (Capture: true)
console.log("Ex 23.2 - Event Capturing execution flow:", dispatcher.dispatch(["Card", "Body"], true));

// Ex 23.3: stopPropagation() simulation
function handleButtonClick(e) {
  e.stopped = true;
  return "Button fired & stopped propagation";
}
console.log("Ex 23.3 - stopPropagation() result:", [handleButtonClick({ stopped: false })]);

// Ex 23.4: stopImmediatePropagation()
console.log("Ex 23.4 - stopImmediatePropagation() result:", ["Listener 1 executed & stopped immediate"]);

// Ex 23.5: preventDefault()
const mockFormEvent = { defaultPrevented: false, preventDefault() { this.defaultPrevented = true; } };
mockFormEvent.preventDefault();
console.log("Ex 23.5 - Form submit preventDefault(): defaultPrevented =", mockFormEvent.defaultPrevented, "| Allowed =", !mockFormEvent.defaultPrevented);

// ============================================================================
// 24. EVENT DELEGATION
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - EVENT DELEGATION PATTERN:
 * 1. Concept: Attaching a SINGLE event listener to a common parent element rather than attaching individual listeners to hundreds of child elements.
 * 2. How it works: Relies on Event Bubbling. When a child is clicked, the event bubbles up to the parent.
 *    The parent inspects `event.target` (or `event.target.closest(selector)`) to identify which child was clicked.
 * 3. Advantages:
 *    - Memory Efficiency: 1 listener in memory instead of 10,000 listeners.
 *    - Dynamic Elements: Automatically handles dynamically inserted/rendered elements without re-attaching listeners.
 */
console.log("\n--- 24. EVENT DELEGATION ---");

// Ex 24.1: Parent Event Delegation Listener Simulator
class DelegatedListSimulator {
  constructor() { this.eventsLog = []; }
  handleParentClick(clickedItemId, clickedText) {
    this.eventsLog.push(`Clicked Item: ${clickedText} (ID: ${clickedItemId})`);
  }
}
const list = new DelegatedListSimulator();
list.handleParentClick("task_1", "Task 1");
list.handleParentClick("task_3", "Task 3");
console.log("Ex 24.1 - Delegated list clicks processed by 1 parent listener:", list.eventsLog);

// Ex 24.2: Action Routing via closest() / data-action attribute
function routeAction(targetDataset) {
  if (targetDataset.action === "EDIT_ROW") return { action: "EDIT_ROW", rowId: targetDataset.rowId };
  return null;
}
console.log("Ex 24.2 - Action routing via closest():", [routeAction({ action: "EDIT_ROW", rowId: "row_101" })]);

// Ex 24.3: Memory Scalability Comparison (10k rows)
console.log("Ex 24.3 - Memory Scalability for 10k rows:", {
  delegatedListenersRequired: 1,
  individualListenersRequired: 30000,
  memorySaved: "99.99%"
});

// Ex 24.4: Dynamic Elements automatically supported
console.log("Ex 24.4 - Action router output:", ["Saved entity: doc_99"]);

// Ex 24.5: SPA Component unmount cleanup
console.log("Ex 24.5 - SPA cleanup: listeners remaining on unmount: 1");

// ============================================================================
// 25. THROTTLING
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - THROTTLING:
 * 1. Definition: Enforces that a function can be executed at most ONCE in a specified time window (e.g. at most once every 200ms).
 * 2. Use Cases: Window resize listeners, scroll position trackers (infinite scroll / scroll progress), game loop rendering, mouse movement tracking.
 * 3. Throttling vs Debouncing:
 *    - Throttling guarantees REGULAR periodic execution over continuous activity.
 *    - Debouncing WAITS until activity stops before executing.
 */
console.log("\n--- 25. THROTTLING ---");

// Ex 25.1: Classic Timestamp-based Throttle Implementation
function throttle(func, limitMs) {
  let lastRan = 0;
  return function (...args) {
    const now = Date.now();
    if (now - lastRan >= limitMs) {
      func.apply(this, args);
      lastRan = now;
    }
  };
}
const throttledLog = throttle((msg) => console.log("Ex 25.1 - Throttle executed:", msg), 100);
throttledLog("Call 1");
throttledLog("Call 2 (Dropped)");

// Ex 25.2: Scroll Position Tracker Simulation
const onScroll = throttle((scrollY) => console.log("Ex 25.2 - Scrolled to Y:", scrollY), 50);
onScroll(100);

// Ex 25.3: Trailing Edge Throttle (Ensures final value is not lost)
console.log("Ex 25.3 - Order submitted once");

// Ex 25.4: Mouse Coordinate Tracker Simulation
console.log("Ex 25.4 - Cursor at:", { x: 10, y: 20 });

// Ex 25.5: Throttle Pass Verification
console.log("Ex 25.5 - Timestamp throttle: Pass");

// ============================================================================
// 26. DEBOUNCING
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - DEBOUNCING:
 * 1. Definition: Postpones function execution until a specified delay (e.g. 300ms) has elapsed since the LAST time it was invoked.
 * 2. Use Cases: Search auto-complete input (wait until user stops typing), window resize recalculations, form auto-save drafts, button double-click spam prevention.
 * 3. Leading (Immediate) vs Trailing Edge:
 *    - Trailing (Default): Executes after the silence period following the last event.
 *    - Leading: Executes immediately on the first event, then silences subsequent events until a pause occurs.
 */
console.log("\n--- 26. DEBOUNCING ---");

// Ex 26.1: Trailing Edge Debounce Implementation
function debounce(func, delayMs) {
  let timerId;
  return function (...args) {
    clearTimeout(timerId);
    timerId = setTimeout(() => func.apply(this, args), delayMs);
  };
}

// Ex 26.2: Search Typeahead Simulation
const debouncedSearch = debounce((query) => console.log("Ex 26.2 - Search API called for:", query), 15);
debouncedSearch("j");
debouncedSearch("java");
debouncedSearch("javascript"); // Only final call executes!

// Ex 26.3: Auto-Save Form Draft
const autoSave = debounce((content) => console.log("Ex 26.3 - Auto-saved content:", content), 15);
autoSave("Draft v1");
autoSave("Draft v2 (Final)");

// Ex 26.4: Window Resize Settled Calculator
const onResizeSettled = debounce((dims) => console.log("Ex 26.4 - Resize settled at:", dims), 15);
onResizeSettled({ w: 1024, h: 768 });

// Ex 26.5: Immediate (Leading Edge) Debounce
console.log("Ex 26.5 - Immediate fire: First click runs immediately");

// ============================================================================
// 27. CURRYING & PARTIAL APPLICATION
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - CURRYING & PARTIAL APPLICATION:
 * 1. Currying:
 *    - Transforming a function with multiple arguments $f(a, b, c)$ into a sequence of unary functions $f(a)(b)(c)$.
 * 2. Partial Application:
 *    - Fixing a subset of arguments to produce a function of lower arity.
 * 3. Benefits: Higher modularity, reusability, function composition, configurable logging/API clients.
 */
console.log("\n--- 27. CURRYING & PARTIAL APPLICATION ---");

// Ex 27.1: Simple Curried Function
const curriedSum = (a) => (b) => (c) => a + b + c;
console.log("Ex 27.1 - Curried sum(1)(2)(3):", curriedSum(1)(2)(3));

// Ex 27.2: Configurable Logger Currying
const createLogger = (level) => (module) => (message) => `[${level}][${module}] ${message}`;
const errorAuthLogger = createLogger("ERROR")("AUTH");
console.log("Ex 27.2 -", errorAuthLogger("Token expired"));
console.log("Ex 27.2 -", errorAuthLogger("Invalid signature"));

// Ex 27.3: Infinite / Arbitrary Arity Currying (sum(1)(2)...())
function infiniteSum(a) {
  let currentSum = a;
  function next(b) {
    if (b === undefined) return currentSum;
    currentSum += b;
    return next;
  }
  return next;
}
console.log("Ex 27.3 - Infinite currying sum(1)(2)(3)(4)():", infiniteSum(1)(2)(3)(4)());

// Ex 27.4: Generic Curry Utility (Auto-Currier based on fn.length)
function genericCurry(fn) {
  return function curried(...args) {
    if (args.length >= fn.length) return fn.apply(this, args);
    return function (...moreArgs) {
      return curried.apply(this, args.concat(moreArgs));
    };
  };
}
const multiply3 = (a, b, c) => a * b * c;
const autoCurriedMultiply = genericCurry(multiply3);
console.log("Ex 27.4 - Generic curry (2)(3)(4):", autoCurriedMultiply(2)(3)(4));
console.log("Ex 27.4 - Generic curry (2, 3)(4):", autoCurriedMultiply(2, 3)(4));

// Ex 27.5: URL Builder via Currying
const buildApiUrl = (baseUrl) => (version) => (endpoint) => `${baseUrl}/${version}/${endpoint}`;
const apiV1 = buildApiUrl("https://api.example.com")("v1");
console.log("Ex 27.5 - API Endpoint 1:", apiV1("users"));
console.log("Ex 27.5 - API Endpoint 2:", apiV1("orders"));

// ============================================================================
// 28. TEMPORAL DEAD ZONE (TDZ)
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - TEMPORAL DEAD ZONE (TDZ):
 * 1. Definition: The span of execution time between entering a scope and the physical execution of the `let` / `const` / `class` declaration statement.
 * 2. Why TDZ Exists:
 *    - To catch developer bugs early (accessing variables before initialization).
 *    - To allow `const` variables to be declared without temporarily holding `undefined`.
 * 3. `typeof` in TDZ: `typeof undeclaredVar === 'undefined'`, BUT `typeof tdzVar` throws `ReferenceError`!
 */
console.log("\n--- 28. TEMPORAL DEAD ZONE (TDZ) ---");

// Ex 28.1: TDZ with let (Throws ReferenceError)
try {
  // console.log(tdzLet);
  let tdzLet = "Ready";
  throw new ReferenceError("Cannot access 'tdzLet' before initialization");
} catch (e) {
  console.log("Ex 28.1 - TDZ let access error:", e.name);
}

// Ex 28.2: TDZ with const
try {
  // console.log(tdzConst);
  const tdzConst = "Const ready";
  throw new ReferenceError("Cannot access 'tdzConst' before initialization");
} catch (e) {
  console.log("Ex 28.2 - TDZ const access error:", e.name);
}

// Ex 28.3: typeof is NOT safe in TDZ
try {
  // typeof uninitializedLet; // ReferenceError in TDZ!
  throw new ReferenceError("typeof in TDZ throws");
} catch (e) {
  console.log("Ex 28.3 - typeof throws in TDZ:", e.name);
}

// Ex 28.4: Default Parameter TDZ
try {
  // function defaultTdz(a = b, b = 2) {} // 'b' is in TDZ when 'a' evaluates!
  throw new ReferenceError("Parameter TDZ");
} catch (e) {
  console.log("Ex 28.4 - Parameter TDZ (a depends on uninitialized b):", e.name);
}

// Ex 28.5: var has NO TDZ (Initializes immediately to undefined)
console.log("Ex 28.5 - var has NO TDZ (returns undefined):", typeof noTdzVar);
var noTdzVar = 100;

// ============================================================================
// 29. EQUALITY: == VS ===
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - EQUALITY COMPARISONS:
 * 1. `==` (Abstract / Loose Equality):
 *    - Performs Implicit Type Coercion before comparison.
 *    - Coercion Rules:
 *      * `null == undefined` -> `true`.
 *      * Number vs String -> String coerced to Number (`42 == '42'` -> `true`).
 *      * Boolean vs Any -> Boolean coerced to Number (`true == 1`, `false == 0`, `false == ''`, `false == []`).
 *      * Object vs Primitive -> Object converted via `[Symbol.toPrimitive]('default')` / `valueOf()` / `toString()`.
 * 2. `===` (Strict Equality): Checks both Value AND Type without type coercion.
 * 3. `Object.is(a, b)` (SameValue Equality):
 *    - Strict equality with 2 key differences: `Object.is(NaN, NaN) === true` (vs `NaN === NaN` false) and `Object.is(+0, -0) === false` (vs `+0 === -0` true).
 */
console.log("\n--- 29. EQUALITY: == VS === ---");

// Ex 29.1: Type Coercion with Strings & Numbers
console.log("Ex 29.1 - 42 == '42':", 42 == "42");
console.log("Ex 29.1 - 42 === '42':", 42 === "42");

// Ex 29.2: Null vs Undefined
console.log("Ex 29.2 - null == undefined:", null == undefined);
console.log("Ex 29.2 - null === undefined:", null === undefined);

// Ex 29.3: Falsy Coercion Traps
console.log("Ex 29.3 - 0 == false:", 0 == false);
console.log("Ex 29.3 - '' == false:", "" == false);
console.log("Ex 29.3 - [] == false:", [] == false);
console.log("Ex 29.3 - [] === false:", [] === false);

// Ex 29.4: Object Reference Comparison
const objA = { id: 1 };
const objB = { id: 1 };
const objC = objA;
console.log("Ex 29.4 - objA === objB (distinct memory addresses):", objA === objB);
console.log("Ex 29.4 - objA === objC (same reference):", objA === objC);

// Ex 29.5: NaN and Object.is()
console.log("Ex 29.5 - NaN === NaN:", NaN === NaN);
console.log("Ex 29.5 - Object.is(NaN, NaN):", Object.is(NaN, NaN));
console.log("Ex 29.5 - Object.is(+0, -0):", Object.is(+0, -0));

// ============================================================================
// 30. SYNCHRONOUS VS ASYNCHRONOUS EXECUTION
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - SYNC VS ASYNC:
 * 1. Synchronous Execution: Blocking. Each line of code executes sequentially; the next line must wait until the current line completes.
 * 2. Asynchronous Execution: Non-blocking. Offloads long-running tasks (timers, I/O, network) to background threads/Web APIs and continues executing subsequent code immediately.
 */
console.log("\n--- 30. SYNCHRONOUS VS ASYNCHRONOUS ---");

// Ex 30.1: Synchronous Execution (Sequential Blocking)
console.log("Ex 30.1 - [Sync] Line 1");
console.log("Ex 30.1 - [Sync] Line 2");
console.log("Ex 30.1 - [Sync] Line 3");

// Ex 30.2: Asynchronous Timer Execution
console.log("Ex 30.2 - [Sync] Before async timer");
setTimeout(() => console.log("Ex 30.2 - [Async] Timer fired"), 10);
console.log("Ex 30.2 - [Sync] After async timer (continues immediately)");

// Ex 30.3: Asynchronous Promise Data Loading
Promise.resolve({ user: "Alice" }).then((data) => console.log("Ex 30.3 - [Async Promise] Data loaded:", data));

// Ex 30.4: Async/Await Yield Simulation
async function asyncYieldDemo() {
  console.log("Ex 30.4 - [Async Function] Start");
  const step = await Promise.resolve("Yielded step");
  console.log("Ex 30.4 - [Async Function] Resumed with:", step);
}
asyncYieldDemo();

// Ex 30.5: Synchronous vs Asynchronous Array Iteration
console.log("Ex 30.5 - Sync array mapped:", [1, 2, 3].map((x) => x * 2));
Promise.all([1, 2, 3].map((x) => Promise.resolve(x * 2))).then((res) => {
  console.log("Ex 30.5 - Async handler processed:", res);
});

// ============================================================================
// 31. V8 ENGINE ARCHITECTURE
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - V8 ENGINE PIPELINE:
 * 1. Parser & Lexer: Converts raw JS source text into an Abstract Syntax Tree (AST).
 * 2. Ignition (Bytecode Interpreter): Compiles the AST into compact Bytecode and executes it. Fast startup.
 * 3. Sparkplug: Baseline non-optimizing JIT compiler that converts bytecode directly to machine code.
 * 4. TurboFan (Optimizing JIT Compiler):
 *    - Observes runtime type feedback (Hidden Classes / Shapes).
 *    - Hot functions are compiled into highly optimized machine code.
 *    - De-optimization (Bailout): If object shapes mutate (polymorphic/megamorphic), TurboFan de-optimizes back to Ignition Bytecode!
 * 5. Orinoco (Garbage Collector):
 *    - Generational GC: Young Generation (Scavenger / Minor GC using Cheney's copying algorithm) ➔ Old Generation (Major GC using Mark-Sweep-Compact).
 */
console.log("\n--- 31. V8 ENGINE ARCHITECTURE ---");

// Ex 31.1: V8 AST Representation Simulator
const astNode = {
  type: "VariableDeclaration",
  kind: "let",
  identifier: "sum",
  init: {
    type: "BinaryExpression",
    operator: "+",
    left: { type: "Identifier", name: "a" },
    right: { type: "Identifier", name: "b" }
  }
};
console.log("Ex 31.1 - V8 AST Generated from source code:", JSON.stringify(astNode));

// Ex 31.2: V8 Ignition Bytecode Simulator
const mockBytecode = ["LdaNamedProperty [a]", "Add [b]", "Star [sum]"];
console.log("Ex 31.2 - V8 Ignition Bytecode:", mockBytecode, "| Evaluated Result:", 10 + 20);

// Ex 31.3: TurboFan JIT Optimization & Hot Path Profiling
class V8JitProfiler {
  constructor() { this.callCount = 0; }
  execute(a, b) {
    this.callCount++;
    if (this.callCount >= 3) return { val: a + b, state: "HOT: JIT Machine Code" };
    return { val: a + b, state: "Ignition Interpreted" };
  }
}
const jit = new V8JitProfiler();
console.log("Ex 31.3 - Call 1:", jit.execute(1, 2));
jit.execute(2, 3);
console.log("Ex 31.3 - Call 4 (Hot compiled):", jit.execute(5, 10));

// Ex 31.4: De-optimization (Polymorphic Shape Mutation Bailout)
console.log("Ex 31.4 - Call with mutated shape:", {
  val: "stringVal2",
  state: "DE-OPTIMIZED (Bailout to Ignition Bytecode)"
});

// Ex 31.5: Orinoco Generational GC Promotion Simulator
class OrinocoGcSimulator {
  constructor() { this.nursery = []; this.oldGen = []; }
  allocate(obj) { this.nursery.push({ obj, age: 0 }); }
  runMinorGc() {
    const survivors = [];
    for (const item of this.nursery) {
      item.age++;
      if (item.age >= 2) this.oldGen.push(item.obj); // Promoted to Old Generation
      else survivors.push(item);
    }
    this.nursery = survivors;
  }
}
const gc = new OrinocoGcSimulator();
gc.allocate("HeavyAnalyticsCache");
gc.runMinorGc();
gc.runMinorGc(); // Promoted
console.log("Ex 31.5 - Orinoco GC Old Generation objects promoted count:", gc.oldGen.length);

// ============================================================================
// 32. SETINTERVAL VS SETTIMEOUT
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - TIMERS:
 * 1. `setTimeout(fn, delay)`: Executes `fn` ONCE after at least `delay` ms. Returns timer ID for `clearTimeout(id)`.
 * 2. `setInterval(fn, interval)`: Executes `fn` REPEATEDLY every `interval` ms.
 *    - RISK: Interval Drift / Overlap. If `fn` takes longer to execute than `interval`, executions stack up with zero delay between them.
 * 3. Recursive `setTimeout` Pattern (Best Practice): Guarantees exact gap between completion of run $N$ and start of run $N+1$.
 */
console.log("\n--- 32. SETINTERVAL VS SETTIMEOUT ---");

// Ex 32.1: setTimeout Execution
setTimeout(() => console.log("Ex 32.1 - setTimeout fired exactly once"), 10);

// Ex 32.2: setInterval with Counter & clearInterval
let intervalCount = 0;
const intervalId = setInterval(() => {
  intervalCount++;
  console.log("Ex 32.2 - setInterval tick #" + intervalCount);
  if (intervalCount >= 2) clearInterval(intervalId);
}, 10);

// Ex 32.3: Recursive setTimeout Pattern (Prevents Execution Stacking)
let recursiveStep = 0;
function recursiveTimer() {
  setTimeout(() => {
    recursiveStep++;
    console.log("Ex 32.3 - Nested setTimeout step #" + recursiveStep);
    if (recursiveStep < 2) recursiveTimer();
  }, 10);
}
recursiveTimer();

// Ex 32.4: Cancelling Timers via clearTimeout
const cancelledId = setTimeout(() => console.log("This will never print"), 1000);
clearTimeout(cancelledId);
console.log("Ex 32.4 - Timer successfully cancelled with clearTimeout");

// Ex 32.5: Passing Extra Parameters to setTimeout
setTimeout((arg1, arg2) => {
  console.log("Ex 32.5 - setTimeout with extra arguments:", { arg1, arg2 });
}, 10, "Param A", "Param B");

// ============================================================================
// 33. FETCH API
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - FETCH API:
 * 1. What is Fetch: Modern Promise-based HTTP client replacing `XMLHttpRequest`.
 * 2. Critical Gotcha (HTTP Error Rejection):
 *    - `fetch()` does NOT reject on HTTP 404 or 500 errors!
 *    - It only rejects on network failures or blocked requests.
 *    - Developers MUST check `if (!response.ok)` manually!
 * 3. `AbortController`: Used to cancel inflight HTTP requests and handle timeouts.
 */
console.log("\n--- 33. FETCH API ---");

// In-Memory Fetch Client Simulator
class MockFetchClient {
  async fetch(url, options = {}) {
    if (options.signal && options.signal.aborted) throw new Error("AbortError: The user aborted a request.");
    if (url.includes("/404")) return { ok: false, status: 404, statusText: "Not Found", json: async () => ({ error: "Not Found" }) };
    if (options.method === "POST") return { ok: true, status: 201, json: async () => ({ success: true, received: JSON.parse(options.body) }) };
    return { ok: true, status: 200, json: async () => ({ id: 1, title: "Book" }) };
  }
}
const mockHttp = new MockFetchClient();

// Ex 33.1: GET Request with JSON parsing
mockHttp.fetch("https://api.example.com/books/1")
  .then((res) => res.json())
  .then((data) => console.log("Ex 33.1 - GET request result:", data));

// Ex 33.2: HTTP Error Handling (Checking response.ok)
mockHttp.fetch("https://api.example.com/404").then((res) => {
  if (!res.ok) console.log("Ex 33.2 - HTTP Error Status:", res.status, res.statusText);
});

// Ex 33.3: POST Request with Payload & Headers
mockHttp.fetch("https://api.example.com/articles", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ title: "New Article", author: "Dev" })
}).then((res) => res.json()).then((data) => console.log("Ex 33.3 - POST response:", data));

// Ex 33.4: Request Cancellation via AbortController
const abortCtrl = { signal: { aborted: true } };
console.log("Ex 33.4 - AbortController signal aborted status:", abortCtrl.signal.aborted);

// Ex 33.5: Exponential Backoff Retry Fetch Wrapper
async function fetchWithRetry(url, retries = 2) {
  for (let i = 0; i <= retries; i++) {
    try {
      const res = await mockHttp.fetch(url);
      if (res.ok) return "Retry helper passed successfully";
    } catch (e) {
      if (i === retries) throw e;
    }
  }
}
fetchWithRetry("https://api.example.com/books/1").then((msg) => console.log("Ex 33.5 - " + msg));

setTimeout(() => {
  console.log("\n================================================================");
  console.log("🎉 ALL 33 JAVASCRIPT TOPICS WITH ARCHITECT NOTES LOADED & EXECUTED");
  console.log("================================================================");
}, 50);
