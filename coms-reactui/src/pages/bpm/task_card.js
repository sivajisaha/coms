import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 

const TaskCard = (props) => {
    const [taskList, setTaskList] = useState({});
    const [taskMessage, setTaskMessage] = useState("Not logged");
    useEffect(() => { 
        console.log("props.loggeduser--"+props.Loggeduser.user_id);
        if(props.Loggeduser.user_id !='undefined')
        {
            const requestbody = {
                service:'COMS-BPM-API',   
                operation:'/process/task/active/' + props.Loggeduser.user_id,
                    requesttype:'get',
                    requestbody:''
                };
                Axios.post('/invoke', requestbody,
                { headers: { "Authorization": 'Bearer ' + props.Token}}
                )
                .then(
                    
                    response => setTaskMessage(response.data.length)
                );  
        }           
    }, []);
    return(
            <React.Fragment>
                <div class="col-xxl-3 col-md-6 mb-5">
                        <div class="card card-raised bg-secondary text-white">
                            <div class="card-body px-4">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <div class="me-2">
                                        <div class="display-5 text-white">{taskMessage}</div>
                                        <div class="card-text">{props.CardTitle}</div>
                                    </div>
                                    <div class="icon-circle bg-white-50 text-secondary"><i class="material-icons">people</i></div>
                                </div>
                                
                            </div>
                        </div>
                    </div>
            </React.Fragment>);
}
export default TaskCard;