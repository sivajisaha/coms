import React , { useState ,useEffect,useRef } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import Header from '../common/header';
const EntityCreateForm = (props) => {
    const [formData, setFormData] = useState({});
    const [fieldData, setFieldData] = useState([]);
    const [fieldvalidationMessage, setFieldvalidationMessage] = useState({});  
    const [statusMessage, setStatusMessage] = useState("");
    const [statusMessageCss, setStatusMessageCss] = useState("alert-warning invisible");  
    const statusmessageFocus = useRef(null);
    const [disableSubmit, setDisableSubmit] = React.useState(false);
      useEffect(() => {                     
        setFormData(JSON.parse(JSON.stringify(props.Formprops.form_data))); //deep copy
        setFieldData(props.Formprops.field_data); //shallow copy
        setFieldvalidationMessage(JSON.parse(JSON.stringify(props.Formprops.form_data)));//deep copy

    }, []); 
    const validate =()=> {
        let valid = true;
        props.Formprops.field_data.map((p)=>
        {
            let fldval = formData[p.field_name];
            let fldvalmsg = fieldvalidationMessage[p.field_name];
            if(fldval=="")
            {
                console.log("setting error message for field :"+ p.field_name);
                fieldvalidationMessage[p.field_name] = p.validation_error_message;
                valid = false;
            }
            else
            {
                fieldvalidationMessage[p.field_name] = "";
            }
            setFieldvalidationMessage({ ...fieldvalidationMessage });
           
        });
        return valid;
    }
    const onSubmit = e => {

        if (validate()) {
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
                statusmessageFocus.current.focus();
                });
        }
    };
    const geterrormessage = (fieldid)=>
    {
        return fieldvalidationMessage[fieldid];
    };
    const handleChange = e =>{
        formData[e.target.id] = e.target.value;
        setFormData({ ...formData });
      };
    return (
        <React.Fragment>
           
            <h3>{props.Formprops.form_title}</h3>
           
            <div className="form-row">
                {fieldData.map(field => {  
                    switch (field.field_type) {
                        case "text":
                                return (
                                    <div className="form-group col-md-12">
                                    <label for={field.field_name} className="col-xs-2">{field.field_label}</label>
                                    <div className="col-xs-10">
                                    <input type="text" className="form-control" key={field.field_name} id={field.field_name} placeholder={field.default_value} onChange={handleChange} />
                                    <span className="text-danger">{geterrormessage(field.field_name)}</span>
                                    </div>
                                 
                                </div>
                                )
                        case "text-area":
                                return (
                                    <div className="form-group col-md-12">
                                        <label for={field.field_name} className="col-xs-2">{field.field_label}</label>
                                        <div className="col-xs-10">
                                        <textarea className="form-control" rows="8" key={field.field_name} id={field.field_name} placeholder={field.default_value} onChange={handleChange} />
                                        <span className="text-danger">{geterrormessage(field.field_name)}</span>
                                        </div>
                                    </div>
                                )
                        case "dropdown":
                            return (
                                <div className="form-group col-md-12">
                                    <label for={field.field_name} className="col-xs-2">{field.field_label}</label>
                                    <div class="col-xs-10">
                                        <select className="form-select" id={field.field_name} onChange={handleChange}>
                                        {JSON.parse(field.default_value).map((dd) => <option value={dd.value}>{dd.label}</option>)}
                                        </select>
                                        <span className="text-danger">{geterrormessage(field.field_name)}</span>
                                    </div>
                                </div>
                            )
                    }
                })
                }
                <div class="col-xs-10 col-xs-offset-2">
                    <button type="submit" class="btn btn-primary" onClick={onSubmit} disabled={disableSubmit}>Submit</button>
                </div>
               
            </div>
            <div class="form-row">
                <div class={statusMessageCss} role="alert" ref={statusmessageFocus}>
                    {statusMessage}
                </div>
            </div>
        </React.Fragment>

    );
};
export default EntityCreateForm;