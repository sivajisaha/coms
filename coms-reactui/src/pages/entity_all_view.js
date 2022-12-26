import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
function EntityAllView(props) {
    const [entitylist, setEntitylist] = useState([]);  
    const [columnheaderData, setColumnheaderData] = useState([]);
    const [columnnameData, setColumnnameData] = useState([]);
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
            Axios.post('/invoke',
                requestbody,
                { headers: { "Authorization": 'Bearer ' + props.token}}
                )
                .then(response => setEntitylist(response.data));             

    }, [props.Formprops.entity_name]); 
    const handlePageActionClick = (action_page,id)=> {
        props.handlePageActionClick(action_page,id);
      } 
      
      const handleEntityCreateClick = ()=> {
        props.handleCreateClick(props.Formprops.form_create_page);
      } 
    return (
        <React.Fragment>
            <div class="row justify-content-between align-items-center mb-5">
                <div class="col-sm">
                    <h3>{props.Formprops.form_title}</h3>
                </div>
                <div class="col-sm">
                    {(props.Formprops.form_create_header!="")&&<button type="button" class="btn btn-primary float-right" onClick={()=>handleEntityCreateClick()}>{props.Formprops.form_create_header}</button>}
                </div>
            </div>
            <div class="form-group col-md-12">
                <hr></hr>
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
                                            <button type="button" class="btn btn-info mr-1" onClick={()=>handlePageActionClick(item2["action_page"],item[props.Formprops.column_id_column])}>{item2["action"]}</button>
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
            
        </React.Fragment>
      );
}
export default EntityAllView;