// ============================================================================
// 6. ARRAY METHODS: MAP, FILTER, REDUCE (REDUCER)
// ============================================================================
/**
 * ARCHITECTURAL & INTERVIEW NOTES - FUNCTIONAL ARRAY TRANSFORMATIONS:
 * 1. `map(callback(val, idx, arr))`:
 *    - Transforms each element 1-to-1. Returns a BRAND NEW array of the exact same length. Pure function (no mutations).
 * 2. `filter(callback(val, idx, arr))`:
 *    - Evaluates predicate boolean. Returns a BRAND NEW array with elements that return truthy (length $\le$ original).
 * 3. `reduce(callback(acc, val, idx, arr), initialValue)`:
 *    - Accumulates array values into a single scalar, object, or aggregated data structure.
 *    - GOTCHA: If `initialValue` is omitted, `acc` starts as `arr[0]` and iteration begins at index 1.
 *      If array is empty and no `initialValue` is provided ➔ Throws `TypeError: Reduce of empty array with no initial value`.
 */
console.log("\n--- 6. MAP, FILTER, REDUCE ---");

const numbers = [1, 2, 3, 4, 5, 6];

// Ex 6.1: map() - 1:1 Immutable Transformation
const squared = numbers.map((n) => n * n);
console.log("Ex 6.1 - map() squared:", squared);

// Ex 6.2: filter() - Predicate Filtering
const evens = numbers.filter((n) => n % 2 === 0);
console.log("Ex 6.2 - filter() evens:", evens);

// Ex 6.3: reduce() - Summing & Aggregating
const totalSum = numbers.reduce((acc, curr) => acc + curr, 0);
console.log("Ex 6.3 - reduce() sum:", totalSum);

// Ex 6.4: reduce() - Grouping Objects by Property (Like SQL GROUP BY)
const employees = [
  { name: "Alice", dept: "Engineering" },
  { name: "Bob", dept: "HR" },
  { name: "Charlie", dept: "Engineering" },
];
const groupedByDept = employees.reduce((acc, emp) => {
  acc[emp.dept] = acc[emp.dept] || [];
  acc[emp.dept].push(emp.name);
  return acc;
}, {});
console.log("Ex 6.4 - reduce() grouped by department:", groupedByDept);

// Ex 6.5: Custom Polyfill for map & filter using reduce
const customMapped = numbers.reduce((acc, n) => [...acc, n * 10], []);
console.log("Ex 6.5 - map implemented via reduce:", customMapped);
