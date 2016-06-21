import expect from 'expect'
import { createStore } from 'redux'
import React from 'react'
import ReactDOM from 'react-dom'
import deepFreeze  from 'deep-freeze'

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


const addCounter =(list)=>{
  return [...list,0];
}

const removeCounter=(list,index)=>{
  return [
    ...list.slice(0,index)
    ,...list.slice(index+1)
  ]
}

const incrementCounter=(list,index)=>{
  return[
    ...list.slice(0,index)
    ,list[index]+1
    ,...list.slice(index+1)
  ]
}

const toggleTodo=(todo)=>{
  return Object.assign({},todo,{completed:!todo.completed});
}

const testToggleTodo=()=>{
  const before={
    id: 1,
    text:'learn redux',
    completed: true
  };
  const after={
    id: 1,
    text:'learn redux',
    completed: false
  }
  expect(
    toggleTodo(before)
  ).toEqual(after);
}

const testAddCounter =()=>{
  const listBefore=[];
  const listAfter=[0];
  deepFreeze(listBefore);
  expect(
    addCounter(listBefore)
  ).toEqual(listAfter);
}

const testRemoveCounter = ()=>{
  const listBefore=[1,2,3];
  const listAfter=[1,3];
  expect(
    removeCounter(listBefore,1)
  ).toEqual(listAfter);
}

const testIncrementCounter = ()=>{
  const listBefore=[1,2,3];
  const listAfter=[1,3,3];
  expect(
    incrementCounter(listBefore,1)
  ).toEqual(listAfter);
}

testToggleTodo();
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
