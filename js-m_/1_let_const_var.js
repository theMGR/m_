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
