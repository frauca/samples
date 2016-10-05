import {ADD_TODO} from '../actions'

const reducer = (state = [], action) => {
  console.log('process action'+action.type+" "+JSON.stringify(action));
  switch (action.type) {
    case ADD_TODO:
      console.log('paso por aqui');
      return [...state,action.todo];
    default:
      console.log('return '+JSON.stringify(state)+" type "+action.type+" "+(ADD_TODO===action.type)+" "+ADD_TODO);
      return state
  }
}

export default reducer;
