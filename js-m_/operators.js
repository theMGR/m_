// unary, binary

let x1 = 1;
x1 = -1;
console.log(x1); // -1, unary negation was applied

let x2 = 1, y2 = 3;
console.log( y2 - x2 ); // 2, binary minus subtracts values

// Remainder %
console.log( 5 % 2 ); // 1, the remainder of 5 divided by 2
console.log( 8 % 3 ); // 2, the remainder of 8 divided by 3
console.log( 8 % 4 ); // 0, the remainder of 8 divided by 4

// Exponentiation **
console.log( 2 ** 2 ); // 2² = 4
console.log( 2 ** 3 ); // 2³ = 8
console.log( 2 ** 4 ); // 2⁴ = 16

console.log( 4 ** (1/2) ); // 2 (power of 1/2 is the same as a square root)
console.log( 8 ** (1/3) ); // 2 (power of 1/3 is the same as a cubic root)



// String concatenation with binary +
let s = "my" + "string";
console.log(s); // mystring

console.log( '1' + 2 ); // "12"
console.log( 2 + '1' ); // "21"

console.log(2 + 2 + '1'); // "41" and not "221"

console.log('1' + 2 + 2); // "122" and not "14"

console.log( 6 - '2' ); // 4, converts '2' to a number
console.log( '6' / '2' ); // 3, converts both operands to numbers


// Numeric conversion, unary +
// No effect on numbers
let x = 1;
console.log( +x ); // 1

let y = -2;
console.log( +y ); // -2

// Converts non-numbers
console.log( +true ); // 1
console.log( +"" );   // 0

let apples = "2";
let oranges = "3";

console.log( apples + oranges ); // "23", the binary plus concatenates strings

// both values converted to numbers before the binary plus
console.log( +apples + +oranges ); // 5

// the longer variant
console.log( Number(apples) + Number(oranges) ); // 5