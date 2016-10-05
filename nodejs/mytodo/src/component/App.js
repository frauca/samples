import React from 'react'
import { connect } from 'react-redux'
import {addToDo} from '../actions'
import ToDoList  from './toDoList.js'


class App extends React.Component {
  render () {
    console.log('props');
    console.log(addToDo('Test'));
    return <div>
      <p> My fancy copy but made by me of redux todos</p>
      <button onClick={()=>{
          this.props.dispatch(addToDo('Test'));
        }}>Add Todo</button>
    <ToDoList/>
    </div>;
  }
}


export default connect()(App);
