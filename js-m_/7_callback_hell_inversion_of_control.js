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
function step1(cb) {
  setTimeout(() => cb("Step A"), 5);
}
function step2(data, cb) {
  setTimeout(() => cb(data + " -> Step B"), 5);
}
function step3(data, cb) {
  setTimeout(() => cb(data + " -> Step C"), 5);
}

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
const asyncStep = (msg) =>
  new Promise((resolve) => setTimeout(() => resolve(msg), 5));
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
Promise.all([
  asyncStep("Task 1"),
  asyncStep("Task 2"),
  asyncStep("Task 3"),
]).then(([t1, t2, t3]) => {
  console.log("Ex 7.5 - Parallel execution:", { t1, t2, t3 });
});
