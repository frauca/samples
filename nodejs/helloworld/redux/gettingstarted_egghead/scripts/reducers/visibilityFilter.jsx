const visibilityFilter = (state='SHOW_ALL',action)=>{
  //console.log('visibilityFilter'+action.visibilityFilter+" "+action.type);
  switch (action.type) {
    case "SET_FILTER":
      return action.visibilityFilter;
    default:
      return state;
  }
}

module.exports=visibilityFilter;
