import expect from 'expect'

function counter(state=0,action){
  switch (action.type) {
    case "INCREMENET":
      return state+1;
    case "DECREMENET":
      return state-1;
    default:
      return state;
  }
  return state;
}

expect(
  counter(0,{type:"INCREMENET"})
).toEqual(1);

expect(
  counter(2,{type:"DECREMENET"})
).toEqual(1);

expect(
  counter(undefined,{type:"INCREMENET"})
).toEqual(1);


console.log("Test passed");
