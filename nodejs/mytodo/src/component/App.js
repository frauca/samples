import React from 'react'
import { connect } from 'react-redux'
import {addToDo} from '../actions'
import AllToDos  from '../containers/AllToDos.js'

const mapStateToProps = (state) => ({
  state: state
})

class App extends React.Component {
  render () {
    console.log('Apps ');
    console.log(this);
    return <div>
      <p> My fancy copy but made by me of redux todos</p>
      <button onClick={()=>{
          this.props.dispatch(addToDo('Test'));
        }}>Add Todo</button>
    <AllToDos/>
    </div>;
  }
}


export default connect(mapStateToProps)(App);
