/**
 * ============================================================================
 * JAVASCRIPT MASTER CHEATSHEET & CONCEPTUAL DEEP-DIVE (my_js.js)
 * ============================================================================
 * Comprehensive, production-grade guide with 4 to 5 in-depth RUNNABLE code
 * implementations for EACH of the 33 essential JavaScript topics.
 *
 * NOTE: For browser-specific APIs (DOM, BOM, Events, V8 internals), full
 * executable mock engines, AST parsers, and event dispatchers are implemented
 * in-memory so every single example runs natively in Node.js and Browser environments!
 *
 * TABLE OF CONTENTS:
 *  1. let vs const vs var
 *  2. undeclared vs undefined vs null vs defined
 *  3. Global, Local, Functional, & Lexical Scope
 *  4. Closure vs Hoisting
 *  5. Arrow Functions vs Regular Functions
 *  6. Array Methods: map, filter, reduce (reducer)
 *  7. Callback Hell & Inversion of Control
 *  8. Promises Lifecycle (Pending, Fulfilled, Rejected)
 *  9. Promise Creation & Error Handling Strategies
 * 10. Promise Combinators (all, allSettled, race, any)
 * 11. Async / Await Mechanics & Internals
 * 12. Higher-Order Functions (HOF)
 * 13. Call Stack vs Microtask Queue vs Macrotask Queue
 * 14. Event Loop & Single-Threaded Concurrency
 * 15. Prototypal Inheritance & Prototype Chain
 * 16. The "this" Keyword & Binding Rules
 * 17. call, apply, and bind
 * 18. Rest Operator vs Spread Operator
 * 19. Array & Object Structuring and Destructuring
 * 20. Shallow Copy vs Deep Copy
 * 21. DOM Manipulation (Selectors, Mutation, Fragments, Classes, Styles)
 * 22. DOM vs BOM (Window, Location, Navigator, Storage, History, Screen)
 * 23. Event Bubbling & Event Capturing (3-Phase Dispatcher Engine)
 * 24. Event Delegation Pattern (Dynamic Elements & Closest Traversal)
 * 25. Throttling (Rate Limiting Implementations)
 * 26. Debouncing (Trailing, Leading, & Auto-Save Implementations)
 * 27. Currying & Partial Application
 * 28. Temporal Dead Zone (TDZ)
 * 29. Equality: == vs === (Type Coercion & Object.is)
 * 30. Synchronous vs Asynchronous Execution
 * 31. V8 Engine Architecture (AST Lexer, Ignition Bytecode, TurboFan JIT, GC)
 * 32. setInterval vs setTimeout
 * 33. Fetch API (GET, POST, Error Handling, AbortController, Retry)
 * ============================================================================
 */

console.log("================================================================");
console.log("🚀 STARTING JAVASCRIPT MASTER REFERENCE (4-5 EXAMPLES PER TOPIC)");
console.log("================================================================\n");


// ============================================================================
// 1. LET VS CONST VS VAR
// ============================================================================
console.log("--- 1. LET VS CONST VS VAR ---");
/**
 * Summary:
 * - var: Function scoped, hoisted (initialized with undefined), re-declarable, attaches to global object.
 * - let: Block scoped {}, hoisted (in TDZ), cannot be re-declared, can be re-assigned.
 * - const: Block scoped {}, hoisted (in TDZ), cannot be re-declared or re-assigned (bindings are constant).
 */

// Example 1.1: Scope difference (Function vs Block Scope)
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

// Example 1.2: Re-declaration vs Re-assignment
function ex1_2_redeclaration() {
  var x = 10;
  var x = 20; // Allowed with var!
  let y = 10;
  // let y = 20; // SyntaxError: Identifier 'y' has already been declared
  y = 25; // Re-assignment is fine
  console.log("Ex 1.2 - Re-declared var:", x, "| Re-assigned let:", y);
}
ex1_2_redeclaration();

// Example 1.3: const Reference vs Mutation
const ex1_3_user = { id: 101, name: "Alpha" };
ex1_3_user.name = "Beta"; // Allowed: mutating property inside the object
console.log("Ex 1.3 - const object property mutation:", ex1_3_user);
try {
  // ex1_3_user = { id: 102 }; // TypeError: Assignment to constant variable
} catch (e) {
  console.log("Ex 1.3 - const reassignment error:", e.message);
}

// Example 1.4: Loop scoping trap (var sharing single binding vs let fresh binding)
function ex1_4_loopScoping() {
  const varCallbacks = [];
  const letCallbacks = [];
  for (var i = 0; i < 3; i++) {
    varCallbacks.push(() => i);
  }
  for (let j = 0; j < 3; j++) {
    letCallbacks.push(() => j);
  }
  console.log("Ex 1.4 - var in loop closure (all share final value 3):", varCallbacks.map((fn) => fn()));
  console.log("Ex 1.4 - let in loop closure (fresh binding per iteration):", letCallbacks.map((fn) => fn()));
}
ex1_4_loopScoping();

// Example 1.5: Global Object property attachment
var globalVarTest = "I attach to window in browser";
let globalLetTest = "I do NOT attach to window";
console.log("Ex 1.5 - var creates global property on global scope:", typeof global !== "undefined" ? "Tested in Node" : window.globalVarTest);


// ============================================================================
// 2. UNDECLARED VS UNDEFINED VS NULL VS DEFINED
// ============================================================================
console.log("\n--- 2. UNDECLARED VS UNDEFINED VS NULL VS DEFINED ---");

// Example 2.1: undefined - uninitialized variable & function without return
let ex2_1_uninit;
function ex2_1_noReturn() {}
console.log("Ex 2.1 - Uninitialized let:", ex2_1_uninit);
console.log("Ex 2.1 - Function return without value:", ex2_1_noReturn());

// Example 2.2: null - intentional absence of an object value
let ex2_2_selectedUser = null; // No user selected currently
console.log("Ex 2.2 - null value:", ex2_2_selectedUser, "| typeof null:", typeof ex2_2_selectedUser, "(historical bug)");

// Example 2.3: undeclared - accessing a variable not declared in any scope
try {
  console.log(ex2_3_nonExistent);
} catch (err) {
  console.log("Ex 2.3 - Undeclared variable access:", err.name, "-", err.message);
}

// Example 2.4: defined check using typeof & Nullish Coalescing (??)
let ex2_4_val1 = undefined;
let ex2_4_val2 = 0;
console.log("Ex 2.4 - Default with ?? (undefined):", ex2_4_val1 ?? "Default Value");
console.log("Ex 2.4 - Default with ?? (0 is defined):", ex2_4_val2 ?? 50); // Keeps 0!

// Example 2.5: Optional chaining (?.) on null/undefined properties
const ex2_5_profile = { settings: null };
console.log("Ex 2.5 - Optional chaining on null:", ex2_5_profile.settings?.theme ?? "default-theme");


// ============================================================================
// 3. GLOBAL, LOCAL, FUNCTIONAL, & LEXICAL SCOPE
// ============================================================================
console.log("\n--- 3. SCOPES: GLOBAL, LOCAL, FUNCTIONAL, LEXICAL ---");

// Example 3.1: Global Scope
const ex3_1_globalAppConfig = { version: "2.4.0", env: "production" };
function getVersion() {
  return `Ex 3.1 - Version from global: ${ex3_1_globalAppConfig.version}`;
}
console.log(getVersion());

// Example 3.2: Functional Scope (var confined to function)
function ex3_2_funcScope() {
  var secretKey = "SECRET_123";
  return `Ex 3.2 - Inside function: ${secretKey}`;
}
console.log(ex3_2_funcScope());
try {
  console.log(secretKey);
} catch (e) {
  console.log("Ex 3.2 - secretKey cannot leak outside function scope:", e.name);
}

// Example 3.3: Block Scope (let/const inside if/switch/for)
{
  const blockScopedItem = "Block Private Value";
  console.log("Ex 3.3 - Inside block:", blockScopedItem);
}
try {
  console.log(blockScopedItem);
} catch (e) {
  console.log("Ex 3.3 - Outside block:", e.name);
}

// Example 3.4: Lexical Scope lookup chain (static nesting in code)
const ex3_4_level0 = "Level 0 (Global)";
function ex3_4_outer() {
  const ex3_4_level1 = "Level 1 (Outer)";
  function ex3_4_middle() {
    const ex3_4_level2 = "Level 2 (Middle)";
    function ex3_4_inner() {
      console.log("Ex 3.4 - Lexical Chain:", [ex3_4_level0, ex3_4_level1, ex3_4_level2].join(" -> "));
    }
    ex3_4_inner();
  }
  ex3_4_middle();
}
ex3_4_outer();

// Example 3.5: Variable Shadowing
const ex3_5_shadow = "Global Shadow Value";
function ex3_5_shadowDemo() {
  const ex3_5_shadow = "Local Function Shadow Value"; // Shadows outer variable
  console.log("Ex 3.5 - Shadowed variable:", ex3_5_shadow);
}
ex3_5_shadowDemo();
console.log("Ex 3.5 - Outer variable remains intact:", ex3_5_shadow);


// ============================================================================
// 4. CLOSURE VS HOISTING
// ============================================================================
console.log("\n--- 4. CLOSURE VS HOISTING ---");

// Example 4.1: Hoisting of Function Declaration vs Function Expression
console.log("Ex 4.1 - Hoisted function call:", ex4_1_hoistedFunc()); // Works!
function ex4_1_hoistedFunc() {
  return "I am fully hoisted!";
}
try {
  ex4_1_funcExpression(); // Throws TypeError or ReferenceError
} catch (e) {
  console.log("Ex 4.1 - Function expression in var/let not hoisted before assignment:", e.name);
}
var ex4_1_funcExpression = function () {
  return "Not hoisted";
};

// Example 4.2: Hoisting with var vs let/const
console.log("Ex 4.2 - var hoisted as undefined:", ex4_2_var); // undefined
var ex4_2_var = "Assigned now";

// Example 4.3: Closure - State Encapsulation / Private Counter
function ex4_3_createCounter(start = 0) {
  let count = start; // Private variable closed over
  return {
    inc: () => ++count,
    dec: () => --count,
    val: () => count,
  };
}
const ex4_3_c1 = ex4_3_createCounter(100);
console.log("Ex 4.3 - Closure Counter increment:", ex4_3_c1.inc());
console.log("Ex 4.3 - Closure Counter increment:", ex4_3_c1.inc());
console.log("Ex 4.3 - Closure Counter value:", ex4_3_c1.val());

// Example 4.4: Closure - Function Factory with Multipliers
function ex4_4_multiplier(factor) {
  return (x) => x * factor;
}
const double = ex4_4_multiplier(2);
const quadruple = ex4_4_multiplier(4);
console.log("Ex 4.4 - Double 15:", double(15));
console.log("Ex 4.4 - Quadruple 15:", quadruple(15));

// Example 4.5: Closure - Memoization / Performance Cache
function ex4_5_memoize(fn) {
  const cache = {}; // Preserved across calls via closure
  return function (n) {
    if (n in cache) {
      return { result: cache[n], fromCache: true };
    }
    const res = fn(n);
    cache[n] = res;
    return { result: res, fromCache: false };
  };
}
const slowSquare = (n) => n * n;
const fastSquare = ex4_5_memoize(slowSquare);
console.log("Ex 4.5 - Fast square 9 (first run):", fastSquare(9));
console.log("Ex 4.5 - Fast square 9 (cached run):", fastSquare(9));


// ============================================================================
// 5. ARROW FUNCTIONS VS REGULAR FUNCTIONS
// ============================================================================
console.log("\n--- 5. ARROW FUNCTIONS VS REGULAR FUNCTIONS ---");

// Example 5.1: 'this' binding in Object Methods
const ex5_1_obj = {
  name: "Service A",
  regularFn: function () {
    return `Regular 'this.name': ${this.name}`;
  },
  arrowFn: () => {
    return `Arrow 'this.name': ${typeof this !== "undefined" ? this?.name : "undefined"}`;
  },
};
console.log("Ex 5.1 -", ex5_1_obj.regularFn());
console.log("Ex 5.1 -", ex5_1_obj.arrowFn());

// Example 5.2: 'this' in asynchronous callbacks / setTimeout
const ex5_2_timer = {
  label: "Timer Widget",
  startTimer: function () {
    setTimeout(() => {
      // Arrow function lexically captures 'this' from startTimer
      console.log("Ex 5.2 - Arrow in setTimeout preserves 'this':", this.label);
    }, 10);
  },
};
ex5_2_timer.startTimer();

// Example 5.3: 'arguments' object vs Rest parameter
function ex5_3_regular() {
  console.log("Ex 5.3 - Regular function has 'arguments':", arguments.length, "args");
}
ex5_3_regular("a", "b", "c");

const ex5_3_arrow = (...args) => {
  console.log("Ex 5.3 - Arrow uses rest param '...args':", args.length, "args");
};
ex5_3_arrow("a", "b", "c");

// Example 5.4: Constructor with 'new'
function Ex5_4_User(name) {
  this.name = name;
}
const u1 = new Ex5_4_User("Sam");
console.log("Ex 5.4 - Regular function instantiated with 'new':", u1.name);

const Ex5_4_ArrowUser = (name) => {
  this.name = name;
};
try {
  new Ex5_4_ArrowUser("Sam");
} catch (e) {
  console.log("Ex 5.4 - Arrow function cannot be used as constructor:", e.message);
}

// Example 5.5: Duplicate named parameters
function ex5_5_reg(a, a, b) {
  return a + b; // Allowed in non-strict mode (last param wins)
}
console.log("Ex 5.5 - Non-strict regular duplicate parameter result:", ex5_5_reg(1, 2, 3)); // 2 + 3 = 5


// ============================================================================
// 6. ARRAY METHODS: MAP, FILTER, REDUCE (REDUCER)
// ============================================================================
console.log("\n--- 6. MAP, FILTER, REDUCE ---");

const ex6_products = [
  { id: 1, name: "Laptop", category: "Electronics", price: 1000, inStock: true },
  { id: 2, name: "Desk Chair", category: "Furniture", price: 200, inStock: true },
  { id: 3, name: "Mouse", category: "Electronics", price: 50, inStock: false },
  { id: 4, name: "Monitor", category: "Electronics", price: 300, inStock: true },
];

// Example 6.1: map - Transforming array of objects to formatted strings
const ex6_1_names = ex6_products.map((p) => `${p.name} ($${p.price})`);
console.log("Ex 6.1 - map formatted names:", ex6_1_names);

// Example 6.2: filter - Multi-condition filtering
const ex6_2_availableElectronics = ex6_products.filter(
  (p) => p.category === "Electronics" && p.inStock,
);
console.log("Ex 6.2 - filter available electronics count:", ex6_2_availableElectronics.length);

// Example 6.3: reduce - Summing and finding stats
const ex6_3_totalPrice = ex6_products.reduce((sum, p) => sum + p.price, 0);
console.log("Ex 6.3 - reduce total price sum:", `$${ex6_3_totalPrice}`);

// Example 6.4: reduce - Grouping by category
const ex6_4_grouped = ex6_products.reduce((acc, p) => {
  acc[p.category] = acc[p.category] || [];
  acc[p.category].push(p.name);
  return acc;
}, {});
console.log("Ex 6.4 - reduce grouping:", ex6_4_grouped);

// Example 6.5: Pipeline Composition (filter -> map -> reduce)
const ex6_5_pipelineSum = ex6_products
  .filter((p) => p.inStock)
  .map((p) => p.price * 1.1) // Add 10% tax
  .reduce((acc, price) => acc + price, 0);
console.log("Ex 6.5 - Chained Pipeline total with tax:", ex6_5_pipelineSum);


// ============================================================================
// 7. CALLBACK HELL & INVERSION OF CONTROL
// ============================================================================
console.log("\n--- 7. CALLBACK HELL & INVERSION OF CONTROL ---");

// Example 7.1: Simulated Callback Hell (Pyramid of Doom)
function ex7_1_stepA(cb) { setTimeout(() => cb(null, "Step A"), 10); }
function ex7_1_stepB(prev, cb) { setTimeout(() => cb(null, `${prev} -> Step B`), 10); }
function ex7_1_stepC(prev, cb) { setTimeout(() => cb(null, `${prev} -> Step C`), 10); }

ex7_1_stepA((err, resA) => {
  if (err) return console.error(err);
  ex7_1_stepB(resA, (err, resB) => {
    if (err) return console.error(err);
    ex7_1_stepC(resB, (err, resC) => {
      if (err) return console.error(err);
      console.log("Ex 7.1 - Callback Hell result:", resC);
    });
  });
});

// Example 7.2: Inversion of Control Problem demonstration
function thirdPartyPayment(callback) {
  // Buggy third-party API may execute callback multiple times or never!
  callback("payment_success_1");
  // callback("payment_success_2"); // Accidental duplicate charge!
}
let chargeCount = 0;
thirdPartyPayment(() => {
  chargeCount++;
  console.log("Ex 7.2 - Inversion of Control callback called count:", chargeCount);
});

// Example 7.3: Solving Callback Hell with Promise Chaining
const asyncStep = (msg) => new Promise((res) => setTimeout(() => res(msg), 10));
asyncStep("Promise Step 1")
  .then((res) => asyncStep(`${res} -> Promise Step 2`))
  .then((res) => asyncStep(`${res} -> Promise Step 3`))
  .then((finalRes) => console.log("Ex 7.3 - Clean Promise Chain:", finalRes));

// Example 7.4: Solving with Async / Await
async function ex7_4_cleanAsync() {
  const r1 = await asyncStep("Async Step 1");
  const r2 = await asyncStep(`${r1} -> Async Step 2`);
  const r3 = await asyncStep(`${r2} -> Async Step 3`);
  console.log("Ex 7.4 - Clean Async/Await:", r3);
}
ex7_4_cleanAsync();

// Example 7.5: Handling Parallel async tasks without nesting
Promise.all([asyncStep("Task 1"), asyncStep("Task 2"), asyncStep("Task 3")]).then(
  ([t1, t2, t3]) => {
    console.log("Ex 7.5 - Parallel execution:", { t1, t2, t3 });
  },
);


// ============================================================================
// 8. PROMISES LIFECYCLE (PENDING, FULFILLED, REJECTED)
// ============================================================================
console.log("\n--- 8. PROMISES LIFECYCLE ---");

// Example 8.1: Pending to Fulfilled transition
const ex8_1_fulfillPromise = new Promise((resolve) => {
  setTimeout(() => resolve("Ex 8.1 - State: FULFILLED"), 15);
});
ex8_1_fulfillPromise.then((state) => console.log(state));

// Example 8.2: Pending to Rejected transition
const ex8_2_rejectPromise = new Promise((_, reject) => {
  setTimeout(() => reject(new Error("Ex 8.2 - State: REJECTED")), 15);
});
ex8_2_rejectPromise.catch((err) => console.log(err.message));

// Example 8.3: State Immutability (Once settled, state NEVER changes)
const ex8_3_settledOnce = new Promise((resolve, reject) => {
  resolve("First Resolve Wins");
  reject(new Error("Ignored Reject")); // Has no effect!
  resolve("Ignored Second Resolve");   // Has no effect!
});
ex8_3_settledOnce.then((val) => console.log("Ex 8.3 - Settled immutability check:", val));

// Example 8.4: Promise Chaining Transformation
Promise.resolve(10)
  .then((num) => num * 2) // 20
  .then((num) => num + 5) // 25
  .then((finalVal) => console.log("Ex 8.4 - Value chained transformation:", finalVal));

// Example 8.5: Returning a new Promise from within .then()
Promise.resolve("Initial Token")
  .then((token) => new Promise((res) => setTimeout(() => res(`Auth with ${token}`), 10)))
  .then((authenticated) => console.log("Ex 8.5 - Nested promise returned in chain:", authenticated));


// ============================================================================
// 9. PROMISE CREATION & ERROR HANDLING STRATEGIES
// ============================================================================
console.log("\n--- 9. PROMISE CREATION & ERROR HANDLING ---");

// Example 9.1: Creation via Constructor
const ex9_1_customPromise = new Promise((resolve, reject) => {
  const isHealthy = true;
  if (isHealthy) resolve({ status: "ok" });
  else reject(new Error("Service Unhealthy"));
});
ex9_1_customPromise.then((data) => console.log("Ex 9.1 - Constructor resolved:", data));

// Example 9.2: Creation via Promise.resolve & Promise.reject
const ex9_2_instantPass = Promise.resolve("Instant success value");
const ex9_2_instantFail = Promise.reject(new Error("Instant failure error"));
ex9_2_instantPass.then((v) => console.log("Ex 9.2 - Promise.resolve:", v));
ex9_2_instantFail.catch((e) => console.log("Ex 9.2 - Promise.reject caught:", e.message));

// Example 9.3: .catch() and Re-throwing errors
Promise.reject(new Error("Network Timeout"))
  .catch((err) => {
    console.log("Ex 9.3 - Error intercepted in first catch:", err.message);
    throw new Error(`Wrapped: ${err.message}`);
  })
  .catch((wrappedErr) => {
    console.log("Ex 9.3 - Error re-caught downstream:", wrappedErr.message);
  });

// Example 9.4: .finally() for unconditional cleanup
let isSpinnerActive = true;
Promise.resolve("Data Loaded")
  .then((res) => console.log("Ex 9.4 - Result:", res))
  .finally(() => {
    isSpinnerActive = false;
    console.log("Ex 9.4 - finally cleanup: isSpinnerActive =", isSpinnerActive);
  });

// Example 9.5: Second callback parameter of .then(onSuccess, onError)
Promise.reject(new Error("Bad Request"))
  .then(
    (data) => console.log(data),
    (err) => console.log("Ex 9.5 - Handled via .then(success, error) 2nd arg:", err.message),
  );


// ============================================================================
// 10. PROMISE COMBINATORS (ALL, ALLSETTLED, RACE, ANY)
// ============================================================================
console.log("\n--- 10. PROMISE COMBINATORS ---");

const ex10_pFast = new Promise((res) => setTimeout(() => res("Fast (10ms)"), 10));
const ex10_pSlow = new Promise((res) => setTimeout(() => res("Slow (30ms)"), 30));
const ex10_pError = new Promise((_, rej) => setTimeout(() => rej("Fail (15ms)"), 15));

// Example 10.1: Promise.all (All must resolve; fails fast on first rejection)
Promise.all([ex10_pFast, ex10_pSlow]).then((results) => {
  console.log("Ex 10.1 - Promise.all success:", results);
});
Promise.all([ex10_pFast, ex10_pError, ex10_pSlow]).catch((err) => {
  console.log("Ex 10.1 - Promise.all failed fast with:", err);
});

// Example 10.2: Promise.allSettled (Never fails; returns status of every promise)
Promise.allSettled([ex10_pFast, ex10_pError]).then((statuses) => {
  console.log("Ex 10.2 - Promise.allSettled statuses:", statuses.map((s) => s.status));
});

// Example 10.3: Promise.race (First settled wins, whether fulfilled or rejected)
Promise.race([ex10_pFast, ex10_pSlow]).then((winner) => {
  console.log("Ex 10.3 - Promise.race winner:", winner);
});

// Example 10.4: Promise.any (First FULFILLED wins; ignores rejections unless ALL fail)
Promise.any([ex10_pError, ex10_pSlow]).then((firstFulfilled) => {
  console.log("Ex 10.4 - Promise.any first fulfilled:", firstFulfilled);
});

// Example 10.5: Promise.any when ALL fail (AggregateError)
Promise.any([
  Promise.reject("Err 1"),
  Promise.reject("Err 2"),
]).catch((aggErr) => {
  console.log("Ex 10.5 - Promise.any AggregateError caught:", aggErr.errors);
});


// ============================================================================
// 11. ASYNC / AWAIT MECHANICS & INTERNALS
// ============================================================================
console.log("\n--- 11. ASYNC / AWAIT MECHANICS ---");

// Example 11.1: Implicit Promise Wrapping
async function ex11_1_getInt() {
  return 42; // Wrapped in Promise.resolve(42)
}
ex11_1_getInt().then((val) => console.log("Ex 11.1 - Async function returns Promise:", val));

// Example 11.2: Sequential vs Parallel with await
async function ex11_2_parallelExecution() {
  const p1 = asyncStep("Parallel Result 1");
  const p2 = asyncStep("Parallel Result 2");
  const [res1, res2] = await Promise.all([p1, p2]); // Non-blocking parallel
  console.log("Ex 11.2 - Parallel await results:", { res1, res2 });
}
ex11_2_parallelExecution();

// Example 11.3: Error handling with try...catch...finally
async function ex11_3_errorHandling() {
  try {
    await Promise.reject(new Error("Async Network Exception"));
  } catch (err) {
    console.log("Ex 11.3 - Caught in async/await try-catch:", err.message);
  } finally {
    console.log("Ex 11.3 - Async finally block executed");
  }
}
ex11_3_errorHandling();

// Example 11.4: Looping with for...of vs forEach pitfall
async function ex11_4_looping() {
  const items = [1, 2, 3];
  const results = [];
  for (const item of items) {
    // Correct: sequentially awaits each iteration
    const res = await asyncStep(`Processed item ${item}`);
    results.push(res);
  }
  console.log("Ex 11.4 - for...of sequential await:", results);
}
ex11_4_looping();

// Example 11.5: Async IIFE (Immediately Invoked Async Function Expression)
(async () => {
  const init = await Promise.resolve("App initialized via Async IIFE");
  console.log("Ex 11.5 -", init);
})();


// ============================================================================
// 12. HIGHER-ORDER FUNCTIONS (HOF)
// ============================================================================
console.log("\n--- 12. HIGHER-ORDER FUNCTIONS (HOF) ---");

// Example 12.1: Function accepting a callback function
function ex12_1_repeat(n, action) {
  for (let i = 0; i < n; i++) {
    action(i);
  }
}
ex12_1_repeat(3, (idx) => console.log(`Ex 12.1 - Repeat tick: ${idx}`));

// Example 12.2: Function returning a configured function (Factory)
function ex12_2_discountApplier(discountPercentage) {
  return (price) => price * (1 - discountPercentage / 100);
}
const blackFridayDiscount = ex12_2_discountApplier(20);
console.log("Ex 12.2 - $100 with 20% discount:", blackFridayDiscount(100));

// Example 12.3: Decorator / Wrapper Function (Logging & Benchmarking)
function ex12_3_withLogger(fn) {
  return function (...args) {
    console.log(`Ex 12.3 - [LOG] Calling '${fn.name}' with args:`, args);
    return fn(...args);
  };
}
const ex12_3_add = (a, b) => a + b;
const loggedAdd = ex12_3_withLogger(ex12_3_add);
console.log("Ex 12.3 - Result:", loggedAdd(10, 20));

// Example 12.4: Function Composition (pipe / compose)
const ex12_4_pipe = (...fns) => (x) => fns.reduce((val, fn) => fn(val), x);
const trim = (str) => str.trim();
const uppercase = (str) => str.toUpperCase();
const exclaim = (str) => `${str}!!!`;
const formatGreeting = ex12_4_pipe(trim, uppercase, exclaim);
console.log("Ex 12.4 - Composed Pipe:", formatGreeting("   hello world   "));

// Example 12.5: Custom Array.prototype.myMap Polyfill
Array.prototype.ex12_5_myMap = function (callback) {
  const result = [];
  for (let i = 0; i < this.length; i++) {
    result.push(callback(this[i], i, this));
  }
  return result;
};
console.log("Ex 12.5 - Custom myMap polyfill:", [1, 2, 3].ex12_5_myMap((x) => x * 10));


// ============================================================================
// 13. CALL STACK VS MICROTASK QUEUE VS MACROTASK QUEUE
// ============================================================================
console.log("\n--- 13. CALL STACK VS MICROTASK VS MACROTASK QUEUE ---");

// Example 13.1: Standard Execution Order (Sync -> Microtask -> Macrotask)
console.log("Ex 13.1 - [1. Sync] Call Stack starting");
setTimeout(() => console.log("Ex 13.1 - [4. Macrotask] setTimeout"), 0);
queueMicrotask(() => console.log("Ex 13.1 - [3. Microtask] queueMicrotask"));
Promise.resolve().then(() => console.log("Ex 13.1 - [3b. Microtask] Promise.then"));
console.log("Ex 13.1 - [2. Sync] Call Stack ending");

// Example 13.2: Nested Microtasks run before next Macrotask
setTimeout(() => {
  console.log("Ex 13.2 - [Macrotask 1] Running");
  Promise.resolve().then(() => {
    console.log("Ex 13.2 - [Microtask inside Macrotask 1] Executed immediately after Macrotask 1");
  });
}, 5);
setTimeout(() => {
  console.log("Ex 13.2 - [Macrotask 2] Runs only after Microtasks are clear");
}, 5);

// Example 13.3: Microtask Queue Draining
Promise.resolve().then(() => {
  console.log("Ex 13.3 - Microtask 1");
  queueMicrotask(() => console.log("Ex 13.3 - Enqueued Nested Microtask (still runs before Macrotask)"));
});

// Example 13.4: Call Stack Overflow (Maximum call stack size exceeded)
function testRecursionDepth(depth) {
  if (depth === 0) return "Depth Reached";
  return testRecursionDepth(depth - 1);
}
console.log("Ex 13.4 - Safe recursion on Call Stack:", testRecursionDepth(500));

// Example 13.5: Trampoline / Non-blocking loop using setTimeout to yield
function nonBlockingLoop(count, onComplete) {
  if (count <= 0) return onComplete();
  setTimeout(() => nonBlockingLoop(count - 1, onComplete), 0);
}
nonBlockingLoop(2, () => console.log("Ex 13.5 - Trampolined loop yielded to Event Loop"));


// ============================================================================
// 14. EVENT LOOP & CONCURRENCY MODEL
// ============================================================================
console.log("\n--- 14. EVENT LOOP ---");

// Example 14.1: Synchronous Blocking of the Event Loop
function ex14_1_heavyTask() {
  const start = Date.now();
  while (Date.now() - start < 10) {} // Blocks stack for 10ms
  console.log("Ex 14.1 - Synchronous blocking work completed in 10ms");
}
ex14_1_heavyTask();

// Example 14.2: Breaking heavy work into non-blocking chunks
function ex14_2_processChunked(items, chunkSize, onFinish) {
  let index = 0;
  function processNext() {
    const chunk = items.slice(index, index + chunkSize);
    index += chunkSize;
    if (index < items.length) {
      setTimeout(processNext, 0); // Yield to event loop
    } else {
      onFinish();
    }
  }
  processNext();
}
ex14_2_processChunked([1, 2, 3, 4, 5], 2, () => {
  console.log("Ex 14.2 - Chunked processing finished without UI lock");
});

// Example 14.3: Zero-delay setTimeout is NOT 0ms (minimum 1ms - 4ms clamping)
const tStart = Date.now();
setTimeout(() => {
  console.log(`Ex 14.3 - setTimeout(0) actual delay elapsed: ${Date.now() - tStart}ms`);
}, 0);

// Example 14.4: Promise microtask starvation prevention concept
console.log("Ex 14.4 - Event loop rule: All microtasks drain before browser paints or next timer triggers");

// Example 14.5: Multiple async operations interleaving
Promise.resolve("P1").then((r) => console.log("Ex 14.5 - Interleaved:", r));
Promise.resolve("P2").then((r) => console.log("Ex 14.5 - Interleaved:", r));


// ============================================================================
// 15. PROTOTYPAL INHERITANCE & PROTOTYPE CHAIN
// ============================================================================
console.log("\n--- 15. PROTOTYPAL INHERITANCE ---");

// Example 15.1: Object.create() prototype linkage
const ex15_1_proto = {
  greet() { return `Hello from ${this.role}`; },
};
const ex15_1_admin = Object.create(ex15_1_proto);
ex15_1_admin.role = "SuperAdmin";
console.log("Ex 15.1 - Prototype method called:", ex15_1_admin.greet());

// Example 15.2: Constructor Function .prototype
function Ex15_2_Shape(color) {
  this.color = color;
}
Ex15_2_Shape.prototype.getColor = function () {
  return `Color: ${this.color}`;
};
const redShape = new Ex15_2_Shape("Red");
console.log("Ex 15.2 - Constructor prototype method:", redShape.getColor());

// Example 15.3: Prototype Chain Inspection
console.log("Ex 15.3 - Object.getPrototypeOf(redShape) === Ex15_2_Shape.prototype:", Object.getPrototypeOf(redShape) === Ex15_2_Shape.prototype);
console.log("Ex 15.3 - redShape.hasOwnProperty('color'):", Object.hasOwn(redShape, "color"));
console.log("Ex 15.3 - redShape.hasOwnProperty('getColor') (on proto):", Object.hasOwn(redShape, "getColor"));

// Example 15.4: ES6 Class Inheritance (Syntactic sugar over prototypes)
class Device {
  constructor(name) { this.name = name; }
  powerOn() { return `${this.name} powered ON`; }
}
class Smartphone extends Device {
  constructor(name, os) {
    super(name);
    this.os = os;
  }
  getSpecs() { return `${this.powerOn()} running ${this.os}`; }
}
const myPhone = new Smartphone("Pixel 9", "Android 15");
console.log("Ex 15.4 - ES6 class inheritance:", myPhone.getSpecs());

// Example 15.5: Property Shadowing in Prototype
const baseProto = { version: "1.0.0" };
const childObj = Object.create(baseProto);
childObj.version = "2.0.0"; // Shadows prototype version
console.log("Ex 15.5 - Child property:", childObj.version, "| Proto property:", baseProto.version);


// ============================================================================
// 16. THE "THIS" KEYWORD & BINDING RULES
// ============================================================================
console.log("\n--- 16. THE 'THIS' KEYWORD ---");

// Example 16.1: Default Binding (Standalone invocation)
function ex16_1_default() {
  return typeof this !== "undefined" ? "Global or Window" : "undefined in strict mode";
}
console.log("Ex 16.1 - Default binding:", ex16_1_default());

// Example 16.2: Implicit Binding (Object method invocation)
const ex16_2_store = {
  city: "Seattle",
  getCity() { return this.city; },
};
console.log("Ex 16.2 - Implicit binding (store.getCity()):", ex16_2_store.getCity());

// Example 16.3: Implicit Binding Loss (Method passed as standalone reference)
const detachedGetCity = ex16_2_store.getCity;
console.log("Ex 16.3 - Detached function call lose 'this':", detachedGetCity()); // undefined

// Example 16.4: 'new' Constructor Binding
function Ex16_4_Car(brand) {
  this.brand = brand;
}
const carInstance = new Ex16_4_Car("Porsche");
console.log("Ex 16.4 - 'new' binding instance property:", carInstance.brand);

// Example 16.5: Lexical Binding in Arrow Functions
const ex16_5_team = {
  name: "Warriors",
  members: ["Steph", "Klay"],
  printMembers() {
    return this.members.map((m) => `${m} plays for ${this.name}`);
  },
};
console.log("Ex 16.5 - Arrow function lexical this in map:", ex16_5_team.printMembers());


// ============================================================================
// 17. CALL, APPLY, AND BIND
// ============================================================================
console.log("\n--- 17. CALL, APPLY, AND BIND ---");

const ex17_userA = { name: "Alice", title: "Engineer" };
const ex17_userB = { name: "Bob", title: "Designer" };

function ex17_describe(greeting, punctuation) {
  return `${greeting}, ${this.name} (${this.title})${punctuation}`;
}

// Example 17.1: call() - Invokes immediately with comma-separated arguments
console.log("Ex 17.1 - call():", ex17_describe.call(ex17_userA, "Hello", "!"));

// Example 17.2: apply() - Invokes immediately with array of arguments
console.log("Ex 17.2 - apply():", ex17_describe.apply(ex17_userB, ["Greetings", "."]));

// Example 17.3: bind() - Returns a new function with permanently bound 'this'
const greetBob = ex17_describe.bind(ex17_userB, "Hey");
console.log("Ex 17.3 - bind():", greetBob("!!!"));

// Example 17.4: Method Borrowing with call()
const arrayLike = { 0: "apple", 1: "banana", length: 2 };
const realArray = Array.prototype.slice.call(arrayLike);
console.log("Ex 17.4 - Borrowing Array slice with call:", realArray);

// Example 17.5: Math.max with apply()
const scoreList = [45, 92, 78, 99, 83];
const maxScore = Math.max.apply(null, scoreList);
console.log("Ex 17.5 - Math.max with apply:", maxScore);


// ============================================================================
// 18. REST OPERATOR VS SPREAD OPERATOR
// ============================================================================
console.log("\n--- 18. REST OPERATOR VS SPREAD OPERATOR ---");

// Example 18.1: Spread - Cloning & Merging Arrays
const ex18_arr1 = [1, 2];
const ex18_arr2 = [3, 4];
const ex18_merged = [...ex18_arr1, ...ex18_arr2, 5];
console.log("Ex 18.1 - Spread array merge:", ex18_merged);

// Example 18.2: Spread - Merging Objects and Overriding properties
const ex18_defaultSettings = { theme: "light", fontSize: 14, sound: true };
const ex18_userSettings = { theme: "dark", fontSize: 16 };
const ex18_activeConfig = { ...ex18_defaultSettings, ...ex18_userSettings };
console.log("Ex 18.2 - Spread object merge & override:", ex18_activeConfig);

// Example 18.3: Rest - Function Parameter Collection
function ex18_calculateTotal(taxRate, ...prices) {
  const subtotal = prices.reduce((acc, p) => acc + p, 0);
  return subtotal * (1 + taxRate);
}
console.log("Ex 18.3 - Rest parameters total:", ex18_calculateTotal(0.08, 10, 20, 30));

// Example 18.4: Rest - Array Destructuring
const [head, second, ...tail] = ["A", "B", "C", "D", "E"];
console.log("Ex 18.4 - Rest in array destructuring:", { head, second, tail });

// Example 18.5: Rest - Object Destructuring to omit properties
const ex18_fullUser = { id: 99, passwordHash: "xyz123", name: "Sarah", role: "admin" };
const { passwordHash, ...safeUserData } = ex18_fullUser;
console.log("Ex 18.5 - Omit password with Rest:", safeUserData);


// ============================================================================
// 19. ARRAY & OBJECT STRUCTURING AND DESTRUCTURING
// ============================================================================
console.log("\n--- 19. STRUCTURING AND DESTRUCTURING ---");

// Example 19.1: Array Destructuring with Default Values & Skipping elements
const ex19_coords = [12.5, 45.8];
const [latitude, longitude, altitude = 0] = ex19_coords;
console.log("Ex 19.1 - Coordinates destructuring:", { latitude, longitude, altitude });

// Example 19.2: Swapping Variables without temp variable
let varA = "First", varB = "Second";
[varA, varB] = [varB, varA];
console.log("Ex 19.2 - Swapped variables:", { varA, varB });

// Example 19.3: Object Destructuring with Aliasing & Defaults
const ex19_response = { data: { user_name: "johndoe", status_code: 200 } };
const {
  data: { user_name: username, status_code: statusCode, retries = 3 },
} = ex19_response;
console.log("Ex 19.3 - Aliased and Nested:", { username, statusCode, retries });

// Example 19.4: Function Parameter Destructuring
function ex19_renderCard({ title = "Untitled", width = 300, isVisible = true } = {}) {
  return `Card [${title}] (Width: ${width}px, Visible: ${isVisible})`;
}
console.log("Ex 19.4 - Parameter destructuring:", ex19_renderCard({ title: "Profile", width: 400 }));

// Example 19.5: Dynamic Computed Property Destructuring
const dynamicKey = "targetEnv";
const ex19_envObj = { targetEnv: "Staging", version: "1.0" };
const { [dynamicKey]: selectedEnv } = ex19_envObj;
console.log("Ex 19.5 - Dynamic key destructuring:", selectedEnv);


// ============================================================================
// 20. SHALLOW COPY VS DEEP COPY
// ============================================================================
console.log("\n--- 20. SHALLOW COPY VS DEEP COPY ---");

const ex20_baseData = {
  title: "Cloud Infrastructure",
  metrics: { cpu: 45, memory: 70 },
  tags: ["aws", "docker"],
};

// Example 20.1: Shallow Copy with Spread Operator (Nested objects share references)
const ex20_shallow1 = { ...ex20_baseData };
ex20_shallow1.title = "Updated Title"; // Top-level is isolated
ex20_shallow1.metrics.cpu = 95;        // MUTATES original nested object!
console.log("Ex 20.1 - Original CPU affected by shallow copy edit:", ex20_baseData.metrics.cpu); // 95

// Example 20.2: Shallow Copy with Object.assign()
const ex20_shallow2 = Object.assign({}, ex20_baseData);
ex20_shallow2.tags.push("k8s"); // MUTATES original array!
console.log("Ex 20.2 - Original tags affected by shallow copy edit:", ex20_baseData.tags);

// Example 20.3: Deep Copy with Native structuredClone() (Modern Standard)
const ex20_deep1 = structuredClone(ex20_baseData);
ex20_deep1.metrics.cpu = 20;
ex20_deep1.tags.push("terraform");
console.log("Ex 20.3 - Deep copy metrics CPU:", ex20_deep1.metrics.cpu);
console.log("Ex 20.3 - Original metrics CPU protected:", ex20_baseData.metrics.cpu);

// Example 20.4: Deep Copy with JSON.parse(JSON.stringify()) & Its Limitations
const ex20_jsonClone = JSON.parse(JSON.stringify({
  date: new Date(),
  nanVal: NaN,
  fn: () => "Lost",
}));
console.log("Ex 20.4 - JSON clone limitations (Date becomes string, fn lost):", ex20_jsonClone);

// Example 20.5: Custom Recursive Deep Clone Function
function ex20_customDeepClone(obj) {
  if (obj === null || typeof obj !== "object") return obj;
  if (Array.isArray(obj)) return obj.map((item) => ex20_customDeepClone(item));
  const cloned = {};
  for (const key in obj) {
    if (Object.hasOwn(obj, key)) {
      cloned[key] = ex20_customDeepClone(obj[key]);
    }
  }
  return cloned;
}
const ex20_customClone = ex20_customDeepClone(ex20_baseData);
console.log("Ex 20.5 - Custom recursive deep clone verified:", ex20_customClone.title);


// ============================================================================
// 21. DOM MANIPULATION (EXECUTABLE MOCK DOM TREE IMPLEMENTATION)
// ============================================================================
console.log("\n--- 21. DOM MANIPULATION ---");

// In-Memory Executable Mock DOM Node Implementation:
class MockDOMElement {
  constructor(tagName, id = "", className = "") {
    this.tagName = tagName.toUpperCase();
    this.id = id;
    this.className = className;
    this.classList = {
      _classes: new Set(className ? className.split(" ") : []),
      add: (...cls) => cls.forEach((c) => this.classList._classes.add(c)),
      remove: (...cls) => cls.forEach((c) => this.classList._classes.delete(c)),
      toggle: (c) => (this.classList._classes.has(c) ? (this.classList._classes.delete(c), false) : (this.classList._classes.add(c), true)),
      contains: (c) => this.classList._classes.has(c),
      toString: () => Array.from(this.classList._classes).join(" "),
    };
    this.children = [];
    this.parentNode = null;
    this.attributes = {};
    this.dataset = {};
    this.style = {};
    this.textContent = "";
  }

  appendChild(child) {
    if (child instanceof MockDocumentFragment) {
      child.children.forEach((c) => this.appendChild(c));
      child.children = [];
      return child;
    }
    child.parentNode = this;
    this.children.push(child);
    return child;
  }

  prepend(child) {
    child.parentNode = this;
    this.children.unshift(child);
  }

  setAttribute(name, val) {
    this.attributes[name] = val;
    if (name.startsWith("data-")) {
      const key = name.slice(5).replace(/-([a-z])/g, (g) => g[1].toUpperCase());
      this.dataset[key] = val;
    }
  }

  getAttribute(name) { return this.attributes[name] || null; }

  querySelector(selector) {
    if (selector.startsWith("#")) {
      const id = selector.slice(1);
      return this.id === id ? this : this.children.find((c) => c.querySelector(selector)) || null;
    }
    if (selector.startsWith(".")) {
      const cls = selector.slice(1);
      return this.classList.contains(cls) ? this : this.children.find((c) => c.querySelector(selector)) || null;
    }
    return this.tagName.toLowerCase() === selector.toLowerCase()
      ? this
      : this.children.find((c) => c.querySelector(selector)) || null;
  }

  querySelectorAll(selector) {
    const matches = [];
    if (selector.startsWith(".") && this.classList.contains(selector.slice(1))) matches.push(this);
    this.children.forEach((c) => matches.push(...c.querySelectorAll(selector)));
    return matches;
  }

  closest(selector) {
    let current = this;
    while (current) {
      if (selector.startsWith(".") && current.classList.contains(selector.slice(1))) return current;
      if (selector.startsWith("#") && current.id === selector.slice(1)) return current;
      if (selector.startsWith("[") && selector.endsWith("]")) {
        const attr = selector.slice(1, -1);
        if (current.attributes && (attr in current.attributes || attr.replace("data-", "") in current.dataset)) return current;
      }
      if (current.tagName.toLowerCase() === selector.toLowerCase()) return current;
      current = current.parentNode;
    }
    return null;
  }
}

class MockDocumentFragment {
  constructor() {
    this.children = [];
  }
  appendChild(child) {
    this.children.push(child);
  }
}

// Example 21.1: Element Selection API with Tree Lookup
const rootContainer = new MockDOMElement("div", "app-root", "container main-theme");
const navBar = new MockDOMElement("nav", "navbar", "flex-header");
const mainCard = new MockDOMElement("section", "user-profile", "card active");
rootContainer.appendChild(navBar);
rootContainer.appendChild(mainCard);

const foundCard = rootContainer.querySelector(".card");
console.log("Ex 21.1 - Selection with querySelector('.card'):", {
  tagName: foundCard.tagName,
  id: foundCard.id,
  classList: foundCard.classList.toString(),
});

// Example 21.2: Creating and Appending Elements safely with textContent
const userHeader = new MockDOMElement("h2");
userHeader.textContent = "Jane Developer";
const userBio = new MockDOMElement("p");
userBio.textContent = "Senior Full-Stack Architect";
foundCard.appendChild(userHeader);
foundCard.appendChild(userBio);
console.log("Ex 21.2 - Appended elements count in Card:", foundCard.children.length, "| Header:", userHeader.textContent);

// Example 21.3: DocumentFragment Batch Insertion (High Performance)
const fragment = new MockDocumentFragment();
const skillList = new MockDOMElement("ul", "skills-list");
["JavaScript", "TypeScript", "Node.js", "React"].forEach((skillName) => {
  const li = new MockDOMElement("li", "", "skill-item");
  li.textContent = skillName;
  fragment.appendChild(li); // Batched in memory
});
skillList.appendChild(fragment); // Single atomic append
foundCard.appendChild(skillList);
console.log("Ex 21.3 - Batched Fragment Skills count:", skillList.children.length);

// Example 21.4: ClassList Manipulation (add, remove, toggle, contains)
mainCard.classList.add("highlighted");
mainCard.classList.remove("main-theme");
const isNowDark = mainCard.classList.toggle("dark-mode");
console.log("Ex 21.4 - classList operations:", {
  classes: mainCard.classList.toString(),
  hasHighlight: mainCard.classList.contains("highlighted"),
  isNowDark,
});

// Example 21.5: Data Attributes & Dynamic Styles
mainCard.setAttribute("data-user-id", "usr_8832");
mainCard.setAttribute("data-role", "admin");
mainCard.style.backgroundColor = "#1e293b";
mainCard.style.color = "#f8fafc";
console.log("Ex 21.5 - dataset & style:", {
  dataset: mainCard.dataset,
  style: mainCard.style,
});


// ============================================================================
// 22. DOM VS BOM (EXECUTABLE MOCK BROWSER ENVIRONMENT)
// ============================================================================
console.log("\n--- 22. DOM VS BOM ---");

// In-Memory Executable Mock BOM Implementation:
class MockStorage {
  constructor() { this.store = new Map(); }
  setItem(k, v) { this.store.set(k, String(v)); }
  getItem(k) { return this.store.get(k) || null; }
  removeItem(k) { this.store.delete(k); }
  clear() { this.store.clear(); }
}

const mockWindow = {
  document: {
    title: "JavaScript Concepts Portal",
    characterSet: "UTF-8",
    compatMode: "CSS1Compat",
  },
  location: {
    href: "https://antigravity.dev/learn/js?topic=dom&filter=all#overview",
    origin: "https://antigravity.dev",
    pathname: "/learn/js",
    search: "?topic=dom&filter=all",
    hash: "#overview",
    searchParams: new URLSearchParams("?topic=dom&filter=all"),
    reload: () => console.log("Page reloaded!"),
  },
  navigator: {
    userAgent: "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36",
    language: "en-US",
    languages: ["en-US", "en"],
    onLine: true,
    hardwareConcurrency: 8,
    platform: "MacIntel",
  },
  localStorage: new MockStorage(),
  sessionStorage: new MockStorage(),
  history: {
    stack: ["https://antigravity.dev/"],
    currentIndex: 0,
    pushState(state, title, url) {
      this.stack.push(url);
      this.currentIndex++;
    },
    back() { if (this.currentIndex > 0) this.currentIndex--; return this.stack[this.currentIndex]; },
    forward() { if (this.currentIndex < this.stack.length - 1) this.currentIndex++; return this.stack[this.currentIndex]; },
    get length() { return this.stack.length; },
  },
  screen: {
    width: 2560,
    height: 1440,
    availWidth: 2560,
    availHeight: 1400,
    colorDepth: 24,
  },
};

// Example 22.1: DOM vs BOM (Document vs Window Environment)
console.log("Ex 22.1 - DOM document properties:", {
  title: mockWindow.document.title,
  charset: mockWindow.document.characterSet,
});

// Example 22.2: BOM Location API & Query Parameter Parsing
console.log("Ex 22.2 - BOM Location details:", {
  pathname: mockWindow.location.pathname,
  topicParam: mockWindow.location.searchParams.get("topic"),
  hash: mockWindow.location.hash,
});

// Example 22.3: BOM Navigator API (Client specs & Network status)
console.log("Ex 22.3 - BOM Navigator device data:", {
  language: mockWindow.navigator.language,
  isOnline: mockWindow.navigator.onLine,
  cpuCores: mockWindow.navigator.hardwareConcurrency,
  platform: mockWindow.navigator.platform,
});

// Example 22.4: BOM LocalStorage Caching Layer with JSON
const userSessionData = { token: "jwt_eyJhbGciOi...", userId: 402, expiresAt: Date.now() + 3600000 };
mockWindow.localStorage.setItem("AUTH_SESSION", JSON.stringify(userSessionData));
const retrievedSession = JSON.parse(mockWindow.localStorage.getItem("AUTH_SESSION"));
console.log("Ex 22.4 - BOM localStorage cache loaded:", {
  userId: retrievedSession.userId,
  valid: retrievedSession.expiresAt > Date.now(),
});

// Example 22.5: BOM History API Navigation Manager
mockWindow.history.pushState(null, "Lesson 2", "https://antigravity.dev/learn/js/closures");
mockWindow.history.pushState(null, "Lesson 3", "https://antigravity.dev/learn/js/promises");
console.log("Ex 22.5 - BOM History stack length:", mockWindow.history.length);
console.log("Ex 22.5 - History Back navigation URL:", mockWindow.history.back());


// ============================================================================
// 23. EVENT BUBBLING & EVENT CAPTURING (EXECUTABLE 3-PHASE DISPATCHER)
// ============================================================================
console.log("\n--- 23. EVENT BUBBLING & CAPTURING ---");

// In-Memory Executable Event Propagation Engine:
class MockEvent {
  constructor(type, bubbles = true, cancelable = true) {
    this.type = type;
    this.bubbles = bubbles;
    this.cancelable = cancelable;
    this.target = null;
    this.currentTarget = null;
    this.eventPhase = 0; // 1 = CAPTURING, 2 = AT_TARGET, 3 = BUBBLING
    this._propagationStopped = false;
    this._immediatePropagationStopped = false;
    this.defaultPrevented = false;
  }

  stopPropagation() { this._propagationStopped = true; }
  stopImmediatePropagation() { this._propagationStopped = true; this._immediatePropagationStopped = true; }
  preventDefault() { if (this.cancelable) this.defaultPrevented = true; }
}

class EventTargetNode extends MockDOMElement {
  constructor(tagName, id = "", className = "") {
    super(tagName, id, className);
    this.listeners = { capture: [], bubble: [] };
  }

  addEventListener(type, callback, useCapture = false) {
    const list = useCapture ? this.listeners.capture : this.listeners.bubble;
    list.push({ type, callback });
  }

  dispatchEvent(event) {
    event.target = this;
    // Step 1: Build propagation path from root to target
    const path = [];
    let curr = this;
    while (curr) { path.unshift(curr); curr = curr.parentNode; }

    // Phase 1: CAPTURING PHASE (Root -> Target's parent)
    event.eventPhase = 1;
    for (let i = 0; i < path.length - 1; i++) {
      if (event._propagationStopped) break;
      const node = path[i];
      event.currentTarget = node;
      for (const listener of node.listeners.capture) {
        if (listener.type === event.type) {
          listener.callback(event);
          if (event._immediatePropagationStopped) break;
        }
      }
    }

    // Phase 2: AT TARGET PHASE
    if (!event._propagationStopped) {
      event.eventPhase = 2;
      event.currentTarget = this;
      const allTargetListeners = [...this.listeners.capture, ...this.listeners.bubble];
      for (const listener of allTargetListeners) {
        if (listener.type === event.type) {
          listener.callback(event);
          if (event._immediatePropagationStopped) break;
        }
      }
    }

    // Phase 3: BUBBLING PHASE (Target's parent -> Root)
    if (event.bubbles && !event._propagationStopped) {
      event.eventPhase = 3;
      for (let i = path.length - 2; i >= 0; i--) {
        if (event._propagationStopped) break;
        const node = path[i];
        event.currentTarget = node;
        for (const listener of node.listeners.bubble) {
          if (listener.type === event.type) {
            listener.callback(event);
            if (event._immediatePropagationStopped) break;
          }
        }
      }
    }
    return !event.defaultPrevented;
  }
}

// Tree Structure: Window -> DocumentBody -> CardDiv -> SubmitBtn
const docBody = new EventTargetNode("body", "body");
const cardDiv = new EventTargetNode("div", "card-container");
const submitBtn = new EventTargetNode("button", "submit-btn", "btn primary");
docBody.appendChild(cardDiv);
cardDiv.appendChild(submitBtn);

// Example 23.1: Bubbling Phase Demo (Default: Target -> Parent -> Body)
const eventLog = [];
docBody.addEventListener("click", () => eventLog.push("Body (Bubble)"), false);
cardDiv.addEventListener("click", () => eventLog.push("Card (Bubble)"), false);
submitBtn.addEventListener("click", () => eventLog.push("Button (Target)"), false);

submitBtn.dispatchEvent(new MockEvent("click"));
console.log("Ex 23.1 - Event Bubbling execution flow:", eventLog.join(" -> "));

// Example 23.2: Capturing Phase Demo (useCapture: true runs Top -> Down)
const captureLog = [];
docBody.addEventListener("click", () => captureLog.push("Body (Capture)"), true);
cardDiv.addEventListener("click", () => captureLog.push("Card (Capture)"), true);
submitBtn.dispatchEvent(new MockEvent("click"));
console.log("Ex 23.2 - Event Capturing execution flow:", captureLog.join(" -> "));

// Example 23.3: stopPropagation()
const stopPropLog = [];
const isolatedCard = new EventTargetNode("div", "isolated-card");
const innerBtn = new EventTargetNode("button", "inner-btn");
isolatedCard.appendChild(innerBtn);
isolatedCard.addEventListener("click", () => stopPropLog.push("Parent should NOT fire"), false);
innerBtn.addEventListener("click", (e) => {
  stopPropLog.push("Button fired & stopped propagation");
  e.stopPropagation();
});
innerBtn.dispatchEvent(new MockEvent("click"));
console.log("Ex 23.3 - stopPropagation() result:", stopPropLog);

// Example 23.4: stopImmediatePropagation() (Blocks sibling listeners on same element)
const immLog = [];
const multiListenerBtn = new EventTargetNode("button");
multiListenerBtn.addEventListener("click", (e) => {
  immLog.push("Listener 1 executed & stopped immediate");
  e.stopImmediatePropagation();
});
multiListenerBtn.addEventListener("click", () => immLog.push("Listener 2 (BLOCKED)"));
multiListenerBtn.dispatchEvent(new MockEvent("click"));
console.log("Ex 23.4 - stopImmediatePropagation() result:", immLog);

// Example 23.5: preventDefault()
const cancelableEvent = new MockEvent("submit", true, true);
const formNode = new EventTargetNode("form");
formNode.addEventListener("submit", (e) => {
  e.preventDefault();
});
const allowed = formNode.dispatchEvent(cancelableEvent);
console.log("Ex 23.5 - Form submit preventDefault(): defaultPrevented =", cancelableEvent.defaultPrevented, "| Allowed =", allowed);


// ============================================================================
// 24. EVENT DELEGATION (EXECUTABLE PATTERN IMPLEMENTATION)
// ============================================================================
console.log("\n--- 24. EVENT DELEGATION ---");

// Example 24.1: Single Parent Listener Managing Dynamic Child Elements
const listContainer = new EventTargetNode("ul", "todo-list");
const delegatedActions = [];

// Attach ONLY 1 listener on the parent UL element:
listContainer.addEventListener("click", (e) => {
  if (e.target.tagName === "LI") {
    delegatedActions.push(`Clicked Item: ${e.target.textContent} (ID: ${e.target.dataset.id})`);
  }
});

// Dynamically add items to the list:
for (let i = 1; i <= 3; i++) {
  const item = new EventTargetNode("li", `item-${i}`, "todo-item");
  item.textContent = `Task ${i}`;
  item.setAttribute("data-id", `task_${i}`);
  listContainer.appendChild(item);
}

// Trigger clicks on individual dynamic children:
listContainer.children[0].dispatchEvent(new MockEvent("click"));
listContainer.children[2].dispatchEvent(new MockEvent("click"));
console.log("Ex 24.1 - Delegated list clicks processed by 1 parent listener:", delegatedActions);

// Example 24.2: Action Routing with element.closest() on Nested Action Buttons
const tableContainer = new EventTargetNode("table", "data-table");
const tableActionsTriggered = [];

tableContainer.addEventListener("click", (e) => {
  const actionButton = e.target.closest(".action-btn");
  if (actionButton) {
    const action = actionButton.dataset.action;
    const rowId = actionButton.dataset.rowId;
    tableActionsTriggered.push({ action, rowId });
  }
});

const tableRow = new EventTargetNode("tr");
const tableCell = new EventTargetNode("td");
const editBtn = new EventTargetNode("button", "", "action-btn edit");
editBtn.setAttribute("data-action", "EDIT_ROW");
editBtn.setAttribute("data-row-id", "row_101");
const editIcon = new EventTargetNode("span", "", "icon"); // Deeply nested icon
editBtn.appendChild(editIcon);
tableCell.appendChild(editBtn);
tableRow.appendChild(tableCell);
tableContainer.appendChild(tableRow);

// Click directly on the inner nested <span> icon:
editIcon.dispatchEvent(new MockEvent("click"));
console.log("Ex 24.2 - Action routing via closest():", tableActionsTriggered);

// Example 24.3: Scalability Comparison (1 vs 10,000 listeners benchmark simulation)
const simulatedRows = 10000;
const memoryDelegatedListeners = 1; // 1 listener on table container
const memoryDirectListeners = simulatedRows * 3; // 3 buttons per row = 30,000 listeners
console.log("Ex 24.3 - Memory Scalability for 10k rows:", {
  delegatedListenersRequired: memoryDelegatedListeners,
  individualListenersRequired: memoryDirectListeners,
  memorySaved: "99.99%",
});

// Example 24.4: Dynamic Data-Attribute Filtering Router
function createActionRouter(handlers) {
  return function (e) {
    const target = e.target.closest("[data-action]");
    if (target && handlers[target.dataset.action]) {
      handlers[target.dataset.action](target.dataset);
    }
  };
}
const routerLog = [];
const router = createActionRouter({
  SAVE: (data) => routerLog.push(`Saved entity: ${data.entityId}`),
  DELETE: (data) => routerLog.push(`Deleted entity: ${data.entityId}`),
});
const panel = new EventTargetNode("div");
panel.addEventListener("click", router);

const saveBtn = new EventTargetNode("button");
saveBtn.setAttribute("data-action", "SAVE");
saveBtn.setAttribute("data-entity-id", "doc_99");
panel.appendChild(saveBtn);
saveBtn.dispatchEvent(new MockEvent("click"));
console.log("Ex 24.4 - Action router output:", routerLog);

// Example 24.5: SPA Lifecycle Clean Unmount
function setupComponent(container) {
  const listener = (e) => console.log("Component action");
  container.addEventListener("click", listener);
  return () => container.listeners.bubble = container.listeners.bubble.filter((l) => l.callback !== listener); // Cleanup
}
const unmount = setupComponent(panel);
unmount();
console.log("Ex 24.5 - SPA cleanup: listeners remaining on unmount:", panel.listeners.bubble.length);


// ============================================================================
// 25. THROTTLING
// ============================================================================
console.log("\n--- 25. THROTTLING ---");

// Example 25.1: Standard Timestamp / Flag-based Throttle
function ex25_throttle(func, limitMs) {
  let inThrottle = false;
  return function (...args) {
    if (!inThrottle) {
      func.apply(this, args);
      inThrottle = true;
      setTimeout(() => (inThrottle = false), limitMs);
    }
  };
}
const ex25_1_log = ex25_throttle((msg) => console.log("Ex 25.1 - Throttle executed:", msg), 50);
ex25_1_log("Call 1");
ex25_1_log("Call 2 (Ignored)");

// Example 25.2: Scroll Position Throttler
const ex25_2_onScroll = ex25_throttle((yPos) => console.log("Ex 25.2 - Scrolled to Y:", yPos), 100);
ex25_2_onScroll(100);

// Example 25.3: Button Click Spam Limiter
const ex25_3_purchase = ex25_throttle(() => console.log("Ex 25.3 - Order submitted once"), 500);
ex25_3_purchase();
ex25_3_purchase(); // Ignored

// Example 25.4: Mouse Move Tracker
const ex25_4_mouseMove = ex25_throttle((x, y) => console.log("Ex 25.4 - Cursor at:", { x, y }), 100);
ex25_4_mouseMove(10, 20);

// Example 25.5: Throttling with Remaining Time calculation
function ex25_5_throttleAdvanced(func, delay) {
  let lastTime = 0;
  return function (...args) {
    const now = Date.now();
    if (now - lastTime >= delay) {
      lastTime = now;
      func.apply(this, args);
    }
  };
}
const ex25_5_adv = ex25_5_throttleAdvanced((val) => console.log("Ex 25.5 - Timestamp throttle:", val), 50);
ex25_5_adv("Pass");


// ============================================================================
// 26. DEBOUNCING
// ============================================================================
console.log("\n--- 26. DEBOUNCING ---");

// Example 26.1: Standard Debounce implementation
function ex26_debounce(func, delayMs) {
  let timer;
  return function (...args) {
    clearTimeout(timer);
    timer = setTimeout(() => func.apply(this, args), delayMs);
  };
}

// Example 26.2: Search Autocomplete Debounce
const ex26_2_search = ex26_debounce((q) => console.log("Ex 26.2 - Search API called for:", q), 30);
ex26_2_search("j");
ex26_2_search("ja");
ex26_2_search("javascript"); // Only this executes

// Example 26.3: Auto-Save Form Input
const ex26_3_autoSave = ex26_debounce((formData) => console.log("Ex 26.3 - Auto-saved content:", formData), 40);
ex26_3_autoSave("Draft v1");
ex26_3_autoSave("Draft v2 (Final)");

// Example 26.4: Window Resize Debouncer
const ex26_4_onResize = ex26_debounce((dims) => console.log("Ex 26.4 - Resize settled at:", dims), 50);
ex26_4_onResize({ w: 1024, h: 768 });

// Example 26.5: Immediate / Leading Edge Debounce
function ex26_5_debounceImmediate(func, delayMs) {
  let timer;
  return function (...args) {
    const callNow = !timer;
    clearTimeout(timer);
    timer = setTimeout(() => (timer = null), delayMs);
    if (callNow) func.apply(this, args);
  };
}
const ex26_5_lead = ex26_5_debounceImmediate((m) => console.log("Ex 26.5 - Immediate fire:", m), 50);
ex26_5_lead("First click runs immediately");
ex26_5_lead("Second click blocked until idle");


// ============================================================================
// 27. CURRYING & PARTIAL APPLICATION
// ============================================================================
console.log("\n--- 27. CURRYING & PARTIAL APPLICATION ---");

// Example 27.1: Simple 3-argument Currying
const ex27_1_sum = (a) => (b) => (c) => a + b + c;
console.log("Ex 27.1 - Curried sum(1)(2)(3):", ex27_1_sum(1)(2)(3));

// Example 27.2: Configurable Logger pipeline
const ex27_2_logger = (level) => (moduleName) => (msg) => `[${level}][${moduleName}] ${msg}`;
const authErrorLogger = ex27_2_logger("ERROR")("AUTH");
console.log("Ex 27.2 -", authErrorLogger("Token expired"));
console.log("Ex 27.2 -", authErrorLogger("Invalid signature"));

// Example 27.3: Infinite Currying sum(1)(2)(3)...()
function ex27_3_infiniteSum(a) {
  return function (b) {
    if (b !== undefined) return ex27_3_infiniteSum(a + b);
    return a;
  };
}
console.log("Ex 27.3 - Infinite currying sum(1)(2)(3)(4)():", ex27_3_infiniteSum(1)(2)(3)(4)());

// Example 27.4: Generic Curry Converter
function ex27_4_curry(fn) {
  return function curried(...args) {
    if (args.length >= fn.length) {
      return fn.apply(this, args);
    }
    return function (...moreArgs) {
      return curried.apply(this, args.concat(moreArgs));
    };
  };
}
const multiply3 = (a, b, c) => a * b * c;
const curriedMultiply = ex27_4_curry(multiply3);
console.log("Ex 27.4 - Generic curry (2)(3)(4):", curriedMultiply(2)(3)(4));
console.log("Ex 27.4 - Generic curry (2, 3)(4):", curriedMultiply(2, 3)(4));

// Example 27.5: URL Builder with Partial Application
const ex27_5_url = (protocol) => (host) => (endpoint) => `${protocol}://${host}/${endpoint}`;
const secureApi = ex27_5_url("https")("api.example.com");
console.log("Ex 27.5 - API Endpoint 1:", secureApi("v1/users"));
console.log("Ex 27.5 - API Endpoint 2:", secureApi("v1/orders"));


// ============================================================================
// 28. TEMPORAL DEAD ZONE (TDZ)
// ============================================================================
console.log("\n--- 28. TEMPORAL DEAD ZONE (TDZ) ---");

// Example 28.1: Accessing let variable before declaration line
function ex28_1_demo() {
  // START OF TDZ
  try {
    console.log(tdzLet);
  } catch (e) {
    console.log("Ex 28.1 - TDZ let access error:", e.name); // ReferenceError
  }
  let tdzLet = "Initialized Value"; // END OF TDZ
}
ex28_1_demo();

// Example 28.2: TDZ with const
function ex28_2_demo() {
  try {
    console.log(tdzConst);
  } catch (e) {
    console.log("Ex 28.2 - TDZ const access error:", e.name);
  }
  const tdzConst = 100;
}
ex28_2_demo();

// Example 28.3: typeof is NOT safe inside TDZ (unlike undeclared variables)
function ex28_3_typeofTDZ() {
  try {
    console.log(typeof tdzItem);
  } catch (e) {
    console.log("Ex 28.3 - typeof throws in TDZ:", e.name);
  }
  let tdzItem = "hello";
}
ex28_3_typeofTDZ();

// Example 28.4: Function parameter default values TDZ trap
try {
  function ex28_4_paramTrap(a = b, b = 2) { return a + b; }
  ex28_4_paramTrap();
} catch (e) {
  console.log("Ex 28.4 - Parameter TDZ (a depends on uninitialized b):", e.name);
}

// Example 28.5: var vs let TDZ comparison
console.log("Ex 28.5 - var has NO TDZ (returns undefined):", typeof ex28_5_varBefore);
var ex28_5_varBefore = 5;


// ============================================================================
// 29. EQUALITY: == VS ===
// ============================================================================
console.log("\n--- 29. EQUALITY: == VS === ---");

// Example 29.1: Type Coercion with Number and String
console.log("Ex 29.1 - 42 == '42':", 42 == "42");   // true (string coerced to number)
console.log("Ex 29.1 - 42 === '42':", 42 === "42"); // false (strict check)

// Example 29.2: null and undefined comparisons
console.log("Ex 29.2 - null == undefined:", null == undefined);   // true (special loose equality rule)
console.log("Ex 29.2 - null === undefined:", null === undefined); // false (different types)

// Example 29.3: Boolean coercion gotchas
console.log("Ex 29.3 - 0 == false:", 0 == false);        // true
console.log("Ex 29.3 - '' == false:", "" == false);      // true
console.log("Ex 29.3 - [] == false:", [] == false);      // true
console.log("Ex 29.3 - [] === false:", [] === false);    // false

// Example 29.4: Object Reference Comparison
const objA = { id: 1 };
const objB = { id: 1 };
const objC = objA;
console.log("Ex 29.4 - objA === objB (distinct memory addresses):", objA === objB); // false
console.log("Ex 29.4 - objA === objC (same reference):", objA === objC);           // true

// Example 29.5: NaN and Object.is()
console.log("Ex 29.5 - NaN === NaN:", NaN === NaN);             // false
console.log("Ex 29.5 - Object.is(NaN, NaN):", Object.is(NaN, NaN)); // true
console.log("Ex 29.5 - Object.is(+0, -0):", Object.is(+0, -0));     // false (distinguishes signed zero)


// ============================================================================
// 30. SYNCHRONOUS VS ASYNCHRONOUS EXECUTION
// ============================================================================
console.log("\n--- 30. SYNCHRONOUS VS ASYNCHRONOUS ---");

// Example 30.1: Synchronous Execution (Sequential & Blocking)
console.log("Ex 30.1 - [Sync] Line 1");
console.log("Ex 30.1 - [Sync] Line 2");
console.log("Ex 30.1 - [Sync] Line 3");

// Example 30.2: Asynchronous Timer Callback
console.log("Ex 30.2 - [Sync] Before async timer");
setTimeout(() => console.log("Ex 30.2 - [Async] Timer fired"), 10);
console.log("Ex 30.2 - [Sync] After async timer (continues immediately)");

// Example 30.3: Asynchronous File/Data Simulation with Promise
function ex30_3_fetchData() {
  return new Promise((res) => setTimeout(() => res({ user: "Alice" }), 15));
}
ex30_3_fetchData().then((d) => console.log("Ex 30.3 - [Async Promise] Data loaded:", d));

// Example 30.4: Sequential Async with Async/Await
async function ex30_4_flow() {
  console.log("Ex 30.4 - [Async Function] Start");
  const val = await Promise.resolve("Yielded step");
  console.log("Ex 30.4 - [Async Function] Resumed with:", val);
}
ex30_4_flow();

// Example 30.5: Mixing Sync and Async operations
const syncList = [1, 2, 3].map((x) => x * 2); // Sync
console.log("Ex 30.5 - Sync array mapped:", syncList);
Promise.resolve(syncList).then((list) => console.log("Ex 30.5 - Async handler processed:", list));


// ============================================================================
// 31. V8 ENGINE ARCHITECTURE (EXECUTABLE SIMULATOR)
// ============================================================================
console.log("\n--- 31. V8 ENGINE ARCHITECTURE ---");

// Example 31.1: Scanner / Lexer & AST Generator Simulation
function miniLexerAndParser(sourceCode) {
  // Parses "let sum = a + b" into tokens: ["let", "sum", "=", "a", "+", "b"]
  const tokens = sourceCode.match(/(\w+|[=+;])/g) || [];
  const ast = {
    type: "VariableDeclaration",
    kind: tokens[0],
    identifier: tokens[1],
    init: {
      type: "BinaryExpression",
      operator: tokens[4],
      left: { type: "Identifier", name: tokens[3] },
      right: { type: "Identifier", name: tokens[5] },
    },
  };
  return { tokens, ast };
}
const { ast } = miniLexerAndParser("let sum = a + b");
console.log("Ex 31.1 - V8 AST Generated from source code:", JSON.stringify(ast));

// Example 31.2: Ignition Bytecode Interpreter Simulation
function simulateIgnitionBytecode(astTree, scope) {
  const bytecodeInstructions = [
    `LdaNamedProperty [${astTree.init.left.name}]`,
    `Add [${astTree.init.right.name}]`,
    `Star [${astTree.identifier}]`,
  ];
  // Execute bytecode:
  const leftVal = scope[astTree.init.left.name];
  const rightVal = scope[astTree.init.right.name];
  scope[astTree.identifier] = leftVal + rightVal;
  return { bytecodeInstructions, result: scope[astTree.identifier] };
}
const ignitionOutput = simulateIgnitionBytecode(ast, { a: 10, b: 20 });
console.log("Ex 31.2 - V8 Ignition Bytecode:", ignitionOutput.bytecodeInstructions, "| Evaluated Result:", ignitionOutput.result);

// Example 31.3: TurboFan JIT Compiler & Monomorphic Inline Caching (IC)
class TurboFanOptimizer {
  constructor(fn) {
    this.fn = fn;
    this.callCount = 0;
    this.feedbackVector = null;
    this.isOptimized = false;
  }
  invoke(arg) {
    this.callCount++;
    const currentShape = typeof arg.x + "_" + typeof arg.y;
    if (!this.feedbackVector) {
      this.feedbackVector = currentShape; // Monomorphic state recorded
    } else if (this.feedbackVector !== currentShape) {
      // Shape / Type changed: DE-OPTIMIZE (Bailout)!
      this.isOptimized = false;
      return { val: this.fn(arg), state: "DE-OPTIMIZED (Bailout to Ignition Bytecode)" };
    }

    if (this.callCount > 3) {
      this.isOptimized = true; // Function becomes HOT -> TurboFan JIT compiles to Machine Code
      return { val: this.fn(arg), state: "HOT: JIT Machine Code" };
    }
    return { val: this.fn(arg), state: "Ignition Interpreted" };
  }
}
const addVector = (pt) => pt.x + pt.y;
const profiler = new TurboFanOptimizer(addVector);
console.log("Ex 31.3 - Call 1:", profiler.invoke({ x: 1, y: 2 }));
profiler.invoke({ x: 3, y: 4 });
profiler.invoke({ x: 5, y: 6 });
console.log("Ex 31.3 - Call 4 (Hot compiled):", profiler.invoke({ x: 7, y: 8 }));

// Example 31.4: TurboFan De-optimization (Bailout on type polymorphism)
console.log("Ex 31.4 - Call with mutated shape:", profiler.invoke({ x: "stringVal", y: 2 }));

// Example 31.5: Generational Garbage Collection Simulation (Orinoco / Scavenger)
class V8GarbageCollectorSimulator {
  constructor() {
    this.nurseryYoungGen = new Set();
    this.intermediateGen = new Set();
    this.oldGen = new Set();
  }
  allocate(obj) { this.nurseryYoungGen.add(obj); }
  minorGC_Scavenge(reachableObjects) {
    // Collect dead young objects & promote surviving objects
    for (const obj of this.nurseryYoungGen) {
      if (reachableObjects.has(obj)) {
        this.intermediateGen.add(obj);
      }
    }
    this.nurseryYoungGen.clear();
  }
  promoteToOldGen() {
    this.intermediateGen.forEach((obj) => this.oldGen.add(obj));
    this.intermediateGen.clear();
  }
}
const gc = new V8GarbageCollectorSimulator();
const aliveObject = { id: 1 };
gc.allocate(aliveObject);
gc.allocate({ id: 2, temporary: true }); // Dead object
gc.minorGC_Scavenge(new Set([aliveObject]));
gc.promoteToOldGen();
console.log("Ex 31.5 - Orinoco GC Old Generation objects promoted count:", gc.oldGen.size);


// ============================================================================
// 32. SETINTERVAL VS SETTIMEOUT
// ============================================================================
console.log("\n--- 32. SETINTERVAL VS SETTIMEOUT ---");

// Example 32.1: Single setTimeout
const ex32_1_id = setTimeout(() => {
  console.log("Ex 32.1 - setTimeout fired exactly once");
}, 20);

// Example 32.2: Recurring setInterval with clearInterval
let ex32_2_ticks = 0;
const ex32_2_id = setInterval(() => {
  ex32_2_ticks++;
  console.log(`Ex 32.2 - setInterval tick #${ex32_2_ticks}`);
  if (ex32_2_ticks >= 2) clearInterval(ex32_2_id);
}, 20);

// Example 32.3: Recursive Nested setTimeout (Guarantees pause BETWEEN executions)
let ex32_3_count = 0;
function ex32_3_recursiveTimer() {
  setTimeout(() => {
    ex32_3_count++;
    console.log(`Ex 32.3 - Nested setTimeout step #${ex32_3_count}`);
    if (ex32_3_count < 2) ex32_3_recursiveTimer();
  }, 20);
}
ex32_3_recursiveTimer();

// Example 32.4: Cancelling a timer before it fires with clearTimeout
const ex32_4_cancelledId = setTimeout(() => {
  console.log("Ex 32.4 - Should never fire!");
}, 50);
clearTimeout(ex32_4_cancelledId);
console.log("Ex 32.4 - Timer successfully cancelled with clearTimeout");

// Example 32.5: Passing Arguments to setTimeout / setInterval
setTimeout((arg1, arg2) => {
  console.log("Ex 32.5 - setTimeout with extra arguments:", { arg1, arg2 });
}, 25, "Param A", "Param B");


// ============================================================================
// 33. FETCH API
// ============================================================================
console.log("\n--- 33. FETCH API ---");

// Example 33.1: Standard GET Request simulation
async function ex33_1_get() {
  const fakeGet = () => Promise.resolve({ ok: true, status: 200, json: async () => ({ id: 1, title: "Book" }) });
  const res = await fakeGet();
  if (res.ok) {
    const data = await res.json();
    console.log("Ex 33.1 - GET request result:", data);
  }
}
ex33_1_get();

// Example 33.2: Handling HTTP 404/500 errors (fetch does NOT reject!)
async function ex33_2_httpError() {
  const fake404 = () => Promise.resolve({ ok: false, status: 404, statusText: "Not Found" });
  try {
    const res = await fake404();
    if (!res.ok) {
      throw new Error(`Ex 33.2 - HTTP Error Status: ${res.status} ${res.statusText}`);
    }
  } catch (e) {
    console.log(e.message);
  }
}
ex33_2_httpError();

// Example 33.3: POST Request with JSON Body and Custom Headers
async function ex33_3_post() {
  const fakePost = (url, options) => {
    return Promise.resolve({
      ok: true,
      json: async () => ({ success: true, received: JSON.parse(options.body) }),
    });
  };
  const res = await fakePost("https://api.example.com/posts", {
    method: "POST",
    headers: { "Content-Type": "application/json", Authorization: "Bearer token123" },
    body: JSON.stringify({ title: "New Article", author: "Dev" }),
  });
  const data = await res.json();
  console.log("Ex 33.3 - POST response:", data);
}
ex33_3_post();

// Example 33.4: Aborting a Request with AbortController
async function ex33_4_abort() {
  const controller = new AbortController();
  const { signal } = controller;
  // Simulate aborting
  controller.abort();
  console.log("Ex 33.4 - AbortController signal aborted status:", signal.aborted);
}
ex33_4_abort();

// Example 33.5: Fetch Retry Wrapper with Exponential Backoff Concept
async function ex33_5_fetchWithRetry(fetchFn, retries = 2, delayMs = 10) {
  try {
    return await fetchFn();
  } catch (err) {
    if (retries === 0) throw err;
    await new Promise((r) => setTimeout(r, delayMs));
    return ex33_5_fetchWithRetry(fetchFn, retries - 1, delayMs * 2);
  }
}
ex33_5_fetchWithRetry(() => Promise.resolve("Retry helper passed successfully")).then((res) => {
  console.log("Ex 33.5 -", res);
});


// ============================================================================
// SCRIPT COMPLETION
// ============================================================================
console.log("\n================================================================");
console.log("🎉 ALL 33 JAVASCRIPT TOPICS WITH 4-5 EXAMPLES LOADED & EXECUTED");
console.log("================================================================\n");
