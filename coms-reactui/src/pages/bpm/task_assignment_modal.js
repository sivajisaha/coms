import React , { useState ,useEffect,useRef } from 'react';
const TaskAssignmentModal = (props) => {
    return(
        <div className="popup">
        <div className="popup_inner">
        <div class="form-row">
        <div class="form-group col-md-12">
            <label><strong>Activity code</strong></label>
            :
            {props.SelectedItem.activity_code}
        </div>
        <div class="form-group col-md-12">
                <table class="table">
                    <thead class="thead-dark">
                        <tr>
                            <th scope="col">Id</th>
                            <th scope="col">Task Assignment Entity Id</th>
                            <th scope="col">Task Assignment Entity Type</th>
                            <th scope="col">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                        {props.SelectedItem.assignments.map(item => {  
                            return (
                                    <tr>
                                        <td>{item.id}</td>
                                        <td>{item.task_assignment_entity_id}</td>
                                        <td>{item.task_assignment_entity_type}</td>
                                        <td><button type="button" class="btn btn-info mr-1">Delete</button></td> 
                                    </tr>
                            )} )
                        }
                        </tbody>
                </table> 
        </div>
        <div class="form-group col-md-12 text-center">
                <button type="button" class="btn btn-info" onClick={props.closePopup}> Close </button>
        </div>
        </div>
      </div>  
      </div>
    )
}
export default TaskAssignmentModal;