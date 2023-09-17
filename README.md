# GraphJava

## Another simple programming language - this time in Java!?

Another iteration of my language "Graph". now a lot more flexible and fully featured than the original C++ version

Key goals :
- lightning fast compile times
- simple C like workflow, but with less awkwardness
- minimal or zero dependencies
- compile time execution (this is why I've added a bytecode intermediary)

Features : 
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

# Moving a square around the screen
```go
length :: (x: float, y: float) -> float {
    <- sqrt(x * x + y * y);
}

get_move_y :: () -> float {
    W := 87;
    S := 83;

    move_y := 0.;
    if key_pressed(W) {
        move_y = - 1.;
    }
    if key_pressed(S) {
        move_y = 1.;
    }
    <- move_y;
}

get_move_x :: () -> float {
    A := 65;
    D := 68;

    move_x := 0.;
    if key_pressed(A) {
        move_x = -1.;
    }
    if key_pressed(D) {
        move_x = 1.;
    }
    <- move_x;
}

main :: (){

    window_width := 1000;
    window_height := 1000;

    open_window(window_width, window_height);

    previous_time : float = time_seconds();
    delta := 0.;

    x := 0.;
    y := 0.;

    vx := 200.;
    vy := 200.;

    move_x := 0.;
    move_y := 0.;

    while true {
        frame_begin();
        clear_screen();

        move_x = get_move_x();
        move_y = get_move_y();

        len : float = length(move_x, move_y);

        if move_x > 0. {
            move_x = move_x / len;
        }
        if move_y > 0. {
            move_y = move_y / len;
        }

        x = x + vx * move_x * delta;
        y = y + vy * move_y * delta;


        fill_rect(int(x), int(y), 20, 20);

        current_time : float = time_seconds();
        delta = current_time - previous_time;
        previous_time = current_time;

        draw();
    }
}
```
## whats next?
I'd like to get a few more things in before transitioning the code back to C++, and writing a backend to convert the bytecode to machine code.
Hopefully this will remove the dependency on the NASM assembler I used on the previous iteration.
  
