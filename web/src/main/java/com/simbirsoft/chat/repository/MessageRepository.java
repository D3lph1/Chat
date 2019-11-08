package com.simbirsoft.chat.repository;

import com.simbirsoft.chat.entity.Message;
import com.simbirsoft.chat.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface MessageRepository extends CrudRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender = ?1 AND m.receiver = ?2) OR " +
            "(m.sender = ?2 AND m.receiver = ?1) ORDER BY m.createdAt ASC")
    Collection<Message> findBySenderAndReceiverAndViceVersaOrderByCreatedAtASC(User sender, User receiver);

    @Query("SELECT m FROM Message m JOIN FETCH m.sender WHERE m.receiver = ?1 AND m.read = false")
    Collection<Message> findUnreadWithSenderByReceiver(User user);

    @Modifying
    @Query("UPDATE Message m SET m.read = true WHERE m.receiver = ?1 AND m.read = false")
    void markAsReadByReceiver(User user);
}
