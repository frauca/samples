import expect from 'expect'
import { createStore, combineReducer} from 'redux'
import React,{Component} from 'react'
import ReactDOM from 'react-dom'
import deepFreeze  from 'deep-freeze'
import todoApp from './reducers/todoApp.jsx'



const store = createStore(todoApp);

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
    const {store}=this.context;
      this.unsubscribe=store.subscribe(()=>{this.forceUpdate()})
  }

  componentWillUnmount(){
    this.unsubscribe();
  }
  render() {
    const {store}=this.context;
    let props=this.props;
    let visibilityFilter=store.getState().visibilityFilter;
    let active=(visibilityFilter===props.filter);
    //console.log('render filter '+props.filter);

    return (
      <Link filter={active}
            onClick={()=>{
              store.dispatch({
                type:'SET_FILTER',
                visibilityFilter:props.filter});
            }}>{props.children}</Link>
    )
  }
}

Filterlink.contextTypes={
  store:React.PropTypes.object
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
const AddToDo =(props,{store})=>{
  let input;
  return(
    <div>
    <input
        ref={node=>{
          input=node
        }}/>
    <button onClick={()=>{
      store.dispatch({type:'ADD_TODO',
                      text:input.value,
                      id:nextTodoId++});
      input.value='';
    }}>Add Todo</button>
    </div>
  )
}
AddToDo.contextTypes={
  store:React.PropTypes.object
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

class VisibleTodos extends Component{
  componentDidMount(){
      const {store} = this.context;
      this.unsubscribe=store.subscribe(()=>{this.forceUpdate()})
  }

  componentWillUnmount(){
    this.unsubscribe();
  }
  render(){
    const {store} = this.context;
    let props=this.props;
    let state=store.getState();
    const visibleTodos = getVisibleTodos(
      state.visibilityFilter,
      state.todos
    );
    return(
      <ToDoList todos={visibleTodos}
                onToDoClick={id =>
                          store.dispatch({type:'TOGLE_TODO',id})}/>
    )
  }
}
VisibleTodos.contextTypes={
  store:React.PropTypes.object
}
const ToDoApp=()=>(
  <div>
      <AddToDo />
      <VisibleTodos/>
      <Footer />
    </div>
)

class Provider extends Component{
  getChildContext(){
    return {
      store: this.props.store
    }
  }
  render(){
    return (this.props.children)
  }
}
Provider.childContextTypes={
  store:React.PropTypes.object
}

ReactDOM.render(
  <Provider store={createStore(todoApp)}>
    <ToDoApp />
  </Provider>
  ,document.getElementById('root')
);

console.log("done");
