import React from 'react'

class ToDoList extends React.Component {
  render () {
    console.log('render todos'+JSON.stringify(this.props.todos));
    console.log(this.props.todos);
    return <ul>
      {this.props.todos.map(todo=>
      <li> {todo}</li>
    )}
    </ul>;
  }
}

export default ToDoList;
