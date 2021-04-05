package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.*;

import java.util.List;

public interface UserDAO {
    /**
     * 유저 반환
     * @param user_id user객체를 찾기위한 아이디
     * @return user객체 1개
     * @throws FindException 아이디에 해당하는 유저가 없으면 반환 //
     */
    User selectById(String user_id) throws FindException;

    /**
     * 회원가입할 때 닉네임 중복확인
     * @param user_nick 입력창에 입력한 닉네임
     * @return 유저 객체
     * @throws FindException 닉네임에 해당하는 유저가 존재하지 않을때
     */
    User selectByNick(String user_nick) throws FindException;

    /**
     * 이메일 중복확인
     * @param user_email 회원가입 시 입력한 이메일
     * @throws FindException 이메일에 해당하는 유저가 존재하지 않을경우
     * @return
     */
    User selectByEmail(String user_email) throws FindException;

    /**
     * 회원가입할 때 email_tmp 에 있는 데이터로 인증
     * @param email 회원가입을위해 입력한 email
     * @throws FindException 인증전송 후 5분이 지났거나 이미 존재하는 이메일
     */
    String selectTmpByEmail(String email) throws FindException;

    /**
     * 날짜 순으로 10개 유저 객체 리스트
     * @param n 10개
     * @return 유저 객체 리스트
     * @throws FindException 값이 존재하지 않을때
     */
    List<User> selectAll(int n) throws FindException;


    /**
     * 유저 추가
     * @param user 유저객체
     * @throws AddException 아이디가 존재하는경우,저장소가 꽉찬경우,
     *                      가입도중 다른 사람이 같은 아이디, 닉네임으로 가입한 경우
     */
    void insert(User user) throws AddException;

    /**
     * 이메일 중복 확인이 끝나고 email_tmp 에 임시로 이메일과 체크번호 등록
     * @param email 회원가입에 입력한 이메일
     * @throws AddException 이미 메일을 보냈고 5분이 안지난 경우
     */
    String insertEmail(String email) throws AddException;

    /**
     * 유저 수정
     * @param user 유저객체(로그인한) // 이메일 비밀번호
     * @throws ModifyException 이메일이 존재하는경우, 타입 자릿수가 안맞는경우
     */
    void update(User user) throws ModifyException;

    /**
     * 아이디 비밀번호 찾기
     * @param user_email 회원가입할때 입력한 이메일
     * @throws ModifyException 이메일이 존재하지 않는경우, 수정에 실패한 경우
     * @return
     */
    String updateByEmail(String user_email) throws ModifyException;



    /**
     * 유저 삭제
     * @param user_id 세션에 있는 유저아이디
     * @throws RemoveException 삭제가 안될때
     */
    void delete(String user_id) throws RemoveException;

    /**
     * tmp 이메일 삭제
     * @param email 입력한 이메일
     * @throws RemoveException 삭제가 안될때
     */
    void deleteEmail(String email) throws RemoveException;

}
