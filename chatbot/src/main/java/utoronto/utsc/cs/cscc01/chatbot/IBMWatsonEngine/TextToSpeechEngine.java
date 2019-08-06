package utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

/**
 * This class is used to convert a string to a byte string encoded in mp3
 * @author Chris
 */
public class TextToSpeechEngine {
  private WatsonTextToSpeech watsonTTS;

  public TextToSpeechEngine(WatsonTextToSpeech tts) {
    this.watsonTTS = tts;
  }
  
  /**
   * Main method used to convert text into audio byte string
   * @param text - the string to be converted to audio
   * @return byte array containing the audio in mp3 format
   * @throws IOException
   */
  public byte[] createAudioFromText(String text) throws IOException {
    SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
        .text(text).accept("audio/mp3").voice("en-US_MichaelVoice").build();

    InputStream inputStream =
        watsonTTS.getTTS().synthesize(synthesizeOptions).execute().getResult();
    InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

    // OutputStream out = new FileOutputStream("../chatbot/hello_world.mp3");
    // 4MB of ram for an audio, turns out audio file are pretty big
    byte[] buffer = new byte[4194304];
    in.read(buffer);
    /*
     * int length; length = in.read(buffer); out.write(buffer, 0, length);
     * 
     * out.close();
     */
    in.close();
    inputStream.close();
    return buffer;
  }

  /**
   * Alternative conversion to base64 encoding
   * @param audioArray - the output from createAudioFromText
   * @return a byte string in base64 encoding
   */
  public String toBase64(byte[] audioArray) {
    String base64String = Base64.encodeBase64String(audioArray);
    return base64String;
  }

}
