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

const todos = (state=[],action)=>{
  switch (action.type) {
    case "ADD_TODO":
      return [
        ...state,
        {id:action.id,
         text:action.text,
         completed:false}
      ]
    case "TOGLE_TODO":
      return state.map((togle)=>{
          if(togle.id!=action.id){
            return togle;
          }
          return Object.assign({},togle,{completed:!togle.completed})
        }
      );
    default:
      return state;
  }
}

const visibilityFilter = (state='SHOW_ALL',action)=>{
  switch (ation.type) {
    case "SET_FILTER":
      return action.visibilityFilter;
    default:
      return state;
  }
}


const todoApp =(state={},action)=>{
  return {
    todos:todos(state.todos,action),
    visibilityFilter:visibilityFilter(state.visibilityFilter,action)
  }
}
const testTodos=()=>{
  const stateBefore=[];
  const action={
    type: "ADD_TODO",
    id:0,
    text: 'learn redux'
  }
  const stateAfter=[
    {
      id:0,
      text: 'learn redux',
      completed: false
    }
  ]
  expect(
    todos(stateBefore,action)
  ).toEqual(stateAfter);
}

const testTogleTodos=()=>{
  const stateBefor=[
    {id:0,
     text: 'learn',
     completed:false
   },{id:1,
    text: 'learn',
    completed:false
  }
  ]
  const action={
    type:"TOGLE_TODO",
    id:1
  }
  const stateAfter=[
    {id:0,
     text: 'learn',
     completed:false
   },{id:1,
    text: 'learn',
    completed:true
  }
  ]
  expect(
    todos(stateBefor,action)
  ).toEqual(stateAfter);
}
testTogleTodos();
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
