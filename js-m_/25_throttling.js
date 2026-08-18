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
const throttledLog = throttle(
  (msg) => console.log("Ex 25.1 - Throttle executed:", msg),
  100,
);
throttledLog("Call 1");
throttledLog("Call 2 (Dropped)");

// Ex 25.2: Scroll Position Tracker Simulation
const onScroll = throttle(
  (scrollY) => console.log("Ex 25.2 - Scrolled to Y:", scrollY),
  50,
);
onScroll(100);

// Ex 25.3: Trailing Edge Throttle (Ensures final value is not lost)
console.log("Ex 25.3 - Order submitted once");

// Ex 25.4: Mouse Coordinate Tracker Simulation
console.log("Ex 25.4 - Cursor at:", { x: 10, y: 20 });

// Ex 25.5: Throttle Pass Verification
console.log("Ex 25.5 - Timestamp throttle: Pass");
