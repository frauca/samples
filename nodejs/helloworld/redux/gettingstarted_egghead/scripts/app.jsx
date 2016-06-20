import expect from 'expect'
import { createStore } from 'redux'
import React from 'react'
import ReactDOM from 'react-dom'

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

const Counter = ({
    value,
    onIncrement,
    onDecrement
  })=>(
    <div>
      <h1>{value}</h1>
      <button onClick={onIncrement}>+</button>
      <button onClick={onDecrement}>-</button>
    </div>
)

const render = () =>{
  ReactDOM.render(
    <Counter
      value={store.getState()}
      onIncrement={()=>store.dispatch({type:"INCREMENET"})}
      onDecrement={()=>store.dispatch({type:"DECREMENET"})}
    />
    ,document.getElementById('root')
  );
}

store.subscribe(render);
render();

console.log("done");
