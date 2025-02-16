package com.mooo.devforin.appservice.domain.repository.qnachat;

import com.mooo.devforin.appservice.domain.entity.QnaChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QnaChatRepository extends JpaRepository<QnaChat, Integer>, QnaChatRepositoryCustom {

    @Query("SELECT q FROM QnaChat q WHERE q.id = :id AND q.useYn = :useYn")
    Optional<QnaChat> findByIdAndUseYn(@Param("id") Integer id, @Param("useYn") String useYn);
}
