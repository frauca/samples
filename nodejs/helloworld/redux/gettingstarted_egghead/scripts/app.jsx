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

const Filterlink = ({filter,currentFilter,onFilClick,children})=>{
  //console.log('stateless'+filter);
  if(currentFilter===filter){
    return(<span>{children}</span>)
  }
  return (
    <a href='#'
        onClick={e=>{
          e.preventDefault();
          onFilClick(filter);
        }}>
            {children}
    </a>
  )
}

const Footer =({visibilityFilter,onFilterClick})=>(
  <p>
    Show:
    {' '}
    <Filterlink filter='SHOW_ALL' currentFilter={visibilityFilter} onFilClick={onFilterClick}>All</Filterlink>
    {' '}
    <Filterlink filter={'SHOW_ACTIVE'} currentFilter={visibilityFilter} onFilClick={onFilterClick}>Active</Filterlink>
    {' '}
    <Filterlink filter='SHOW_COMPLETED' currentFilter={visibilityFilter} onFilClick={onFilterClick}>
    Completed
    </Filterlink>
  </p>
)
const AddToDo =({onAddClick})=>{
  let input;
  return(
    <div>
    <input
        ref={node=>{
          input=node
        }}/>
    <button onClick={()=>{
      onAddClick(input.value);
      input.value='';
    }}>Add Todo</button>
    </div>
  )
}

const ToDo = ({onClick,completed,text})=>{
  return(
    <li onClick={onClick}
        style={{
          textDecoration:
            completed?
              'line-through':
              'none'
        }}>{text}</li>
  )
}

const ToDoList = ({todos,onToDoClick})=>{
  return(
    <ul>
      {todos.map(todo=>
        <ToDo key={todo.id}
            {...todo}
            onClick={()=>onToDoClick(todo.id)}/>
      )}
    </ul>
  )
}
let nextTodoId=0;

class ToDoApp extends Component {
  render(){
    const {visibilityFilter,todos}=this.props;
    const visibleTodos = getVisibleTodos(
      visibilityFilter,
      todos
    );
    //console.log('filert'+this.props.visibilityFilter);
    return (<div>
      <AddToDo onAddClick={text=>{
        store.dispatch({type:'ADD_TODO',
                        text,
                        id:nextTodoId++});
      }}/>
      <ToDoList todos={visibleTodos}
                onToDoClick={id =>
                          store.dispatch({type:'TOGLE_TODO',id})}/>
      <Footer visibilityFilter={visibilityFilter}
          onFilterClick={filter=>{
            store.dispatch({
              type:'SET_FILTER',
              visibilityFilter:filter});
          }}/>
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
    <ToDoApp {...store.getState()}/>
    ,document.getElementById('root')
  );
}

const store = createStore(todoApp);
store.subscribe(render);
render();

console.log("done");
