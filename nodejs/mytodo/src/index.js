
import React from 'react'
import {render} from 'react-dom';
import { createStore } from 'redux'
import { Provider } from 'react-redux'
import App from './component/App.js'
import reducer from './reducers'

var store = createStore(reducer);

const paintMe=()=>{
  console.log('Ei you must paint me');
  render(
    <Provider store={store}>
      <App />
    </Provider>,
    document.getElementById('root')
  )
}

store.subscribe(paintMe);
paintMe();
