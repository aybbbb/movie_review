package com.example.movie_review.helpers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PageHelper {
    // QueryString 으로 받아야 하는 값
    private int nowPage;

    private int totalCount; // DB에서 조회해야하는 값
    // 개발자가 정의하는 값
    private int listCount; // 한페이지에 보여질 글의 목록 수
    private int groupCount; // 한그룹에 표시할 페이지번호 갯수

    // 연산처리 필요한 값
    private int totalPage;
    private int startPage;
    private int endPage;
    private int prevPage;
    private int nextPage;
    private int offset; // db limit 시작위치


    public PageHelper(int nowPage, int totalCount, int listCount, int groupCount){
        this.nowPage = nowPage;
        this.totalCount = totalCount;
        this.listCount = listCount;
        this.groupCount = groupCount;

        totalPage = ((totalCount -1) / listCount) + 1;

        if(nowPage <0){
            nowPage =1;
        }else if(nowPage > totalPage){
            nowPage = totalPage;
        }

        startPage = ((nowPage -1) / groupCount ) * groupCount +1;
        endPage = startPage - 1 +groupCount;

        if(endPage >totalPage){
            endPage = totalPage;
        }

        prevPage = (startPage > groupCount) ?  startPage -1 : 0;
        nextPage = (endPage < totalPage) ? endPage +1 : 0;
        offset = (nowPage -1 ) * listCount;

        log.debug(this.toString());
    }

}
