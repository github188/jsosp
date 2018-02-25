package com.jieshun.ops.comm;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;


public class PIMSException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 9021625936473809207L;

    private Throwable nested = null;
    
   
    private int code=-1;

   
    public PIMSException()
    {
        super();
    }

    
    public PIMSException(int code,String msg)
    {
    	super(msg);
    	this.code=code;
    }

    
    public PIMSException(int code,Throwable nested)
    {
        super();
        this.code=code;
        this.nested = nested;
    }

    
    public PIMSException(int code,String msg, Throwable nested)
    {
        super(msg);
        this.code=code;
        this.nested = nested;
    }

    @Override
    public String getMessage()
    {
        StringBuffer msg = new StringBuffer();
        String ourMsg = super.getMessage();
        if (ourMsg != null)
        {
            msg.append(ourMsg);
        }
        if (nested != null)
        {
            String nestedMsg = nested.getMessage();
            if (nestedMsg != null)
            {
                if (ourMsg != null)
                {
                    msg.append(": ");
                }
                msg.append(nestedMsg);
            }

        }
        return (msg.length() > 0 ? msg.toString() : null);
    }

    public int getCode(){
    	return this.code;
    }
    @Override
    public void printStackTrace()
    {
        synchronized(System.err)
        {
            printStackTrace(System.err);
        }
    }
    @Override
    public void printStackTrace(PrintStream out)
    {
        synchronized(out)
        {
            PrintWriter pw = new PrintWriter(out, false);
            printStackTrace(pw);
            // Flush the PrintWriter before it's GC'ed.
            pw.flush();
        }
    }

    @Override
    public void printStackTrace(PrintWriter out)
    {
        synchronized(out)
        {
            printStackTrace(out, 0);
        }
    }

   
    public void printStackTrace(PrintWriter out, int skip)
    {
        String[] st = captureStackTrace();
        if(nested != null)
        {
            if(nested instanceof PIMSException)
            {
                ((PIMSException)nested).printStackTrace(out, st.length - 2);
            } else
            {
                String[] nst = captureStackTrace(nested);
                for(int i = 0; i < nst.length - st.length + 2; i++)
                {
                    out.println(nst[i]);
                }
            }
            out.print("rethrown as ");
        }
        for(int i=0; i<st.length - skip; i++)
        {
            out.println(st[i]);
        }
    }

    
    private String[] captureStackTrace()
    {
        StringWriter sw = new StringWriter();
        super.printStackTrace(new PrintWriter(sw, true));
        return splitStackTrace(sw.getBuffer().toString());
    }

   
    private String[] captureStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return splitStackTrace(sw.getBuffer().toString());
    }

   
    private String[] splitStackTrace(String stackTrace)
    {
        String linebreak = System.getProperty("line.separator");
        StringTokenizer st = new StringTokenizer(stackTrace, linebreak);
        LinkedList<String> list = new LinkedList<String>();
        while(st.hasMoreTokens())
        {
            list.add(st.nextToken());
        }
        return (String [])list.toArray(new String[] {});
    }
}

