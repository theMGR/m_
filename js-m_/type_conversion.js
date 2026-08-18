"use strict";

//
// String conversion
let value = true;
// alert(typeof value); // boolean
console.log(typeof value); // boolean

value = String(value); // now value is a string "true"
console.log(typeof value); // string
// alert(typeof value); // string

//
// Numeric conversion
console.log("6" / "2"); // 3, strings are converted to numbers
// alert( "6" / "2" ); // 3, strings are converted to numbers

let str = "123";
console.log(typeof str); // string
// alert(typeof str); // string

let num = Number(str); // becomes a number 123

console.log(typeof num); // number
// alert(typeof num); // number

let age = Number("an arbitrary string instead of a number");

console.log(age); // NaN
// alert(age); // NaN

// alert( Number("   123   ") ); // 123
// alert( Number("123z") );      // NaN (error reading a number at "z")
// alert( Number(true) );        // 1
// alert( Number(false) );       // 0

console.log(Number("   123   ")); // 123
console.log(Number("123z")); // NaN (error reading a number at "z")
console.log(Number(true)); // 1
console.log(Number(false)); // 0

//
// Boolean conversion
// alert(Boolean(1) ); // true
// alert(Boolean(0) ); // false

// alert(Boolean("hello") ); // true
// alert(Boolean("") ); // false

console.log(Boolean(1)); // true
console.log(Boolean(0)); // false

console.log(Boolean("hello")); // true
console.log(Boolean("")); // false

// alert(Boolean("0")); // true
// alert(Boolean(" ")); // spaces, also true (any non-empty string is true)

console.log(Boolean("0")); // true
console.log(Boolean(" ")); // spaces, also true (any non-empty string is true)
