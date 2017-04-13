package com.teaminology.hp.dao.hibernate;

import java.util.List;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.dao.HibernateDAO;
import com.teaminology.hp.dao.ILoginDAO;

/**
 * Contains method to authenticate user.
 *
 * @author sayeedm
 */
public class LoginDAOImpl extends HibernateDAO implements ILoginDAO
{
    private static Logger logger = Logger.getLogger(LoginDAOImpl.class);

    /**
     * To authenticate user
     *
     * @param userName Logged user name
     * @param password Logged user password
     * @return User
     */
    @Transactional
    public User authentication(String userName, String password) throws DataAccessException {

        Session session = getHibernateSession();

        Criteria criteria = session.createCriteria(User.class);
        logger.info("User authentication");

        if (StringUtil.isNotEmpty(userName) && StringUtil.isNotEmpty(password)) {
            criteria.add(Restrictions.eq("userName", userName.toLowerCase()));
            criteria.add(Restrictions.eq("password", password));
            criteria.add(Restrictions.eq("isActive", "Y"));
            return (User) criteria.uniqueResult();
        }
        return null;
    }

    /**
     * To get user role  for a userId
     *
     * @param userId Integer to get user role
     * @return List of user role w.r.t the userId
     */
    @Transactional
    public List<UserRole> getUserRole(Integer userId) throws DataAccessException {
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(UserRole.class);

        if (userId != null && userId.intValue() > 0) {
            criteria.add(Restrictions.eq("userId", userId));
            criteria.add(Restrictions.eq("isActive", "Y"));
            return (List<UserRole>) criteria.list();
        }
        return null;
    }
}
