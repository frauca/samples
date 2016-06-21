import expect from 'expect'
import { createStore, combineReducers} from 'redux'
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
  switch (action.type) {
    case "SET_FILTER":
      return action.visibilityFilter;
    default:
      return state;
  }
}

// const todoApp =combineReducers(
//   {todos:todos,
//   visibilityFilter:visibilityFilter}
// );

const todoApp =combineReducers(
  {todos, visibilityFilter}
);



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

let nextTodoId=0;
const ToDoApp =({todos})=>(
  <div>
    <button onClick={()=>{
      store.dispatch({type:'ADD_TODO',
                      text:'Test',
                      id:nextTodoId++});
    }}>Add Todo</button>
    <ul>
      {todos.map(todo =>
        <li key={todo.id}>{todo.text}</li>
      )}
    </ul>
  </div>
)

const render = () =>{
  ReactDOM.render(
    // <ToDoApp
    //   value={store.getState()}
    //   onIncrement={()=>store.dispatch({type:"INCREMENET"})}
    //   onDecrement={()=>store.dispatch({type:"DECREMENET"})}
    // />
    <ToDoApp todos={store.getState().todos}/>
    ,document.getElementById('root')
  );
}

const store = createStore(todoApp);
store.subscribe(render);
render();

console.log("done");
