import React , { useState ,useEffect,useRef } from 'react';
import ReactDOM from 'react-dom';
const ActivityDetailsModal = (props) => {
    return(
        <div className="popup">
        <div className="popup_inner">
        <div class="form-row">
            <div class="form-group col-md-12">
            <label><strong>Item code</strong></label>
                <div class="col-sm">
                    {props.SelectedItem.element_code}
                </div>
            </div>
            <div class="form-group col-md-12">
                <label><strong>Request message</strong></label>
                <div><pre>{props.SelectedItem.request_message}</pre></div>
            </div>
            <div class="form-group col-md-12">
                <label><strong>Response message</strong></label>
                <div class="vertical-scroll"><pre>{props.SelectedItem.response_message}</pre></div>
            </div>
            <div class="form-group col-md-12">
                <label><strong>Handled message</strong></label>
                <div class="vertical-scroll"><pre>{props.SelectedItem.handled_message}</pre></div>
            </div>
            <div class="form-group col-md-12 text-center">
                <button type="button" class="btn btn-info" onClick={props.closePopup}> Close </button>
            </div>
        </div>
      </div>  
      </div>
    )
}
export default ActivityDetailsModal;