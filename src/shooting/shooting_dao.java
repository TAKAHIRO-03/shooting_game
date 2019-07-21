package shooting;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;

public class shooting_dao {
    private final String path = "~/ranking.txt"; //ディレクトリの変更
    
    public ArrayList<String> text_read() {
    	ArrayList<String> texts = new ArrayList<>();
        String filename, line;
        filename = this.path;
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            while((line = br.readLine()) != null) {
                texts.add(line);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
        return texts;
    }
    
	public void text_write(String name,double num) {
        String filename; 
        filename = this.path;
        try {
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(name + ":" + num);
            pw.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }
	
	public void text_make() {
		File file = new File(this.path); //ディレクトリ変更する必要有り。
		try {
			if (file.createNewFile()){
			    System.out.println("ファイル作成成功");
			}else{
			    System.out.println("既にファイルが存在しています。");
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public ArrayList<ArrayList<String>> parth_text(ArrayList<String> AllData) {
		ArrayList<ArrayList<String>> ParthData = 
				new ArrayList<ArrayList<String>>();
		ArrayList<String> AllDatas = AllData;
        Matcher match = null;
        int GroupCount;
		 
        // 正規表現のパターンを作成
        Pattern p = Pattern.compile("([一-龠ぁ-ゞァ-ヶa-zA-Z]+):([0-9]{1,}.[0-9])");

        for(int i = 0; i < AllDatas.size(); i++){
        	match = p.matcher(AllDatas.get(i));
        	GroupCount = match.groupCount();
   		   ArrayList<String> InputData = new ArrayList<>();
	   		   for(int j = 0; j < GroupCount; j++) {
		           if(match.find()) {
		        	   InputData.add(match.group(1));
		        	   InputData.add(match.group(2));
		           }
	   		   }
	   	   ParthData.add(InputData);
        }		
        return ParthData;
	}
	
	public String[][] LogicData(ArrayList<ArrayList<String>> ParthData) {
		ArrayList<ArrayList<String>> ParthDatas = ParthData;
		String[][] Ranking = new String[3][2];
		double NumData = 0.0;
		double num1 = 0.0;
		double num2 = 0.0;
		double num3 = 0.0;
		
		 for(ArrayList<String> NumStr: ParthDatas){
	            for(int i = 0; i < NumStr.size(); i++){
	            	NumData = Double.parseDouble(NumStr.get(1));
	            	if(NumData > num1) {
	            		Ranking[0][0] = NumStr.get(0);
	            		Ranking[0][1] = NumStr.get(1);
	            		num1 = NumData;
	            	}else if(NumData > num2 && num1 != NumData) {
	            		Ranking[1][0] = NumStr.get(0);
	            		Ranking[1][1] = NumStr.get(1);
	            		num2 = NumData;
	            	}else if(NumData > num3 && num2 != NumData && num1 != NumData) {
	            		Ranking[2][0] = NumStr.get(0);
	            		Ranking[2][1] = NumStr.get(1);	 
	            		num3 = NumData;
	            	}
	            }        
		  }
		 
		 Collections.reverse(ParthDatas); //逆順でfor文回す。
		 
		 for(ArrayList<String> NumStr: ParthDatas){
	            for(int i = 0; i < NumStr.size(); i++){
	            	NumData = Double.parseDouble(NumStr.get(1));
	            	if(NumData > num1) {
	            		Ranking[0][0] = NumStr.get(0);
	            		Ranking[0][1] = NumStr.get(1);
	            		num1 = NumData;
	            	}else if(NumData > num2 && num1 != NumData) {
	            		Ranking[1][0] = NumStr.get(0);
	            		Ranking[1][1] = NumStr.get(1);
	            		num2 = NumData;
	            	}else if(NumData > num3 && num2 != NumData && num1 != NumData) {
	            		Ranking[2][0] = NumStr.get(0);
	            		Ranking[2][1] = NumStr.get(1);	 
	            		num3 = NumData;
	            	}
	            }        
		  }
		return Ranking;
}

}
