package org.example.stockage.jpa;

import jakarta.persistence.*;
import org.example.data.BoardPOJO;
import org.example.stockage.DAO;
import org.example.stockage.DAOException;

import java.util.List;
import java.util.Optional;

public class BoardJPADAO implements DAO<BoardPOJO> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");

    @Override
    public Optional<BoardPOJO> get(int id) throws DAOException {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(BoardPOJO.class, id));
        } catch (Exception e) {
            throw new DAOException("Erreur lors de la récupération du board", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<BoardPOJO> getAll() throws DAOException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("from BoardPOJO", BoardPOJO.class).getResultList();
        } catch (Exception e) {
            throw new DAOException("Erreur lors de la récupération de tous les boards", e);
        } finally {
            em.close();
        }
    }

    @Override
    public int create(BoardPOJO board) throws DAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(board);
            tx.commit();
            return board.getId(); // JPA met à jour l'id automatiquement
        } catch (Exception e) {
            tx.rollback();
            throw new DAOException("Erreur lors de la création du board", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(BoardPOJO board) throws DAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(board) ? board : em.merge(board));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new DAOException("Erreur lors de la suppression du board", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(BoardPOJO board) throws DAOException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(board);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new DAOException("Erreur lors de la mise à jour du board", e);
        } finally {
            em.close();
        }
    }
}
