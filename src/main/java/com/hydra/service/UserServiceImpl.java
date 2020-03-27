package com.hydra.service;

import com.hydra.dao.UserDao;

public class UserServiceImpl implements UserService {

    UserDao dao;

//    public UserServiceImpl(UserDao dao){
//        this.dao=dao;
//    }

    @Override
    public void find() {
        System.out.println("service");
        dao.query();
    }

    public UserDao getDao() {
        return dao;
    }

    public void setDao(UserDao dao) {
        this.dao = dao;
    }
}
