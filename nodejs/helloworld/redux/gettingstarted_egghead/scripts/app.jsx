import expect from 'expect'
import { createStore, combineReducer} from 'redux'
import React,{Component} from 'react'
import ReactDOM from 'react-dom'
import deepFreeze  from 'deep-freeze'
import todoApp from './reducers/todoApp.jsx'



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

let nextTodoId=0;
class ToDoApp extends Component {
  render(){
    return (<div>
      <input ref={node=>{this.input=node}}/>
      <button onClick={()=>{
        store.dispatch({type:'ADD_TODO',
                        text:this.input.value,
                        id:nextTodoId++});
        this.input.value='';
      }}>Add Todo</button>
      <ul>
        {this.props.todos.map(todo =>
          <li key={todo.id}
              onClick={()=>{
                store.dispatch({
                  type:'TOGLE_TODO',
                  id:todo.id
                });
              }}
              style={{
                textDecoration:
                  todo.completed?
                    'line-through':
                    'none'
              }}>{todo.text}</li>
        )}
      </ul>
    </div>
)
  }
}

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
