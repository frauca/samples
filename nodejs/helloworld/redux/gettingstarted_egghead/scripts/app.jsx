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

const Link = ({active,onClick,children})=>{
  //console.log('stateless'+filter);
  if(active){
    return(<span>{children}</span>)
  }
  return (
    <a href='#'
        onClick={e=>{
          e.preventDefault();
          onClick();
        }}>
            {children}
    </a>
  )
}

class Filterlink extends Component{

  componentDidMount(){
      this.unsubscribe=store.subscribe(()=>{this.forceUpdate()})
  }

  componentWillUnmount(){
    this.unsubscribe();
  }
  render() {
    let props=this.props;
    let visibilityFilter=store.getState().visibilityFilter;
    let active=(visibilityFilter===props.filter);
    console.log('render filter '+props.filter);
    
    return (
      <Link filter={active}
            onClick={()=>{
              store.dispatch({
                type:'SET_FILTER',
                visibilityFilter:filter});
            }}>{props.children}</Link>
    )
  }

}

const Footer =()=>(
  <p>
    Show:
    {' '}
    <Filterlink filter='SHOW_ALL'>All</Filterlink>
    {' '}
    <Filterlink filter={'SHOW_ACTIVE'}>Active</Filterlink>
    {' '}
    <Filterlink filter='SHOW_COMPLETED'>Completed</Filterlink>
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
      <Footer />
    </div>
)
  }
}

const render = () =>{
  console.log('full render');
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
