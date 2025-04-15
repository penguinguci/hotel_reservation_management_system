package dao;

import entities.RoomType;
import interfaces.RoomTypesDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import utils.AppUtil;

import java.util.List;

public class RoomTypesImpl extends GenericDAOImpl<RoomType, String> implements RoomTypesDAO {
    private EntityManager em;

    public RoomTypesImpl() {
        super(RoomType.class);
        em = AppUtil.getEntityManager();
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
