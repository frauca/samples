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

module.exports=todos;
