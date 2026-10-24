package com.lias.gestionlabo.repositories;

import com.lias.gestionlabo.models.Evenement;
import com.lias.gestionlabo.models.TypeEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    // جلب الأحداث حسب النوع
    List<Evenement> findByType(TypeEvenement type);

    // الحل: استخدام JPQL لاستخراج السنة من حقل التاريخ
    @Query("SELECT e FROM Evenement e WHERE YEAR(e.date) = :year")
    List<Evenement> findByDateYear(@Param("year") int year);

    // جلب الأحداث حسب المنظم
    List<Evenement> findByOrganisateurIdMembre(Long idMembre);
}