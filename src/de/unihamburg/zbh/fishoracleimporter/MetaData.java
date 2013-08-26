/*
  Copyright (c) 2012 Malte Mader <mader@zbh.uni-hamburg.de>
  Copyright (c) 2012 Center for Bioinformatics, University of Hamburg

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.csvreader.CsvReader;

import de.unihamburg.zbh.fishoracle_db_api.data.Study;

public class MetaData {

	private Study[] study;
	private String[] path;
	private Boolean[] createStudy;
	private String[] importType;
	private String[] subType;
	private Integer[] platformId;
	private Integer[] projectId;
	
	public MetaData() {
		readMetaDataFromFile();
	}

	public void readMetaDataFromFile(){
		
		CsvReader reader = null;
		try {
			reader = new CsvReader("metadata.csv");
			reader.setDelimiter('\t');
			reader.readHeaders();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Study s;
		
		ArrayList<String> dataPathContainer = new ArrayList<String>();
		ArrayList<Study> studyContainer = new ArrayList<Study>();
		ArrayList<Boolean> cStudyContainer = new ArrayList<Boolean>();
		ArrayList<String> iTypeContainer = new ArrayList<String>();
		ArrayList<String> sTypeContainer = new ArrayList<String>();
		ArrayList<Integer> plIdContainer = new ArrayList<Integer>();
		ArrayList<Integer> pIdContainer = new ArrayList<Integer>();
		
		try {
			while (reader.readRecord())
			{
				String cs = reader.get("CREATE_STUDY");
				boolean cStudy = Boolean.parseBoolean(cs);
				String dataPath = reader.get("DATA_PATH");
				String iType = reader.get("IMPORT_TYPE");
				String sType = reader.get("SUB_TYPE");
				Integer plId = Integer.parseInt(reader.get("PLATFORM_ID"));
				Integer pId = Integer.parseInt(reader.get("PROJECT_ID"));
				
				s = new Study();
				String p = dataPath;
				
				if(cStudy){
				
					String assembly = reader.get("ASSEMBLY");
					String description = reader.get("DESCRIPTION");
					int organId = Integer.parseInt(reader.get("ORGAN_ID"));
					
					s.setAssembly(assembly);
					s.setDescription(description);
					s.setOrganId(organId);
					s.setPropertyIds(new int[]{});
					
				}
				
				dataPathContainer.add(p);
				studyContainer.add(s);
				cStudyContainer.add(cStudy);
				iTypeContainer.add(iType);
				sTypeContainer.add(sType);
				plIdContainer.add(plId);
				pIdContainer.add(pId);
				
			}
			
			path = new String[dataPathContainer.size()];
			dataPathContainer.toArray(path);
			
			study = new Study[studyContainer.size()];
			studyContainer.toArray(study);
			
			createStudy = new Boolean[cStudyContainer.size()];
			cStudyContainer.toArray(createStudy);
			
			importType = new String[iTypeContainer.size()];
			iTypeContainer.toArray(importType);
			
			subType = new String[sTypeContainer.size()];
			sTypeContainer.toArray(subType);
			
			platformId = new Integer[plIdContainer.size()];
			plIdContainer.toArray(platformId);
			
			projectId = new Integer[pIdContainer.size()];
			pIdContainer.toArray(projectId);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Study[] getStudy() {
		return study;
	}

	public void setStudy(Study[] study) {
		this.study = study;
	}

	public String[] getPath() {
		return path;
	}

	public void setPath(String[] path) {
		this.path = path;
	}

	public Boolean[] getCreateStudy() {
		return createStudy;
	}

	public void setCreateStudy(Boolean[] createStudy) {
		this.createStudy = createStudy;
	}

	public String[] getImportType() {
		return importType;
	}

	public void setImportType(String[] importType) {
		this.importType = importType;
	}
	
	public String[] getSubType() {
		return subType;
	}

	public void setSubType(String[] subType) {
		this.subType = subType;
	}
	
	public Integer[] getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer[] platformId) {
		this.platformId = platformId;
	}

	public Integer[] getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer[] projectId) {
		this.projectId = projectId;
	}
}
