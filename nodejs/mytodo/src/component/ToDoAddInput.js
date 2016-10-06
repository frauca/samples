import React from 'react'
import {addToDo} from '../actions'

class ToDoAddInput extends React.Component {
  constructor(props) {
    super(props);
    this.toDoinput="";
  }

  render () {
    return <div>
      <input ref={input=>{this.toDoinput=input}}/>
      <button onClick={()=>{
          this.props.dispatch(addToDo(this.toDoinput.value));
        }}>Add Todo</button>
    </div>;
  }
}

export default ToDoAddInput;
