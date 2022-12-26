import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import BPMDiagram from '../bpm/bpm_diagram';
function ProcessDefinitionEdit(props) {
    const [formData, setFormData] = useState({});
    //const [workflowschema, setWorkflowschema] = useState("");
    const [statusMessage, setStatusMessage] = useState("");
    const [statusMessageCss, setStatusMessageCss] = useState("alert-warning invisible");  
      useEffect(() => {          
        console.log("useEffect-edit form called");
        const requestbody = {
            service:props.Formprops.service,    
            operation:props.Formprops.dataload_operation + props.Entityid,
                requesttype:'get',
                requestbody:''
            };
            Axios.post('/invoke', requestbody,
                { headers: { "Authorization": 'Bearer ' + props.token}}
            )
                  .then(response => 
                    {   
                        setFormData(response.data);
                        //console.log("schema 1--"+ response.data["definition"]);
                        //setWorkflowschema(response.data["definition"]);
                    }
                ); 
    }, [props.Entityid]); 
    const onSubmit = e => {
        e.preventDefault();
        const requestbody = {
            service:props.Formprops.service,    
            operation: props.Formprops.submit_operation,
                requesttype:'post',
                requestbody:JSON.stringify(formData)
            };
        Axios.post('/invoke', requestbody,
            { headers: { "Authorization": 'Bearer ' + props.token}}
        )
        .then(res=>{
        console.log(res);
        console.log(res.data);
        setStatusMessage(res.data.response_message);
        setStatusMessageCss("alert-warning visible");
        });
    };
   
    const handleChange = e =>{
        formData[e.target.name] = e.target.value;
        setFormData({ ...formData });
      };
    return (
        <React.Fragment>
 
            <h3>{props.Formprops.form_title}</h3>
            <hr></hr>
                <div class="row mb-3">    
                    <label for="id" class="col-sm-2 col-form-label">Id</label>
                    <div class="col-sm-10">{formData["id"] }</div>
                </div>
                <div class="row mb-3">  
                    <label for="code" class="col-sm-2 col-form-label">Code</label>
                    <div class="col-sm-10">
                            <input type="text" class="form-control" name="code" key="code" value={formData["code"]} onChange={handleChange} />
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="version" class="col-sm-2 col-form-label">Version</label>
                            <div class="col-sm-10">
                            <input type="text" class="form-control" name="version" key="version" value={formData["version"]} onChange={handleChange} />
                    </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="description" class="col-xs-2">Model</label>
                            <BPMDiagram ModelType="DefinitionBySchema" EntityId={props.Entityid} EntitySchema={formData["definition"]} token={props.token}></BPMDiagram>
                        </div>
                        <div class="form-group col-md-6"></div>
                    </div>
                   
                    <div class="row mb-3">  
                        <label for="code" class="col-sm-2 col-form-label">Definition</label>
                            <div class="col-sm-10">
                            <textarea type="text" rows="8" class="form-control" name="definition" key="definition" value={formData["definition"]} onChange={handleChange} />
                        </div>
                    </div>
                <div class="row mb-3">
                        <label for="description" class="col-sm-2 col-form-label">Description</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="description" key="description" value={formData["description"]} onChange={handleChange} />
                        </div> 
                </div>
                <div class="col-xs-10 col-xs-offset-2">
                    <button type="submit" class="btn btn-primary" onClick={onSubmit}>Submit</button>
                </div>
                <div class="col-xs-10 col-xs-offset-2">
                <div class={statusMessageCss} role="alert">
                    {statusMessage}
                </div>
            </div>
            
            </React.Fragment>
    );
};
export default ProcessDefinitionEdit;
