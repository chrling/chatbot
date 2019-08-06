package utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine;

import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;

/**
 * Singleton class wrapping the Watson TextToSpeech object
 * 
 * @author Chris
 *
 */
public class WatsonTextToSpeech {
  private static WatsonTextToSpeech ttsi = null;
  private TextToSpeech textToSpeech;

  private WatsonTextToSpeech() {
    IamOptions options = new IamOptions.Builder()
        .apiKey("o0xxQxZ21NgAvJLSBfQosUV8oodK6NVo0u0140l7juT-").build();
    textToSpeech = new TextToSpeech(options);
    textToSpeech.setEndPoint(
        "https://gateway-wdc.watsonplatform.net/text-to-speech/api");
  }

  /**
   * Singleton constructor
   * 
   * @return a WatsonTextToSpeech object wrapping the Watson TextToSpeech object
   */
  public static WatsonTextToSpeech buildWatsonTTS() {
    if (ttsi == null)
      ttsi = new WatsonTextToSpeech();
    return ttsi;
  }

  /**
   * Retrieve the Watson TextToSpeech object from within the singleton
   * 
   * @return
   */
  public TextToSpeech getTTS() {
    return this.textToSpeech;
  }
}
