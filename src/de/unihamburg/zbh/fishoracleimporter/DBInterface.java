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

import de.unihamburg.zbh.fishoracle_db_api.data.Study;
import de.unihamburg.zbh.fishoracle_db_api.driver.FODriver;
import de.unihamburg.zbh.fishoracle_db_api.driver.FODriverImpl;
import de.unihamburg.zbh.fishoracle_db_api.driver.GenericAdaptor;
import de.unihamburg.zbh.fishoracle_db_api.driver.SNPMutationAdaptor;
import de.unihamburg.zbh.fishoracle_db_api.driver.SegmentAdaptor;
import de.unihamburg.zbh.fishoracle_db_api.driver.StudyAdaptor;
import de.unihamburg.zbh.fishoracle_db_api.driver.TranslocationAdaptor;

public class DBInterface {
	
	private DBConfig dbConfig;
	private DBConfigData connectionData;
	
	public DBInterface(String serverPath) {
		dbConfig = new DBConfig(serverPath);
		connectionData = dbConfig.getConnectionData();
	}
	
	private FODriver getFoDriver(){
		return new FODriverImpl(connectionData.getFhost(),
									connectionData.getFdb(),
									connectionData.getFuser(),
									connectionData.getFpw(),
									"3306");
	}
	
	public int createNewStudy(Study study, int projectId) {
		FODriver driver = getFoDriver();
		StudyAdaptor sa = driver.getStudyAdaptor();
		
		return sa.storeStudy(study, projectId);
	}
	
	public void importData(Study study, String importType) {
		FODriver driver = getFoDriver();
		
		StudyAdaptor sa = driver.getStudyAdaptor();
		
		Study s = sa.fetchStudyForName(study.getName(), false);
		
		if(importType.equals("Segments")){
			SegmentAdaptor ca = driver.getSegmentAdaptor();
		
			ca.storeSegments(study.getSegments(), s.getId());
		}
		else if(importType.equals("Mutations")){
			SNPMutationAdaptor ma = driver.getSNPMutationAdaptor();
			
			ma.storeSNPMutations(study.getMutations(), s.getId());
		}
		else if(importType.equals("Translocations")){
			TranslocationAdaptor ta = driver.getTranslocationAdaptor();
			
			ta.storeTranslocations(study.getTranslocs(), s.getId());
		} else {
			GenericAdaptor gfa = driver.getGenericAdaptor();
			
			gfa.storeGenericFeatures(study.getFeatures(), s.getId());
		}
	}
}