package org.example.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SynonymCrawling {
    @GetMapping("/synonyms")
    public ResponseEntity<String> saveData(@RequestParam String word) {

        String[] data = {
                "OD,SPL,R,가족돌봄휴가",
                "WD,WHS,V,거래처명",
                "WD,RGW,V,거래처명",
                "WD,DLV,V,거래처명",
                "WD,RGD,V,거래처명",
                "OD,ALV,R,결근",
                "OD,APL,A,결재",
                "OD,APL,V,결재자",
                "OD,APL,V,결재제목",
                "OD,DBE,V,경유지",
                "OD,OBE,V,경유지",
                "WD,MSW,A,고정로케이션",
                "OD,VAC,R,공가",
                "OD,OLV,V,공가 시작일자와 종료일자",
                "OD,OLV,V,공가구분",
                "OD,OLV,V,공가사유",
                "OD,OLV,A,공가신청",
                "OD,SLV,R,공무상병가",
                "OD,DBT,V,공용차량 이용여부",
                "OD,OBT,V,공용차량 이용여부",
                "OD,DBT,A,관내",
                "OD,DBT,A,관내출장",
                "OD,BZT,R,관내출장",
                "OD,BTE,R,관내출장",
                "OD,DBT,A,관내출장신청",
                "OD,DBE,A,관내출장종료신청",
                "OD,OBT,A,관외 출장",
                "OD,DBT,A,관외외",
                "OD,OBT,A,관외출장",
                "OD,DBT,A,관외출장",
                "OD,BZT,R,관외출장",
                "OD,BTE,R,관외출장",
                "OD,OBE,A,관외출장종료신청",
                "OD,DBE,V,교통편",
                "OD,OBE,V,교통편",
                "OD,OTR,V,근무구분",
                "OD,OTR,V,근무내역",
                "OD,SPL,R,난임치료시술휴가",
                "WD,MSW,V,단번호",
                "OD,OBT,A,대중교통",
                "OD,OBT,A,대중교통통",
                "OD,VAC,R,대체휴가",
                "OD,SBH,V,대체휴가사유",
                "OD,SBH,V,대체휴무 시작일자와 종료일자",
                "OD,SBH,V,대체휴무구분",
                "OD,SBH,A,대체휴무신청",
                "OD,DBE,V,도착지",
                "OD,OBE,V,도착지",
                "WD,MSW,V,랙번호",
                "WD,MSW,R,랙번호구분분",
                "WD,MVL,A,로케이션이동",
                "OD,SPL,R,모성보호시간",
                "OD,OLV,R,무급",
                "OD,SBH,R,무급",
                "OD,SPL,R,무급",
                "OD,DSP,A,배차신청",
                "OD,OBT,A,버스",
                "OD,OBT,A,버스스",
                "OD,VAC,R,병가",
                "OD,SLV,V,병가 시작일자와 종료일자",
                "OD,SLV,V,병가구분",
                "OD,SLV,V,병가사유",
                "OD,SLV,A,병가신청",
                "WD,MSW,A,보충랙",
                "OD,OTR,R,비상",
                "OD,DBT,V,상시출장여부",
                "WD,MSW,R,센터구분",
                "WD,MSW,V,셀번호",
                "WD,MSW,R,셀번호구분",
                "OD,OTR,V,신청시간",
                "OD,SPL,R,심리안정휴가",
                "VP,CMM,R,아니오",
                "OD,DBE,V,여비종별명칭",
                "OD,OBE,V,여비종별명칭",
                "OD,SPL,R,여성보건휴가",
                "OD,VAC,R,연가",
                "OD,ALV,R,연가",
                "OD,ALV,V,연가 시작일자와 종료일자",
                "OD,ALV,V,연가구분",
                "OD,ALV,V,연가사유",
                "OD,ALV,A,연가신청",
                "OD,ALV,V,연가저축 사용여부",
                "WD,MSW,V,열번호",
                "OD,APL,A,예",
                "OD,CMM,A,예",
                "VP,CMM,R,예",
                "OD,ALV,R,외출",
                "OD,DSP,V,운전자이름",
                "OD,OLV,R,유급",
                "OD,SBH,R,유급",
                "OD,SPL,R,유급",
                "OD,SPL,R,육아시간",
                "WD,RGD,V,이동로케이션명",
                "WD,RGD,V,이동박스번호",
                "WD,RGD,V,이동수량",
                "OD,OTR,R,일반--",
                "OD,SLV,R,일반병가",
                "OD,SPL,R,임신검진휴가",
                "WD,WHS,A,입고",
                "WD,RGW,A,입고반품",
                "WD,WHS,V,입고수량",
                "WD,MSW,V,작업라인",
                "WD,MSW,R,작업라인구분",
                "WD,TSO,V,작업유형",
                "WD,MSW,V,작업존",
                "WD,MSW,R,작업존구분",
                "WD,TSO,A,작업주문",
                "OD,SPL,R,재해구호휴가",
                "OD,ALV,R,조퇴",
                "OD,DBE,V,주가지출항목과 비용",
                "OD,OBE,V,주가지출항목과 비용",
                "OD,ALV,R,지각",
                "OD,DSP,V,차량번호",
                "OD,DSP,V,차량사용 시작일자와 종료일자",
                "OD,APL,V,첨부파일",
                "OD,OTR,A,초과근무",
                "OD,OTR,V,초과근무일자",
                "OD,DBE,V,추가지출",
                "OD,OBE,V,추가지출",
                "WD,DLV,A,출고",
                "WD,RGD,A,출고반품",
                "OD,GTW,A,출근",
                "OD,DBE,V,출발지",
                "OD,OBE,V,출발지",
                "OD,SPL,R,출산휴가",
                "OD,BZT,V,출장구분",
                "OD,BTE,V,출장구분",
                "OD,DBT,V,출장기간",
                "OD,OBT,V,출장기간",
                "OD,DBT,V,출장목적",
                "OD,OBT,V,출장목적",
                "OD,BZT,A,출장신청",
                "OD,,V,출장조회",
                "OD,BTE,A,출장종료신청",
                "OD,DBT,V,출장지",
                "OD,OBT,V,출장지",
                "WD,MSW,V,통로번호",
                "OD,LVW,A,퇴근",
                "OD,VAC,R,특별휴가",
                "OD,SPL,V,특별휴가 시작일자와 종료일자",
                "OD,SPL,V,특별휴가구분",
                "OD,SPL,V,특별휴가사유",
                "OD,SPL,A,특별휴가신청",
                "OD,SPL,V,특별휴가종류",
                "WD,WHS,V,품목명",
                "WD,DLV,V,품목번호",
                "WD,RGD,V,품목번호",
                "OD,DSP,V,행선지",
                "WD,MVL,V,현재로케이션명",
                "WD,RGD,V,현재박스번호",
                "OD,VAC,V,휴가구분",
                "OD,VAC,A,휴가신청",
        };

        String[] data_unit_split = null;
        for(String data_unit : data) {
            data_unit_split = data_unit.split(",");
//            System.out.println(data_unit_split[0]
//                    + "/" + data_unit_split[1]
//                    + "/" + data_unit_split[2]
//                    + "/" + data_unit_split[3]);

//        String url = "https://ko.dict.naver.com/#/search?query=" + word;
        String url = "https://dict.naver.com/dict.search?query=" + data_unit_split[3] + "&from=tsearch";
        List<String> synonyms = new ArrayList<>();
//        Elements scriptElements = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements scriptElements = doc.getElementsByTag("script");
//            scriptElements = doc.getAllElements();

            for (Element element : scriptElements) {
                String scriptContent = element.html();
                if (scriptContent.contains("similarWordList")) {
                    int startIndex = scriptContent.indexOf("similarWordList");
                    int endIndex = scriptContent.indexOf("]", startIndex) + 1;
                    String jsonString = scriptContent.substring(startIndex + "similarWordList:".length(), endIndex).trim();

                    if (jsonString.endsWith(",")) {
                        jsonString = jsonString.substring(0, jsonString.length() - 1);
                    }

                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String similarWordName = obj.getString("similarWordName");
                        synonyms.add(similarWordName);
                    }
                    break;
                }
            }

            // 각 요청 사이에 2초 대기 (서버 부하 방지)
            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred while fetching synonyms");
        }

        //String join_synonyms = String.join(",", synonyms);

        for(String synonym : synonyms) {
            synonym = synonym.replaceAll("\\<.*?\\>", ""); // <sup> 태그 제거
            synonym = synonym.replaceAll("\\d+", ""); // 숫자 제거
            System.out.println(
                    data_unit_split[0] + "/"
                            + data_unit_split[1] + "/"
                            + data_unit_split[2] + "/"
                            + data_unit_split[3] + "/" + synonym
            );
        }
        //return ResponseEntity.ok("Synonyms:ss " + String.join(", ", synonyms));
//        return ResponseEntity.ok("Synonyms: " + scriptElements.html());
        }
        return ResponseEntity.ok("");
    }
}