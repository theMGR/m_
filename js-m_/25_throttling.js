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

// Ex 25.2: Scroll Position Tracker Simulation (Throttling High-Frequency Scroll Events)
const onScroll = throttle(
  (scrollY) => console.log("Ex 25.2 - Scrolled to Y:", scrollY),
  50,
);
onScroll(100);
onScroll(150); // Dropped (within 50ms)
onScroll(200); // Dropped (within 50ms)

// Ex 25.3: Trailing Edge Throttle (Guarantees final invocation executes after cooldown)
function throttleWithTrailing(func, limitMs) {
  let timerId = null;
  let lastRan = 0;
  let lastArgs = null;
  let lastThis = null;

  return function (...args) {
    const now = Date.now();
    const remaining = limitMs - (now - lastRan);

    if (remaining <= 0) {
      if (timerId) {
        clearTimeout(timerId);
        timerId = null;
      }
      lastRan = now;
      func.apply(this, args);
    } else {
      lastArgs = args;
      lastThis = this;
      if (!timerId) {
        timerId = setTimeout(() => {
          lastRan = Date.now();
          timerId = null;
          func.apply(lastThis, lastArgs);
        }, remaining);
      }
    }
  };
}
const throttledOrder = throttleWithTrailing(
  (orderId) => console.log("Ex 25.3 - Trailing throttle processed:", orderId),
  20,
);
throttledOrder("Order #101 (Initial click)");
throttledOrder("Order #102 (Final update)");

// Ex 25.4: Mouse Coordinate Tracker Simulation (Batching high-frequency events)
const trackMouseMove = throttle(
  (coords) => console.log("Ex 25.4 - Cursor at:", coords),
  50,
);
trackMouseMove({ x: 10, y: 20 });
trackMouseMove({ x: 12, y: 24 }); // Dropped
trackMouseMove({ x: 15, y: 30 }); // Dropped

// Ex 25.5: rAF Throttle (Synchronized to Screen Refresh Rate) & Cancellation
function throttleRAF(func) {
  let isQueued = false;
  let timerId = null;
  const throttled = function (...args) {
    if (isQueued) return;
    isQueued = true;
    const schedule =
      typeof requestAnimationFrame === "function"
        ? requestAnimationFrame
        : (cb) => (timerId = setTimeout(cb, 16));
    schedule(() => {
      isQueued = false;
      func.apply(this, args);
    });
  };
  throttled.cancel = () => {
    isQueued = false;
    if (typeof cancelAnimationFrame === "function")
      cancelAnimationFrame(timerId);
    else clearTimeout(timerId);
  };
  return throttled;
}
const renderFrame = throttleRAF((frame) =>
  console.log("Ex 25.5 - rAF Rendered Frame:", frame),
);
renderFrame("Frame #1");
renderFrame("Frame #2 (Skipped in same frame tick)");
