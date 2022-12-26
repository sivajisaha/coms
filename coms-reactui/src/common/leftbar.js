import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
function LeftBar(props) {
    const [userroles, setUserroles] = useState([]);
    useEffect(() => { 
        //console.log("leftmenu useeffect called"); 
        if(props.Loggeduser.roles!== undefined) 
        {
            setUserroles(props.Loggeduser.roles);
        }           
    }, [props.Loginstatus]); 
    const handleClick = (page)=> {
        props.onClick(page);
      } 
    return (
        <div id="layoutDrawer_nav">
                <nav class="drawer accordion drawer-light bg-white" id="drawerAccordion">
                    <div class="drawer-menu">
                        <div class="nav">
                            <div class="drawer-menu-divider d-sm-none"></div>
                                <a class="nav-link" href="#" onClick={()=>handleClick("Home")}>
                                    <div class="nav-link-icon"><i class="material-icons">language</i></div>
                                    Home
                                </a>
                                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseDashboards" aria-expanded="false" aria-controls="collapseDashboards">
                                    <div class="nav-link-icon"><i class="material-icons">dashboard</i></div>
                                    Loan application
                                    <div class="drawer-collapse-arrow"><i class="material-icons">expand_more</i></div>
                                </a>
                                <div class="collapse" id="collapseDashboards" aria-labelledby="headingOne" data-bs-parent="#drawerAccordion">
                                    <nav class="drawer-menu-nested nav">
                                        <a href="#" class="nav-link" onClick={()=>handleClick("ApplyLoan")}>Apply for loan</a>
                                    </nav>
                                </div>
                            
                            <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseLayouts" aria-expanded="false" aria-controls="collapseLayouts">
                                <div class="nav-link-icon"><i class="material-icons">dashboard</i></div>
                                Dashboard
                                <div class="drawer-collapse-arrow"><i class="material-icons">expand_more</i></div>
                            </a>
                           
                            <div class="collapse" id="collapseLayouts" aria-labelledby="headingOne" data-bs-parent="#drawerAccordion">
                                <nav class="drawer-menu-nested nav">
                                    <a class="nav-link" href="#" onClick={()=>handleClick("ProcessDefintionAllView")}>Process Definition</a>
                                    <a class="nav-link" href="#" onClick={()=>handleClick("JSONSchemaDefintionAllView")}>JSON Schema Definition</a>
                                </nav>
                            </div>
                            {
                        ((userroles.includes("admin")||userroles.includes("superadmin")) &&
                            <div>
                                <div class="drawer-menu-divider"></div>
                                
                                <div class="drawer-menu-heading">Admin</div>

                                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseForms" aria-expanded="false" aria-controls="collapseForms">
                                    <div class="nav-link-icon"><i class="material-icons">description</i></div>
                                    BPM
                                    <div class="drawer-collapse-arrow"><i class="material-icons">expand_more</i></div>
                                </a>
                            
                                <div class="collapse" id="collapseForms" aria-labelledby="headingOne" data-bs-parent="#drawerAccordion">
                                    <nav class="drawer-menu-nested nav">
                                        <a class="nav-link" href="#" onClick={()=>handleClick("ProcessDefintionAllView")}>Process Definition</a>
                                        
                                    
                                    </nav>
                                </div>
                                
                                <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapseUtilities" aria-expanded="false" aria-controls="collapseUtilities">
                                    <div class="nav-link-icon"><i class="material-icons">build</i></div>
                                    Utilities
                                    <div class="drawer-collapse-arrow"><i class="material-icons">expand_more</i></div>
                                </a>
                                
                                <div class="collapse" id="collapseUtilities" aria-labelledby="headingOne" data-bs-parent="#drawerAccordion">
                                    <nav class="drawer-menu-nested nav">
                                        <a class="nav-link" href="utilities-background.html">Menu 1</a>
                                        <a class="nav-link" href="utilities-borders.html">Menu 2</a>
                                    
                                    </nav>
                                </div>
                            </div>
                        )}
                        </div>
                    </div>
                    
                    <div class="drawer-footer border-top">
                        <div class="d-flex align-items-center">
                            <i class="material-icons text-muted">account_circle</i>
                            <div class="ms-3">
                                <div class="caption">Logged in as:</div>
                                <div class="small fw-500">{props.Loggeduser.first_name}</div>
                            </div>
                        </div>
                    </div>
                </nav>
            </div>
    )
}
export default LeftBar;