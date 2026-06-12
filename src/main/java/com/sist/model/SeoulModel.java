package com.sist.model;
import java.util.*;

import com.sist.controller.Controller;
import com.sist.controller.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.sist.dao.*;
import com.sist.vo.*;
// 브라우저 전송 => Model
// Controller와 연결
// Model : 1. 요청 받기 2. 요청 처리 (DAO, Open API) 3. 결과값
// Model을 찾기 => Controller가 해준다
/*
*  데이터 전송
*  => request.setAttribute() / session.setAttribute()
*  	| => 대부분 				  |	=> 사용자 정보 / 장바구니 / 예약
*  							  | => 프로그램 종료시까지 기억
*  								=> 모든 JSP에서 사용이 가능
*  	  => 한개의 JSP에서만 사용이 가능
*/

@Controller
public class SeoulModel {
	private String[] table= {
		"",
		"seoul_location",
		"seoul_nature",
		"seoul_shop",
		"seoul_hotel"
	};
	private String[] title= {
			"",
			"서울 명소",
			"서울 자연",
			"서울 쇼핑",
			"서울 호텔"
	};
	@RequestMapping("seoul/list.do")
	public String seoul_list(HttpServletRequest request, HttpServletResponse response) {
		String page=request.getParameter("page");
		if(page==null)
			page="1";
		String tno=request.getParameter("tno");
		if(tno==null)
			tno="1";
		// 현재 페이지 지정
		int curpage=Integer.parseInt(page);
		// 오라클에서 값을 가져오기
		// FROM ${table} => seoul_location
		// OFFSET #{start} ROWS FETCH NEXT 12 ROWS ONLY
		// map.get("start") ==> ' ' 일반 데이터 => setString() : ''
		Map map=new HashMap();
		map.put("table", table[Integer.parseInt(tno)]);
		map.put("start", (curpage*12)-12);
		List<SeoulVO> list=SeoulDAO.seoulListData(map);
		int totalpage=SeoulDAO.seoulTotalPage(map);
		
		// 데이터를 전송 (출력대상)
		request.setAttribute("list", list);
		request.setAttribute("curpage", curpage);
		request.setAttribute("totalpage", totalpage);
		request.setAttribute("tno", tno);
		request.setAttribute("title", title[Integer.parseInt(tno)]);
		
		return "../seoul/list.jsp"; // request를 받는 JSP 지정
	}
	// 어노테이션 => 구분자 (인데스 => 빠르게 찾기)
	@RequestMapping("seoul/detail.do") // 조건문 대신 사용 / 메소드 구분자
	public String seoul_detail(HttpServletRequest request, HttpServletResponse response) {
		String no=request.getParameter("no");
		String tno=request.getParameter("tno");
		// detail.do?tno=1&no=100
		Map map=new HashMap();
		map.put("no", Integer.parseInt(no));
		map.put("table", table[Integer.parseInt(tno)]);
		SeoulVO vo=SeoulDAO.seoulDetailData(map);
		// JSP 로 전송
		request.setAttribute("vo", vo);
		request.setAttribute("tno", tno);
		
		return "../seoul/detail.jsp";
	}
}