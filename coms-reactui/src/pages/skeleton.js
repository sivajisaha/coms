import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import Header from '../common/header';
function Skeleton(props) {
    const bearertoken = props.Token;
    /*useEffect(() => {           

    }, []); */
    return ( 
        <div id="layoutDrawer_content"></div>
    );
}
export default Skeleton;