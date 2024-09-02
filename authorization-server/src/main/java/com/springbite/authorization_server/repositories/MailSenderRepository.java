package com.springbite.authorization_server.repositories;

import com.springbite.authorization_server.entities.MailSenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSenderRepository extends JpaRepository<MailSenderEntity, Long> {
}
