/*
  Copyright (c) 2009-2012 Malte Mader <mader@zbh.uni-hamburg.de>
  Copyright (c) 2009-2012 Center for Bioinformatics, University of Hamburg

  Permission to use, copy, modify, and distribute this software for any
  purpose with or without fee is hereby granted, provided that the above
  copyright notice and this permission notice appear in all copies.

  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
  WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
  MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
  ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
  ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
  OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*/

package de.unihamburg.zbh.fishoracleimporter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBConfig {
	
	private DBConfigData connectionData;
	private String serverPath;
	
	public DBConfig(){	
	}
	
	public DBConfig(String serverPath){
		this.serverPath = serverPath; 
		 loadConfigDataFromFile();
	}

	public boolean writeConfigDataToFile() throws Exception{
		BufferedWriter bufferedWriter = null;
		try {
            
            bufferedWriter = new BufferedWriter(new FileWriter(serverPath + "database.conf"));
            
            bufferedWriter.newLine();
            bufferedWriter.write("-- This is the configuration file for the database connection parameters.");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("[ensembl]");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("host = " + connectionData.getEhost());
            bufferedWriter.newLine();
            bufferedWriter.write("port = " + connectionData.getEport());
            bufferedWriter.newLine();
            bufferedWriter.write("db = " + connectionData.getEdb());
            bufferedWriter.newLine();
            bufferedWriter.write("user = " + connectionData.getEuser());
            bufferedWriter.newLine();
            bufferedWriter.write("pw = " + connectionData.getEpw());
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("[fishoracle]");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("host = " + connectionData.getFhost());
            bufferedWriter.newLine();
            bufferedWriter.write("db = " + connectionData.getFdb());
            bufferedWriter.newLine();
            bufferedWriter.write("user = " + connectionData.getFuser());
            bufferedWriter.newLine();
            bufferedWriter.write("pw = " + connectionData.getFpw());
            bufferedWriter.newLine();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
		return true;
    }
	
	public void loadConfigDataFromFile(){
		
		String ehost = null;
		int eport = 0;
		String edb = null;
		String euser = null;
		String epw = null;
			
		String fhost = null;
		String fdb = null;
		String fuser = null;
		String fpw = null;
		
		try{
			
		    FileInputStream fStream = new FileInputStream(serverPath + "database.conf");
		    DataInputStream inStream = new DataInputStream(fStream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		    
		    String strLine;
		    String[] dataStr;
		    
		    Boolean ensmbl = false;
		    Boolean fishoracle = false;   
		    
		    while ((strLine = br.readLine()) != null)   {
			  
		      Pattern pensmbl = Pattern.compile("^\\[ensembl\\]$");
			  Matcher mensmbl = pensmbl.matcher(strLine);
		      
			  if(mensmbl.find()){
				  ensmbl = true;  
				  fishoracle = false; 
			  }
			  
			  Pattern pforacle = Pattern.compile("^\\[fishoracle\\]$");
			  Matcher mforacle = pforacle.matcher(strLine);
			  
			  if(mforacle.find()){
				  fishoracle = true; 
				  ensmbl = false; 
			  }
			  
			  Pattern phost = Pattern.compile("^host");
			  Matcher mhost = phost.matcher(strLine);
		      
			  Pattern pport = Pattern.compile("^port");
			  Matcher mport = pport.matcher(strLine);
			  
			  Pattern pdb = Pattern.compile("^db");
			  Matcher mdb = pdb.matcher(strLine);
			  
			  Pattern puser = Pattern.compile("^user");
			  Matcher muser = puser.matcher(strLine);
			  
			  Pattern ppw = Pattern.compile("^pw");
			  Matcher mpw = ppw.matcher(strLine);
			  
			  if(ensmbl){
				  
				  if(mhost.find()){
					  dataStr = strLine.split("=");
					  ehost = dataStr[1].trim();
				  }
				  if(mport.find()){
					  dataStr = strLine.split("=");
					  eport = Integer.parseInt(dataStr[1].trim());
				  }
				  if(mdb.find()){
					  dataStr = strLine.split("=");
					  edb = dataStr[1].trim();
				  }
				  if(muser.find()){
					  dataStr = strLine.split("=");
					  euser = dataStr[1].trim();
				  }
				  if(mpw.find()){
					  dataStr = strLine.split("=");
					  epw = dataStr[1].trim();
				  }
			  }
			  if(fishoracle){
				  
				  if(mhost.find()){
					  dataStr = strLine.split("=");
					  fhost = dataStr[1].trim();
				  }
				  if(mdb.find()){
					  dataStr = strLine.split("=");
					  fdb = dataStr[1].trim();
				  }
				  if(muser.find()){
					  dataStr = strLine.split("=");
					  fuser = dataStr[1].trim();
				  }
				  if(mpw.find()){
					  dataStr = strLine.split("=");
					  fpw = dataStr[1].trim();
				  }
			  }
		    }

		    inStream.close();
		    } catch (Exception e){
		    	e.printStackTrace();
		    	System.err.println("Error: " + e.getMessage());
		    }
		    connectionData = new DBConfigData(ehost, 
		    									eport, 
		    									edb,
		    									euser,
		    									epw, 
		    									fhost, 
		    									fdb, 
		    									fuser,
		    									fpw);
		
	}
	
	public DBConfigData getConnectionData() {
		return connectionData;
	}

	public void setConnectionData(DBConfigData connectionData) {
		this.connectionData = connectionData;
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
}
