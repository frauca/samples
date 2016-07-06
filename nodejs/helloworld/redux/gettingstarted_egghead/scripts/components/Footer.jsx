import React,{Component} from 'react'
import {Provider,connect} from 'react-redux'
import Filterlink from './Filterlink.jsx'

const Footer =()=>(
  <p>
    Show:
    {' '}
    <Filterlink filter='SHOW_ALL'>All</Filterlink>
    {' '}
    <Filterlink filter={'SHOW_ACTIVE'}>Active</Filterlink>
    {' '}
    <Filterlink filter='SHOW_COMPLETED'>Completed</Filterlink>
  </p>
)

module.exports=Footer
