import React,{Component} from 'react'
import {Provider,connect} from 'react-redux'
import {togleTodo} from '../actions/actions.jsx'

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

const mapSateToProps = (state)=>{
  return {
    todos:getVisibleTodos(
      state.visibilityFilter,
      state.todos
    )
  };
}

const mapStateToDispatch = (dispatch)=>{
  return {
    onToDoClick:(id) =>{
      //console.log('click me');
      dispatch(togleTodo(id))
    }
  };
}
const VisibleTodos = connect(mapSateToProps,mapStateToDispatch)(ToDoList);

module.exports=VisibleTodos;
