package com.my.controller;

import com.my.exception.FindException;
import com.my.service.BoardService;
import com.my.vo.Board;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board/*")
@Log4j
public class BoardController {
    @Autowired
    private BoardService service;

    @ResponseBody
    @RequestMapping("/boarddetail")
    public Map<String, Object> boardDetail(@RequestParam(value = "board_id") String board_id) throws FindException {
        Map<String, Object> jacksonMap = new HashMap<>();
        
        Board board;
        board = service.boardById(board_id);
        jacksonMap.put("board_id",board.getBoard_id());
        jacksonMap.put("user_nickname",board.getUser().getUser_nickname());
        jacksonMap.put("board_subtitle",board.getBoard_subtitle());
        jacksonMap.put("board_title",board.getBoard_title());
        jacksonMap.put("board_content",board.getBoard_content());
        jacksonMap.put("board_wdate",board.getBoard_wdate());
        jacksonMap.put("board_view",board.getBoard_view());
        jacksonMap.put("board_file",board.getBoard_file());
        jacksonMap.put("board_up", board.getBoard_up());
        return jacksonMap;

    }
    
    @ResponseBody
    @RequestMapping("/boardlist")
    public List<Map<String, Object>> boardList(@RequestParam(value = "page") int page, @RequestParam(value = "num") int num) throws FindException {

        List<Map<String,Object>> jacksonList = new ArrayList<>();
        
        List<Board> boards = service.boardAll(page,num);
        for(Board board : boards) {
            Map<String, Object> jacksonMap = new HashMap<>();
            jacksonMap.put("board_id",board.getBoard_id());
            jacksonMap.put("user_nickname",board.getUser().getUser_nickname());
            jacksonMap.put("board_subtitle",board.getBoard_subtitle());
            jacksonMap.put("board_title",board.getBoard_title());
            jacksonMap.put("board_content",board.getBoard_content());
            jacksonMap.put("board_wdate",board.getBoard_wdate());
            jacksonMap.put("board_view",board.getBoard_view());
            jacksonMap.put("board_file",board.getBoard_file());
            jacksonMap.put("board_total",board.getBoard_total());
            jacksonList.add(jacksonMap);
        }
        return jacksonList;

    }
    
    
}
