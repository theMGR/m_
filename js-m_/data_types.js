const MY_DOB = "10:10:1990";

const COLOR_ORANGE = "#FF7F00";

let color = COLOR_ORANGE;
console.log(color);
typeof alert !== "undefined" && alert(color);

// data types
// primitive and Non-Primitive
// Primitive =>
// Numering -> number, bigInt
// Non-Numeric -> String, Boolean, undefined, null, Symbol
// Non-Primitive -> Array, Object, Function, Date, RegExp

// Number
let n1 = 2;
console.log(n1);

let n2 = 1.3;
console.log(n2);

let n3 = Infinity;
console.log(n3);

let n4 = "something here too" / 2;
console.log(n4);

// String
let s1 = "Hello There";
console.log(s1);

let s2 = "Single quotes work fine";
console.log(s2);

let s3 = `can embed ${s1}`;
console.log(s3);

// Boolean
let b1 = true;
console.log(b1);

let b2 = false;
console.log(b2);

// Null
let age = null;
console.log(age);

// Undefined
let a;
console.log(a);

// Symbol
let sy1 = Symbol("Geeks");
let sy2 = Symbol("Geeks");
console.log(sy1 == sy2);

// BigInt
let b = BigInt("0b1010101001010101001111111111111111");
console.log(b);

// Object
let gfg = {
  type: "Company",
  location: "Noida",
};
console.log(gfg.type);

// Arrays
let a1 = [1, 2, 3, 4, 5];
console.log(a1);

let a2 = [1, "two", { name: "Object" }, [3, 4, 5]];
console.log(a2);

// Function
// Defining a function
function greet(name) {
  return "Hello, " + name + "!";
}
// Calling the function
console.log(greet("Ajay"));

// Date Object
let currentDate = new Date();
console.log(currentDate);

// Regular Expression
// Creating a regular expression to match the word "hello"
let pattern = /hello/;

// Testing the pattern against a string
// Returns false because "hello" is not present
let result = pattern.test("Hello, world!");

console.log(result);
