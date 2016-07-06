let nextTodoId=0;

const addTodo = (text)=>{
  return {type:'ADD_TODO',
                  text:text,
                  id:nextTodoId++};
}

const setVisibilityFilter = (filter)=>{
  return {
    type:'SET_FILTER',
    visibilityFilter:filter};
}

const togleTodo = (id)=>{
  return {type:'TOGLE_TODO',id};
}


export default addTodo;
exports.setVisibilityFilter=setVisibilityFilter;
exports.togleTodo=togleTodo;
