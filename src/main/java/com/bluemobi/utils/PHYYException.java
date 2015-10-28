package com.bluemobi.utils;
public class PHYYException extends Exception
{
    String errorMessage;
 
    public PHYYException(String errorMessage)
    {
         this.errorMessage = errorMessage;
    }
 
    public String toString()
    {
         return errorMessage;
    }
 
    public String getMessage()
    {
         return errorMessage;
    }
}