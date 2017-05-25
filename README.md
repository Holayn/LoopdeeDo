Language created for Marist College Theory of Programming Languages.
Host language is Java.
Parser used: JavaCC.
See Language Specification Doc for more details.

## Language Syntax: ##
* bind x = 5 //cross-program variable assignment
* (x - 5) //after binding x to 5. operators: +, -, *, /, %
* do (f,5) f = fn(x)(x-8) //functions
* (false||zero?(4)) //logicals: ||, &&
* (5 > -19) //relationals: >, <, >=, <=, ==
* if zero?(4) then 5 else 8 //conditional
* 5 //constants
* -5 //negative constants
* print "hey" //printing
* read input.txt //reading from .txt file
* loop fn(x)(x%4) //looping through defined function
* save fn(x)(x*4) //defining a function and saving it to memory
* run saved //running function that has been saved to memory
* cons([1],[4,5]) //lists, and the cons operator to combine two lists
* head [4,5] //head and tail operators for lists
