package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.data.BoardPOJO;

public class DatabaseDemo {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            BoardPOJO board = new BoardPOJO();
            board.setSize(5);
            board.setSymbols("R");

            em.merge(board);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}
