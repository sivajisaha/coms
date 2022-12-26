import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import TaskAssignmentModal from '../bpm/task_assignment_modal';
const TaskAssignment = (props) => {
    const [tasklist, setTasklist] = useState([]); 
    const [showMessagePopup, setShowMessagePopup] = useState(false);
    const [selectedItemAssignmentPopupView, setSelectedItemAssignmentPopupView] = useState({});
    useEffect(() => { 
        const requestbody = {
            service:'COMS-BPM-API',   
            operation:'/process/taskdef/' + props.Entityid,
                requesttype:'get',
                requestbody:''
            };
            Axios.post('/invoke', requestbody,
            { headers: { "Authorization": 'Bearer ' + props.token}}
            )
            .then(response => setTasklist(response.data));             

    }, []);
    const toggleMessagePopup = (activity_code) => { 
        setSelectedItemAssignmentPopupView(tasklist.find(function(enitity, index) {
            if(enitity.activity_code == activity_code)
                return true;
        }));
        if(showMessagePopup) 
        {
            setShowMessagePopup(false);
        }  
        else
        {
            setShowMessagePopup(true);
        }
    }
    return(
        <React.Fragment>
           <div class="row">
                <div class="col-sm">
                    <h3>Task assignment</h3>
                </div>
                <div class="col-sm"></div>
                <div>
                    <table class="table">
                    <thead class="thead-dark">
                        <tr>
                            <th scope="col">Task</th>
                            <th scope="col">Assignment status</th>
                            <th scope="col">Edit assignment</th>
                            </tr>
                        </thead>
                        <tbody>
                        {tasklist.map(item => {  
                            return (
                                <React.Fragment>
                                    <tr>
                                        <td>{item.activity_code}</td>
                                        <td>{item.assignments!=""?"Assigned":"Not assigned"}</td>
                                        <td><button type="button" class="btn btn-info mr-1" onClick={()=>toggleMessagePopup(item.activity_code)}>View/Edit assignment</button></td> 
                                    </tr>
                                </React.Fragment>
                            )} )
                        }
                        </tbody>
                    </table>  
                </div>
        </div>
        {
            showMessagePopup ?          
                <TaskAssignmentModal SelectedItem={selectedItemAssignmentPopupView} closePopup={toggleMessagePopup} />
            : null
        }
        </React.Fragment>
    );
}
export default TaskAssignment;