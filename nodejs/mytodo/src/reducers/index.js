import ADD_TODO from '../actions'

const reducer = (state = [], action) => {
  console.log('process action'+action.type+" "+JSON.stringify(action));
  switch (action.type) {
    case ADD_TODO:
      return [...state,action.todo];
    default:
      return state
  }
}

export default reducer;
