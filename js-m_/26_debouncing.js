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
const debouncedSearch = debounce(
  (query) => console.log("Ex 26.2 - Search API called for:", query),
  15,
);
debouncedSearch("j");
debouncedSearch("java");
debouncedSearch("javascript"); // Only final call executes!

// Ex 26.3: Auto-Save Form Draft
const autoSave = debounce(
  (content) => console.log("Ex 26.3 - Auto-saved content:", content),
  15,
);
autoSave("Draft v1");
autoSave("Draft v2 (Final)");

// Ex 26.4: Window Resize Settled Calculator
const onResizeSettled = debounce(
  (dims) => console.log("Ex 26.4 - Resize settled at:", dims),
  15,
);
onResizeSettled({ w: 1024, h: 768 });

// Ex 26.5: Immediate (Leading Edge) Debounce
console.log("Ex 26.5 - Immediate fire: First click runs immediately");
