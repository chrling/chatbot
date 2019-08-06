package utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DialogRuntimeResponseGeneric;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageOutput;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;

/**
 * Contains the methods used to query IBM Assistant
 * @author Chris
 *
 */
public class QueryAssistant implements SearchAssistant {
	private WatsonAssistant wa;
	
	public QueryAssistant(WatsonAssistant assistant) {
		this.wa = assistant;
	}
	/**
	 * Main method used to query IBM Watson Assistant and returns a hashtable
	 * with the string key representing the type of file being returned and the
	 * list string values representing the content of the file
	 * 
	 * @param q - string containing the user query
	 * @return hashtable with the format specified by queryServlet
	 */
	public Hashtable<String, ArrayList<String>> simpleAssistantQuery(String q) {
	    Assistant service = wa.getAssistant();
	    
	    Hashtable<String, ArrayList<String>> dict = new Hashtable<>();
	    
	    ArrayList<String> text = new ArrayList<>();
	    ArrayList<String> image = new ArrayList<>();
	    ArrayList<String> url = new ArrayList<>();
	    ArrayList<String> queryFlag = new ArrayList<>();
	    
	    dict.put("text", text);
	    dict.put("image", image);
	    dict.put("url", url);
	    dict.put("queryFlag", queryFlag);
	    // TODO: we may have to move this session to query servlet itself
	    // so we aren't creating new session everytime
	    CreateSessionOptions options = new CreateSessionOptions.Builder(wa.getAssistantId()).build();
	    SessionResponse response = service.createSession(options).execute().getResult();
	    String sessionId = response.getSessionId();
	    
	    // build the message for ibm watson assisntant using user input q
	    MessageInput input = new MessageInput.Builder()
	        .messageType("text")
	        .text(q)
	        .build();

	    MessageOptions msgOptions = new MessageOptions.Builder(wa.getAssistantId(), sessionId)
	      .input(input)
	      .build();
	    
	    // grab response from watson assistant
	    MessageResponse msgResponse = service.message(msgOptions).execute().getResult();

	    //System.out.println(msgResponse);
	    
	    // unpack the output from assistant's response
	    MessageOutput output = msgResponse.getOutput();
	    List<DialogRuntimeResponseGeneric> outputList = output.getGeneric();
	    
	    // we only care about text and image response right now
	    // and we will create json string for that result
	    // if assistant pattern match fails, it will return "Need to query"
	    for (DialogRuntimeResponseGeneric generic : outputList) {
	      if (generic.getResponseType().equals("text")){
	        if (generic.getText().equals("Need to query")) {
	          // special case - assistant cannot find answer
	          queryFlag.add("Need to query");
	          return dict;
	        }
	        // general case, add text to our hashtable
	        else
	          text.add(generic.getText());
	      }
	      // image case
	      else if (generic.getResponseType().equals("image")) {
	        image.add(generic.getSource());
	      }
	    }
	    return dict;
	}
}
