package utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine;

import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.Collection;
import com.ibm.watson.discovery.v1.model.Configuration;
import com.ibm.watson.discovery.v1.model.CreateCollectionOptions;
import com.ibm.watson.discovery.v1.model.CreateConfigurationOptions;
import com.ibm.watson.discovery.v1.model.CreateCredentialsOptions;
import com.ibm.watson.discovery.v1.model.CredentialDetails;
import com.ibm.watson.discovery.v1.model.Credentials;
import com.ibm.watson.discovery.v1.model.CredentialsList;
import com.ibm.watson.discovery.v1.model.DeleteConfigurationOptions;
import com.ibm.watson.discovery.v1.model.DeleteConfigurationResponse;
import com.ibm.watson.discovery.v1.model.DeleteCredentialsOptions;
import com.ibm.watson.discovery.v1.model.GetCollectionOptions;
import com.ibm.watson.discovery.v1.model.GetConfigurationOptions;
import com.ibm.watson.discovery.v1.model.ListConfigurationsOptions;
import com.ibm.watson.discovery.v1.model.ListConfigurationsResponse;
import com.ibm.watson.discovery.v1.model.ListCredentialsOptions;
import com.ibm.watson.discovery.v1.model.Source;
import com.ibm.watson.discovery.v1.model.SourceSchedule;
import com.ibm.watson.discovery.v1.model.UpdateCollectionOptions;
import com.ibm.watson.discovery.v1.model.UpdateConfigurationOptions;
import com.ibm.watson.discovery.v1.model.UpdateCredentialsOptions;

/**
 * Contain methods used to change the URL to which Watson Discovery crawls. As it is still Beta version, it does not suppport
 * link adding. It only supports creation of collections. As our lite version only supports two collections, we
 * are not using this class for now, but it can be used in the future
 * It is not a lazy class, just for future usage
 * @author Reynold
 *
 */

public class CrawlerEngine {

  private WatsonDiscovery wdisc;
  private String environmentId;
  private String credentialType;
  private String sourceType;
  private String languageCode;
  private String collectionId;

  public CrawlerEngine(WatsonDiscovery wdisc) {
    this.wdisc = wdisc;
    this.environmentId = this.wdisc.getEnvironmentId();
    this.collectionId = this.wdisc.getCrawlerCollectionId();
    this.credentialType = "noauth";
    this.sourceType = "web_crawl";
    this.languageCode = "en";
  }

  public String createCredential(String url) {
    Discovery discovery = wdisc.getDiscovery();
    CredentialDetails credentialDetails = new CredentialDetails();
    credentialDetails.setCredentialType(this.credentialType);
    credentialDetails.setUrl(url);
    Credentials credentials = new Credentials();
    credentials.setSourceType(this.sourceType);
    credentials.setCredentialDetails(credentialDetails);
    CreateCredentialsOptions createOptions =
        new CreateCredentialsOptions.Builder().environmentId(this.environmentId)
            .credentials(credentials).build();
    Credentials response =
        discovery.createCredentials(createOptions).execute().getResult();
    return response.getCredentialId();
  }

  public void list() {
    Discovery discovery = wdisc.getDiscovery();
    ListCredentialsOptions listOptions = new ListCredentialsOptions.Builder()
        .environmentId(environmentId).build();

    CredentialsList response =
        discovery.listCredentials(listOptions).execute().getResult();
    System.out.println(response.getCredentials());
  }

  public String updateCredential(String url, String credentialId) {
    Discovery discovery = wdisc.getDiscovery();
    CredentialDetails updatedDetails = new CredentialDetails();
    updatedDetails.setCredentialType(credentialType);
    updatedDetails.setUrl(url);

    UpdateCredentialsOptions updateOptions =
        new UpdateCredentialsOptions.Builder().environmentId(environmentId)
            .credentialId(credentialId).sourceType(sourceType)
            .credentialDetails(updatedDetails).build();

    Credentials response =
        discovery.updateCredentials(updateOptions).execute().getResult();
    System.out.println(response.getCredentialDetails());
    return response.getStatus();
  }

  public void deleteCredential(String credentialId) {
    Discovery discovery = wdisc.getDiscovery();
    DeleteCredentialsOptions deleteOptions =
        new DeleteCredentialsOptions.Builder().environmentId(environmentId)
            .credentialId(credentialId).build();

    discovery.deleteCredentials(deleteOptions).execute();
  }

  public String createConfiguration(String configurationName,
      String credentialId) {
    Discovery discovery = wdisc.getDiscovery();

    CreateConfigurationOptions.Builder createBuilder =
        new CreateConfigurationOptions.Builder();
    Configuration configuration = new Configuration();
    configuration.setName(configurationName);
    createBuilder.configuration(configuration);
    createBuilder.environmentId(environmentId);
    Source source = new Source();
    source.setCredentialId(credentialId);
    source.setType(sourceType);
    source.setSchedule(getSourceSchedule());
    createBuilder.source(source);
    Configuration createResponse = discovery
        .createConfiguration(createBuilder.build()).execute().getResult();
    return createResponse.getConfigurationId();

  }

  public String updateConfiguration(String configurationId, String credentialId,
      String name) {
    Discovery discovery = wdisc.getDiscovery();
    Configuration updatedConfiguration = new Configuration();
    UpdateConfigurationOptions.Builder updateBuilder =
        new UpdateConfigurationOptions.Builder().environmentId(environmentId)
            .configurationId(configurationId).name(name);
    updatedConfiguration.setName(name);
    Source source = new Source();
    source.setCredentialId(credentialId);
    source.setType(sourceType);
    source.setSchedule(getSourceSchedule());
    updateBuilder.source(source);
    updateBuilder.configuration(updatedConfiguration);
    Configuration updateResponse = discovery
        .updateConfiguration(updateBuilder.build()).execute().getResult();
    return updateResponse.getConfigurationId();
  }

  public SourceSchedule getSourceSchedule() {
    SourceSchedule ss = new SourceSchedule();
    ss.setEnabled(true);
    ss.setTimeZone("America/New_York");
    ss.setFrequency("weekly");
    return ss;

  }

  public void listConfiguration() {
    Discovery discovery = wdisc.getDiscovery();
    ListConfigurationsOptions listOptions =
        new ListConfigurationsOptions.Builder(environmentId).build();
    ListConfigurationsResponse listResponse =
        discovery.listConfigurations(listOptions).execute().getResult();
    System.out.println(listResponse.getConfigurations());
  }

  public String getCredentialId() {
    Discovery discovery = wdisc.getDiscovery();
    GetConfigurationOptions getOptions =
        new GetConfigurationOptions.Builder(environmentId, getConfigurationId())
            .build();
    Configuration getResponse =
        discovery.getConfiguration(getOptions).execute().getResult();
    return getResponse.getSource().getCredentialId();
  }

  public String createCrawlerCollection(String configurationId,
      String collectionName) {

    Discovery discovery = wdisc.getDiscovery();

    CreateCollectionOptions.Builder createCollectionBuilder =
        new CreateCollectionOptions.Builder().environmentId(environmentId)
            .configurationId(configurationId).name(collectionName)
            .language(languageCode);
    Collection createResponse =
        discovery.createCollection(createCollectionBuilder.build()).execute()
            .getResult();
    return createResponse.getCollectionId();
  }

  public String getConfigurationId() {
    Discovery discovery = wdisc.getDiscovery();
    GetCollectionOptions getOptions =
        new GetCollectionOptions.Builder(environmentId, collectionId).build();
    Collection getResponse =
        discovery.getCollection(getOptions).execute().getResult();
    return getResponse.getConfigurationId();
  }

  public void deleteConfiguration(String configurationId) {
    Discovery discovery = wdisc.getDiscovery();
    DeleteConfigurationOptions deleteRequest =
        new DeleteConfigurationOptions.Builder(environmentId, configurationId)
            .build();
    DeleteConfigurationResponse deleteResponse =
        discovery.deleteConfiguration(deleteRequest).execute().getResult();
  }

  public String updateCollection(String configurationId) {

    Discovery discovery = wdisc.getDiscovery();

    String updateCollectionName = "newCrawler";

    UpdateCollectionOptions updateOptions =
        new UpdateCollectionOptions.Builder(environmentId, collectionId)
            .name(updateCollectionName).configurationId(configurationId)
            .build();

    Collection updatedCollection =
        discovery.updateCollection(updateOptions).execute().getResult();
    return updatedCollection.getCollectionId();

  }

}
