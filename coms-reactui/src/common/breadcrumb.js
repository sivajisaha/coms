import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
function Breadcrumb(props) {
    const [breadcrumburls, setBreadcrumburls] = useState([]);
    useEffect(() => { 
        //console.log("breadcrumb data--"+ JSON.stringify(props.BreadcrumbData)); 
        const result  = props.BreadcrumbData.filter(item =>item.page_id==props.Page);
        //console.log("lets check--"+ JSON.stringify(result)); 
        //console.log("Page--"+ props.Page); 
        //console.log("result length--"+ result.length); 
        if(result.length ==1)
        {
            setBreadcrumburls(result[0].bread_crumb_urls);
            result[0].bread_crumb_urls.forEach(url => { 
                console.log("writing object--"+url.uri_title);
            });
        }
        else
        {
            setBreadcrumburls([]);
        }
        //setBreadcrumburls(props.BreadcrumbData.filter(item =>item.page_id==props.Page));
       // console.log("breadcrumb urls--"+ result); 
                 
    }, [props.Page]); 
    return(
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                {breadcrumburls.map(item => {  
                       return (
                        item.link =="true" ?
                            <li class="breadcrumb-item"><a href="#">{item.uri_title}</a></li>:
                            <li class="breadcrumb-item active" aria-current="page">{item.uri_title}</li>
                        )} )
                    }
            </ol>
        </nav>
    );
}
export default Breadcrumb;