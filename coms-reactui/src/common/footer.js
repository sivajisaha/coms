import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
function Footer(props) {
    return(
        <footer class="py-4 mt-auto border-top">
                <div class="container-xl px-5">
                  <div class="d-flex flex-column flex-sm-row align-items-center justify-content-sm-between small">
                      <div class="me-sm-2">Copyright © Your Website 2021</div>
                      <div class="d-flex ms-sm-2">
                          <a class="text-decoration-none" href="#!">Privacy Policy</a>
                          <div class="mx-1">·</div>
                          <a class="text-decoration-none" href="#!">Terms &amp; Conditions</a>
                      </div>
                  </div>
              </div>
        </footer>
    );
}
export default Footer;
