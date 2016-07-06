import {connect} from 'react-redux'
import React from 'react'
import {setVisibilityFilter} from '../actions/actions.jsx'


const Link = ({active,onClick,children})=>{
  //console.log('stateless'+filter);
  if(active){
    return(<span>{children}</span>)
  }
  return (
    <a href='#'
        onClick={e=>{
          e.preventDefault();
          onClick();
        }}>
            {children}
    </a>
  )
}

const mapStateToFilterLink=(state,ownProps)=>{
  return{
    active:state.visibilityFilter===ownProps.filter
  };
};

const mapDispatchToFilterLink=(dispatch,ownProps)=>{
  return{
    onClick:()=>{
      dispatch(setVisibilityFilter(ownProps.filter) );
    }
  };
};

const Filterlink=connect(
  mapStateToFilterLink,
  mapDispatchToFilterLink
)(Link);


module.exports=Filterlink;
