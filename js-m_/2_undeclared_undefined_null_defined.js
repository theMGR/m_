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
console.log(
  "Ex 2.3 - Null value:",
  currentSelectedUser,
  "| typeof:",
  typeof currentSelectedUser,
);

// Ex 2.4: Equality comparisons (== vs ===)
console.log("Ex 2.4 - null == undefined:", null == undefined);
console.log("Ex 2.4 - null === undefined:", null === undefined);
console.log(
  "Ex 2.4 - null + 10:",
  null + 10,
  "| undefined + 10:",
  undefined + 10,
);

// Ex 2.5: Nullish Coalescing (??) vs Logical OR (||)
const userConfig = { timeout: 0, title: "" };
console.log(
  "Ex 2.5 - OR (||) treats 0 as falsy -> fallback:",
  userConfig.timeout || 3000,
);
console.log(
  "Ex 2.5 - Nullish (??) preserves 0 & '' -> correct:",
  userConfig.timeout ?? 3000,
);
