package com.tickets.server.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.tickets.model.User;

@Repository("dao")
@Scope("prototype")
public class Dao<E> extends HibernateDaoSupport {

    public void delete(E e) {
        deleteObject(e);
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> void delete(Class clazz, T id) {
        getHibernateTemplate().delete(getById(clazz, id));
    }

    @SuppressWarnings("unchecked")
    public <T> T getById(Class<T> clazz, Serializable id) {
        return (T) getHibernateTemplate().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public E save(E e) {
        if (!getHibernateTemplate().contains(e))
            e = merge(e);

        getHibernateTemplate().saveOrUpdate(e);
        return e;
    }

    @SuppressWarnings("unchecked")
    public E merge(E e) {
        e = (E) getHibernateTemplate().merge(e);
        return e;
    }

    @SuppressWarnings("unchecked")
    public List findByQuery(String query, String[] names, Object[] args) {
        if (names == null)
            names = new String[] {};

        if (args == null)
            args = new Object[] {};

        return getHibernateTemplate().findByNamedParam(query, names, args);
    }

    @SuppressWarnings("unchecked")
    public List findByNamedQuery(String name, String[] names, Object[] args) {
        return getHibernateTemplate().findByNamedQueryAndNamedParam(name,
                names, args);
    }

    public Object saveObject(Object o) {
        if (!getHibernateTemplate().contains(o))
            o = getHibernateTemplate().merge(o);

        getHibernateTemplate().saveOrUpdate(o);
        return o;
    }

    public Object mergeIfNotContained(Object o) {
        if (!getHibernateTemplate().contains(o))
            o = getHibernateTemplate().merge(o);

        return o;
    }

    /**
     * Use this if no merging is required
     *
     * @param o
     */
     public void saveOrUpdate(Object o) {
         evict(o);
         getHibernateTemplate().saveOrUpdate(o);
     }

     public void evict(Object o) {
         getHibernateTemplate().evict(o);
     }

     @SuppressWarnings("unchecked")
     public void deleteObject(Object object) {
         getHibernateTemplate().delete(object);
     }

     public int executeQuery(final String query, final String[] names,
             final Object[] args) {
         HibernateCallback cb = new HibernateCallback() {
             @Override
             public Object doInHibernate(Session session) {
                 Query q = session.createQuery(query);
                 for (int j = 0; j < args.length; j++) {
                     q.setParameter(names[j], args[j]);
                 }
                 return q.executeUpdate();
             }
         };

         return (Integer) getHibernateTemplate().execute(cb);
     }

     public Connection getConnection() {
         return getSession().connection();
     }

     /**
      * Convinient method for calling no-param queries
      * @param query
      * @return result list
      */
      @SuppressWarnings("unchecked")
      public List findByQuery(String query) {
          return findByQuery(query, null, null);
      }

      /**
       * Convinient method for calling no-param named queries
       * @param query
       * @return result list
       */
      @SuppressWarnings("unchecked")
      public List findByNamedQuery(String query) {
          return findByNamedQuery(query, null, null);
      }

    public void update(User user) {
        getHibernateTemplate().update(user);
    }
}