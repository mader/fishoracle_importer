/*
  Copyright (c) 2012-2013 Malte Mader <mader@zbh.uni-hamburg.de>
  Copyright (c) 2012-2013 Center for Bioinformatics, University of Hamburg

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

import java.io.File;
import java.util.ArrayList;

import com.csvreader.CsvReader;

import de.unihamburg.zbh.fishoracle_db_api.data.GenericFeature;
import de.unihamburg.zbh.fishoracle_db_api.data.Location;
import de.unihamburg.zbh.fishoracle_db_api.data.SNPMutation;
import de.unihamburg.zbh.fishoracle_db_api.data.Segment;
import de.unihamburg.zbh.fishoracle_db_api.data.Study;
import de.unihamburg.zbh.fishoracle_db_api.data.Translocation;

public class FishoracleImporter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MetaData md = new MetaData();
		
		int nofProjects = md.getPath().length;
		
		String[] paths = md.getPath();
		
		Study[] studies = md.getStudy();
		
		for(int j = 0; j < nofProjects; j++){
		
			System.out.println(paths[j]);
			System.out.println("----------");
			
			File dir = new File(paths[j]);
			String[] children = dir.list();
			
			try {
				importData(studies[j],
							children,
							paths[j],
							md.getImportType()[j],
							md.getSubType()[j],
							md.getCreateStudy()[j],
							md.getPlatformId()[j],
							md.getProjectId()[j],
							"",
							0,
							0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			spacer();
			
			System.out.println("----------");
			System.out.println("");
		}
		System.out.println("Finished, bye.");
	}
	
	/* Work around for mysterious MySQL connection problems */
	static public void spacer(){
		
		try {
				System.out.println("");
				for(int k = 0; k < 30; k++){
					Thread.sleep(1000);
					System.out.print(".");
				}
				System.out.println("");
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	static public int[] importData(Study study,
								String[] fileNames,
								String path,
								String importType,
								String dataSubType,
								boolean createStudy,
								int platformId,
								int projectId,
								String tool,
								int importNumber,
								int nofImports) throws Exception {

		int s = 0;
		
		for(int i = 0; i < fileNames.length; i++){
			
			if(s > 50){
				
				spacer();
				s = 0;
			}
			
			System.out.println(" Import: " + fileNames[i] + " (" + (i + 1) + ")");
			
			study.setName(fileNames[i].split("\\.")[0]);

			CsvReader reader = new CsvReader(path + System.getProperty("file.separator") + fileNames[i]);
			reader.setDelimiter('\t');
			reader.readHeaders();

			DBInterface db = new DBInterface("");

			if(importType.equals("Segments")){
				
				Segment[] segments;
				ArrayList<Segment> segmentContainer = new ArrayList<Segment>();
		
				while (reader.readRecord())
				{
					String chr = reader.get("chrom");
					String start = reader.get("loc.start");
					String end = reader.get("loc.end");
					String mean = "0";
					String markers = "0";
					String status = "-1";
					String statusScore = "-1.0";
							
					if(dataSubType.equals("cnv_intensity")){
						
						mean = reader.get("seg.mean");
						markers = reader.get("num.mark");
						status = "-1";
						statusScore = "-1.0";
					}
					if(dataSubType.equals("cnv_status")){
						
						status = reader.get("cnv.status");
						statusScore = reader.get("status.score");
						mean = "0";
						markers = "0";
					}
			
					Location loc = new Location(chr, Integer.parseInt(start), Integer.parseInt(end));
			
					Segment segment = new Segment(0,
													loc,
													dataSubType);
					
					segment.setMean(Double.parseDouble(mean));
					segment.setNumberOfMarkers(Integer.parseInt(markers));
					
					segment.setStatus(Integer.parseInt(status));
					segment.setStatusScore(Double.parseDouble(statusScore));
					
					segment.setPlatformId(platformId);
					
					segmentContainer.add(segment);
				}
				
				reader.close();

				segments = new Segment[segmentContainer.size()];
				segmentContainer.toArray(segments);
			
				study.setSegments(segments);
				
				
			} else if(importType.equals("Mutations")){

				SNPMutation[] mutations;
				ArrayList<SNPMutation> snpContainer = new ArrayList<SNPMutation>();

				while (reader.readRecord())
				{
					String chr = reader.get("#CHROM");
					String pos = reader.get("POS");
					String dbSnpId = reader.get("DBSNP_ID");
					String ref = reader.get("REF");
					String alt = reader.get("ALT");
					String quality = reader.get("QUAL");
					String somatic = reader.get("SOMATIC_GERMLINE_CLASSIFICATION");
					String confidence = reader.get("CONFIDENCE");

					if(quality.equals("")){
						quality = "100.0";
					}

					SNPMutation mut = new SNPMutation(0, new Location(chr,
							Integer.parseInt(pos),
							Integer.parseInt(pos)),
							dbSnpId,
							ref,
							alt,
							Double.parseDouble(quality),
							somatic,
							confidence,
							tool);

					mut.setPlatformId(platformId);
					
					snpContainer.add(mut);

				}

				reader.close();

				mutations = new SNPMutation[snpContainer.size()];
				snpContainer.toArray(mutations);

				study.setMutations(mutations);

			} else if(importType.equals("Translocations")){

				Translocation[][] translocs;
				ArrayList<Translocation[]> translocContainer = new ArrayList<Translocation[]>();

				while (reader.readRecord())
				{

					Translocation[] transloc = new Translocation[2];

					String chr1 = reader.get("CHR1");
					String pos1 = reader.get("POS1");
					String chr2 = reader.get("CHR2");
					String pos2 = reader.get("POS2");

					transloc[0] = new Translocation(0, new Location(chr1,
							Integer.parseInt(pos1),
							Integer.parseInt(pos1)),
							0);
					transloc[0].setPlatformId(platformId);
					
					transloc[1] = new Translocation(0, new Location(chr2,
							Integer.parseInt(pos2),
							Integer.parseInt(pos2)),
							0);
					transloc[1].setPlatformId(platformId);

					translocContainer.add(transloc);

				}

				reader.close();

				translocs = new Translocation[translocContainer.size()][];
				translocContainer.toArray(translocs);

				study.setTranslocs(translocs);

			} else {

				GenericFeature[] features;
				ArrayList<GenericFeature> featureContainer = new ArrayList<GenericFeature>();

				while (reader.readRecord())
				{

					String chr = reader.get("#CHROM");
					String start = reader.get("START");
					String end = reader.get("END");

					GenericFeature f = new GenericFeature(0, new Location(chr,
							Integer.parseInt(start),
							Integer.parseInt(end)),
							dataSubType);

					f.setPlatformId(platformId);
					
					featureContainer.add(f);

				}

				reader.close();

				features = new GenericFeature[featureContainer.size()];
				featureContainer.toArray(features);

				study.setFeatures(features);

			}

			study.setUserId(1);

			if(createStudy){
				db.createNewStudy(study, projectId);
			} else {
				db.importData(study, importType);
			}

			s++;
		}

		return new int[]{importNumber, nofImports};
	}
}