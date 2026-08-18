let user = new Object(); // "object constructor" syntax
user = {}; // "object literal" syntax

user = {
  name: "John",
  age: 30,
  "likes birds": true, // multiword property name must be quoted
};

// The last property in the list may end with a comma:
// That is called a “trailing” or “hanging” comma. Makes it easier to add/remove/move around properties, because all lines become alike.

// get property values of the object:
console.log(user.name); // John
console.log(user.age); // 30

delete user.age;

user["likes birds"] = true;
console.log(user["likes birds"]); // true

let key = "likes birds";

// same as user["likes birds"] = true;
user[key] = true;

/// Property value shorthand
function makeUser(name, age) {
  return {
    name: name,
    age: age,
    // ...other properties
  };
}

let user = makeUser("John", 30);
console.log(user.name); // John

let user1 = {
  name, // same as name:name
  age: 30,
};

////
let codes = {
  49: "Germany",
  41: "Switzerland",
  44: "Great Britain",
  // ..,
  1: "USA",
};

for (let code in codes) {
  console.log(code); // 1, 41, 44, 49
}
