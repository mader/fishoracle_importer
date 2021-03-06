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

public class DBConfigData {

	//ensembl connection parameters
	private String ehost = null;
	private int eport;
	private String edb = null;
	private String euser = null;
	private String epw = null;
	
	//fish oracle connection parameters
	private String fhost = null;
	private String fdb = null;
	private String fuser = null;
	private String fpw = null;
	
	public DBConfigData() {
	}

	public DBConfigData(String ehost, int eport, String edb, String euser,
			String epw, String fhost, String fdb, String fuser, String fpw) {
		super();
		this.ehost = ehost;
		this.eport = eport;
		this.edb = edb;
		this.euser = euser;
		this.epw = epw;
		this.fhost = fhost;
		this.fdb = fdb;
		this.fuser = fuser;
		this.fpw = fpw;
	}

	public String getEhost() {
		return ehost;
	}

	public void setEhost(String ehost) {
		this.ehost = ehost;
	}

	public int getEport() {
		return eport;
	}

	public void setEport(int eport) {
		this.eport = eport;
	}

	public String getEdb() {
		return edb;
	}

	public void setEdb(String edb) {
		this.edb = edb;
	}

	public String getEuser() {
		return euser;
	}

	public void setEuser(String euser) {
		this.euser = euser;
	}

	public String getEpw() {
		return epw;
	}

	public void setEpw(String epw) {
		this.epw = epw;
	}

	public String getFhost() {
		return fhost;
	}

	public void setFhost(String fhost) {
		this.fhost = fhost;
	}

	public String getFdb() {
		return fdb;
	}

	public void setFdb(String fdb) {
		this.fdb = fdb;
	}

	public String getFuser() {
		return fuser;
	}

	public void setFuser(String fuser) {
		this.fuser = fuser;
	}

	public String getFpw() {
		return fpw;
	}

	public void setFpw(String fpw) {
		this.fpw = fpw;
	}

}
