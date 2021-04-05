package com.my.model;

import com.my.exception.RemoveException;
import com.my.service.UserService;

public class DeleteEmailThread extends Thread {
    String email;

    public DeleteEmailThread(String email) {
        this.email = email;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(300*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserService service = new UserService();
        try {
            service.removeEmailById(email);
            System.out.println(email + "삭제 완료");
        } catch (RemoveException e) {
            e.printStackTrace();
            System.out.println("이미 tmp_email에 존재하지 않는 이메일");
        }
    }
}
