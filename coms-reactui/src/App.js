import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import logo from './logo.svg';
import Header from './common/header';
import LeftBar from './common/leftbar';
import Footer from './common/footer';
import Breadcrumb from './common/breadcrumb';
import EntityAllView from './pages/entity_all_view';
import EntitySingleView from './pages/entity_single_view';
import EntityCreateForm from './pages/entity_create_form';
import EntityEditForm from './pages/entity_single_edit';
import LoginForm from './pages/login_form';
import Home from './pages/home';
import ActionOnActivity from './pages/bpm/action_on_activity';
import ProcessInstanceView from './pages/bpm/process_instance_view';
import ProcessDefinitionView from './pages/bpm/process_definition_view';
import ProcessDefinitionEdit from './pages/bpm/process_definition_edit';
import TaskAssignment from './pages/bpm/task_assignment';
import * as configClass from './common/config';
function App() {
    const [page, setPage] = useState("Home");
    const [entityid, setEntityid] = useState(0);
    const [parententityid, setParentEntityid] = useState(0);
    const [bearertoken, setBearertoken] = useState("");
    const [loggeduser, setLoggeduser] = useState({});
    const [loginstatus, setLoginstatus] = useState(false);
    useEffect(() => { 
        console.log("page called--"+ page);
    }, [page]);
    const handlePageNavigationLinkClick = (page) => {
        console.log("page:"+ page);
        setPage(page);
      }
      const handlePageActionClick = (page,entid) => {
        console.log("page:"+ page);
        setPage(page);
        setEntityid(entid);
      }
    
      const handleLoginsuccess =(token, useremail) =>
      {
        console.log("token:"+ token);
        setBearertoken(token);
        let headers = { "Authorization": 'Bearer ' + token};
        Axios.get('/getuser',{
                  headers: headers
            })
            .then(response => 
            {
                console.log(response.data);
                setLoggeduser(response.data);
                setPage("Home");
                setLoginstatus(true);
            });     
      }
  return (
        <div id="layoutDrawer">
          <LeftBar Loginstatus={loginstatus} Loggeduser={loggeduser} onClick={handlePageNavigationLinkClick}/>

          <Header Loginstatus={loginstatus} Useremail={loggeduser.first_name} onClick={handlePageNavigationLinkClick}/>
          <React.Fragment>
          <div id="layoutDrawer_content">
            <main>
                <div class="container-xl p-5">
                  <Breadcrumb Page={page} BreadcrumbData={configClass.breadcrumb_data}></Breadcrumb>
                  {
                    page==="Home"?<Home Token={bearertoken} Loggeduser={loggeduser}/>:
                    page==="Login"?<LoginForm handleLoginsuccess={handleLoginsuccess} handleRegisterClick={handlePageNavigationLinkClick}/>:
                    page==="ProcessDefintionAllView"?<EntityAllView Formprops={configClass.entity_all_view.view_all_process_definition} handlePageActionClick={handlePageActionClick} handleCreateClick={handlePageNavigationLinkClick} token={bearertoken}/>:
                    page==="JSONSchemaDefintionAllView"?<EntityAllView Formprops={configClass.entity_all_view.view_all_json_schema_definition} handlePageActionClick={handlePageActionClick} handleCreateClick={handlePageNavigationLinkClick} token={bearertoken}/>:
                    page==="ProcesInstanceAllView"?<EntityAllView Formprops={configClass.entity_all_view.view_all_process_instances} Entityid={entityid} handlePageActionClick={handlePageActionClick} token={bearertoken}/>:
                    page==="ProcesInstanceSingleView"?<ProcessInstanceView Formprops={configClass.entity_all_view.view_all_process_elements} Entityid={entityid} handlePageActionClick={handlePageActionClick} token={bearertoken}/>:
                    page==="SingleProcessDefinitionView"?<ProcessDefinitionView Entityid={entityid} Formprops={configClass.entity_single_view.view_single_process_defintion} handleEditClick={handlePageActionClick} token={bearertoken}/>:
                    page==="SingleJSONSchemaDefinitionView"?<EntitySingleView Entityid={entityid} Formprops={configClass.entity_single_view.view_single_json_schema_defintion} handleEditClick={handlePageActionClick} token={bearertoken}/>:
                    page==="ProcessDefinitionCreateForm"?<EntityCreateForm Formprops={configClass.entity_create.create_process_definition} token={bearertoken}/>:
                    page==="JSONSchemaDefinitionCreateForm"?<EntityCreateForm Formprops={configClass.entity_create.create_json_schema_definition} token={bearertoken}/>:
                    page==="EditProcessDefinition"?<ProcessDefinitionEdit Formprops={configClass.entity_edit.edit_process_definition} Entityid={entityid} token={bearertoken}/>:
                    page==="EditJSONSchemaDefinition"?<EntityEditForm Formprops={configClass.entity_edit.edit_json_schema_definition} Entityid={entityid} token={bearertoken}/>:
                    page==="ApplyLoan"?<EntityCreateForm Formprops={configClass.entity_create.apply_loan} token={bearertoken}/>:
                    page==="ActionOnActivity"?<ActionOnActivity Entityid={entityid} token={bearertoken}/>:
                    page==="TaskAssignment"?<TaskAssignment Entityid={entityid} token={bearertoken}/>:
                    page==="RegisterUser"?<EntityCreateForm Formprops={configClass.entity_create.register_user}/>:<Home/>
                  }
                </div>
              </main>
              <Footer/>
          </div>
        </React.Fragment>
      </div>
  );
}

export default App;
