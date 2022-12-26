import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import Header from '../common/header';
import BPMDiagram from './bpm/bpm_diagram';
import TaskCard from './bpm/task_card';
function Home(props) {
    const bearertoken = props.Token;
    /*useEffect(() => {           

    }, []); */
    return ( 
       <React.Fragment>
            <div class="row justify-content-between align-items-center mb-5">
                <div class="col flex-shrink-0 mb-5 mb-md-0">
                    <h1 class="display-4 mb-0">Dashboard</h1>
                    <div class="text-muted">COMS BPM portal</div>
                </div>
                
            </div>
              
                <div class="row gx-5">
                <TaskCard CardTitle="Active tasks" Loggeduser={props.Loggeduser} Token={props.Token}></TaskCard>
                <TaskCard CardTitle="Active tasks" Loggeduser={props.Loggeduser} Token={props.Token}></TaskCard>
                <TaskCard CardTitle="Active tasks" Loggeduser={props.Loggeduser} Token={props.Token}></TaskCard>
                <TaskCard CardTitle="Active tasks" Loggeduser={props.Loggeduser} Token={props.Token}></TaskCard>   
                </div>
        </React.Fragment>

    );
}
export default Home;