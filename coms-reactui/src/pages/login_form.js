import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
const LoginForm = (props) => {

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const handleSubmit = (event) => {
        //Prevent page reload
        event.preventDefault();
    
        var { uemail, password } = document.forms[0];
        const requestbody = {
            "username":uemail.value,    
            "password": password.value
            };
        Axios.post('/authenticate', requestbody)
        .then(res=>{
            props.handleLoginsuccess(res.data.token, uemail.value);
  
        }).catch(function (error) {
            if (error.response) {
                console.log("login fail");
            setErrorMessages({ name: "password", message: "invalid user email/password" });
              console.log(error.response.data);
              console.log(error.response.status);
              console.log(error.response.headers);
            }
        });
      };
      const renderErrorMessage = (name) =>
        name === errorMessages.name && (
        <div className="error">{errorMessages.message}</div>
        );
        const handleRegister = (page)=> {
            props.handleRegisterClick(page);
          } 
    return (
       
            <div class="row justify-content-center">
            <div class="col-xxl-4 col-xl-5 col-lg-6 col-md-8">
                <form onSubmit={handleSubmit}>
                    <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                    <div class="form-group">
                    <label htmlFor="inputEmail" className="sr-only">Email address</label>
                        <input type="email" name="uemail" id="uemail" className="form-control" placeholder="Email address" required autoFocus/>
                    </div>
                    <div class="form-group">
                        <label htmlFor="inputPassword" className="sr-only">Password</label>
                        <input type="password" name="password" id="password" className="form-control" placeholder="Password" required/>
                        {renderErrorMessage("password")}
                    </div>
                    <div class="form-group">
                        <button className="btn btn-primary btn-block" type="submit">Sign in</button>
                    </div>
                    Don't have an account? <a href="#" onClick={()=>handleRegister("RegisterUser")}>Register</a> 
                
                </form>
            </div>
        </div>
        
    );
};
export default LoginForm;