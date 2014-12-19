/*
    @author Conor Hayes
 */
package com.ftp.service;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServiceGUI extends JFrame implements ActionListener {
    private JTextField serverName;
    JTextArea server_output;
    JButton start_server;
    FTPService service;

    /*
        No-arg Constructor
     */
    public ServiceGUI(){
        buildAppWindow();
        buildServerGUI();
    }

    /*
        BuildServerGUI Method
        Builds the window for the Server
     */
    private void buildServerGUI() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(250, 150));
        p.setLayout(new GridLayout(3, 1));
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Server Configuration"));
        JLabel l = new JLabel("Server");
        p.add(l);
        serverName = new JTextField("localhost");
        p.add(serverName);
        start_server = new JButton("Start Server");
        start_server.addActionListener(this);
        p.add(start_server);
        add(p);
        p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Server Output"));
        p.setLayout(new GridLayout(1, 1));
        p.setPreferredSize(new Dimension(450, 150));
        server_output = new JTextArea();
        DefaultCaret caret = (DefaultCaret)server_output.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        server_output.setLineWrap(true);
        server_output.setEditable(false);
        p.add(new JScrollPane (server_output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        add(p);
    }

    /*
        BuildAppWindow Method
        Creates the interface for the Server
     */
    private void buildAppWindow() {
        setSize(750, 200);
        setResizable(false);
        setLayout(new FlowLayout());
        Calendar sysDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        setTitle("Server App" + " - " + sdf.format(sysDate.getTime()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /*
        ActionPerformed Method
        Handles GUI events
        @param e The event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start Server")){
            if (serverName.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter the Port Number.");
            }else{
                // Show wait cursor when starting the service
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                service = new FTPService(this, serverName.getText());
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }else if (e.getActionCommand().equals("Stop Server")){
            // Kill the endpoint
            service.endpoint.stop();
            start_server.setText("Start Server");
            server_output.setText("");
        }
    }
}
