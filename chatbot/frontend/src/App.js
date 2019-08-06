import React, { Component } from "react";
import "./App.css";
import { BrowserRouter as Router, Route } from "react-router-dom";
import Home from "./components/Home/Home";
import AdminDocuments from "./components/Admin/AdminDocuments/AdminDocuments";
import AdminFeedback from "./components/Admin/AdminFeedback/AdminFeedback";
import AdminCrawler from './components/Admin/AdminCrawler/AdminCrawler';
import AdminReset from './components/Admin/AdminReset/AdminReset';
import axios from "axios";
import qs from "qs";

/**
 * Main controller for application, holds state and functions for admin dashboard
 * and home page
 */
class App extends Component {
  /**
   * State
   * @param {bool} loggedIn - represents whether the user has successfuly logged i,
   * @param {bool} showModal - if True the login modal will be visible to the user otherwise it will be hidden
   * @param {list} files - A list holding all of the files crawled by the crawler
   *
   */
  state = {
    loggedIn: false,
    showModal: false,
    files: [],
    queries: []
  };

  /**
   * Function which changes the showModal portion of state to either True or False
   * which will result in the login modal being visible or hidden
   */
  modalClickHandler = () => {
    const showModal = !this.state.showModal;

    this.setState({
      ...this.state,
      showModal: showModal
    });
  };

  /**
   * Function which changes the loggedIn portion of state to either True or False
   * which will result in the user being logged out
   */
  logOutHandler = () => {
    const loggedOut = false;
    this.setState({
      ...this.state,
      loggedIn: loggedOut
    });
  };

  /**
   * Creates a POST request which is sent to the backend and if autehtnicated will
   * alter change the loggedIn part of state to True
   * @event
   */
  loginHandler = async event => {
    event.preventDefault();
    // Get variables from event
    const target = event.target;
    const username1 = target.elements.username.value;
    const password1 = target.elements.password.value;

    // Create JSON object
    const data = {
      username: username1,
      password: password1
    };

    // Make Post request but data must be altered with qs.stringify
    await axios
      .post("/login", qs.stringify(data))
      .then(response => {
        // If request is successful then set loggedIn to what response['data']['authenticated']
        // returns
        const status = response["data"]["authenticated"];
        this.setState({
          ...this.state,
          showModal: false,
          loggedIn: status
        });
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  /**
   * Makes a request to the backend through a post request which
   * adds a file to Files.db
   * @event
   */
  addFileHandler = async event => {
    // Get the data from event and add it to a form
    event.preventDefault();
    const target = event.target;
    const file2 = target.file.files[0];
    let data = new FormData();
    data.append("action", "upload");
    data.append("file", file2);

    await axios
      .post("/handlefiles", data)
      .then(response => {
        // viewAllFilesHandler needs to be called to update the file list being displayed
        this.viewAllFilesHandler();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  /**
   * Makes a request to the backend through a post request which
   * removes a file in Files.db specified in the event object
   * @event
   */
  removeFileHandler = async fileName => {
    let data = new FormData();
    data.append("action", "remove");
    data.append("file", fileName);
    await axios
      .post("/handlefiles", data)
      .then(response => {
        this.viewAllFilesHandler();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  /**
   * Retrieves every file in Files.db and updates state (files) to reflect this.
   */
  viewAllFilesHandler = () => {
    axios
      .get("/handlefiles")
      .then(response => {
        // If the get request is successful state (files) is updated
      
        const data = response["data"];
        let files = [];
        for (var key in data){
          files.push(data[key])
        }
        this.setState({
          files: files
        });
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  viewGraphHandler = () => {
    axios
      .get("/getdata")
      .then(response => {
        // If the get request is successful state (files) is updated
        const data = response["data"]["queries"];
        this.setState({
          queries: data
        });
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  viewAllFeedbackHandler = () => {};

  render() {
    return (
      <div className="App">
        <Router>
          <Route
            exact
            path="/"
            render={props => (
              <Home
                {...props}
                loggedIn={this.state.loggedIn}
                showModal={this.state.showModal}
                modalClickHandler={this.modalClickHandler}
                loginHandler={this.loginHandler}
                logOutHandler={this.logOutHandler}
              />
            )}
          />

          <Route
            exact
            path="/admin"
            render={props => (
              <AdminDocuments
                {...props}
                files={this.state.files}
                queries={this.state.queries}
                removeFileHandler={this.removeFileHandler}
                viewAllFilesHandler={this.viewAllFilesHandler}
                addFileHandler={this.addFileHandler}
                logOutHandler={this.logOutHandler}
                loggedIn={this.state.loggedIn}
                viewGraphHandler={this.viewGraphHandler}
              />
            )}
          />

          <Route
            exact
            path="/admin/feedback"
            render={props => (
              <AdminFeedback
                {...props}
                removeFileHandler={this.removeFileHandler}
                viewAllFilesHandler={this.viewAllFilesHandler}
                addFileHandler={this.addFileHandler}
                loggedIn={this.state.loggedIn}
                logOutHandler={this.logOutHandler}
              />
            )}
          />
           <Route
            exact
            path="/admin/crawler"
            render={props => (
              <AdminCrawler
                {...props}
                addFileHandler={this.addFileHandler}
                loggedIn={this.state.loggedIn}
                logOutHandler={this.logOutHandler}
              />
            )}
          />
           <Route
            exact
            path="/admin/reset"
            render={props => (
              <AdminReset
                {...props}
                loggedIn={this.state.loggedIn}
                logOutHandler={this.logOutHandler}
              />
            )}
          />

         
        </Router>
      </div>
    );
  }
}

export default App;
