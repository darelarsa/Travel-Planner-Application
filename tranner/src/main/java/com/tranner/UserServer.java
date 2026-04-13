package com.tranner;

import java.io.*;
import java.net.*;
import java.util.*;

public class UserServer
{
    private ArrayList<User> users;
    private final int PORT_NUMBER = 8189;

    public UserServer()
    {
        // Create initial users here
    }

    public void startServer()
    {
        try
        {
            ServerSocket s = new ServerSocket(PORT_NUMBER);

            while(true)
            {
                Socket incoming = s.accept();

                try
                {
                    InputStream inStream = incoming.getInputStream();
                    OutputStream outStream = incoming.getOutputStream();

                    Scanner in = new Scanner(inStream);
                    PrintWriter out = new PrintWriter(outStream, true);

                    if (in.hasNextLine()) {
                        String lineIn = in.nextLine().trim();
                        String inputId = lineIn.substring(lineIn.indexOf("id:") + 3, lineIn.indexOf("---pwd:"));
                        String inputPwd = lineIn.substring(lineIn.indexOf("---pwd:") + 7);

                        boolean found = false;
                        String targetUser = "";

                        for (int i = 0; i < users.size(); i++) {
                            User curr = users.get(i);
                            if (inputId.equals(curr.getId()) && inputPwd.equals(curr.getPassword())) {
                                found = true;
                                targetUser = curr.getUsername();
                                break;
                            }
                        }

                        if (found) {
                            out.println(targetUser);
                        }
                    }

                }
                catch (Exception exc1)
                {
                    exc1.printStackTrace();
                }
                finally
                {
                    if (s != null) {
                        try
                        {
                            incoming.close();
                        }
                        catch (Exception exc2)
                        {
                            exc2.printStackTrace();
                        }
                    }
                }
            }
        }
        catch (Exception exc2)
        {
            exc2.printStackTrace();
        }
    }

    public static void main(String [] args)
    {
        UserServer aServer = new UserServer();
        aServer.startServer();
    }
}
