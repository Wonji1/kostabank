package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.Question;

import java.util.List;

public interface QuestionDAO {
    /**
     * 문제 아이디로 문제 찾기
     * @param question_id 문제 아이디
     * @return question 객체
     * @throws FindException 문제 아이디에 해당하는 문제가 없을때
     */
    Question selectById(String question_id) throws FindException;

    /**
     * 내노트에 해당하는 문제 정보 // mynote와 question 조인
     * @param user_id 세션에 있는 유저 아이디
     * @param rownum 페이징 숫자
     * @return 문제 객체
     * @throws FindException 페이징 숫자에 해당하는 문제가 없을경우
     */
    Question selectMNById(String user_id, int rownum) throws FindException;

    /**
     * 풀었던 문제에서 노트로 추가
     * @param user_id 세션에 있는 유저 아이디
     * @param question_id_list 문제 아이디가 담긴 리스트
     * @throws AddException
     */
    void insertMNById(String user_id, String[] question_id_list) throws AddException;

    /**
     * 삭제 버튼 누르고 삭제
     * @param user_id 세션에 있는 아이디
     * @param question_id 요청받을 문제 아이디
     * @throws RemoveException 삭제가 진행되지 않았을 경우
     */
    void deleteMNById(String user_id, String question_id) throws RemoveException;

    /**
     * 내가 푼 문제 가져오기 // solved와 questrion 조인 200개
     * @param user_id 세션에 있는 유저 아이디
     * @param n 몇개뽑을지 기본은 200
     * @return 문제객체 리스트
     * @throws FindException 존재하지 않거나 데이터가 없을때
     */
    List<Question> selectSQById(String user_id, int n) throws FindException;

    /**
     * 문제 종류 code(문제 id 앞 2자리)에 대한 문제들
     * @return 문제 객체 리스트
     * @throws FindException 존재하지 않거나 데이터가 없을때
     */
    List<Question> selectAll() throws FindException;

    /**
     * 디비에서 임시 문제 지우고 선택해온 문제를 insert
     * @param user_id 세션 아이디
     * @param question_year 문제 코드
     * @throws AddException question_tmp에 추가 에러
     */
    void insertQTmpByQYear(String user_id, String question_year) throws AddException;

    /**
     * 디비에서 임시 문제 지우고 선택해온 문제를 insert
     * @param user_id 세션 아이디
     * @throws AddException question_tmp에 추가 에러
     */
    void insertRandomQTmp(String user_id) throws AddException;

    /**
     * row_num으로 찾는 문제 아이디
     * @param user_id 세션에 있는 아이디
     * @param row_num 몇번째 문제
     * @return 문제 객체
     * @throws FindException 존재하지 않을경우
     */
    Question selectQTmpByQId(String user_id, String row_num) throws FindException;

    /**
     * 문제 정답 배열을 받아서 ox를 판별
     * @param user_id 세션에 있는 아이디
     * @param question_answer_list 문제 정답 리스트
     * @throws ModifyException 수정실패 시
     */
    void updateQTmpByQ(String user_id, String[] question_answer_list) throws ModifyException;

    /**
     * 문제 푼 후 회차별로 문제를 가져오는 리스트
     * @param user_id 세션에 있는 아이디
     * @param question_round question_tmp에 저장되어있는 회차
     * @throws FindException 정보가 존재하지 않을 경우
     */
    List<Question> selectAfterSolveQByRound(String user_id, String question_round) throws FindException;
}

