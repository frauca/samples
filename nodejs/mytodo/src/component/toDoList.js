import React from 'react'

class ToDoList extends React.Component {
  render () {
    console.log('state');
    console.log(this.props.state);
    return <ul>
      {this.props.state.todos.map(todo=>
      <li> {todo}</li>
    )}
    </ul>;
  }
}

export default ToDoList;
