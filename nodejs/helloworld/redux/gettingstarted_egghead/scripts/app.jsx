import expect from 'expect'
import { createStore, combineReducer} from 'redux'
import React,{Component} from 'react'
import ReactDOM from 'react-dom'
import deepFreeze  from 'deep-freeze'
import todoApp from './reducers/todoApp.jsx'
import Footer from './components/Footer.jsx'
import VisibleTodos from './components/VisibleTodos.jsx'
import {Provider,connect} from 'react-redux'


const store = createStore(todoApp);

let AddToDo =({dispatch})=>{
  let input;
  return(
    <div>
    <input
        ref={node=>{
          input=node
        }}/>
    <button onClick={()=>{
      dispatch({type:'ADD_TODO',
                      text:input.value,
                      id:nextTodoId++});
      input.value='';
    }}>Add Todo</button>
    </div>
  )
}
AddToDo=connect()(AddToDo);
//equivalent
//AddToDo = connect(
//   state =>{return{}},
//   dispatch=>{return{dispatch}}
// )(AddToDo);




let nextTodoId=0;


const ToDoApp=()=>(
  <div>
      <AddToDo />
      <VisibleTodos/>
      <Footer />
    </div>
)



ReactDOM.render(
  <Provider store={createStore(todoApp)}>
    <ToDoApp />
  </Provider>
  ,document.getElementById('root')
);

console.log("done");
