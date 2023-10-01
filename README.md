# GraphJava

## Another simple programming language - this time in Java!?

Another iteration of my language "Graph". now a lot more flexible and fully featured than the original C++ version

Key goals :
- lightning fast compile times
- simple C like workflow, but with less awkwardness
- minimal or zero dependencies
- compile time execution (this is why I've added a bytecode intermediary)

Features : 
- bool, float, and int types
- arrays of aformentioned types
- structs
- type inference
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

structs
```go
Vector2 :: struct {
    x: float;
    y: float;
}

...

position: Vector2;
position.x = 50;
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
Vector2 :: struct {
    x: float;
    y: float;
}

Box :: struct {
    position: Vector2;
    velocity: Vector2;
    size: int;
}

length :: (v: *Vector2) -> float {
    <- sqrt(v.x * v.x + v.y * v.y);
}

get_movement :: (movement: *Vector2){
    W := 87; A := 65; S := 83; D := 68;

    movement.x = float(key_pressed(D) - key_pressed(A));
    movement.y = float(key_pressed(S) - key_pressed(W));

    len := length(movement);
    if movement.x > 0. {
        movement.x = movement.x / len;
    }
    if movement.y > 0. {
        movement.y = movement.y / len;
    }
}

main :: (){

    window_width := 1000;
    window_height := 1000;

    open_window(window_width, window_height);

    previous_time := time_seconds();
    delta := 0.;

    box: Box;
    box.position.x = 10.;
    box.position.y = 10.;

    box.velocity.x = 200.;
    box.velocity.y = 200.;

    box.size = 40;

    move: Vector2;

    /* block
       comments */

    while true {
        frame_begin();
        clear_screen();

        get_movement(&move);

        box.position.x = box.position.x + box.velocity.x * move.x * delta;
        box.position.y = box.position.y + box.velocity.y * move.y * delta;

        fill_rect(int(box.position.x), int(box.position.y), box.size, box.size);

        current_time := time_seconds();
        delta = current_time - previous_time;
        previous_time = current_time;

        draw();
    }
}
```
## whats next?
I'd like to get a few more things in before transitioning the code back to C++, and writing a backend to convert the bytecode to machine code.
Hopefully this will remove the dependency on the NASM assembler I used on the previous iteration.
  
