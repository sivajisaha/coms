import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import BPMDiagram from '../bpm/bpm_diagram';
function ProcessDefinitionView(props) {
    const [entity, setEntity] = useState(null);
    const [fieldData, setFieldData] = useState([]);
    useEffect(() => {  
        console.log("passed id:"+ props.Entityid); 
        setFieldData(props.Formprops.field_data);
        let headers = { "Authorization": 'Bearer ' + props.token};
        
        const requestbody = {
            service:props.Formprops.service,    
            operation: '/process/processdef/' + props.Entityid,
                requesttype:'get',
                requestbody:''
            };
            Axios.post('/invoke', requestbody,
                { headers: { "Authorization": 'Bearer ' + props.token}}
            )
        .then(response => setEntity(response.data));     
    },[props.Entityid]);
    const handleEditClick = (id)=> {
        console.log('id clicked:'+ id);
        props.handleEditClick(props.Formprops.edit_form,id);
      } 
    if (!entity) return null;
    return (
        <React.Fragment>
            <div class="row">
                
            <div class="col-sm">
                <h3>{props.Formprops.form_title}</h3>
            </div>
            <div class="col-sm"></div>
                
            <div class="col-sm">
                <button type="button" class="btn btn-primary float-right" onClick={()=>handleEditClick(entity[props.Formprops.column_id_column])}>{props.Formprops.form_edit_header}</button>
                </div>
            </div>
            <div>
                <table class="table">
                    <tbody>
                        <tr><td>Process Id</td><td>{entity["id"]}</td><td class="col-sm-6"></td></tr>
                        <tr><td>Process Code</td><td>{entity["code"]}</td><td class="col-sm-6"></td></tr>
                        <tr><td>Version</td><td>{entity["version"]}</td><td class="col-sm-6"></td></tr>
                        <tr><td>Description</td><td>{entity["description"]}</td><td class="col-sm-6"></td></tr>
                        <tr><td>Model</td><td colSpan={2}><BPMDiagram ModelType="DefinitionById" EntityId={props.Entityid} token={props.token} EntitySchema=""></BPMDiagram></td></tr>
                        <tr><td>Defintion</td><td><div><pre>{entity["definition"]}</pre></div></td><td class="col-sm-6"></td></tr>
                    </tbody>
                </table>  
            </div>
            <div class="col-sm">
            </div>
        </React.Fragment>
        
    );
}
export default ProcessDefinitionView;