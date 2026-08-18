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
async function returnScalar() {
  return 42;
}
returnScalar().then((val) =>
  console.log("Ex 11.1 - Async function returns Promise:", val),
);

// Ex 11.2: Concurrent vs Sequential Await
async function parallelExecution() {
  const p1 = new Promise((res) =>
    setTimeout(() => res("Parallel Result 1"), 10),
  );
  const p2 = new Promise((res) =>
    setTimeout(() => res("Parallel Result 2"), 10),
  );
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
    const res = await new Promise((r) =>
      setTimeout(() => r("Processed item " + item), 5),
    );
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
