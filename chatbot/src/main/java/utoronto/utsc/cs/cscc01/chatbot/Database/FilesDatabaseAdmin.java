package utoronto.utsc.cs.cscc01.chatbot.Database;

import org.apache.commons.io.FilenameUtils;
import utoronto.utsc.cs.cscc01.chatbot.LuceneEngine.FileParser;
import utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine.FilesEngine;
import utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects.UploadedFile;
import utoronto.utsc.cs.cscc01.chatbot.IBMWatsonEngine.WatsonDiscovery;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Admin page for handling uploading/removing files from the database
 *
 * @author Reynold
 */
public class FilesDatabaseAdmin extends GeneralDatabaseAdmin {

    private FilesEngine fileEngine;
    private FileParser fileParser;

    public FilesDatabaseAdmin() {
       
        WatsonDiscovery w =  WatsonDiscovery.buildDiscovery();
        this.fileEngine = new FilesEngine(w);
        this.fileParser = new FileParser();

    }


    /**
     * Insert the given information to the database, filename and the content of files
     *
     * @param filename
     * @param content
     */
    public void insert(String filename, InputStream content, InputStream contentForDb, long size) {

        PreparedStatement stmt;
        // SQL code for insert
        String insertSQL = "INSERT INTO FILES VALUES(?, ?, ?, ?)";

        String existDocumentId = getDocumentId(filename);

        if (existDocumentId.equals("noFile")) {
            String documentId = "";
            if (! FilenameUtils.getExtension(filename).equals("txt")) {
                documentId = this.fileEngine.uploadFiles(content, filename);
            }

            try {
                connect();
                // Create SQL statement for inserting
                stmt = this.connection.prepareStatement(insertSQL);
                stmt.setString(1, documentId);
                stmt.setString(2, filename);
                stmt.setBinaryStream(3, contentForDb, (int) size);
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                String formattedDate = formatter.format(date);
                stmt.setString(4, formattedDate);
                stmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("error");
            }

            finally{

                close();

            }

        } else {
            update(filename, existDocumentId, content, contentForDb, size);
        }
    }


    public void update(String filename, String existDocumentId, InputStream content, InputStream contentForDb,long size) {
        PreparedStatement stmt;
        String updateSQL = "UPDATE FILES SET DOCUMENTID = ?, FILE = ?, DATE = ? WHERE FILENAME = ?";
        String documentId = "";
        if (! FilenameUtils.getExtension(filename).equals("txt")) {
            documentId = this.fileEngine.updateFiles(content, filename, existDocumentId);
        }

        try {
            connect();
            // Create SQL statement for inserting
            stmt = this.connection.prepareStatement(updateSQL);
            stmt.setString(1, documentId);
            stmt.setBinaryStream(2, contentForDb, (int) size);
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            String formattedDate = formatter.format(date);
            stmt.setString(3, formattedDate);
            stmt.setString(4, filename);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Can't update file");
        }

        finally{

            close();
        }
    }

    /**
     * Remove the given information of the given filename from the database
     *
     * @param filename
     */
    public boolean remove(String filename) {
        PreparedStatement stmt;
        String documentId = getDocumentId(filename);
        // SQL code for delete
        String deleteSQL = "DELETE FROM FILES WHERE filename = ?";

        String result;
        if (! FilenameUtils.getExtension(filename).equals("txt")) {
            result = this.fileEngine.removeFiles(documentId);
        } else {
            result = "deleted";
        }

        if (result.equals("deleted")) {
            try {
                connect();
                // Create SQL statement for deleting
                stmt = this.connection.prepareStatement(deleteSQL);
                stmt.setString(1, filename);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            finally {

                close();
            }
        }
        return false;
    }

    /**
     * Read file using the given filename
     *
     * @param filename
     */

    public String extractFile(String filename) throws SQLException, IOException {
        // update sql

        FileOutputStream fos;
        // Connection conn = null;
        String content = "";

        if (!getDocumentId(filename).equals("noFile")) {

            connect();
            // write binary stream into file
            PreparedStatement stmt = this.connection.prepareStatement("SELECT file FROM FILES WHERE filename=?");
            stmt.setString(1, filename);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                InputStream input = rs.getBinaryStream(1);
                content = fileParser.parse(filename, input);
            }

        } else {
            System.out.println("File uploaded");
        }

        return content;
    }


    public String getDocumentId(String filename) {
        String selectSQL = "SELECT * FROM FILES WHERE filename=?";
        ResultSet rs;
        PreparedStatement stmt;
        String documentId = "noFile";

        try {
            connect();
            stmt = this.connection.prepareStatement(selectSQL);
            stmt.setString(1, filename);
            rs = stmt.executeQuery();

            while (rs.next()) {
                documentId = rs.getString("DOCUMENTID");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally{

            close();
        }

        return documentId;
    }

    public List<UploadedFile> list() throws SQLException {
        List<UploadedFile> listUploadedFile = new ArrayList<>();
        try{


            String sql = "SELECT * FROM files ORDER BY filename";
            connect();

            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                int id = result.getInt("documentId");
                String filename = result.getString("filename");
                String date = result.getString("date");
                UploadedFile file = new UploadedFile(id, filename, date);

                listUploadedFile.add(file);
            }


        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        finally{

            close();
        }
        return listUploadedFile;
       
    }

}
