package utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine;

import java.io.InputStream;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.discovery.v1.model.DeleteDocumentOptions;
import com.ibm.watson.discovery.v1.model.DeleteDocumentResponse;
import com.ibm.watson.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.discovery.v1.model.UpdateDocumentOptions;

/**
 * Contain methods for uploading/removing files from IBM Watson Discovery
 * 
 * @author Reynold
 */

public class FilesEngine {

  private WatsonDiscovery wdisc;
  private String environmentId;
  private String uploadedFilesCollectionId;

  public FilesEngine(WatsonDiscovery wdisc) {
    this.wdisc = wdisc;
    this.environmentId = this.wdisc.getEnvironmentId();
    this.uploadedFilesCollectionId = this.wdisc.getUploadedFilesCollectionId();
  }


  /**
   * Used to upload a file to IBM Watson Discovery
   * 
   * @param documentStream - input stream of the file to be uploaded
   * @param filename - name of file
   * @return string response of the file upload denoting success or failure
   */
  public String uploadFiles(InputStream documentStream, String filename) {
    Discovery discovery = wdisc.getDiscovery();
    AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder(
        environmentId, uploadedFilesCollectionId);
    builder.file(documentStream);
    builder.filename(filename);
    DocumentAccepted response =
        discovery.addDocument(builder.build()).execute().getResult();
    return response.getDocumentId();
  }

  /**
   * Used to update a file on IBM Watson Discovery
   * 
   * @param updatedDocumentStream - input stream of the file to be updated
   * @param filename - name of file
   * @param documentId - ID of the document on IBM Watson Discovery
   * @return string response of the file update denoting success or failure
   */
  public String updateFiles(InputStream updatedDocumentStream, String filename,
      String documentId) {
    Discovery discovery = wdisc.getDiscovery();

    UpdateDocumentOptions.Builder updateBuilder =
        new UpdateDocumentOptions.Builder(environmentId,
            uploadedFilesCollectionId, documentId);
    updateBuilder.file(updatedDocumentStream);
    updateBuilder.filename(filename);
    DocumentAccepted updateResponse =
        discovery.updateDocument(updateBuilder.build()).execute().getResult();
    return updateResponse.getDocumentId();
  }

  /**
   * Used to remove a file on IBM Watson Discovery
   * 
   * @param documentId - ID of the document on IBM Watson Discovery
   * @return string response of the file removal denoting success or failure
   */
  public String removeFiles(String documentId) {
    Discovery discovery = wdisc.getDiscovery();
    DeleteDocumentOptions deleteRequest =
        new DeleteDocumentOptions.Builder(environmentId,
            uploadedFilesCollectionId, documentId).build();
    DeleteDocumentResponse deleteResponse =
        discovery.deleteDocument(deleteRequest).execute().getResult();
    return deleteResponse.getStatus();
  }
  
}
