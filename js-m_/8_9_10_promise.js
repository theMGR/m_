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
const pFulfilled = new Promise((resolve) =>
  setTimeout(() => resolve("SUCCESS_DATA"), 10),
);
pFulfilled.then((val) => console.log("Ex 8.1 - State: FULFILLED, Value:", val));

// Ex 8.2: Pending to Rejected Transition
const pRejected = new Promise((_, reject) =>
  setTimeout(() => reject(new Error("NETWORK_TIMEOUT")), 10),
);
pRejected.catch((err) =>
  console.log("Ex 8.2 - State: REJECTED, Reason:", err.message),
);

// Ex 8.3: State Immutability (First settle wins)
const pImmutable = new Promise((resolve, reject) => {
  resolve("First Resolve Wins");
  reject("Ignored Reject");
  resolve("Ignored Second Resolve");
});
pImmutable.then((res) =>
  console.log("Ex 8.3 - Settled immutability check:", res),
);

// Ex 8.4: Value Chaining in .then()
Promise.resolve(5)
  .then((n) => n * 2) // 10
  .then((n) => n + 15) // 25
  .then((res) => console.log("Ex 8.4 - Value chained transformation:", res));

// Ex 8.5: Returning a Promise inside .then() (Promise unwrapping)
Promise.resolve("Initial Token")
  .then(
    (tok) =>
      new Promise((resolve) =>
        setTimeout(() => resolve("Auth with " + tok), 10),
      ),
  )
  .then((user) =>
    console.log("Ex 8.5 - Nested promise returned in chain:", user),
  );

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
fetchUserAsync(1).then((u) =>
  console.log("Ex 9.1 - Constructor resolved:", u.status),
);

// Ex 9.2: Static Instant Creators (Promise.resolve & Promise.reject)
Promise.resolve("Instant success value").then((v) =>
  console.log("Ex 9.2 - Promise.resolve:", v),
);
Promise.reject("Instant failure error").catch((e) =>
  console.log("Ex 9.2 - Promise.reject caught:", e),
);

// Ex 9.3: Error bubbling through multiple then() blocks
Promise.resolve("Start")
  .then(() => {
    throw new Error("Network Timeout");
  })
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
  (err) =>
    console.log("Ex 9.5 - Handled via .then(success, error) 2nd arg:", err),
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
const failP = new Promise((_, rej) =>
  setTimeout(() => rej(new Error("Fail (15ms)")), 15),
);

// Ex 10.1: Promise.all (Fail-Fast)
Promise.all([fastP, slowP]).then((res) =>
  console.log("Ex 10.1 - Promise.all success:", res),
);
Promise.all([fastP, failP]).catch((err) =>
  console.log("Ex 10.1 - Promise.all failed fast with:", err.message),
);

// Ex 10.2: Promise.allSettled (Zero fail-fast; full inspection)
Promise.allSettled([fastP, failP]).then((results) => {
  console.log(
    "Ex 10.2 - Promise.allSettled statuses:",
    results.map((r) => r.status),
  );
});

// Ex 10.3: Promise.race (First to settle wins)
Promise.race([fastP, slowP]).then((winner) =>
  console.log("Ex 10.3 - Promise.race winner:", winner),
);

// Ex 10.4: Promise.any (First success wins)
Promise.any([failP, slowP]).then((winner) =>
  console.log("Ex 10.4 - Promise.any first fulfilled:", winner),
);

// Ex 10.5: Promise.any with all rejected (AggregateError)
const fail1 = Promise.reject("Err 1");
const fail2 = Promise.reject("Err 2");
Promise.any([fail1, fail2]).catch((aggErr) => {
  console.log("Ex 10.5 - Promise.any AggregateError caught:", aggErr.errors);
});
