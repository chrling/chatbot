package utoronto.utsc.cs.cscc01.chatbot;

import static org.junit.Assert.assertSame;
import org.junit.Test;
import utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine.WatsonTextToSpeech;

public class WatsonTextToSpeechTest {

  @Test
  public void testWatsonTextToSpeechSingleton() {
    WatsonTextToSpeech tts1 = WatsonTextToSpeech.buildWatsonTTS();
    WatsonTextToSpeech tts2 = WatsonTextToSpeech.buildWatsonTTS();
    assertSame(tts1, tts2);
  }
}
