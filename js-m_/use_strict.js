"use strict"

// num = 5;

// console.log("Number ", num);

// alert("Number ", num);

// error: num is not defined


try {
  num = 5; // Will trigger the catch block
  console.log("Number ", num);
} catch (err) {
  console.error("Caught error in strict mode:", err.message);
  alert("Error in strict mode: " + err.message);
}
