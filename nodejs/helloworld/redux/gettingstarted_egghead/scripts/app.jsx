import expect from 'expect'
import { createStore } from 'redux'

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

const store = createStore(counter);

const render = () =>{
  document.body.innerText = store.getState();
}

store.subscribe(render);

document.addEventListener('click',()=>{
  store.dispatch({type:"INCREMENET"});
})
console.log("done");
