/*
    @author Conor Hayes
 */

package com.ftp.service;

import javax.jws.WebService;
import javax.swing.*;
import javax.xml.ws.Endpoint;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/*
    The FTPService class
 */
@WebService(endpointInterface = "com.ftp.service.IFTPService")
public class FTPService implements IFTPService {
    private ServiceGUI gui;
    Endpoint endpoint;

    /*
        No-arg Constructor
     */
    public FTPService(){}

    /*
        Two-arg Constructor
        @param gui The service GUI
        @param server The server of the Web Service
     */
    public FTPService(ServiceGUI gui, String server){
        this.gui = gui;

        try{
            endpoint = Endpoint.publish("http://" + server + ":8080/ftpservice", this);
            gui.server_output.setText("FTP Server is running...");
            gui.start_server.setText("Stop Server");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + server);
        }
    }

    /*
        Login Method
        Handles login attempts to the Server
        @param username The username of the user requesting access
        @returns Successful or failed login
     */
    @Override
    public boolean login(String username){
        File userDirectory = new File("C:\\" + username);
        gui.server_output.append("\nChecking if Directory '" + username + "' exists...");
        if (!userDirectory.exists()) {
            gui.server_output.append("\nDirectory '" + username + "' not found...");
            gui.server_output.append("\nCreating Directory '" + username + "'...");
            try{
                userDirectory.mkdir();
                gui.server_output.append("\nDirectory '" + username + "' created...");
            } catch(SecurityException se){
                gui.server_output.append("\nError creating directory..." + se.toString());
                return false;
            }
        }else{
            gui.server_output.append("\nDirectory '" + username + "' already exists...");
        }
        gui.server_output.append("\nServer Ready to serve " + username + "...");
        return true;
    }

    /*
        Upload Method
        Handles upload attempts to the Server
        @param username The username of the user requesting to upload
        @param filename The name of the file to upload
        @param file The file that they wish to upload
        @returns Successful of failed upload
     */
    @Override
    public boolean upload(String username, String filename, byte[] file){
        String path = "C:\\" + username + "\\" + filename;
        try {
            Files.write(Paths.get(path), file);
            gui.server_output.append("\nFile '" + filename + "' created at " + path.replace(filename, "") + "...");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
        GetListFiles Method
        Gets all the files of the user
        @param username The username of the user requesting their files
        @return The users' files
     */
    @Override
    public Object[] getListFiles(String username) {
        ArrayList<String> list = new ArrayList<String>();
        File directory = new File("C:\\" + username);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            list.add(file.getName());
        }
        return list.toArray();
    }

    /*
        Download Method
        Handles download attempts from the Server
        @param username The username of the user requesting to download
        @param filename The name of the file that they wish to download
        @return The requested file
     */
    @Override
    public byte[] download(String username, String filename) {
        String path =  "C:\\" + username + "\\" + filename;
        if (!new File(path).exists()) {
            gui.server_output.append("\nServer could not locate the file '" + filename + "'...");
            return null;
        }
        byte[] file = null;
        try {
            file = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        gui.server_output.append("\nRequested file '" + filename + "' sent to client...");
        return file;
    }

    /*
        Logout Method
        Handles Logout attempts from the Server
        @param The username of the user requesting to logout
        @returns True as no action is taken on server side
     */
    @Override
    public boolean logout(String username) {
        gui.server_output.append("\n" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) +
            " - Request to Logout received from " + username + "...\nLog out successful...");
        return true;
    }
}
