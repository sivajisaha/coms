import React , { useState ,useEffect,useRef } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
const ActionOnActivity = (props) => {
    const [formData, setFormData] = useState({});
    const [comment, setComment] = useState("");
    const [targetAction, setTargetAction] = useState("");
    const [transactionOutcome, setTransactionOutcome] = useState("");
    const [statusMessage, setStatusMessage] = useState("");
    const [statusMessageCss, setStatusMessageCss] = useState("alert-warning invisible");  
    const statusmessageFocus = useRef(null);
    const [disableSubmit, setDisableSubmit] = React.useState(false);
    useEffect(() => {          
        console.log("useEffect called-"+ props.Entityid);
        const requestbody = {
            service:'COMS-BPM-API',    
            operation:'/process/instance/element/2/' + props.Entityid,
                requesttype:'get',
                requestbody:''
            };
            Axios.post('/invoke', 
                    requestbody,
                    { headers: { "Authorization": 'Bearer ' + props.token}}
                )
                .then(response => 
                {   
                    setFormData(response.data);
                }
            ); 
       
    }, []); 
    const onSubmit = e => {
        e.preventDefault();
        const submitdata = {
            context: {
                "processInstanceId": formData.process_instance_id,
                "processCode": "",
                "version": "",
                "transactionCode": formData.element_code,
                "transactionOutcome": transactionOutcome,
                "targetactionCode": targetAction,
                "transactionComment": comment
            },
            data:formData.handled_message
          };
          const submitbody = {
            service:"COMS-BPM-API",
            operation:"/postmessage",
            requesttype:"post",
            requestbody:JSON.stringify(submitdata)
          };
        if(targetAction!="")
        {
            Axios.post("/invoke", submitbody,
            { headers: { "Authorization": 'Bearer ' + props.token}}
            )
            .then(res => {
                console.log(res);
                console.log("response received--"+ res.data);
                setStatusMessage(res.data);
                setStatusMessageCss("alert-warning visible");
                statusmessageFocus.current.focus();
                //setDisableSubmit(true);to uncomment
            })
        }
        else
        {
            setStatusMessage("Please select action");
            setStatusMessageCss("alert-warning visible");
        }
        //alert(submitdata);
    };
    const handleChange = e =>{
        setComment(e.target.value);
    };
    const handleActionDropdownChange = e =>{
        setTargetAction(e.target.value);
        setTransactionOutcome(e.target.value);
        console.log("Action dropdown value --"  +e.target.value);
    };
    return(
        <React.Fragment>
       
                <h3>Action</h3>
                <div class="col-sm"></div>
                <div class="form-row">
                    <div class="form-group col-md-12">
                        <label>Element code</label>
                        <div>{formData.element_code}</div>
                    </div>
                    <div class="form-group col-md-12">
                        <label>Data</label>
                        <div class="vertical-scroll"><pre>{formData.handled_message}</pre></div>
                    </div>
                    <div class="form-group col-md-12">
                        <label>Action</label>
                        <div>
                            <select class="form-select" aria-label="Default select example" onChange={e => handleActionDropdownChange(e)}>
                                <option value="-1">Select</option>
                                <option value="SAVE">Save</option>
                                <option value="APPROVE">Approve</option>
                                <option value="REJECT">Reject</option>
                            </select></div>
                        </div>
                    <div class="form-group col-md-12">
                        <label for="actioncomment">Comment</label>
                        <input type="text" class="form-control" id="actioncomment" placeholder="Comment" onChange={handleChange}/>
                    </div>
                    <div class="form-group col-md-12">
                        <button type="submit" class="btn btn-primary mr-1" onClick={onSubmit} disabled={disableSubmit}>Submit</button>
                    </div>
                    <div class="form-group col-md-12">
                        <div class={statusMessageCss} role="alert" ref={statusmessageFocus}>
                            {statusMessage}
                        </div>
                    </div>
                </div>
  </React.Fragment>
    );
    
}
export default ActionOnActivity;