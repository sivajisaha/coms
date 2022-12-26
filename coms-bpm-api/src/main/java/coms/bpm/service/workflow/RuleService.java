package coms.bpm.service.workflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.jexl2.Expression;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class RuleService {
	public Boolean processRule(String jsonrulestr) throws JsonMappingException, JsonProcessingException
	{
		//System.out.println("now reading jackson");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(jsonrulestr);
		JexlContext facts = new MapContext();
		 Iterator<Entry<String, JsonNode>> factfields = jsonNode.get("facts").fields();
		    while (factfields.hasNext()) {
		    	 Map.Entry<String, JsonNode> field = factfields.next();
		    	 System.out.println("Fact Field name: " + field.getKey());
		    	 System.out.println("Fact Field value: " + field.getValue().toString());
		    	 facts.set(field.getKey(), field.getValue().toString().replaceAll("\"", ""));
		    }
	    Iterator<Entry<String, JsonNode>> conditionfields = jsonNode.get("conditions").fields();
	    Boolean finalresult = false;
	    while (conditionfields.hasNext()) {
	    	 Map.Entry<String, JsonNode> field = conditionfields.next();
	    	 System.out.println("Root Field name: " + field.getKey());
	    	 finalresult = processchildRule(jsonNode,field.getKey(),facts);
	    }
	    return finalresult;
	}
	Boolean processchildRule(JsonNode rootnode,String parentcondition,JexlContext facts) throws JsonMappingException, JsonProcessingException
	{
		System.out.println("--processchildRule-- " + parentcondition);
		JsonNode childNodeArr = rootnode.at("/conditions/"+ parentcondition);
		ArrayList<Boolean> parentruleesult = new ArrayList<Boolean>();
		if (childNodeArr.isArray()) {
			 System.out.println("yes array");
			 for (JsonNode arrayItem : childNodeArr) {
				
				Iterator<Entry<String, JsonNode>> childrulenodeitr = arrayItem.fields();
	 		    while (childrulenodeitr.hasNext()) {
	 		    	Map.Entry<String, JsonNode> childrulenode = childrulenodeitr.next();
	 		    	System.out.println("Child rule field  name: " + childrulenode.getKey());
	 		    	System.out.println("Child rule Field value: " + childrulenode.getValue().toString());
	 		    	System.out.println("###Child rule#######");
	 		      	JsonNode grandchildruleNodeArr = new ObjectMapper().readTree(childrulenode.getValue().toString());
		 		    if (grandchildruleNodeArr.isArray()) {
		 		    	 ArrayList<Boolean> grandchildrruleesult = new ArrayList<Boolean>();
		 		    	 for (JsonNode grandchildrulenode : grandchildruleNodeArr) {
		 		    		StringBuilder expressionbuilder = new StringBuilder(""); 
		 		    		String factdatatype ="";
		 		    	 	Iterator<Entry<String, JsonNode>> grandchildrulenodefields = grandchildrulenode.fields();
		 		    		while (grandchildrulenodefields.hasNext()) {
		 		    			 Map.Entry<String, JsonNode> gcrnf = grandchildrulenodefields.next();
		 		    			  if(gcrnf.getKey().equals("fact_datatype"))
		 			 		      {
		 		    				 if(gcrnf.getValue().toString().equals("\"string\""))
			 			 		     {
		 		    					factdatatype ="string"; 
		 		    					System.out.println("data type is string");
			 			 		     }
		 		    				 else
		 		    				 {
		 		    					System.out.println("data type is not string");
		 		    				 }
		 			 		      }
		 			 		      if(gcrnf.getKey().equals("fact"))
		 			 		      {
		 			 		    	expressionbuilder.append(gcrnf.getValue().toString().replaceAll("\"", ""));
		 			 		    	//expressionbuilder.append(" ");
		 			 		      }
		 			 		      if(gcrnf.getKey().equals("operator"))
		 			 		      {
			 			 		    	switch(gcrnf.getValue().toString().replaceAll("\"", "")) {
			 			 		    	case "equal":
			 			 		    		if(factdatatype.equals("string"))
			 			 		    		{
			 			 		    			expressionbuilder.append(".equals(");
			 			 		    		}
			 			 		    		else
			 			 		    		{
			 			 		    			expressionbuilder.append("==");
			 			 		    		}
			 			 		    		
			 			 		        break;
			 			 		    	case "greaterThanInclusive":
			 			 		    		expressionbuilder.append(">=");
			 			 		        break;
			 			 		    	case "lessThanInclusive":
			 			 		    		expressionbuilder.append("<=");
			 			 		        break;
		 			 		    	}
		 			 		      }
		 			 		      if(gcrnf.getKey().equals("value"))
		 			 		      {
		 			 		    	expressionbuilder.append(factdatatype.equals("string")?(gcrnf.getValue().toString()+")"): gcrnf.getValue().toString().replaceAll("\"", ""));
		 			 		      }
		 		    		}
		 		    		System.out.println("expression derived at grandchild rule--"+ expressionbuilder.toString());
		 		    		grandchildrruleesult.add(evaluaterule(expressionbuilder.toString(),facts));
		 		    	 }
		 		    	StringBuilder sbchildexpr = new StringBuilder();
		 		    	String childconditionxexpr = childrulenode.getKey().equals("all")?"&&":"||";
		 		    	for(int i=0;i<grandchildrruleesult.size();i++)
		 		    	{
		 		    		if(i<grandchildrruleesult.size()-1)
		 		    		{
		 		    			sbchildexpr.append("boolArray["+i+"]"+ childconditionxexpr);
		 		    		}
		 		    		if(i == grandchildrruleesult.size()-1)
		 		    		{
		 		    			sbchildexpr.append("boolArray["+i+"]");
		 		    		}
		 		    			
		 		    	}
		 		    	System.out.println("evaulation came as child level--"+ sbchildexpr.toString());
		 		    	JexlContext jexlContext = new MapContext();
		 		        jexlContext.set("boolArray", grandchildrruleesult.toArray());
		 		        Boolean childresult = (Boolean)new JexlEngine()
		 			    		.createExpression(sbchildexpr.toString())
		 			    		.evaluate(jexlContext);
		 		        parentruleesult.add(childresult);
		 		        System.out.println("decision derived at child level--"+ childresult);
		 		    }    
		 		  }
	 		   
			 }
		}
		String parentconditionexpr = parentcondition.equals("all")?"&&":"||";
		StringBuilder sbparentexpr = new StringBuilder();
		for(int k=0;k<parentruleesult.size();k++)
	    {
			System.out.println("decision derived at parent rule level --"+  parentruleesult.get(k));
			if(k<parentruleesult.size()-1)
    		{
				sbparentexpr.append("boolArray["+k+"]"+ parentconditionexpr);
    		}
    		if(k == parentruleesult.size()-1)
    		{
    			sbparentexpr.append("boolArray["+k+"]");
    		}
	    }
		System.out.println("evaulation came as parent level--"+ sbparentexpr.toString());
		JexlContext jexlContext = new MapContext();
        jexlContext.set("boolArray", parentruleesult.toArray());
        JexlEngine jexl = new JexlEngine();
        Expression expression = jexl.createExpression(sbparentexpr.toString());
        Boolean finalresult = (Boolean)expression.evaluate(jexlContext);
        System.out.println("final result--"+ finalresult);
        return finalresult;
		
	}
	Boolean evaluaterule(String expressionstring,JexlContext facts)
	{
	    System.out.println("decision derived--"+ new JexlEngine()
	    		.createExpression(expressionstring)
	    		.evaluate(facts));
	    return (Boolean)new JexlEngine()
	    		.createExpression(expressionstring)
	    		.evaluate(facts);
	}
}

