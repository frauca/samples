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

const getVisibleTodos = (filter,values)=>{
  switch (filter) {
    case 'SHOW_ALL':
      return values;
    case 'SHOW_ACTIVE':
      return values.filter(t=>!t.completed);
    case 'SHOW_COMPLETED':
      return values.filter(t=>t.completed);
    default:
      return values;
  }
}

const Filterlink = ({filter,children})=>{
  console.log('stateless'+filter);
  return (
    <a href='#'
        onClick={e=>{
          e.preventDefault();
          store.dispatch({
            type:'SET_FILTER',
            visibilityFilter:filter});
        }}>
            {children}
    </a>
  )
}

let nextTodoId=0;
class ToDoApp extends Component {
  render(){
    const visibleTodos = getVisibleTodos(
      this.props.visibilityFilter,
      this.props.todos
    );
    console.log('filert'+this.props.visibilityFilter);
    return (<div>
      <input ref={node=>{this.input=node}}/>
      <button onClick={()=>{
        store.dispatch({type:'ADD_TODO',
                        text:this.input.value,
                        id:nextTodoId++});
        this.input.value='';
      }}>Add Todo</button>
      <ul>
        {visibleTodos.map(todo =>
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
      <p>
        Show:
        {' '}
        <Filterlink filter='SHOW_ALL'>All</Filterlink>
        {' '}
        <Filterlink filter={'SHOW_ACTIVE'}>Active</Filterlink>
        {' '}
        <Filterlink filter='SHOW_COMPLETED'>
        Completed
        </Filterlink>
      </p>
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
    <ToDoApp todos={store.getState().todos} visibilityFilter={store.getState().visibilityFilter}/>
    ,document.getElementById('root')
  );
}

const store = createStore(todoApp);
store.subscribe(render);
render();

console.log("done");
