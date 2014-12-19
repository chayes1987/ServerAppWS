/*
    @author Conor Hayes
 */
package com.ftp.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IFTPService{
    @WebMethod boolean login(@WebParam(name = "username") String username);
    @WebMethod boolean upload(@WebParam(name = "username") String username,
                              @WebParam(name = "filename") String filename,
                              @WebParam(name = "file") byte[] file);
    @WebMethod
    Object[] getListFiles(@WebParam(name = "username") String username);
    @WebMethod byte[] download(@WebParam(name = "username") String username,
                               @WebParam(name = "filename") String filename);
    @WebMethod boolean logout(@WebParam(name = "username") String username);
}

