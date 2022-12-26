import React , { useState ,useEffect,useRef } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import * as d3 from 'd3'
const BPMDiagram = (props) => {
  const [diagramData, setDiagramData] = useState([]);
  //const [lineData, setLineData] = useState([]);
  const [canvasX, setCanvasX] = useState(0);
  const [canvasY, setCanvasY] = useState(0);
  const ref = useRef()
  useEffect(() => {
    console.log("props.EntitySchema--"+ props.EntitySchema);
    console.log("props.EntityId--"+ props.EntityId);
    let diagrampath = props.ModelType=="Instance"?"process/instance/elements/visualization/"+ props.EntityId:
    props.ModelType=="DefinitionById"?"process/definition/elements/visualization/byid/"+ props.EntityId:
    "process/definition/elements/visualization/byschema";
    const requestbody = {
      service:"COMS-BPM-API",    
      operation: diagrampath,
          requesttype: props.ModelType=="DefinitionBySchema"?"post":"get",
          requestbody:props.ModelType=="DefinitionBySchema"?props.EntitySchema:""
      };
      Axios.post('/invoke', requestbody,
              { headers: { "Authorization": 'Bearer ' + props.token}}
            )
            .then(response => 
              {   
                setDiagramData(response.data);
                let maxxindex = Math.max(...response.data.map(o => o.xindex));
                let maxyindex = Math.max(...response.data.map(o => o.yindex));
                setCanvasX(maxxindex*(120));
                setCanvasY(maxyindex*(120));

                const svgElement = d3.select(ref.current);
                
                let activitywidth = 80;
                let activityheight = 40;
                
                const drawactivity = (g,x,y)=>
                {
                  g.append("rect")
                  .attr("x", x)
                  .attr("y", y)
                  .attr("width", activitywidth)
                  .attr("height", activityheight)
                  .attr("rx", 10)
                  .attr("ry", 10)
                  //.attr("stroke",  "black")
                  .attr("fill","#4472C4");
                }
                let eventradius = 20;
                let drawevent = (g,x,y)=>
                {
                  g.append("circle")
                  .attr("cx", x)
                  .attr("cy", y)
                  .attr("r",  eventradius)
                  .attr("fill","#CC0099");
                }
                let gatewaylength = 30;
                let gatewayrotateoffset = 15;
                const drawgateway = (g,x,y)=>
                {
                  g.append("rect")
                  .attr("x", x)
                  .attr("y", y)
                  .attr("width", gatewaylength)
                  .attr("height", gatewaylength)
                  .attr("fill","#FFC000")
                  .attr("transform", "rotate(45," + (x+gatewayrotateoffset) + ","+ (y+gatewayrotateoffset) +")");
                  g.append("text")
                  .attr("x", x+12)
                  .attr("y", y+18)
                  .style("font-size", "0.7em")
                  .style("font-weight", "bold")
                  .text("G");
                }
                let rulelength = 30;
                let rulerotateoffset = 15;
                const drawrule = (g,x,y)=>
                {
                  g.append("rect")
                  .attr("x", x)
                  .attr("y", y)
                  .attr("width", rulelength)
                  .attr("height", rulelength)
                  .attr("fill","#BDD7EE")
                  .attr("transform", "rotate(45," + (x+rulerotateoffset) + ","+ (y+rulerotateoffset) +")");
                  g.append("text")
                  .attr("x", x+12)
                  .attr("y", y+18)
                  .style("font-size", "0.7em")
                  .style("font-weight", "bold")
                  .text("R");
                }
                const drawtext = (g,x,y,text)=>
                {
                  g.append("text")
                  .attr("x", x)
                  .attr("y", y)
                  .style("font-size", "0.6em")
                  .style("fill", "white")
                  .text(text);
                }
                const drawimage =(g,path,w,h,x,y)=>
                {
                  if(path!='')
                  {
                    g.append('image')
                    .attr('xlink:href', path)
                    .attr('width', w)
                    .attr('height', h)
                    .attr('x', x)
                    .attr('y', y);
                  }
                }
                const getstatusimagepath = (status)=>
                {
                  switch(status) {
                    case 'Not started':
                      return '/images/icon_pending.png';
                    case 'Completed':
                        return '/images/icon_completed.png';
                    case 'In progress':
                          return '/images/icon_in_progress.png';
                    case 'Rejected':
                            return '/images/icon_cancelled.png';
                    default:
                      return '';
                  }
                }
                let startx =20;
                let starty =5;
                let xspace = 95;
                let yspace = 50;
                response.data.map(item => { 
                  if(item.element_type=="Event")
                  {
                    console.log("drawing event at x="+(startx+ xspace*item.xindex)+" y="+(starty+yspace*item.yindex));
                    drawevent(svgElement,(startx+ xspace*item.xindex+20),(starty+yspace*item.yindex+20));
                  }
                  if(item.element_type=="Activity")
                  {
                    let xtextoffset = 15;
                    drawactivity(svgElement,(startx+ xspace*item.xindex),(starty+yspace*item.yindex));
                    let statusimagepath = getstatusimagepath(item.status);
                    if(statusimagepath!='')
                    {
                      drawimage(svgElement,statusimagepath,20,20,(startx+ xspace*item.xindex+activitywidth-20),(starty+yspace*item.yindex));
                    }   
                    const titlearr = item.element_title.split(" ");
                    if(titlearr.length ==1)
                    {
                      let ytextoffset1 = 20;
                      drawtext(svgElement,(startx+ xtextoffset + xspace*item.xindex),((starty+yspace*item.yindex)+ytextoffset1),item.element_title);
                    }
                    else if (titlearr.length ==2)
                    {
                      let ytextoffset2 = 20;
                      let ytextoffset3 = 30;
                      drawtext(svgElement,(startx+ xtextoffset + xspace*item.xindex),((starty+yspace*item.yindex)+ytextoffset2),titlearr[0]);
                      drawtext(svgElement,(startx+ xtextoffset+ xspace*item.xindex),((starty+yspace*item.yindex)+ytextoffset3),titlearr[1]);
                    }
                    else if (titlearr.length ==3)
                    {
                      let ytextoffset4 = 15;
                      let ytextoffset5 = 25;
                      let ytextoffset6 = 35;
                      drawtext(svgElement,(startx+ xtextoffset+ xspace*item.xindex),((starty+yspace*item.yindex)+ytextoffset4),titlearr[0]);
                      drawtext(svgElement,(startx+ xtextoffset + xspace*item.xindex),((starty+yspace*item.yindex)+ytextoffset5),titlearr[1]);
                      drawtext(svgElement,(startx+ xtextoffset+ xspace*item.xindex),((starty+yspace*item.yindex)+ytextoffset6),titlearr[2]);
                    }
                    //drawtext(svgElement,(startx+ xspace*item.xindex),((starty+yspace*item.yindex)+20),item.element_title);
                    console.log("drawing activity "+ item.element_code +" at x="+(startx+ xspace*item.xindex)+" y="+(starty+yspace*item.yindex));
                  }
                  if(item.element_type=="Gateway")
                  {
                    drawgateway(svgElement,(startx+ xspace*item.xindex+30),(starty+yspace*item.yindex+5));
                    console.log("drawing gateway "+ item.element_code +" at x="+(startx+ xspace*item.xindex)+" y="+(starty+yspace*item.yindex));
                  }
                  if(item.element_type=="Rule")
                  {
                    drawrule(svgElement,(startx+ xspace*item.xindex+30),(starty+yspace*item.yindex+5));
                    console.log("drawing rule "+ item.element_code +" at x="+(startx+ xspace*item.xindex)+" y="+(starty+yspace*item.yindex));
                  }
                  
                });
                const returnlineendcordinates = (item,endtype)=>
                {
                  if(item.element_type=="Event")
                  {
                    if(endtype =="src")
                    {
                      return new Array((startx+ xspace*item.xindex+20+20),(starty+yspace*item.yindex+20));
                    }
                    else if(endtype =="dest")
                    {
                      return new Array((startx+ xspace*item.xindex+20),(starty+yspace*item.yindex+20));
                    }  
                  }
                  if(item.element_type=="Activity")
                  {
                    if(endtype =="src")
                    {
                      return new Array((startx+ xspace*item.xindex+80),(starty+yspace*item.yindex+20));
                    }
                    else if(endtype =="dest")
                    {
                      return new Array((startx+ xspace*item.xindex-5),(starty+yspace*item.yindex+20));
                    }
                  }
                  if(item.element_type=="Gateway")
                  {
                    if(endtype =="src")
                    {
                      return new Array((startx+ xspace*item.xindex+65),(starty+yspace*item.yindex+20));
                    }
                    else if(endtype =="dest")
                    {
                      return new Array((startx+ xspace*item.xindex+15),(starty+yspace*item.yindex)+20);
                    }
                  }
                  if(item.element_type=="Rule")
                  {
                    if(endtype =="src")
                    {
                      return new Array((startx+ xspace*item.xindex+65),(starty+yspace*item.yindex+20));
                    }
                    else if(endtype =="dest")
                    {
                      return new Array((startx+ xspace*item.xindex+20),(starty+yspace*item.yindex+20));
                    }
                  }
                }
                let lineData = [];
                response.data.map(item => { 
                  if(item.next_element_codes.length>0)
                  {
                    for (let i = 0; i < item.next_element_codes.length; i++) {
                      if(item.next_element_codes[i]!="")
                      {
                        var next_element = response.data.find(p => p.element_code==item.next_element_codes[i]);
          
                        var line = {
                          "src_element": item.element_code,
                          "dest_element": item.next_element_codes[i],
                          "src_coords": returnlineendcordinates(item,"src"),
                          "dest_coords": returnlineendcordinates(next_element,"dest")
                        }
                        lineData.push(line);
                      }
                     
                    }
                  }
                });
               
                console.log("linedata--"+ lineData);
                svgElement
                .append('defs')
                .append('marker')
                .attr('id', 'arrow')
                .attr('viewBox', [0, 0, 10, 10])
                .attr('refX', 1)
                .attr('refY', 5)
                .attr('markerWidth', 10)
                .attr('markerHeight', 10)
                .attr('orient', 'auto-start-reverse')
                .append('path')
                .attr('d', 'M 0 0 L 10 5 L 0 10 z')
                .attr('stroke', 'grey');

                for ( var i = 0; i < lineData.length; i++) {
                  var obj = lineData[i];
                  svgElement
                  .append('path')
                  .attr('d', d3.line()([obj.src_coords, obj.dest_coords]))
                  .attr('stroke', 'grey')
                  .attr('marker-end', 'url(#arrow)')
                  .attr('fill', 'none');
                }
              }
          );     
    
  }, [props.EntitySchema])
  return (
    <svg ref={ref} width={canvasX} height={canvasY} >
      
    </svg>
  )
}
export default BPMDiagram;