# GraphJava

## Another simple programming language - this time in Java!?

Another iteration of my language "Graph". now a lot more flexible and fully featured than the original C++ version
Key goals :
- lightning fast compile times
- simple C like workflow, but with less awkwardness
- minimal or zero dependencies

- supports bool, float, and int types
- supports arrays of aformentioned types
- type inference (not in all cases yet)
- pointers
- ability to call out to java functions (limited to certain types)
- now compiles to a intermediate bytecode, and contains a virtual machine for running the bytecode.

# Examples

variable decleration
```go
x := 2;
y :int = 3;
happy := false;
```

vector length procedure
```go
length :: (x: float, y: float) -> float {
    <- sqrt(x * x + y * y);
}
```

arrays
```go
key_codes: int[3];
key_codes[0] = 2;
key_codes[1] = 4;
key_codes[2] = 8;
```

control flow
```go
counter := 0;
while counter < 100 {
  if counter == 50 {
      print(counter);
  }
  counter = counter + 1;
}
```


## whats next?
I'd like to get a few more things in before transitioning the code back to C++, and writing a backend to convert the bytecode to machine code.
Hopefully this will remove the dependency on the NASM assembler I used on the previous iteration.

  
