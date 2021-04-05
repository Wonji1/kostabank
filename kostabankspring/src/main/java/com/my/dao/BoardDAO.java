package com.my.dao;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.vo.Board;
import com.my.vo.BoardComment;
import com.my.vo.Notice;

import java.util.List;

//board notice
public interface BoardDAO {
    /**
     * 게시판 아이디로 게시판 상세정보 조회
     * 보드 보드코멘트 조인 좋아요도 조인
     * 조회수 증가
     * @param board_id 게시판 아이디
     * @return 게시판 객체
     * @throws FindException 게시판 아이디에 대한 게시판이 없을때
     */
    Board BoardById(String board_id) throws FindException;

//    /**
//     * 유저가 좋아요 눌렀는지 확인
//     * @param user_id 세션에 있는 아이디
//     * @param board_id 유저가 클릭한 보드 아이디
//     * @throws FindException 게시판 아이디에 대한 정보가 없을때
//     */
//    void BoardLikeById(String user_id,String board_id) throws FindException;

    /**
     * 10개씩 끊어서 전체 게시판 조회
     * @return 페이징 숫자에 대한 n개의 게시판 정보
     * @throws FindException page페이징 숫자에 대한 정보가 하나도 없을때 발생
     */
    List<Board> BoardAll(int page, int n) throws FindException;


    /**
     * 게시판 댓글들
     * @param board_id 게시판 아이디
     * @return 보드 코멘트 리스트
     * @throws FindException 없을 때
     */
    List<BoardComment> CommentAll(String board_id) throws FindException;


    /**
     * 댓글 등록
     * @param comment 댓글 객체
     * @throws AddException
     */
    public void CommentInsert(BoardComment comment) throws AddException;


    /**
     * 좋아요 체크
     * @param user_id 세션에 있는 아이디
     * @param board_id 보드 아이디
     * @throws FindException 좋아요 안누른사람
     */
    void UpCheck(String user_id, String board_id) throws FindException;

    /**
     * 좋아요 추가
     * @param board_id 게시글 번호
     * @param user_id 로그인된 아이디
     * @throws AddException
     */
    void BoardUpInsert(String board_id, String user_id) throws AddException;

    /**
     * 좋아요 삭제
     * @param board_id 게시글 번호
     * @param user_id 로그인 아이디
     * @throws RemoveException
     */
    void BoradUpDelete(String board_id, String user_id) throws RemoveException;

    /**
     * 새로 만든 게시판 인서트
     * @param board 보드 객체
     * @throws AddException 저장소 꽉찰경우
     */
    void BoardInsert(Board board) throws AddException;

    /**
     * 게시판 정보 업데이트
     * @param board 보드 객체
     * @throws ModifyException 수정 실패시 예외발생
     */
    void BoardUpdate(Board board) throws ModifyException;

    /**
     * 게시판 삭제
     * @param board_id 보드 id
     * @throws RemoveException 삭제 실패시 예외발생
     */
    void BoardDelete(String board_id) throws RemoveException;

    /**
     * 공지사항 아이디로 공지사항 상세정보 조회
     * @param notice_id 공지사항 아이디
     * @return 공지사항 객체
     * @throws FindException 공지사항 아이디에 대한 게시판이 없을때
     */
    Notice NoticeById(String notice_id) throws FindException;

    /**
     * 10개씩 끊어서 전체 공지사항 조회
     * @return 페이징 숫자에 대한 n개의 게시판 정보
     * @throws FindException page페이징 숫자에 대한 정보가 하나도 없을때 발생
     */
    List<Notice> NoticeAll(int page, int n) throws FindException;

    /**
     * 새로 만든 공지사항 인서트
     * @param notice 공지사항 객체
     * @throws AddException 저장소 꽉찰경우
     */
    void NoticeInsert(Notice notice) throws AddException;

    /**
     * 공지사항 정보 업데이트
     * @param notice 공지사항 객체
     * @throws ModifyException 수정 실패시 예외발생
     */
    void NoticeUpdate(Notice notice) throws ModifyException;

    /**
     * 공지사항 삭제
     * @param notice_id 공지사항 id
     * @throws RemoveException 삭제 실패시 예외발생
     */
    void NoticeDelete(String notice_id) throws RemoveException;

    int NextId() throws FindException;
    /**
     * 조회수 조회
     * @param board_id
     * @return 조회수
     * @throws FindException
     */
    Board BoardViewChk(String board_id) throws FindException;
    /**
     * 조회수 1씩 증가
     * @param board_id
     * @throws ModifyException
     */
    void BoardViewUpdate(String board_id) throws ModifyException;

    /**
     * 제목으로 게시글 찾기
     *
     * @param board_title 게시글제목
     * @return 게시글
     * @throws FindException 제목에 해당하는 게시글이 없을 때
     */
    List<Board> BoardByTitle(String board_title) throws FindException;


    /**
     * 게시판 여러개 삭제
     * @param board_id_list 삭제할 게시판 리스트
     * @throws RemoveException 삭제 실패 시 예외발생
     */
    void DeleteBoardByList(String[] board_id_list) throws RemoveException;

    void selectBoardUp(String user_id, String board_id) throws FindException;
}
