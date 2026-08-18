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
  },
};
console.log(
  "Ex 5.1 - Regular func gets object context:",
  timerObj.regularFunc(),
);
console.log(
  "Ex 5.1 - Arrow func gets lexical outer scope:",
  timerObj.arrowFunc(),
);
timerObj.delayedGreet();

// Ex 5.3: Constructor invocation (new operator)
function RegularConstructor(val) {
  this.val = val;
}
const ArrowConstructor = (val) => {
  this.val = val;
};
console.log(
  "Ex 5.3 - new RegularConstructor():",
  new RegularConstructor(42).val,
);
try {
  new ArrowConstructor(42);
} catch (e) {
  console.log(
    "Ex 5.3 - new ArrowConstructor() fails:",
    e.name + ":",
    e.message,
  );
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
console.log(
  "Ex 5.5 - Arrow func ignores .call(boundObj):",
  arrowTest.call(boundObj),
);
