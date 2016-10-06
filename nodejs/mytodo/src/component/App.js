import React from 'react'
import { connect } from 'react-redux'
import AllToDos  from '../containers/AllToDos.js'
import AddToDo  from '../containers/AddToDo.js'

const mapStateToProps = (state) => ({
  state: state
})

class App extends React.Component {
  render () {
    return <div>
      <p> My fancy copy but made by me of redux todos</p>
    <AddToDo/>
    <AllToDos/>
    </div>;
  }
}


export default connect(mapStateToProps)(App);
