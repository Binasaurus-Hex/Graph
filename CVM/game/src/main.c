#include "raylib.h"
#include <stdio.h>
#include <stdlib.h>
#include "InstructionSet.h"
#include <math.h>
#include <stdint.h>

static const int screenWidth = 800;
static const int screenHeight = 450;

typedef struct {
    unsigned char* buffer;
    long length;
} Bytecode;

void ReadBinFile(const char* filename , Bytecode* bytecode)
{
    FILE* input = fopen(filename, "rb");
    long file_size = 0;
    char* buffer;

    fseek(input, 0, SEEK_END);
    file_size = ftell(input);
    rewind(input);

    buffer = malloc(file_size);

    fread(buffer, file_size, 1, input);
    fclose(input);

    bytecode->buffer = buffer;
    bytecode->length = file_size / sizeof(int);
}

enum EXT_TYPES {
    EXT_EMPTY, EXT_FLOAT,EXT_INT,EXT_BOOL,EXT_STRING
};
#define MAX_ARGS 10
typedef struct {
    int args[MAX_ARGS];
    int (*function)(int[MAX_ARGS]);
    bool has_return;
} ExternalProcedure;

int print_int(int args[MAX_ARGS]) {
    printf("%d", args[0]);
}
int print(int args[MAX_ARGS]) {
    void* ptr = 0;
    memcpy(&ptr, &args[0], sizeof(void*));
    char* string = (char*)ptr;
    printf(string);
    free(string);
}
int print_float(int args[MAX_ARGS]) {
    printf("%f", *(float*)&args[0]);
}
int print_bool(int args[MAX_ARGS]) {
    bool b = *(bool*)&args[0];
    if (b)printf("true");
    else printf("false");
}
int _float(int args[MAX_ARGS]) {
    float result = (float)args[0];
    return *(int*)&result;
}
int time_seconds(int args[MAX_ARGS]) {
    float time = GetTime();
    return *(int*)&time;
}
int _int(int args[MAX_ARGS]) {
    float f = *(float*)&args[0];
    return (int)f;
}
int open_window(int args[MAX_ARGS]) {
    InitWindow(args[0], args[1], "Graph");
}
int frame_begin() {
    BeginDrawing();
}

Color current_colour;
int extern_set_colour(int args[MAX_ARGS]) {
    float r = *(float*)&args[0];
    float g = *(float*)&args[1];
    float b = *(float*)&args[2];
    float a = *(float*)&args[3];
    current_colour.r = r * 255;
    current_colour.g = g * 255;
    current_colour.b = b * 255;
    current_colour.a = a * 255;
}
int clear_screen() {
    ClearBackground(BLACK);
}

int draw() {
    EndDrawing();
}
int fill_rect(int args[MAX_ARGS]) {
    DrawRectangle(args[0], args[1], args[2], args[3], current_colour);
}

int fill_circle(int args[MAX_ARGS]) {
    DrawCircle(args[0], args[1], args[2], current_colour);
}

int key_pressed(int args[MAX_ARGS]){
    return IsKeyDown(args[0]);
}
int get_mouse_x() {
    return GetMouseX();
}
int get_mouse_y() {
    return GetMouseY();
}
int mouse_down() {
    bool down = IsMouseButtonDown(MOUSE_BUTTON_LEFT) || IsMouseButtonDown(MOUSE_BUTTON_RIGHT);
    if (down) {
        printf("down");
    }
    return down;
}
int _sqrt(int args[MAX_ARGS]) {
    float x = *(float*)&args[0];
    float result = sqrt(x);
    return *(int*)&result;
}
void wait(int args[MAX_ARGS]){
    float seconds = *(float*)&args[0];
    //Sleep(1000 * seconds);
}


int main(int argc, char** argv)
{
    ExternalProcedure externals[] = {
        {{EXT_INT}, &_float, true},
        {{EXT_FLOAT}, &_int, true},
        {{EXT_EMPTY}, &clear_screen},
        {{EXT_EMPTY}, &draw},
        {{EXT_INT,EXT_INT,EXT_INT, EXT_INT}, &extern_set_colour},
        {{EXT_INT, EXT_INT, EXT_INT}, &fill_circle},
        {{EXT_INT, EXT_INT, EXT_INT, EXT_INT}, &fill_rect},
        {{EXT_EMPTY}, &frame_begin},
        {{EXT_EMPTY}, &get_mouse_x, true},
        {{EXT_EMPTY}, &get_mouse_y, true},
        {{EXT_INT}, &key_pressed, true},
        {{EXT_EMPTY}, &mouse_down, true},
        {{EXT_INT, EXT_INT}, &open_window},
        {{EXT_STRING}, &print},
        {{EXT_BOOL}, &print_bool},
        {{EXT_FLOAT}, &print_float},
        {{EXT_INT}, &print_int},
        {{EXT_FLOAT}, &_sqrt, true},
        {{EXT_EMPTY}, &time_seconds, true},
        {{EXT_FLOAT}, &wait}
    };
    

    const char* filepath = argv[1];


    Bytecode bytecode;
    ReadBinFile(filepath, &bytecode);

    int program[10000];
    for (int i = 0; i < bytecode.length; i++) {
        long bof = i * 4;
        uint32_t value = (bytecode.buffer[bof] << 24) + (bytecode.buffer[bof + 1] << 16) + (bytecode.buffer[bof + 2] << 8) + (bytecode.buffer[bof + 3]);
        program[i] = *(int*)&value;

    }
    
    int instruction = 0;
    int memory[100000];

    int stack_pointer = 0;
    int base_pointer = 0;
    int program_counter = 0;

    while (program_counter < bytecode.length) {
        instruction = program[program_counter++];
        switch (instruction) {
        case ALLOCATE:
        {
            int size = program[program_counter++];
            stack_pointer += size;
            break;
        }
        case DEALLOCATE:
        {
            int size = program[program_counter++];
            stack_pointer -= size;
            break;
        }
        case ASSIGN_LITERAL:
        {
            int to_memory = program[program_counter++];
            int value = program[program_counter++];
            memory[base_pointer + to_memory] = value;
            break;
        }
        case ASSIGN_RAW_DATA:
        {   
            int to_memory = program[program_counter++];
            int size = program[program_counter++];
            for(int i = 0; i < size; i++){
                memory[base_pointer + to_memory + i] = program[program_counter++];
            }
            break;
        }
        case MEMSET:
        {
            int to_memory = program[program_counter++];
            int size = program[program_counter++];
            for (int i = 0; i < size; i++) {
                memory[to_memory + i] = program[program_counter++];
            }
            break;
        }
        case ASSIGN_MEMORY:
        {
            int to_memory = program[program_counter++];
            int from_memory = program[program_counter++];
            int size = program[program_counter++];
            for (int i = 0; i < size; i++) {
                int value = memory[base_pointer + from_memory + i];
                memory[base_pointer + to_memory + i] = value;
            }
            break;
        }
        case ASSIGN_VAR_FROM_LOCATION:
        {
            int to_memory = program[program_counter++];
            int from_location = memory[base_pointer + program[program_counter++]];
            int size = program[program_counter++];
            for (int i = 0; i < size; i++) {
                int addr = base_pointer + to_memory + i;
                memory[addr] = memory[from_location + i];
            }
            break;
        }
        case ASSIGN_VAR_TO_LOCATION:
        {
            int to_location = memory[base_pointer + program[program_counter++]];
            int from_memory = program[program_counter++];
            int size = program[program_counter++];
            for (int i = 0; i < size; i++) {
                memory[to_location + i] = memory[base_pointer + from_memory + i];
            }
            break;
        }
        case ASSIGN_TO_LOCATION_FROM_LOCATION:
        {
            int to_location = memory[base_pointer + program[program_counter++]];
            int from_location = memory[base_pointer + program[program_counter++]];
            int size = program[program_counter++];
            for (int i = 0; i < size; i++) {
                memory[to_location + i] = memory[from_location + i];
            }
            break;
        }
        case PUSH_MEMORY:
        {
            int memory_address = program[program_counter++];
            int size = program[program_counter++];
            for (int i = 0; i < size; i++) {
                int value = memory[base_pointer + memory_address + i];
                memory[stack_pointer++] = value;
            }
            break;
        }
        case ASSIGN_POP:
        {
            int memory_address = program[program_counter++];
            int size = program[program_counter++];

            for (int i = 0; i < size; i++) {
                memory[base_pointer + memory_address + i] = memory[stack_pointer - size + i];
            }
            stack_pointer = stack_pointer - size;
            break;
        }
        case ASSIGN_ADDRESS:
        {
            int memory_address = program[program_counter++];
            int variable = program[program_counter++];
            memory[base_pointer + memory_address] = base_pointer + variable;
            break;
        }
        case ASSIGN_DEREFERENCE:
        {
            int memory_address = program[program_counter++];
            int pointer = program[program_counter++];
            int value_address = memory[base_pointer + pointer];
            memory[base_pointer + memory_address] = memory[value_address];
            break;
        }
        case STRUCT_LOCATION:
        {
            int memory_address = program[program_counter++];
            int _struct = program[program_counter++];
            int field = program[program_counter++];
            memory[base_pointer + memory_address] = base_pointer + _struct + field;
            break;
        }
        case STRUCT_PTR_LOCATION:
        {
            int memory_address = program[program_counter++];
            int struct_ptr = memory[base_pointer + program[program_counter++]];
            int field = program[program_counter++];
            memory[base_pointer + memory_address] = struct_ptr + field;
            break;
        }
        case ARRAY_LOCATION:
        {
            int memory_address = program[program_counter++];
            int array = program[program_counter++];
            int index = memory[base_pointer + program[program_counter++]]; // read value of index
            int size = program[program_counter++];

            memory[base_pointer + memory_address] = base_pointer + array + index * size;
            break;
        }
        case ARRAY_PTR_LOCATION:
        {
            int memory_address = program[program_counter++];
            int array_ptr = memory[base_pointer + program[program_counter++]];
            int index = memory[base_pointer + program[program_counter++]]; // read value of index
            int size = program[program_counter++];

            memory[base_pointer + memory_address] = array_ptr + index * size;
            break;
        }
        case ADD:
        case SUBTRACT:
        case MULTIPLY:
        case DIVIDE:
        case LESS_THAN:
        case GREATER_THAN:
        case EQUALS:
        case AND:
        case OR:
        {
            int storage_location = (int)program[program_counter++];
            int mem_a = (int)program[program_counter++];
            int mem_b = (int)program[program_counter++];
            int a = memory[base_pointer + mem_a];
            int b = memory[base_pointer + mem_b];
            int result = 0;
            switch (instruction) {
            case LESS_THAN: result = a < b ? 1 : 0; break;
            case GREATER_THAN: result = a > b ? 1 : 0; break;
            case EQUALS:result = a == b ? 1 : 0; break;
            case AND: result = a == 1 && b == 1 ? 1 : 0; break;
            case OR: result = a == 1 || b == 1 ? 1 : 0; break;
            case ADD: result = a + b; break;
            case SUBTRACT: result = a - b; break;
            case MULTIPLY: result = a* b; break;
            case DIVIDE: result = a / b; break;
            default: result = 0;
            };

            memory[base_pointer + storage_location] = result;

            break;
        }
        case FLOAT_ADD:
        case FLOAT_SUBTRACT:
        case FLOAT_MULTIPLY:
        case FLOAT_DIVIDE:
        case FLOAT_LESS_THAN:
        case FLOAT_GREATER_THAN:
        case FLOAT_EQUALS:
        {
            int storage_location = program[program_counter++];
            int mem_a = program[program_counter++];
            int mem_b = program[program_counter++];
            float a = *(float*)&memory[base_pointer + mem_a];
            float b = *(float*)&memory[base_pointer + mem_b];
            float result;
            switch (instruction) {
            case FLOAT_LESS_THAN:result = a < b ? 1 : 0; break;
            case FLOAT_GREATER_THAN: result = a > b ? 1 : 0; break;
            case FLOAT_EQUALS: result = a == b ? 1 : 0; break;
            case FLOAT_ADD: result = a + b; break;
            case FLOAT_SUBTRACT: result = a - b; break;
            case FLOAT_MULTIPLY: result = a * b; break;
            case FLOAT_DIVIDE: result = a / b; break;
            default: result = 0; break;
            };

            bool comparison = false;
            switch (instruction) {
            case FLOAT_LESS_THAN:
            case FLOAT_GREATER_THAN:
            case FLOAT_EQUALS: comparison = true; break;                   
            };
            memory[base_pointer + storage_location] = (!comparison) ? *(int*)&result : (int)result;
            break;
        }

        case RETURN:
        {
            stack_pointer = base_pointer;
            int previous_base_ptr = memory[--stack_pointer];
            int return_location = memory[--stack_pointer];
            base_pointer = previous_base_ptr;
            program_counter = return_location;
            break;
        }
        case PROGRAM_EXIT:
        {
            return memory;
        }
        case PROCEDURE_HEADER:
        {
            memory[stack_pointer++] = base_pointer;
            base_pointer = stack_pointer;
            break;
        }
        case CALL_PROCEDURE:
        {
            int procedure_location = program[program_counter++];
            memory[stack_pointer++] = program_counter; // where to return to
            program_counter = procedure_location; // jump to function
            break;
        }
        case JUMP:
        {
            program_counter = program[program_counter];
            break;
        }
        case JUMP_IF:
        {
            int location = program[program_counter++];
            int boolean_address = program[program_counter++];
            if (memory[base_pointer + boolean_address] == 1) {
                program_counter = location;
            }
            break;
        }
        case JUMP_IF_NOT:
        {
            int location = program[program_counter++];
            int boolean_address = program[program_counter++];
            if (memory[base_pointer + boolean_address] != 1) {
                program_counter = location;
            }
            break;
        }
        case CALL_EXTERNAL:
        {
            int external_index = program[program_counter++];
            int offset = stack_pointer;

            ExternalProcedure external = externals[external_index];
            
            int n_args = sizeof(external.args) / sizeof(external.args[0]);
            
            int args_array[MAX_ARGS];
            
            for (int i = n_args - 1; i >= 0; i--) {
                int parameter_type = external.args[i];               
                switch (parameter_type) {
                case EXT_BOOL:
                case EXT_INT:
                case EXT_FLOAT:
                {
                    offset -= 1;
                    args_array[i] = *(int*)&memory[offset];
                    break;
                }
                case EXT_STRING:
                {
                    offset -= 2;
                    int pointer = memory[offset];
                    int length = memory[offset + 1];
                    char* string = malloc(length + 1);
                    for (int j = 0; j < length; j++) {
                        string[j] = (unsigned char)memory[pointer + j];
                    }
                    string[length] = '\0';
                    
                    memcpy(&args_array[i], &string, sizeof(void*));
                    break;
                }
                }
            }

            if (external.has_return) {
                int result = external.function(args_array);
                memory[offset - 1] = result;
            }
            else {
                external.function(args_array);
            }

            break;
        }
        }
    }
}
