import React, { useEffect } from "react";
import DocumentTable from "./DocumentsTable/DocumentsTable";
import Paper from "@material-ui/core/Paper";
import { makeStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import Box from "@material-ui/core/Box";
import Button from "@material-ui/core/Button";
import DragAndDrop from "./DragAndDrop/DragAndDrop";
import axios from "axios";

const useStyles = makeStyles(theme => ({
  root: {
    padding: theme.spacing(3, 2)
  },
  dragAndDrop: {
    width: "100%",
    height: "300px",
    backgroundColor: "rgba(0,0,0,0.1)",
    border: "1px solid #ccc",
    padding: theme.spacing(2),
    borderRadius: "5px"
  }
}));

/**
 * Compoenent representing all the documents the admin has uploaded
 * and has the abillity for admins to uplaod or remove documents
 * @param {props} props:
 *  @param {list} files - a list containing every file in Files.db
 *  @param {function} addFileHandler - function which adds a file to the database
 *  @param {function} removeFileHandler - function which removes a file from the database
 */
const AdminDocuments = props => {
  // when component is first loaded we should load all the files in the database
  useEffect(() => {
    props.viewAllFilesHandler();
  }, []);
  const classes = useStyles();

  const [values, setFiles] = React.useState({
    files: []
  });
  const handleDrop = files => {
    let fileList = values.files;
    console.log(fileList);
    for (var i = 0; i < files.length; i++) {
      if (!files[i].name) {
        return;
      } else {
        fileList.push(files[i]);
      }
    }
    setFiles({ files: fileList });
  };

  /**
   * Makes a request to the backend through a post request which
   * adds a file to Files.db
   * @event
   */
  const addAllFilesHandler = async listFiles => {
    for (let i = 0; i < listFiles.length; i++) {
      let res = addFileHandler(listFiles[i]);
    }
    // empty fiels lsit
    setFiles({ files: [] });
    props.viewAllFilesHandler();
  };

  /**
   * Makes a request to the backend through a post request which
   * adds a file to Files.db
   * @event
   */
  const addFileHandler = async file => {
    // Get the data from event and add it to a form

    let data = new FormData();
    data.append("action", "upload");
    data.append("file", file);

    let res = await axios
      .post("/handlefiles", data)
      .then(response => {
        // viewAllFilesHandler needs to be called to update the file list being displayed
        props.viewAllFilesHandler();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  return (
    <React.Fragment>
      <Box marginTop={3}>
        <Container maxWidth="md">
          <Paper className={classes.root}>
            <Typography variant="h4" component="h4" align="left">
              File Upload
            </Typography>
            <Typography variant="body1" component="p" align="left">
              Drag and drop or select a file to train the chatbot
            </Typography>
            <Box
              className={classes.dragAndDrop}
              color="secondary"
              marginTop={3}
              align="center"
            >
              <DragAndDrop handleDrop={handleDrop}>
                <div style={{ height: "250px", width: "800px" }}>
                  {values.files.map((file, i) => (
                    <div key={i}>
                      <li>{file.name}</li>
                    </div>
                  ))}
                </div>
              </DragAndDrop>
            </Box>
            <Button
              onClick={() => addAllFilesHandler(values.files)}
              type="submit"
              color="secondary"
              variant="contained"
              style={{
                display: "block",
                marginTop: "20px",
                position: "relative",
                marginLeft: "auto"
              }}
            >
              upload files
            </Button>
          </Paper>
        </Container>
      </Box>

      <Box marginTop={3} marginBottom={3}>
        <Container maxWidth="md">
          <Paper className={classes.root}>
            <Typography variant="h4" component="h4" align="left">
              What's In The Chatbot's Brain?
            </Typography>
            <Typography
              variant="body1"
              component="p"
              align="left"
              style={{ margin: "10px" }}
            >
              Take a look at what DFI's chatbot is thinking about
            </Typography>

            <DocumentTable
              removeFileHandler={props.removeFileHandler}
              files={props.files}
            />
          </Paper>
        </Container>
      </Box>
    </React.Fragment>
  );
};

export default AdminDocuments;
