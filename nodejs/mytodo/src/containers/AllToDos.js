import { connect } from 'react-redux'
import ToDoList  from '../component/toDoList.js'

const mapStateToProps = (state) => ({
  todos: state
})
export default connect(mapStateToProps)(ToDoList);
