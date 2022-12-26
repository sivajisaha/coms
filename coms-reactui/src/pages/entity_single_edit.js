import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import Header from '../common/header';
const EntityEditForm = (props) => {
    const [formData, setFormData] = useState({});
    const [fieldData, setFieldData] = useState([]);
    const [statusMessage, setStatusMessage] = useState("");
    const [statusMessageCss, setStatusMessageCss] = useState("alert-warning invisible");  
      useEffect(() => {          
        console.log("useEffect-edit form called");
        setFieldData(props.Formprops.field_data);
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
        setStatusMessage(res.data.response_status);
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
            <form class="form-horizontal" onSubmit={onSubmit}>
                {formData && fieldData.map(field => {  
                    switch (field.field_type) {
                        case "text":
                                return (
                                    <div class="form-row">
                                    <div class="form-group col-md-6">
                                    <label for={field.field_name} class="col-xs-2">{field.field_label}</label>
                                    <input type="text" class="form-control" name={field.field_name} key={field.field_name} value={formData[field.field_name]} onChange={handleChange} />
                                    </div>
                                    <div class="form-group col-md-6">
                                    </div>
                                </div>
                                )
                         case "label":
                                return (
                                    <div class="form-row">
                                    <div class="form-group col-md-6">
                                    <label for={field.field_name} class="col-xs-2">{field.field_label}</label>
                                   {formData[field.field_name] }</div>
                                    <div class="form-group col-md-6">
                                    </div>
                                </div>
                                    
                                )
                        case "text_area":
                                return (
                                    <div class="form-row">
                                    <div class="form-group col-md-6">
                                    <label for={field.field_name} class="col-xs-2">{field.field_label}</label>
                                    <textarea class="form-control" rows="8" name={field.field_name} key={field.field_name} value={formData[field.field_name]} onChange={handleChange} />
                                    </div>
                                    <div class="form-group col-md-6">
                                        </div>
                                    </div>
                                    )
                    }
                })
                }
                <div class="col-xs-10 col-xs-offset-2">
                    <button type="submit" class="btn btn-primary" >Submit</button>
                </div>
                <div class="col-xs-10 col-xs-offset-2">
                <div class={statusMessageCss} role="alert">
                    {statusMessage}
                </div>
            </div>
            </form>
            
            </React.Fragment>

    );
};
export default EntityEditForm;