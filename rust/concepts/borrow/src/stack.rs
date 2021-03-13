pub fn test_them_all() {
    coping_on_stack();
    str_also_on_stack();
    with_functions();
}

//On the stack there is no borrow as the elements are copied
fn coping_on_stack() {
    let a = 1;
    let _b = a;
    let _c = a;
}

//On the stack there is no borrow as the elements are copied
fn str_also_on_stack() {
    let a = "1";
    let _b = a;
    let _c = a;
}

//functions return and element as they are on the stack
fn stack_as_result() -> i32 {
    1
}

fn stack_same(i: i32) -> i32 {
    i
}

fn with_functions() {
    let a = stack_as_result();
    let _b = stack_same(a);
    let _b = a;
}

//now lets mutate
fn mutating() {
    let mut a = 1;
    a = a + 1;
    a = stack_as_result();
    a = a + 1;
}

//now with str
fn str_as_result()->&str{
    let text = "just me";
    text[1..4]
}