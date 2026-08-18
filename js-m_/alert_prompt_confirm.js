"use strict"


// alert
alert("hello from alert_prompt_confirm.js");

// Prompt
let userAge = prompt('How old are you?', 100);
// let age_ = prompt('How old are you?', ''); // default value blank
alert(`You are ${userAge} years old!`); // You are 100 years old!

// confirm
let isBoss = confirm("Are you the boss?");
alert( isBoss );