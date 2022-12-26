import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import BPMDiagram from '../bpm/bpm_diagram';
import ActivityDetailsModal from '../bpm/activity_details_modal';
function ProcessInstanceView(props) {
    const [entitylist, setEntitylist] = useState([]);  
    const [columnheaderData, setColumnheaderData] = useState([]);
    const [columnnameData, setColumnnameData] = useState([]);
    const [showMessagePopup, setShowMessagePopup] = useState(false);
    const [selectedItemPopupView, setSelectedItemPopupView] = useState({});
    useEffect(() => { 
        setColumnheaderData(props.Formprops.column_header_data);
        setColumnnameData(props.Formprops.column_name_data);
        var extract_operation_parameter = props.Formprops.extract_operation_param_needed=="y"?props.Entityid: "";
        const requestbody = {
            service:props.Formprops.service,    
            operation:props.Formprops.extract_operation+extract_operation_parameter,
                requesttype:'get',
                requestbody:''
            };
            Axios.post('/invoke', requestbody,
            { headers: { "Authorization": 'Bearer ' + props.token}}
            )
                  .then(response => setEntitylist(response.data));             

    }, [props.Formprops.entity_name]); 
    const handlePageActionClick = (action_page,id)=> {
        props.handlePageActionClick(action_page,id);
      } 
    const toggleMessagePopup = (id) => { 
        setSelectedItemPopupView(entitylist.find(function(enitity, index) {
            if(enitity.id == id)
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
    return (
        <React.Fragment>
            <div class="row">
                <div class="col-sm">
                    <h3>{props.Formprops.form_title}</h3>
                    <hr></hr>
                </div>
            </div>
            <div class="form-group col-md-12">
                <BPMDiagram ModelType="Instance" EntityId={props.Entityid} token={props.token}></BPMDiagram>
            </div>
            <div class="form-group col-md-12">
            <table class="table table-bordered table-striped">
                <thead class="thead-dark">
                    <tr>
                        {columnheaderData.map(item => {  
                                return (
                                    <th scope="col">{item}</th>
                                )
                        })} 
                        <th scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                        {entitylist.map(item => {  
                            return (
                                <tr key={item[props.Formprops.column_id_column]}>
                                    {columnnameData.map(column => {  
                                        return (
                                            <td>{item[column]}</td>
                                        )
                                    })} 
                                    <td>
                                    {
                                    props.Formprops.action_pages.map(item2 => {  
                                    return (
                                        <span>
                                            {item["status"]=="In progress"?    
                                             <a href="#" class="link-dark" onClick={()=>handlePageActionClick(item2["action_page"],item[props.Formprops.column_id_column])}>Action</a>:
                                            item["status"]=="Completed" ||  item["status"]=="Rejected"?
                                            <a href="#" class="link-info" onClick={()=>toggleMessagePopup(item[props.Formprops.column_id_column])}>View</a>
                                           :""
                                            }
                                             
                                       </span>
                                    )
                                    }) }
                                    </td> 
                                </tr>
                            )
                        })}  
                </tbody>
            </table>
            </div>
            {
            showMessagePopup ?          
                <ActivityDetailsModal SelectedItem={selectedItemPopupView} closePopup={toggleMessagePopup} />
            : null
            }

        </React.Fragment>
      );
}
export default ProcessInstanceView;