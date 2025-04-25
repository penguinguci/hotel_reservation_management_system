package dao;

import entities.RoomType;
import interfaces.RoomTypesDAO;
import interfaces.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class RoomTypeDAOImpl extends GenericDAOImpl<RoomType, String> implements RoomTypesDAO, Serializable {
    private static final long serialVersionUID = 1L;
    private EntityManager em;
    private GenericDAO genericDAO;

    public RoomTypeDAOImpl() throws RemoteException {
        super(RoomType.class);
        em = AppUtil.getEntityManager();
    }

    public void setGenericDAO(GenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        EntityManager em = AppUtil.getEntityManager();
        try {
            String jpql = "SELECT rt FROM RoomType rt ORDER BY rt.typeName";
            TypedQuery<RoomType> query = em.createQuery(jpql, RoomType.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}