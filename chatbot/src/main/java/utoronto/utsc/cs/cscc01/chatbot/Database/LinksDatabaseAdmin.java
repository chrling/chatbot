package utoronto.utsc.cs.cscc01.chatbot.Database;

import utoronto.utsc.cs.cscc01.chatbot.EnscapsulatedObjects.CrawledLink;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class that will handle the crawled links data in the database
 *
 * @author Reynold
 */
public class LinksDatabaseAdmin extends GeneralDatabaseAdmin {

    /**
     * Insert the given information to the database, filename and the content of files
     *
     * @param seed
     * @param content
     */
    public void insert(String seed, HashMap<String, HashMap<String, String>> content) throws IOException, SQLException {

        PreparedStatement stmt;
        // SQL code for insert
        String insertSQL = "INSERT INTO LINKS(SEED, HASHMAP, DATE) VALUES(?, ?, ?)";

        String existLink = getLink(seed);

        if (existLink.equals("")) {

            connect();
            // Create SQL statement for inserting
            stmt = this.connection.prepareStatement(insertSQL);
            stmt.setString(1, seed);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(b);
            output.writeObject(content);
            byte[] bytes = b.toByteArray();
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            stmt.setBinaryStream(2, input, bytes.length);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            String formattedDate = formatter.format(date);
            stmt.setString(3, formattedDate);
            stmt.executeUpdate();
            close();

        } else {
            remove(seed);
            insert(seed, content);
        }

    }

    /**
     * Remove the given information of the given link from the database
     *
     * @param seed
     */
    public void remove(String seed) throws SQLException {
        PreparedStatement stmt;
        String documentId = getLink(seed);
        // SQL code for delete
        String deleteSQL = "DELETE FROM LINKS WHERE seed = ?";

        connect();
        // Create SQL statement for deleting
        stmt = this.connection.prepareStatement(deleteSQL);
        stmt.setString(1, seed);
        stmt.executeUpdate();

        close();
    }

    /**
     * Read file using the given filename
     *
     * @param seed
     */

    public HashMap<String, HashMap<String, String>> extractLinkCollection(String seed) throws SQLException, IOException, ClassNotFoundException {
        // update sql

        HashMap<String, HashMap<String, String>> linksCollection = new HashMap<>();
        // Connection conn = null;

        if (!getLink(seed).equals("")) {
            connect();
            // write binary stream into file
            PreparedStatement stmt = this.connection.prepareStatement("SELECT hashmap FROM LINKS WHERE seed=?");
            stmt.setString(1, seed);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                InputStream input = rs.getBinaryStream(1);
                ObjectInputStream ois = new ObjectInputStream(input);
                linksCollection = (HashMap<String, HashMap<String, String>>) ois.readObject();
            }

        }
        return linksCollection;
    }


    public String getLink(String link) throws SQLException {
        String selectSQL = "SELECT * FROM LINKS WHERE seed=?";
        ResultSet rs;
        PreparedStatement stmt;
        String seed = "";
        this.connect();
        stmt = this.connection.prepareStatement(selectSQL);
        stmt.setString(1, link);
        rs = stmt.executeQuery();

        while (rs.next()) {
            seed = rs.getString("SEED");
        }
        this.close();

        return seed;
    }

    public List<CrawledLink> list() throws SQLException {
        List<CrawledLink> listCrawledLink = new ArrayList<>();

        String sql = "SELECT * FROM links ORDER BY seed";
        this.connect();

        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            int id = result.getInt("linkid");
            String seed = result.getString("seed");
            String date = result.getString("date");
            CrawledLink link = new CrawledLink(id, seed, date);

            listCrawledLink.add(link);
        }

        this.close();

        return listCrawledLink;

    }
}
